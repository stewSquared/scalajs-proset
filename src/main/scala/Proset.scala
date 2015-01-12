import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery

object Proset extends JSApp {
  val DOTS = 1 to 6
  var deck = Deck()

  def main(): Unit = {
    jQuery(setupUI _)
  }

  def setupUI(): Unit = {
    jQuery("body").append(View.gameTable.render)
  }

  def tearDownUI(): Unit = {
    jQuery("#game-table").remove()
  }

  def insert(card: Int): Unit =
    jQuery("#game-table .slot:empty:first").append(View.card(card).render)

  def deal(): Unit = deck.upcards.map(insert _)

  object View {
    import scalatags.Text.all._

    def gameTable =
      div(id:="game-table")(
        (1 to 7).map(n => div(cls:="slot", id:=s"slot-$n")): _*)

    def card(num: Int) = {
      val bits = f"${(num % 64).toBinaryString.toInt}%06d" //TODO: hardcoded everything
      div(cls:="card", id:=s"card-$num")(
        ((DOTS zip bits)
          .filter{ case (_, bit) => bit == '1' }
          .map{ case (num, _) => div(cls:=s"dot dot-$num") }
        ): _*)
    }
  }
}
