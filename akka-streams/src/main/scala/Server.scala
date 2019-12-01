import akka.stream.scaladsl._
import Tcp.{IncomingConnection, ServerBinding}
import akka.NotUsed
import akka.actor.ActorSystem
import akka.util.ByteString
import akka.stream.scaladsl.Framing

import scala.concurrent.Future
import scala.io.StdIn

object Server extends App {
  implicit val system = ActorSystem("QuickStart")
  val connections: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("127.0.0.1", 8888)
  connections.runForeach { connection =>
    println(s"New connection from: ${connection.remoteAddress}")

    val echo: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString]
      .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 256, allowTruncation = true))
      .map(_.utf8String)
      .map(_ + "!!!\n")
      .map(ByteString(_))
    connection.handleWith(echo)
  }
}

object Client extends App {
  implicit val system = ActorSystem("QuickStart")
  val connection = Tcp().outgoingConnection("127.0.0.1", 8888)

  val replParser: Flow[String, ByteString, NotUsed] =
    Flow[String].takeWhile(_ != "q").concat(Source.single("BYE")).map(elem => ByteString(s"$elem\n"))

  val repl = Flow[ByteString]
    .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 256, allowTruncation = true))
    .map(_.utf8String)
    .map(text => println("Server: " + text))
    .map(_ => StdIn.readLine("> "))
    .via(replParser)
  val connected = connection.join(repl).run()
}
