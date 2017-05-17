package traits

/**
  * Created by danecek on 6/13/16.
  */
object xxx {
  val primes : PartialFunction[Int, Int] = Seq(2,3,5,7) // ???
  println(primes.isDefinedAt(1)) // true
  println(primes(1)) // 3
}
