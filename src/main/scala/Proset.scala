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

  def tearDownUI(): Unit = {
    jQuery("#game-table").remove()
  }

  def appendCard(card: Int): Unit = {
    val bits = f"${(card % 64).toBinaryString.toInt}%06d" //TODO: hardcoded everything
    jQuery("#game-table").append(
      div(cls:="card", id:=s"card-$card")(
        ((DOTS zip bits)
          .filter{ case (_, bit) => bit == '1' }
          .map{ case (num, _) => div(cls:=s"dot dot-$num") }
        ): _*).render)
  }

  def deal(): Unit = ???
}
