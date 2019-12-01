import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Source, Sink}

object FlowTest extends App {
  val identFlow: Flow[String, String, NotUsed] = Flow[String]
  val parseFlow: Flow[String, Int, NotUsed] = Flow[String].map(Integer.parseInt(_))
  val sqrtFlow: Flow[Int, Double, NotUsed] = Flow[Int].map(Math.sqrt(_))
  val sqrFlow: Flow[Int, Int, NotUsed] = Flow[Int].map(x => x * x)
  val stringToSqrtFlow: Flow[String, Double, NotUsed] = parseFlow via sqrtFlow
  val stringSource: Source[String, NotUsed] = Source("1" :: "2" :: "3" :: "4" :: "5" :: Nil)
  implicit val system = ActorSystem("QuickStart")
  val stringToSqrtSource: Source[Double, NotUsed] = stringSource via stringToSqrtFlow
  val stringToParseSource: Source[Int, NotUsed] = stringSource via parseFlow
  stringToSqrtSource.runWith(Sink.foreach(println))


}
