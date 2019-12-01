import akka.actor.ActorSystem
import akka.stream.scaladsl.{RunnableGraph, Sink, Source}
import akka.{Done, NotUsed}
import scala.concurrent.Future

object Hello extends App {
  val in: Source[Int, NotUsed] = Source(1 to 10)
  val inSqr = in.map(x => x * x)
  val out: Sink[Any, Future[Done]] = Sink.foreach(println)

  val rg: RunnableGraph[NotUsed] = inSqr to out
  implicit val system = ActorSystem("QuickStart")
  rg.run()
}
