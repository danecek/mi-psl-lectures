object ContextBounds {

  def maxIntList(elements: List[Int]): Int = {
    elements match {
      case head :: Nil => head
      case head :: tail =>
        val maxTail = maxIntList(tail)
        if (head >= maxTail) head else maxTail
    }
  }

  /*  def maxList0[T](elements: List[T]): T = {
      elements match {
        case head :: Nil => head
        case head :: tail => {
          val maxTail = maxList0(tail)
          if (head >= maxTail) head else maxTail // cannot resolve >=
        }
      }
    }*/

  def maxList1[T <: Ordered[T]](elements: List[T]): T = {
    elements match {
      case head :: Nil => head
      case head :: tail => {
        val maxTail = maxList1(tail)
        if (head >= maxTail) head else maxTail
      }
    }
  }

  def maxList2[T](elements: List[T])(implicit convertor: T => Ordered[T]): T = {
    elements match {
      case head :: Nil => head
      case head :: tail => {
        val maxTail = maxList2(tail)
        if (convertor(head) >= maxTail) head else maxTail
      }
    }
  }

  def maxList3[T <% Ordered[T]](elements: List[T]): T = {
    elements match {
      case head :: Nil => head
      case head :: tail => {
        val maxTail = maxList3(tail)
        val convertor = implicitly[T => Ordered[T]]
        if (convertor(head) >= maxTail) head else maxTail
      }
    }
  }

  def maxList4[T <% Ordered[T]](elements: List[T]): T = {
    elements match {
      case head :: Nil => head
      case head :: tail => {
        val maxTail = maxList4(tail)
        if (head >= maxTail) head else maxTail
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val intList = List(1, 2, 10, -2, 30, 4)
    val strList = List("abc", "xyz")
    val biList = List(BigInt("10000000000000000000000000000000000000"))
     println(maxList1(intList))
    println(maxList1(biList))
  }
}
