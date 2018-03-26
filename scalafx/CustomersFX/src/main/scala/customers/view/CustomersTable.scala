package customers.view

import customers.{Customer, Data}
import scalafx.beans.property.StringProperty
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{SelectionMode, TableColumn, TableView}

object CustomersTable extends TableView[Customer](Data.data) {
  columns ++= List(
    new TableColumn[Customer, String] {
      text = "Name"
      cellValueFactory = { x => new StringProperty(x.value.name) }
      prefWidth = 180
    },
    new TableColumn[Customer, String] {
      text = "Address"
      cellValueFactory = { x => new StringProperty(x.value.address) }
      prefWidth = 180
    }
  )

  this.getSelectionModel.setSelectionMode(SelectionMode.Multiple)


}

