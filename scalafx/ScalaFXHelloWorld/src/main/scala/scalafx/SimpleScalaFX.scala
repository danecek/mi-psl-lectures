
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

object SimpleScalaFX extends JFXApp {
  stage = new PrimaryStage {
    title = "Simple ScalaFX App"
    scene = new Scene {
      root = new StackPane {
        padding = Insets(20)
        content = Seq(
          new Rectangle {
            width = 200
            height = 100
            fill = Color.DEEPSKYBLUE
          },
          new Text {
            text = "ScalaFX"
            style = "-fx-font-size: 50px;-fx-fill: linear-gradient(yellow, red);"
          })
      }
    }
  }

}

