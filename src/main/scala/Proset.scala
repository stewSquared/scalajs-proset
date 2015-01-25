import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.{jQuery => $}

object Proset extends JSApp {
  val NUM_DOTS = 6

  def main(): Unit = {
    val game = new Proset(NUM_DOTS)
    game.setupUI()
    $(game.deal _)
  }
}

class Proset(numDots: Int) {
  private val slots = 0 to numDots
  private val numCards = math.pow(2,numDots).toInt
  private var deck = Deck(numDots)

  def setupUI(): Unit = {
    $("body").append(View.gameTable.render)
    $("#game-table")
      .dblclick(submit _)
    slots foreach { n =>
      $(s"#slot-$n")
        .click(toggle(n) _)
    }
  }

  def tearDownUI(): Unit = $("#game-table").remove()

  def deal(): Unit = deck.upcards
    .filter(n => $(s"#card-$n").length == 0)
    .map(insert _)

  def insert(card: Int): Unit =
    $("#game-table .slot")
      .filter(":not(:has(.card))").first()
      .prepend(View.card(card).render)

  @JSExport
  def toggle(slot: Int)(): Unit = {
    if ($(s"#slot-$slot .card-chosen").length == 0) {
      select(slot)
    } else {
      deselect(slot)
    }
  }

  def select(slot: Int): Unit =
    $(s"#slot-$slot .card").attr("class", "card card-chosen")
      
  def deselect(slot: Int): Unit =
    $(s"#slot-$slot .card").attr("class", "card")

  @JSExport
  def submit(): Unit = {
    val chosen: Set[Int] = slots
      .map(n => $(s"#slot-$n .card-chosen"))
      .filter(_.length > 0)
      .map(_.attr("id").split('-').last.toInt)
      .toSet // NOTE: assumes .card number n has #card-n
    val newDeck = deck.remove(chosen)
    if (newDeck.upcards == this.deck.upcards) {
      slots foreach deselect
    } else {
      this.deck = newDeck
      $(".card-chosen").attr("class", "card-won")
      deal()
    }
  }

  object View {
    import scalatags.Text.all._

    def gameTable =
      div(id:="game-table")(
        slots.map(n => div(cls:="slot", id:=s"slot-$n")): _*)

    def card(card: Int) = {
      val bits = (card % numCards).toBinaryString.reverse.take(numDots)
      //val bits =  f"${(card % 64).toBinaryString.toInt}%06d".reverse
      div(cls:="card", id:=s"card-$card")(
        (bits.zipWithIndex
          .filter{ case (bit, _) => bit == '1' }
          .map{ case (_, dot) => div(cls:=s"dot dot-$dot") }
        ): _*)
    }
  }
}
