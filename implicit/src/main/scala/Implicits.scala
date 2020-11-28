trait T {
  implicit val inherited = true
}

object O {
  implicit val imported = 'c'
}

class Outer {

  implicit val enclosing: String = "a"

  class Inner extends T {

    implicit val member: Double = 1.1

    def fun(implicit i: Int, d: Double, b: Boolean, s: String, c: Char): Unit = {}

    import O.imported

    implicit val local = 1
    fun(local, member, inherited, enclosing, imported)
    fun // all arguments are passed implicitly

  }


}
