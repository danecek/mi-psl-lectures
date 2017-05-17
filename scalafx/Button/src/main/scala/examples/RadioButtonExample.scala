package examples

import javafx.scene.control

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

object RadioButtonExample extends JFXApp {

  stage = new PrimaryStage {
    title = "Grid Pane Example"
    scene = new Scene {
      content = new HBox() {
        spacing = 5
        padding = Insets(5)
        val target = new Circle {
          radius = 8; fill = Color.Red
        }
        val red = new RadioButton("Red");
        red.selected = true;
        val blue = new RadioButton("Blue");
        children = List(red, blue, target)
        red.onAction = handle {
          target.fill = Color.Red
        }
        blue.onAction = handle {
          target.fill = Color.Blue
        }
        new ToggleGroup() { toggles = List(red, blue) }
     }
    }
  }
}


