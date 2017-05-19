
abstract class Monoid[A] {
  def unit: A
  def add(x: A, y: A): A
}

object Monoid {

  implicit object intMonoid extends Monoid[Int] {
    def add(x: Int, y: Int): Int = x + y

    def unit: Int = 0
  }
  implicit object strMonoid extends Monoid[String] {
    def add(x: String, y: String): String = x + y
    def unit: String = ""
  }

}

object Main {

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
    if (xs.isEmpty) m.unit
    else m.add(xs.head, sum(xs.tail))

  def sum2[A : Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    if (xs.isEmpty) m.unit
    else m.add(xs.head, sum(xs.tail))
  }


  def main(args: Array[String]): Unit = {
    import Monoid._
    println(sum(List(1, 2, 3)))
    println(sum(List("1", "2", "3")))

  }
}
