package examples

import java.util.Date

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.HBox
import scalafx.scene.text.Text

object Buttons extends JFXApp {

  stage = new PrimaryStage {

    scene = new Scene {

      content = new HBox(5) {
        padding = Insets(5)
        val lab = new Text {
          prefWidth = 220
        }
        content = List(
          lab,
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
