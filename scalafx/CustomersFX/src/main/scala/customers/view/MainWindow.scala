package customers.view

import customers.CustomersMenuBar
import scalafx.scene.layout.VBox

object MainWindow extends VBox {

  children.addAll(
    CustomersMenuBar,
    CustomersTable)

}



