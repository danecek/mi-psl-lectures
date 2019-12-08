import scala.annotation.tailrec

/**
 * Created by danecek on 5/4/17.
 */
object Main {
  @tailrec
  def isSafe(row: Int, queenRows: List[Int], d: Int): Boolean = {
    queenRows match {
      case Nil => true
      case h :: t => {
        if (row == h || row == h + d || row == h - d)
          false
        else
          isSafe(row, t, d + 1)
      }
    }
  }

  def queens(n: Int): List[List[Int]] = {
    def placeQueens(k: Int): List[List[Int]] =
      if (k == 0) List(List())
      else for {queenRows <- placeQueens(k - 1)
                row <- 1 to n
                if isSafe(row, queenRows, 1)
                } yield row :: queenRows

    placeQueens(n)
  }

  def main(args: Array[String]): Unit = {
    println(queens(8))
  }
}
