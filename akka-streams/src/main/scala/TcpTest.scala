import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Tcp}
import Tcp.ServerBinding

import scala.concurrent.{ExecutionContext, Future}

object TcpTest extends App {
  implicit val sa = ActorSystem("as")
  implicit val ec = ExecutionContext.global
  val binding: Future[ServerBinding] =
    Tcp().bind("127.0.0.1", 8888).to(Sink.ignore).run()

  binding.map { b: ServerBinding =>
    val f: Future[Unit] = b.unbind()
    f.onComplete {
      case _ => println(f)
    }
  }
}
