
package customers

import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.Text


class CreateDialog extends Dialog[Customer] {
  initOwner(ScalaFXCustomers.stage)
  title = "Create"

  // Set the button types.
  //  val loginButtonType = new ButtonType("Login", ButtonData.OKDone)

  dialogPane().getButtonTypes.addAll(ButtonType.OK, ButtonType.Cancel)

  // Create the username and password labels and fields.
  val name = new TextField() {
    promptText = "Name"
    onKeyReleased = _ => myvalidate
  }
  val address = new TextField() {
    promptText = "Address"
    onKeyReleased = _ => myvalidate
  }

  val grid: GridPane = new GridPane() {
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(new Label("Name:"), 0, 0)
    add(name, 1, 0)
    add(new Label("Address:"), 0, 1)
    add(address, 1, 1)
  }

  val error = new Text {
    fill = Color.Red
  }

  dialogPane().setContent(new VBox(grid, error))

  //  // Request focus on the username field by default.
  Platform.runLater(name.requestFocus())

  // When the login button is clicked, convert the result to
  // a username-password-pair.

  resultConverter = dialogButton =>
    if (dialogButton == ButtonType.OK)
      Customer(name.getText, address.getText)
    else
      null

  myvalidate()

  def exec() {
    showAndWait() match {
      case Some(c@Customer(n, a)) => Data.addCustomer(c)
      case _ =>
    }
  }

  def myvalidate() {
    dialogPane.value.lookupButton(ButtonType.OK).setDisable(true)
    if (name.text.value.isEmpty) {
      error.text = "invalid name"
    }
    else if (address.text.value.isEmpty)
      error.text = "invalid adddress"
    else {
      dialogPane.value.lookupButton(ButtonType.OK).setDisable(false)
      error.text.value = ""
    }

  }

}
