package testslick

import slick.driver.H2Driver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

case class User(name: String, id: Option[Int] = None)

class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
  def id: Rep[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  // The name can't be null
  def name: Rep[String] = column[String]("NAME")

  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a hw.testslick.User
  def * : ProvenShape[User] = (name, id.?) <> (User.tupled, User.unapply)
}

case class Email(email: String, userId: Int)

class EmailsTable(tag: Tag)
  extends Table[Email](tag, "EMAILS") {

  def email = column[String]("ROOM_COLOR")

  def userId: Rep[Int] = column[Int]("USER_ID")

  def * : ProvenShape[Email] = (email, userId) <> (Email.tupled, Email.unapply)

  def user: ForeignKeyQuery[UsersTable, User] =
    foreignKey("USER_ID", userId, TableQuery[UsersTable])(_.id)
}