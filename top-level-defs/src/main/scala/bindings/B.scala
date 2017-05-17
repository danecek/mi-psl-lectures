package bindings {

  object B {
    val x = 1
  }

}

package bindings {

  object Main {
    def main(args: Array[String]) {
      println(args) // binding by definition
      println(B) // binding by package clause
      import B.x
      print(x) // binding by import clause

    }
  }

}

