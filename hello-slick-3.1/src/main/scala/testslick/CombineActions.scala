package testslick

import slick.dbio.{DBIOAction, Effect}
import slick.driver.H2Driver.api._
import slick.profile.{FixedSqlAction, FixedSqlStreamingAction}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object CombineActions extends App {

  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  val createSchemaAction: DBIO[Unit] = users.schema.create

  def insertUsers(us: Seq[User]): DBIO[Option[Int]] = users ++= us

  val db = Database.forConfig("h2mem1")
  Await.result(db.run(createSchemaAction andThen insertUsers(Seq(User("Tom"), User("Jerry")))), Duration.Inf)

  import scala.concurrent.ExecutionContext.Implicits.global

  val printAllAction: DBIO[Unit] = users.result.map(println)

  val duplicateAction: DBIO[Option[Int]] = for (
    usrs: Seq[User] <- users.result;
    number: Option[Int] <- insertUsers(usrs)
  ) yield number

  val duplicateAction2: DBIO[Option[Int]] = users.result.flatMap(usrs => insertUsers(usrs))

  println(Await.result(db.run(duplicateAction2), Duration.Inf))
  Await.result(db.run(printAllAction), Duration.Inf)


}