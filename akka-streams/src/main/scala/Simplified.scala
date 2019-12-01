import java.nio.file.{Files, Path}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{Broadcast, FileIO, Merge, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

object Simplified {

  def s1(): Unit = {
    val sourceOne = Source(1 to 2)
    val sourceTwo = Source(3 to 4)
    val merged: Source[Int, NotUsed] = Source.combine(sourceOne, sourceTwo)(Merge(_))
    implicit val system = ActorSystem("QuickStart")
    merged.runForeach(println)
  }

  def s2(): Unit = {
    val write2first: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Path.of("first"))
    val write2sec = FileIO.toPath(Path.of("second"))
    val sink = Sink.combine(write2first, write2sec)(Broadcast[ByteString](_))
    implicit val system = ActorSystem("QuickStart")
    Source(List(ByteString("alfa\n"), ByteString("beta\n"))).runWith(sink)
  }

  def main(args: Array[String]): Unit = {
    s2()
  }
}
