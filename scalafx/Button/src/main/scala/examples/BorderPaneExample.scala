package examples

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane

/**
  * Created by danecek on 15.3.15.
  */
object BorderPaneExample extends JFXApp {

  stage = new PrimaryStage {
    title = "BorderPaneExample"
    scene = new Scene {
      resizable = true
      content = new BorderPane {

        prefWidth = 200
        prefHeight = 100
        padding = Insets(5)
        val b = new Button("left") {
          alignmentInParent = Pos.Center
        }

        left = b

        right = new Button("right")
        center = new Button("center")
        val t = new Button("top")
        t.alignmentInParent  = Pos.Center
        top = t

        bottom = new Button("bottom")
      }
    }
  }
}
