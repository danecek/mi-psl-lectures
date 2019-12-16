package assoc
import slick.driver.H2Driver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import slick.driver.H2Driver

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

case class Supplier(name: String, id: Option[Int] = None)

class Suppliers(tag: Tag)
  extends Table[Supplier](tag, "SUPPLIERS") {
  def id: Rep[Int] = column[Int]("SUP_ID", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("SUP_NAME")

  def * : ProvenShape[Supplier] = (name, id.?) <> (Supplier.tupled, Supplier.unapply)
}

case class Coffee(name: String, price: Double, supId: Int)

class Coffees(tag: Tag)
  extends Table[Coffee](tag, "COFFEES") {

  def name: Rep[String] = column[String]("COF_NAME", O.PrimaryKey)

  def supId: Rep[Int] = column[Int]("SUP_ID")

  def price: Rep[Double] = column[Double]("PRICE")

  def * : ProvenShape[Coffee] = (name, price, supId) <> (Coffee.tupled, Coffee.unapply)

  // A reified foreign key relation that can be navigated to create a join
  def supplier: ForeignKeyQuery[origin.Suppliers, (Int, String)] =
    foreignKey("SUP_FK", supId, TableQuery[origin.Suppliers])(_.id)
}

object Assoc extends App {

  def execAction[T](a: DBIO[T], mess: String = ""): Unit = {
    println(s"$mess : ${Await.result(db.run(a), Duration.Inf)}")
  }

  val db = Database.forConfig("h2mem1")
  val suppliers: TableQuery[Suppliers] = TableQuery[Suppliers]
  println(suppliers.result.statements.mkString)
  val suppliersIds: Query[Rep[Int], Int, Seq] = suppliers.map(_.id)
  println(suppliersIds.result.statements.mkString)
  val pkgen: H2Driver.ReturningInsertActionComposer[Supplier, Int] = suppliers returning suppliersIds
  println(pkgen.insertStatement.mkString)

  val coffees: TableQuery[Coffees] = TableQuery[Coffees]

  val createAction: DBIO[Unit] = DBIO.seq((suppliers.schema ++ coffees.schema).create)

  execAction(createAction)

  val insertAction: DBIO[Int] = suppliers += Supplier("Acme, Inc.")

  execAction(insertAction, "insertAction")

  val PK_Action = pkgen += Supplier("Dave")

  val a: DBIO[Int] = PK_Action.flatMap((id: Int) => coffees += Coffee("xxx", 0.1, id)).map((i: Int) =>i*i)

  val a2: DBIO[Int] =
    for {id <- PK_Action
         i <- coffees += Coffee("xxx", 0.1, id)} yield i*i

  execAction(PK_Action, "PK_Action")

  val selectAllAction: DBIO[Seq[Supplier]] = suppliers.result
  val printAllAction: DBIO[Unit] = selectAllAction.map(println)

  execAction(printAllAction, "printAllAction")

  //        suppliers += (49, "Superior Coffee"),
  //        suppliers += (150, "The High Ground")*/

  /*
  val setupFuture: Future[Unit] = db.run(createAction)

t
    val f: Future[Unit] = setupFuture.flatMap { _ =>

      val insertAction: DBIO[Option[Int]] = coffees ++= Seq(
        ("Colombian", 101, 7.99),
        ("French_Roast", 49, 8.99)
      )

      val insertAndPrintAction: DBIO[Unit] = insertAction.map { coffeesInsertResult =>
        coffeesInsertResult foreach { numRows =>
          println(s"Inserted $numRows rows into the p2.Coffees table")
        }
      }

      val allSuppliersAction: DBIO[Seq[(Int, String)]] =
        suppliers.result

      val combinedAction: DBIO[Seq[(Int, String)]] =
        insertAndPrintAction >> allSuppliersAction

      val combinedFuture: Future[Seq[(Int, String)]] =
        db.run(combinedAction)

      combinedFuture.map { allSuppliers => }
      finally db.close
      allSuppliers.foreach(println)
    }
  */


  //  Await.result(setupFuture, Duration.Inf)


}
