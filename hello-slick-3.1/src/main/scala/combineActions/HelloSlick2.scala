package combineActions
import slick.driver.H2Driver.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Suppliers(tag: Tag)
  extends Table[(Int, String)](tag, "SUPPLIERS") {
  def id: Rep[Int] = column[Int]("SUP_ID", O.PrimaryKey)

  def name: Rep[String] = column[String]("SUP_NAME")

  def * : ProvenShape[(Int, String)] = (id, name)
}

class Coffees(tag: Tag)
  extends Table[(String, Int, Double)](tag, "COFFEES") {
  def name: Rep[String] = column[String]("COF_NAME", O.PrimaryKey)

  def supID: Rep[Int] = column[Int]("SUP_ID")

  def price: Rep[Double] = column[Double]("PRICE")

  def * : ProvenShape[(String, Int, Double)] =
    (name, supID, price)

  // A reified foreign key relation that can be navigated to create a join
  def supplier: ForeignKeyQuery[origin.Suppliers, (Int, String)] =
    foreignKey("SUP_FK", supID, TableQuery[origin.Suppliers])(_.id)
}

object HelloSlick extends App {
  val db = Database.forConfig("h2mem1")
  try {
    val suppliers: TableQuery[origin.Suppliers] = TableQuery[origin.Suppliers]
    val coffees: TableQuery[origin.Coffees] = TableQuery[origin.Coffees]

    val setupAction: DBIO[Unit] = DBIO.seq(
      (suppliers.schema ++ coffees.schema).create,
      suppliers += (101, "Acme, Inc."),
      suppliers += (49, "Superior Coffee"),
      suppliers += (150, "The High Ground")
    )

    val setupFuture: Future[Unit] = db.run(setupAction)

    val f: Future[Unit] = setupFuture.flatMap { _ =>

      val cofeeInsertAction: DBIO[Option[Int]] = coffees ++= Seq(
        ("Colombian", 101, 7.99),
        ("French_Roast", 49, 8.99)
      )

      val insertAndPrintAction: DBIO[Unit] = cofeeInsertAction.map { coffeesInsertResult: Option[Int] =>
        coffeesInsertResult foreach { numRows =>
          println(s"Inserted $numRows rows into the p2.Coffees table")
        }
      }

      val allSuppliersAction: DBIO[Seq[(Int, String)]] =
        suppliers.result

      val combinedAction: DBIO[Seq[(Int, String)]] =
        insertAndPrintAction >> allSuppliersAction //  insertAndPrintAction andThen allSuppliersAction

      val combinedFuture: Future[Seq[(Int, String)]] =
        db.run(combinedAction)

      combinedFuture.map { allSuppliers =>
        allSuppliers.foreach(println)
      }

    }
    Await.result(f, Duration.Inf)

  } finally db.close
}
