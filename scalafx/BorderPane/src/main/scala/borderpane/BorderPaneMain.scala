package borderpane

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object BorderPaneMain extends JFXApp {

  stage = new PrimaryStage {
    title = "Border Pane"
    scene = new Scene() {
      content = List(
        new BorderPane {
          left = new Label("left")
          right = new Label("right")
          center = new Label("center")
          bottom  = new Label("bottom")
          top = new Label("top")
        }
      )
    }

  }


}
