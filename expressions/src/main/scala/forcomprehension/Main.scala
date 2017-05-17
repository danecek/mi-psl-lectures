package forcomprehension

import scala.annotation.tailrec

object Main {

  @tailrec
  def isSafe(tryRow: Int, queensOnRows: List[Int], col: Int): Boolean = {
    queensOnRows match {
      case Nil => true
      case rowOnCol :: t =>
        if (rowOnCol == tryRow || rowOnCol == tryRow + col ||rowOnCol == tryRow - col) false
        else isSafe(tryRow, t, col + 1)
    }
  }
  def queens(n: Int): List[List[Int]] = {
    def placeQueens(k: Int): List[List[Int]] =
      if (k == 0) List(List())
      else for {queensOnRows <- placeQueens(k - 1)
                tryRow <- 1 to n
                if isSafe(tryRow, queensOnRows, 1)
      } yield tryRow :: queensOnRows
    placeQueens(n)
  }

  def main(args: Array[String]) {
    println(queens(8).mkString("\n"))
  }
}
