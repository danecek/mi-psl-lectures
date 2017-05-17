import java.util.concurrent.ExecutorService

import scala.annotation.tailrec
import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
object Promises {

  def Future_apply[T](task : =>T): Future[T] = {
    val p = Promise[T]()
    ExecutionContext.global.execute {
      new Runnable {
        override def run() {
          try {
            p.success(task)
          } catch  {
            case ex : Exception => p.failure(ex)
          }
        }
      }
    }
    p.future
  }

  def main(args: Array[String]): Unit = {

    val f : Future[BigInt]= Future_apply {
      @tailrec
      def fac(f : BigInt, i : Int): BigInt = i match {
         case 0  => f
         case _ => fac(f * i, i - 1)
      }
      fac(1, 22)
    }
    println(Await.result(f, Duration(25, SECONDS)))
  }

}
