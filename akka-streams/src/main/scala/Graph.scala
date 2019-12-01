import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl._
object Graph {

  val g: RunnableGraph[NotUsed] = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    val in = Source(List("OnX", "Two", "ThrXX")).map(s=>s.replace('X', 'e'))
    val out = Sink.foreach(println)

    val bcast = builder.add(Broadcast[String](2))
    val merge = builder.add(Merge[String](2))

    val toUpper: Flow[String, String, NotUsed] = Flow[String].map(_.toUpperCase)
    val toLower = Flow[String].map(_.toLowerCase)
    
    val cb: CombinerBase[String] = in~>bcast // in.~>(bcast)(builder)
    cb ~> toUpper ~> merge ~> out
    bcast ~> toLower ~> merge
    ClosedShape
  })

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("QuickStart")
    g.run()
  }
}
