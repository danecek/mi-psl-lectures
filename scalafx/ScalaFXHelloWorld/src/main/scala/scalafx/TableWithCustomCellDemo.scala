package scalafx

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableCell, TableColumn, TableView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

object TableWithCustomCellDemo extends JFXApp {
  val characters = ObservableBuffer[Person](
    new Person("Peggy", "Sue", Color.VIOLET),
    new Person("Rocky", "Raccoon", Color.GREENYELLOW),
    new Person("Bungalow ", "Bill", Color.DARKSALMON)
  )

  this.stage = new PrimaryStage {
    title = "TableView with custom color cell"
    scene = new Scene {
      content = new TableView[Person](characters) {
        columns ++= List(
          new TableColumn[Person, String] {
            text = "First Name"
            cellValueFactory = _.value.firstName
            prefWidth = 100
          },
          new TableColumn[Person, String]() {
            text = "Last Name"
            cellValueFactory = {
              _.value.lastName
            }
            prefWidth = 100
          },
          new TableColumn[Person, Color] {
            text = "Favorite Color"
            cellValueFactory = {
              _.value.favoriteColor
            }
            // Render the property value when it changes,
            // including initial assignment
            cellFactory = { _ =>
              new TableCell[Person, Color] {
                item.onChange { (aaa, bbb, newColor) =>
                  graphic = new Circle {
                    fill = newColor; radius = 8
                  }
                }
              }
            }
            prefWidth = 100
          }
        )
      }
    }
  }
}
