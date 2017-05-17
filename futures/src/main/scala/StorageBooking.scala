import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


object StorageBooking {

  def weatherForecast(): String = {
    Thread.sleep(1000)
    "rainy"
  }

  def predictHarvest(forecast: String) = {
    Thread.sleep(1000)
    forecast match {
      case "sunny" => "small"
      case "rainy" => "big"
    }
  }

  def bookingStorage(harvest: String) {
    println(harvest)
  }

  def main(args: Array[String]) {

    val forecastFuture = Future {
      weatherForecast()
    }
    forecastFuture onComplete {
      case Success(forecast) =>
        val harvestFuture = Future {
          predictHarvest(forecast)
        }
        harvestFuture onComplete {
          case Success(harvest) => bookingStorage(harvest)
          case Failure(harvestEx) => throw harvestEx
        }
      case Failure(forecastEx) => throw forecastEx
    }
    Await.ready(forecastFuture, 30 seconds)

    val f: Future[Unit] = for {forecast <- Future {
      weatherForecast()
    }
                               harvest <- Future {
                                 predictHarvest(forecast)
                               }
    } yield bookingStorage(harvest)
    Await.ready(f, 30 seconds)

    val f2 = Future {
      weatherForecast()
    }.flatMap(forecast => Future {
      predictHarvest(forecast)
    }).map(bookingStorage(_))

    Await.ready(f2, 30 seconds)

  }
}
