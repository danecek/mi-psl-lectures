package monads.test

import monads.Monad
import org.scalatest.FunSuite

class SetSuite extends FunSuite {

  def f(s : String) : Monad[String] = { Monad(s.reverse); }
  def g(s : String) : Monad[String] = { Monad(s.reverse) }

  test("left-identity law") {
    val x = "123"
    assert(Monad(x).flatMap(f) == f(x))
  }
  test("right-identity law") {
    val m = Monad("123")
    assert(m.flatMap(Monad.apply) == m)
  }

  test("associativity law"){

    val m = Monad("123")
    assert(m.flatMap(f).flatMap(g) == m.flatMap(x ⇒ f(x).flatMap(g)))
  }



}
