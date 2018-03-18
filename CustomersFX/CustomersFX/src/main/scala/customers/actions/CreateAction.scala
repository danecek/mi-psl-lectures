package customers.actions

import customers.CreateDialog
import javafx.event.{ActionEvent, EventHandler}

object CreateAction extends CustomersAction {
  override val title: String = "Create"
  override val handler: EventHandler[ActionEvent] = _=>new  CreateDialog().exec()
}
