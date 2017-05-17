package monads

/**
  * Created by danecek on 5/12/17.
  */
case class Person(name: String, child: Option[Person])

object Main {

  val grandson = Person("grandson", None)
  val father = Person("father", Some(grandson))
  val grandfather = Person("grandfather", Some(father))
  val persons: Map[String, Person] =
    Map(grandfather.name -> grandfather,
      father.name -> father,
      grandson.name -> grandson
    )

  def grandson(grandfatherName: String): Option[String] = {
    val grandfather: Option[Person] = persons.get(grandfatherName)
    if (grandfather.isEmpty) None
    else {
      val father = grandfather.get.child
      if (father.isEmpty) None
      else {
        val son = father.get.child
        if (son.isEmpty) None
        else Some(son.get.name)
      }
    }
  }

  def main(args: Array[String]) {

    println(persons.get("father").flatMap(_.child).flatMap(_.child).flatMap(p => Option(p.name)))
    val grandsonName: Option[String] = persons.get("grandfather"). // None or Some(grandfather)
      flatMap(_.child). // None or Some(father)
      flatMap(_.child). // None or Some(son)
      flatMap(p => Option(p.name)) // None or (Some(son's name)

    println(grandson("grandfather"))

    println(for (gf <- persons.get("grandfather");
                 f <- gf.child;
                 gs <- f.child
    ) yield gs.name)
    val grandsonName2: Option[String] = for (gf <- persons.get("father");
                                             f <- gf.child;
                                             gs <- f.child
    ) yield gs.name


  }
}
