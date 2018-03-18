package customers

import customers.view.MainWindow
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene


object ScalaFXCustomers extends JFXApp {

  stage = new PrimaryStage {
    title = "ScalaFX Customers"
    scene = new Scene(MainWindow, 800, 600)
  }
}