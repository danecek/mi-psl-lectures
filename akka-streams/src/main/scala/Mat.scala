import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

object KeepTest extends App {
  implicit val system = ActorSystem("QuickStart")
  // Keep.left
  val srcProm: Source[Int, Promise[Option[Int]]] =  Source.maybe[Int]
  val snkFtrD: Sink[Int, Future[Done]] = Sink.foreach(println)
  val promise: Promise[Option[Int]] = srcProm.to(snkFtrD).run()
  implicit val ec = ExecutionContext.global
  promise.complete(Try(Some(1))) // 1
  // Keep.right
  val srcNU: Source[Int, NotUsed] = Source(1 to 5)
  val snkFtrI: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)
  val ftrInt: Future[Int] = srcNU.toMat(snkFtrI)(Keep.right).run()
  ftrInt.onComplete(println)  // Success(3)
}



