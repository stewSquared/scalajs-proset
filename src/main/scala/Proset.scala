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

  def appendCard(card: Int): Unit = {
    jQuery("#game-table").append(
      div(cls:="card", id:=s"card-$card")(
        ((DOTS zip f"${(card % 64).toBinaryString.toInt}%06d")
          .filter{ case (_, bit) => bit == '1' }
          .map{ case (num, _) => div(cls:=s"dot dot-$num") }
        ): _*).render)
  }
}
