package examples

import java.util.Date

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Separator}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text

object Buttons extends JFXApp {

  stage = new PrimaryStage {

    scene = new Scene(400, 300) {
      root = new VBox(5) {
        padding = Insets(5)
        val lab = new Text() {
          prefWidth = 220
        }
        children = List(
          lab,
          new Separator(),
          new Button("Time") {
            onAction = handle {
              lab.text = (new Date).toString
            }
          }

        )

      }


    }

  }
}
