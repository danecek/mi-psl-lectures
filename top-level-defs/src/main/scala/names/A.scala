package names

object A {
  val x =1

  def main(args: Array[String]) {
     B
     C
  }
}

object B {
 val x = 3; // `x' bound by local definition
  {
    import A.x
   // println(x) reference to x is ambiguous; it is both defined in object A and imported subsequently by import B.x
  }
}

object C {
  import A.x
  val x = 3; // `x' bound by local definition
  println(x)  // 3
  }