package customers.actions

import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.control.MenuItem

trait CustomersAction {
  val title: String
  val handler: EventHandler[ActionEvent]

  def menuItem = new MenuItem(title) {
    onAction = handler
  }

}
