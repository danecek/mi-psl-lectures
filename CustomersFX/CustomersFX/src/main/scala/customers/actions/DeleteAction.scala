package customers.actions

import java.util

import customers.{Customer, Data}
import customers.view.CustomersTable
import javafx.collections.ObservableList
import javafx.event.{ActionEvent, EventHandler}
import scalafx.application.Platform

import scala.collection.JavaConverters

object DeleteAction extends CustomersAction {

  val title = "Delete"

  val handler: EventHandler[ActionEvent] = _ => {

    val ji: java.util.Iterator[Customer] = CustomersTable.getSelectionModel.getSelectedItems.iterator()

    val si: scala.Iterator[Customer] = JavaConverters.asScalaIterator(ji)

    Data.data --=si
  }


}
