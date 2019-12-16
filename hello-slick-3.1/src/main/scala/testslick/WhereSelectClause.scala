package testslick

import slick.dbio.Effect
import slick.driver.H2Driver.api._
import slick.lifted.BaseJoinQuery
import slick.profile.FixedSqlStreamingAction

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object WhereSelectClause extends App {

  val users: TableQuery[UsersTable] = TableQuery[UsersTable]

  val whereQuery: Query[UsersTable, User, Seq] =
    users.filter(_.name.length === 3)

  val whereQueryAction = whereQuery.result
  println(whereQueryAction.statements)

  val db = Database.forConfig("h2mem1")
  val createAction = users.schema.create
  val addTomJerryAction: DBIO[Option[Int]] = users ++= Seq(User("Tom"), User("Jerry"))
  val fc: Future[Seq[User]] = db.run(createAction andThen addTomJerryAction andThen whereQueryAction)

  println(Await.result(fc, Duration.Inf))

  val selectNamesQuery: Query[Rep[String], String, Seq] = users.map(_.name)
  println(selectNamesQuery.result.statements)
  val selectNamesQuery2: Query[Rep[String], String, Seq] = for (u <- users) yield u.name
  val selectIndexedNamesQuery = selectNamesQuery.zipWithIndex

  println(Await.result(db.run(selectNamesQuery.result), Duration.Inf))
  println(Await.result(db.run(selectIndexedNamesQuery.result), Duration.Inf))


}
