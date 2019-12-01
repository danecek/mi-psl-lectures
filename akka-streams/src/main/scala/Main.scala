import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}

import scala.concurrent.Future

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("QuickStart")
    val so: Source[Int, NotUsed] = Source(1 to 3)
    val si: Sink[Int, Future[Done]] = Sink.foreach[Int](println)
    val r: RunnableGraph[NotUsed] = so.to(si)
    val f: NotUsed = r.run()
    val r2: RunnableGraph[Future[Done]] = so.toMat(si)(Keep.right)
    val f2: Future[Done] = r2.run()
    val r3: RunnableGraph[NotUsed] = so.toMat(si)(Keep.left)
    val f3: NotUsed = r3.run()
    val r4: RunnableGraph[(NotUsed, Future[Done])] = so.toMat(si)(Keep.both)
    val f4: (NotUsed, Future[Done]) = r4.run()
    val r5: RunnableGraph[NotUsed] = so.toMat(si)(Keep.none)
    val f5: NotUsed = r5.run()
  }
}
