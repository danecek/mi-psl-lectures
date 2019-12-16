import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global



object ForeignKeyExample extends App {

  case class User(name: String, id: Long = 0L)

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    //  def * = (name, id).mapTo[testslick.User]
    def * : ProvenShape[User] = (name, id) <> (User.tupled, User.unapply)
  }

  lazy val users = TableQuery[UserTable]
  lazy val insertUser = users returning users.map(_.id)

  //
  // The message table, in which we represent the sender as
  // the key in the user table (rather than a String name
  // we've used up until this point)
  //
  case class Message(
                      senderId: Long,
                      content: String,
                      id: Long = 0L)

  class MessageTable(tag: Tag) extends Table[Message](tag, "message") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def senderId = column[Long]("sender")

    def content = column[String]("content")

    def * : ProvenShape[Message] = (senderId, content, id) <> (Message.tupled, Message.unapply)

    //
    // Establish a FK relation:
    //
    def sender = foreignKey("sender_fk", senderId, users)(_.id, onDelete = ForeignKeyAction.Cascade)
  }

  lazy val messages = TableQuery[MessageTable]

  // The schema for both tables:
  lazy val ddl = users.schema ++ messages.schema


  def exec[T](action: DBIO[T]): T =
    Await.result(db.run(action), Duration.Inf)

  val db = Database.forConfig("chapter05")

  val initalise: DBIO[Option[Int]] =
    for {
      _ <- ddl.create
      halId <- insertUser += User("HAL")
      daveId <- insertUser += User("Dave")
      count <- messages ++= Seq(
        Message(daveId, "Hello, HAL. Do you read me, HAL?"),
        Message(halId, "Affirmative, Dave. I read you."),
        Message(daveId, "Open the pod bay doors, HAL."),
        Message(halId, "I'm sorry, Dave. I'm afraid I can't do that.")
      )
    } yield count

  val initalise2: DBIO[Option[Int]] =
    for {
      halId <- insertUser += User("HAL")
      count <- messages ++= Seq(
        Message(halId, "Affirmative, Dave. I read you."))
    } yield count

  val f: DBIO[Int] = (insertUser += User("HAL")).flatMap(halId => messages +=
    Message(halId, "Affirmative, Dave. I read you."))

  // A simple join using the foreign key:
  val join = for {
    msg <- messages
    usr <- msg.sender
  } yield (usr.name, msg.content)

  // Set up the database:
  exec(initalise)

  println("\nResult of foreign key join:")
  println(exec(join.result))

  // If we delete a user, that user's messages will be
  // deleted because the foreign key has been configured
  // to CASCADE
  println("\nMessages after deleting the user Dave:")
  val deleteDave = for {
    daveId <- users.filter(_.name === "Dave").map(_.id).result.headOption
    rowsAffected <- messages.filter(_.senderId === daveId).delete
  } yield rowsAffected
  exec(deleteDave)
  println(exec(messages.result))
}