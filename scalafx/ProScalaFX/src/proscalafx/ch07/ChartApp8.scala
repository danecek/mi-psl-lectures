package proscalafx.ch07

import javafx.scene.{chart => jfxsc}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.chart._
import scalafx.scene.layout.StackPane


/**
 * @author Jarek Sacha
 */
object ChartApp8 extends JFXApp {
  val xAxis = new CategoryAxis()
  val yAxis = new NumberAxis()
  val barChart = BarChart(xAxis, yAxis)
  barChart.title = "Speculations"
  barChart.data = createChartData()
  barChart.barGap = 1

  stage = new PrimaryStage {
    title = "BarChart example"
    scene = new Scene(400, 250) {
      root = new StackPane {
        children = barChart
      }
    }
  }


  // NOTE: explicit type signature using Number instead Int and Double
  // NOTE: use of jfxsc.XYChart.Series as type for ObservableBuffer, this the same as
  // signature for scalafx.scene.chart.XYChart.data used above.
  private def createChartData(): ObservableBuffer[jfxsc.XYChart.Series[String, Number]] = {
    var javaValue = 17.56
    var cValue = 17.06
    var cppValue = 8.25
    val answer = new ObservableBuffer[jfxsc.XYChart.Series[String, Number]]()
    val java = new XYChart.Series[String, Number] {
      name = "Java"
    }
    val c = new XYChart.Series[String, Number] {
      name = "C"
    }
    val cpp = new XYChart.Series[String, Number] {
      name = "C++"
    }
    for (i <- 2011 to 2016) {
      java.data() += XYChart.Data[String, Number](i.toString, javaValue)
      javaValue += math.random - .5

      c.data() += XYChart.Data[String, Number](i.toString, cValue)
      cValue += math.random - .5

      cpp.data() += XYChart.Data[String, Number](i.toString, cppValue)
      cppValue += math.random - .5
    }
    answer.addAll(java, c, cpp)
    answer
  }
}