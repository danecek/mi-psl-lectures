
class  C(private val p : Int, private[this] val pt : Int, c : C) {

  //def m(c : C): Unit = {
    assert (c.p == this.p)
    assert (c.pt == this.pt)
//  }

}

object Main {
  def main(args: Array[String]): Unit = {

  }
}
