package testslick

import slick.driver.H2Driver.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object HelloSlick extends App {

  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  val createSchemaAction: DBIOAction[Unit, NoStream, Effect.Schema] = users.schema.create
  val addTomAction: DBIO[Int] = users += User("Tom")
  val selectAllAction = users.result

  val db = Database.forConfig("h2mem1")
  val runAllFuture = db.run(createSchemaAction andThen addTomAction andThen selectAllAction)

  /*
   import scala.concurrent.ExecutionContext.Implicits.global
    runAllFuture.onComplete {
      case Success(allAuser) => {
        println(allAuser)
      }
      case Failure(e) => {
        println(e)
      }
      Predef.readline
    }*/
  println(Await.result(runAllFuture, Duration.Inf))

}