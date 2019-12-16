package testslick

import slick.driver.H2Driver.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

object Template extends App {

  // the base query for the hw.testslick.Users table
  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  val selectAllAction: DBIO[Seq[User]] = users.result
  println(users.result.statements.mkString)

  val db = Database.forConfig("h2mem1")

  val createSchema: slick.driver.H2Driver.SchemaDescription = users.schema
  println(createSchema.createStatements.mkString)
  println(createSchema.dropStatements.mkString)
  val createSchemaAction: DBIOAction[Unit, NoStream, Effect.Schema] = createSchema.create
  val dropSchemaAction: DBIOAction[Unit, NoStream, Effect.Schema] = createSchema.drop

  val fc: Future[Unit] = db.run(createSchemaAction)

  import scala.concurrent.ExecutionContext.Implicits.global

  fc.onComplete { (t: Try[Unit]) =>
    t match {
      case Success(_) => {
        println("USERS created")
      }
      case Failure(e) => {
        print(e.getMessage)
      }
    }
  }
  Await.result(fc, Duration.Inf)
  val addTomAction: DBIO[Int] = users += User("Tom")
  println((users += User("Spike")).statements.mkString)
  val addJerrySpikeAction: DBIO[Option[Int]] = users ++= Seq(User("Jerry"), User("Spike"))
  println((users ++= Seq(User("Tom"), User("Jerry"))).statements.mkString)
  val pa: DBIO[Unit] = users.result.map(println)

  val actionSequence: DBIO[Unit] = DBIO.seq(
    addTomAction,
    addJerrySpikeAction
  )


  val f: Future[Unit] = db.run(actionSequence)
  Await.result(f, Duration.Inf)
  db.close
}
