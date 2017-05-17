package monads

import com.typesafe.scalalogging.LazyLogging

abstract class Monad[+A] {
  def flatMap[B](f: (A) => Monad[B]): Monad[B] = this match {
    case MonadSuccess(x) => f(x)
    case MonadFailure(_) => this.asInstanceOf[Monad[B]]
  }
  def map[B](f: (A) => B): Monad[B] = flatMap(x => Monad(f(x)))
  def withFilter[B](p: (A) => Boolean): Monad[B] = this match {
    case MonadSuccess(x) => if (p(x)) this.asInstanceOf[Monad[B]] else MonadFailure("filtered")
    case MonadFailure(_) => this.asInstanceOf[Monad[B]]
  }
}

object Monad {
  def apply[A](a: A): Monad[A] = MonadSuccess(a)
}

case class MonadSuccess[A](x: A) extends Monad[A] {
 // def flatMap[B](f: (A) => Monad[B]): Monad[B] = f(x)
  //def map[B](f: (A) => B) = Monad(f(x))
}

case class MonadFailure[A](s: String) extends Monad[A] {
 // def flatMap[B](f: (A) => Monad[B]): Monad[B] = this.asInstanceOf[Monad[B]]
 // def map[B](f: (A) => B) = this.asInstanceOf[Monad[B]]
}

object MonadMain extends LazyLogging {

  def dec(x : Int) : Monad[Int] = {
    println(s"value=$x")
    if (x > 0)
      MonadSuccess(x - 1)
    else
      MonadFailure("Zero cannot be decremented")
  }

  def main(args: Array[String]): Unit = {
    val m1 : Monad[Int] = Monad(3).flatMap(dec).flatMap(dec).flatMap(dec)
    println(m1)
    val m2  = for (i <- Monad(2);
         j <- dec(i);
         k <- dec(j);
         l<- dec(k))
      yield l
    println(m2)



  }
}



