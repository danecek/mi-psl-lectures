package testslick

import slick.driver.H2Driver
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object FKApp extends App {

  val db = Database.forConfig("h2mem1")

  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  // val usersInsert: H2Driver.ReturningInsertActionComposer[User, Int] = users returning users.map(_.id)

  def addUser(u: User): DBIO[Int] = users returning users.map(_.id) += u

  //println(usersInsert.insertStatement.mkString)

  val emails: TableQuery[EmailsTable] = TableQuery[EmailsTable]

  val createSchemas: DBIO[Unit] = (users.schema ++ emails.schema).create

  val createTomEmails: DBIO[Option[Int]] = addUser(User("Tom")).flatMap(tomId =>
    emails ++= Seq(Email("tom@seznam.cz", tomId), Email("tom@gmail.com", tomId)))

  val createJerryEmails: DBIO[Option[Int]] =
    for {jerryId <- addUser(User("Jerry"))
         emailsCount <- emails ++= Seq(Email("jerry@seznam.cz", jerryId), Email("jerry@gmail.com", jerryId))
         } yield emailsCount

  println(Await.result(db.run(createSchemas >> createTomEmails >> createJerryEmails >> emails.result), Duration.Inf))



}
