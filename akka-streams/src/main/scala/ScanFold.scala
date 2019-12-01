import akka.actor.ActorSystem
import akka.stream.scaladsl.{RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object ScanFold extends App {
  val s: Source[Int, NotUsed] = Source(List(1,2,3))
  val scanin: Source[Int, NotUsed] = s.scan(0)(_ + _)
  val foldin: Source[Int, NotUsed] = s.fold(0)(_ + _)
  val out: Sink[Any, Future[Done]] = Sink.foreach(println)
  implicit val system = ActorSystem("QuickStart")
  val f: Future[Done] = scanin.runWith(out)
  println(Await.result(f, Duration.Inf))
  foldin.runWith(out)
}
