package customers.actions

import javafx.event.{ActionEvent, EventHandler}
import scalafx.application.Platform

object ExitAction extends CustomersAction {
  override val title: String = "Exit"
  override val handler: EventHandler[ActionEvent] =  _=>Platform.exit()
}
