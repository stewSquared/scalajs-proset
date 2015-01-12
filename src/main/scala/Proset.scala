import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery

import scalatags.Text.all._

object Proset extends JSApp {
  val DOTS = 1 to 6

  def main(): Unit = {
    jQuery(setupUI _)
  }

  def setupUI(): Unit = {
    jQuery("body").append(div(id:="game-table").render)
  }

  def appendCard(): Unit = {
    jQuery("#game-table").append(
      div(cls:="card")(
        DOTS map (n => div(cls:=s"dot dot-$n")): _*).render)
  }
}
