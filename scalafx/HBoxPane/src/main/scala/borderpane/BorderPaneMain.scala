package borderpane

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, _}
import scalafx.scene.layout.HBox

/**
 * Created by danecek on 15.3.15.
 */
object BorderPaneMain extends JFXApp {

  stage = new PrimaryStage {
    title = "Horizontal Box"
    scene = new Scene() {
      content = List {
        new HBox {
          padding = Insets(20)
          spacing = 5
          alignment = Pos.CENTER
          content = List(
            new Label { text = "Text:" },
            new TextField { promptText = "Type something..." },
            new Button { text = "Search..." })
        }
      }
    }

  }


}
