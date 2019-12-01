import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.{Done, NotUsed}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, duration}

object RunSugar extends App {
  implicit val system = ActorSystem("QuickStart")
  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sink: Sink[Any, Future[Done]] = Sink.foreach(println)

  val f: Future[Done] = source.runWith(sink)
  println(Await.result(f, Duration.Inf))
  val f1 = source.runForeach(println)
  println(Await.result(f1,  Duration.Inf))
  val f2 = source.runFold(0)(_ + _)
  println(Await.result(f2, Duration(1, duration.SECONDS)))

}
