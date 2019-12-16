package origin
import slick.jdbc.H2Profile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Suppliers(tag: Tag)
  extends Table[(Int, String)](tag, "SUPPLIERS") {

  def id: Rep[Int] = column[Int]("SUP_ID", O.PrimaryKey)
  def name: Rep[String] = column[String]("SUP_NAME")

  def * : ProvenShape[(Int, String)] =  (id, name)
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
  val db: Database = Database.forConfig("h2mem1")
  try {
    val suppliers: TableQuery[origin.Suppliers] = TableQuery[origin.Suppliers]
    val coffees: TableQuery[origin.Coffees] = TableQuery[origin.Coffees]

    val setupAction: DBIO[Unit] = DBIO.seq(
      (suppliers.schema ++coffees.schema).create,
      suppliers+=(101, "Acme, Inc."),
      suppliers += (49, "Superior Coffee"),
      suppliers += (150, "The High Ground")
    )

    val setupFuture: Future[Unit] = db.run(setupAction)

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

      combinedFuture.map { allSuppliers =>
        allSuppliers.foreach(println)
      }

    }.flatMap { _ =>

      /* Streaming */

      val coffeeNamesAction: StreamingDBIO[Seq[String], String] =
        coffees.map(_.name).result

      val coffeeNamesPublisher =
        db.stream(coffeeNamesAction)

      coffeeNamesPublisher.foreach(println)

    }.flatMap { _ =>

      /* Filtering / Where */

      // Construct a query where the price of p2.Coffees is > 9.0
      val filterQuery: Query[origin.Coffees, (String, Int, Double), Seq] =
        coffees.filter(_.price > 9.0)

      // Print the SQL for the filter query
      println("Generated SQL for filter query:\n" + filterQuery.result.statements)

      // Execute the query and print the Seq of results
      db.run(filterQuery.result.map(println))

    }.flatMap { _ =>

      /* Update */

      // Construct an update query with the sales column being the one to update
      val updateQuery: Query[Rep[Double], Double, Seq] = coffees.map(_.price)

      val updateAction: DBIO[Int] = updateQuery.update(1)

      // Print the SQL for the p2.Coffees update query
      println("Generated SQL for p2.Coffees update:\n" + updateQuery.updateStatement)

      // Perform the update
      db.run(updateAction.map { numUpdatedRows =>
        println(s"Updated $numUpdatedRows rows")
      })

    }.flatMap { _ =>

      /* Delete */

      // Construct a delete query that deletes coffees with a price less than 8.0
      val deleteQuery: Query[origin.Coffees, (String, Int, Double), Seq] =
        coffees.filter(_.price < 8.0)

      val deleteAction = deleteQuery.delete

      // Print the SQL for the p2.Coffees delete query
      println("Generated SQL for p2.Coffees delete:\n" + deleteAction.statements)

      // Perform the delete
      db.run(deleteAction).map { numDeletedRows =>
        println(s"Deleted $numDeletedRows rows")
      }

    }.flatMap { _ =>

      /* Sorting / Order By */

      val sortByPriceQuery: Query[origin.Coffees, (String, Int, Double), Seq] =
        coffees.sortBy(_.price)

      println("Generated SQL for query sorted by price:\n" +
        sortByPriceQuery.result.statements)

      // Execute the query
      db.run(sortByPriceQuery.result).map(println)

    }.flatMap { _ =>

      /* Query Composition */

      val composedQuery: Query[Rep[String], String, Seq] =
        coffees.sortBy(_.name).take(3).filter(_.price > 9.0).map(_.name)

      println("Generated SQL for composed query:\n" +
        composedQuery.result.statements)

      // Execute the composed query
      db.run(composedQuery.result).map(println)

    }.flatMap { _ =>

      /* Joins */

      // Join the tables using the relationship defined in the p2.Coffees table
      val joinQuery: Query[(Rep[String], Rep[String]), (String, String), Seq] = for {
        c <- coffees if c.price > 9.0
        s <- c.supplier
      } yield (c.name, s.name)

      println("Generated SQL for the join query:\n" + joinQuery.result.statements)

      // Print the rows which contain the coffee name and the supplier name
      db.run(joinQuery.result).map(println)

    }.flatMap { _ =>

      /* Computed Values */

      // Create a new computed column that calculates the max price
      val maxPriceColumn: Rep[Option[Double]] = coffees.map(_.price).max

      println("Generated SQL for max price column:\n" + maxPriceColumn.result.statements)

      // Execute the computed value query
      db.run(maxPriceColumn.result).map(println)

    }.flatMap { _ =>

      /* Manual SQL / String Interpolation */

      // A value to insert into the statement
      val state = "CA"

      // Construct a SQL statement manually with an interpolated value
      val plainQuery = sql"select SUP_NAME from SUPPLIERS where STATE = $state".as[String]

      println("Generated SQL for plain query:\n" + plainQuery.statements)

      // Execute the query
      db.run(plainQuery).map(println)

    }
    Await.result(f, Duration.Inf)

  } finally db.close
}
