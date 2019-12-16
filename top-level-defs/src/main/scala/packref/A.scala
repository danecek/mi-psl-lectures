package packref {
  class C
}

package x.packref {

  class X

  object A {
    println(new x.packref.X)
    println(new _root_.packref.C)
    println(new _root_.x.packref.X)
    println(new X)
    //  println(new C)
  }

}
