package functions

object Main extends App {

  def mltXYZ(x:Int, y:Int, z:Int) = x*y*z

  val f0 = mltXYZ _
 // val f1 = mltXYZ(1, _ , _ )
  val f2: (Int,Int) => Int = mltXYZ(1, _, _)

  def mltXYZa(x: Int): (Int, Int) => Int
  // the result type is a function type
  = (y, z) => x * y * z // = x * _ * _

  def mltXYZb(x: Int): ((Int) => (Int => Int)) =
    (y: Int) => ((z: Int) => x * y * z)

  def mltXYZc(x: Int): Int => Int => Int =
    (y: Int) => (z: Int) => x * y * z

  def mltXYZd(x: Int) =
    (y: Int) => (z: Int) => x * y * z

  def mltXYZcurr(x: Int)(y: Int)(z: Int) = x * y * z

  def mltXYWZ(x: Int, y: Int)(w: Int, z: Int) = x * y * w * z


  val mlt10YZa = mltXYZa(10)

  val mlt10YZb = mltXYZb(10)

  val mlt10YZc = mltXYZc(10)

  val mlt10YZd = mltXYZd(10)

  val mlt10YZe = mltXYZcurr(10) _

  val mlt10YZf = mltXYZcurr(10)(_)

  val mlt10YZg : (Int) => (Int => Int) = mltXYZcurr(10)
  /*  println(mlt10YZ(2, 3)) // prints 60

    println(mltXYZb(1)(2)(3))
    println(mltXYZc(1)(2)(3))
    println(mltXYZd(1)(2)(3))
    println(mltXYZe(1)(2)(3))*/

  val mltXYW12 = mltXYWZ(1, 2)(_, _)



 /* val f2 = mltXY(_, 1)

  val f1 = mltXY(1, _)*/






}
