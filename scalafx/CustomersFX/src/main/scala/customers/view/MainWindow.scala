package customers.view

import customers.{CustomersMenuBar, Data}
import scalafx.scene.control.ListView
import scalafx.scene.layout.VBox

object MainWindow extends VBox {

  children.addAll(
    CustomersMenuBar,
    CustomersTable)

}



