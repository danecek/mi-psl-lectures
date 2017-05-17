package specification.p

// `X' bound by package clause

import scala.Console._

// `println' bound by wildcard import
object A {

  def main(args: Array[String]) {
       B
      B.C
  }

  println("L4: " + X)

  // `X' refers to `P.X' here
  object B {

    import _root_.specification.q._

    // `X' bound by wildcard import
    println("L7: " + X) // `X' refers to `Q.X' here               // `x' and `y' bound by wildcard import
    import X._               // `x' and `y' bound by wildcard import
    println("L8: " + x)

    // `x' refers to `Q.X.x' here
    object C {
      val x = 3 // `x' bound by local definition
      println("L12: " + x); // `x' refers to constant `3' here
      {
        import _root_.specification.q.X._
        // `x' and `y' bound by wildcard import
        //      println("L14: "+x)   // reference to `x' is ambiguous here           // `y' bound by explicit import
        println("L16: " + y); // `y' refers to `Q.X.y' here
        {
          val x = "abc" // `x' bound by local definition       // `x' and `y' bound by wildcard import
          println("L19: "+y) // reference to `y' is ambiguous here
          println("L20: " + x) // `x' refers to string "abc" here
        }
      }
    }

  }

}