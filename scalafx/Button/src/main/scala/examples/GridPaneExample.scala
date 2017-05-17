package examples

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.GridPane

/**
 * Created by danecek on 15.3.15.
 */
object GridPaneExample extends JFXApp {

  stage = new PrimaryStage {
    title = "Grid Pane Example"
    scene = new Scene {
      resizable = true
      content = new GridPane {
        padding = Insets(5)
        hgap = 5; vgap = 5
        gridLinesVisible = true
        prefWidth = 200; prefHeight = 100
        add(new Button("0,0"), 0, 0)
        add(new Button("1,0, colspan = 2"), 1, 0, colspan = 2, rowspan = 1)
        add(new Button("1,1"), 1, 1)
        add(new Button("2,2"), 2, 2)
      }
    }
  }
}
