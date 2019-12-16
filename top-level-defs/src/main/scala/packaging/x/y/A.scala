package packaging

package x.y {

  object A

}

package x {
  package y {

    object B {

      def main(args: Array[String]) {
        println(A)
      }

    }

  }

}


