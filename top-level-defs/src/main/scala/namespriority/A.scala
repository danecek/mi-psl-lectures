package namespriority

object A {

  def main(args: Array[String]) {

 //   val x = 2
    println(x)
    import namespriority.x
    println(x)
  }
}
