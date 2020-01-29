package bounds

import scala.reflect.runtime.universe

object Main {

  import scala.reflect.runtime.universe._

  def printType[T](implicit tt: TypeTag[T]): Unit = {
    val x: universe.TypeTag[T] =  implicitly[TypeTag[T]]
    println(x.tpe)
  }

  def printType2[T : TypeTag] : Unit = {
    val x: universe.TypeTag[T] =  implicitly[TypeTag[T]]
    println(x.tpe)
  }

  def outputLength[T <% String](x : T)= x.length

  implicit def int2String(x : Int) = x.toString

  def main(args: Array[String]){
    println(outputLength(4*4))
    printType2[Int]

  }

}
