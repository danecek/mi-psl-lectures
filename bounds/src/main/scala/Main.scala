/**
  * Created by danecek on 5/19/17.
  */
object Main {

  def outputLength[T <% String](x : T )={
    x.length
  }

  implicit def int2String(x : Int) = x.toString

  def main(args: Array[String]){
     println(outputLength(4*4))

  }

}
