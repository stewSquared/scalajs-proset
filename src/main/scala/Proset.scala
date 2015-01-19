import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.{jQuery => $}

object Proset extends JSApp {
  val NUM_DOTS = 6
  val SLOTS = 1 to NUM_DOTS+1
  val NUM_CARDS = math.pow(2,NUM_DOTS).toInt

  var deck = Deck(NUM_DOTS)

  def main(): Unit = {
    setupUI()
    $(deal _)
  }

  def setupUI(): Unit = {
    $("body").append(View.gameTable.render)
    $("#game-table")
      .dblclick(submit _)
    SLOTS foreach { n =>
      $(s"#slot-$n")
        .click(toggle(n) _)
    }
  }

  def tearDownUI(): Unit = $("#game-table").remove()

  def deal(): Unit = deck.upcards
    .filter(n => $(s"#card-$n").length == 0)
    .map(insert _)

  def insert(card: Int): Unit =
    $("#game-table .slot:empty:first").append(View.card(card).render)

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
    val chosen: Set[Int] = SLOTS
      .map(n => $(s"#slot-$n .card-chosen"))
      .filter(_.length > 0)
      .map(_.attr("id").split('-').last.toInt)
      .toSet // NOTE: assumes .card number n has #card-n
    val newDeck = deck.remove(chosen)
    if (newDeck.upcards == this.deck.upcards) {
      SLOTS foreach deselect
    } else {
      this.deck = newDeck
      $(".card-chosen").remove()
      deal()
    }
  }

  object View {
    import scalatags.Text.all._

    def gameTable =
      div(id:="game-table")(
        SLOTS.map(n => div(cls:="slot", id:=s"slot-$n")): _*)

    def card(card: Int) = {
      val bits = (card % NUM_CARDS).toBinaryString.reverse.take(NUM_DOTS)
      //val bits =  f"${(card % 64).toBinaryString.toInt}%06d".reverse
      div(cls:="card", id:=s"card-$card")(
        (bits.zipWithIndex
          .filter{ case (bit, _) => bit == '1' }
          .map{ case (_, dot) => div(cls:=s"dot dot-$dot") }
        ): _*)
    }
  }
}
