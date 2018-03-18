package customers

import customers.view.MainWindow
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.BorderPane


object ScalaFXCustomers extends JFXApp {

  stage = new PrimaryStage {
    title = "ScalaFX Customers"
    scene = new Scene(MainWindow, 800, 600)
//    {
//      root = new MainWindow
//    }
  }
}