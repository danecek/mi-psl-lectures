package testslick

import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object FKApp extends App {

  val db = Database.forConfig("h2mem1")

  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  def addUser(u: User): DBIO[Int] = users returning users.map(_.id) += u

  def addUser2(u: User): DBIO[User] = {
    val messagesReturningRow = users returning users.map(_.id) into {
      (user: User, genId: Int) => user.copy(id = Option(genId))
    }
    messagesReturningRow += u
  }


  val emails: TableQuery[EmailsTable] = TableQuery[EmailsTable]

  val createSchemas: DBIO[Unit] = (users.schema ++ emails.schema).create

  val createTomEmails: DBIO[Option[Int]] = addUser(User("Tom")).flatMap((tomId: Int) =>
    emails ++= Seq(Email("tom@seznam.cz", tomId), Email("tom@gmail.com", tomId)))

  val createJerryEmails: DBIO[Option[Int]] =
    for {jerryId <- addUser(User("Jerry"))
         emailsCount <- emails ++= Seq(Email("jerry@seznam.cz", jerryId), Email("jerry@gmail.com", jerryId))
         } yield emailsCount

  println(Await.result(db.run(createSchemas >> createTomEmails >> createJerryEmails >> emails.result), Duration.Inf))

}
