import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.{jQuery => $}
import org.scalajs.dom.Element

object Proset extends JSApp {
  val NUM_DOTS = 4

  def main(): Unit = {
    val game = new Proset(NUM_DOTS)
    game.setupUI()
    $(game.deal _)
  }
}

class Proset(numDots: Int) {
  val numCards = math.pow(2,numDots).toInt
  val gameTable = View.gameTable.render
  val slots = Stream.continually(View.slot.render)
    .take(numDots+1).toList

  private var deck = Deck(numDots)

  def setupUI(): Unit = {
    $("body").append(gameTable)
    $(gameTable).dblclick(submit _)
    slots.foreach{ slot => 
      $(gameTable).append(slot)
      $(slot).click(toggle(slot) _)
    }
  }

  def tearDownUI(): Unit = $(gameTable).remove()

  def deal(): Unit = deck.upcards
    .filter(n => $(s"#card-$n").length == 0)
    .map(insert _)

  def insert(card: Int): Unit =
    $(slots
      .filter(s => $(s).find(".card").length == 0)
      .head)
      .prepend(View.card(card).render)

  @JSExport
  def toggle(slot: Element)(): Unit = {
    if ($(slot).find(".card-chosen").length == 0) {
      select(slot)
    } else {
      deselect(slot)
    }
  }

  def select(slot: Element): Unit =
    $(slot).find(".card").attr("class", "card card-chosen")
      
  def deselect(slot: Element): Unit =
    $(slot).find(".card").attr("class", "card")

  @JSExport
  def submit(): Unit = {
    val chosen: Set[Int] = slots
      .map(s => $(s).find(".card-chosen"))
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
    import scalatags.JsDom.all._

    def gameTable = div(cls:="game-table")

    def slot = div(cls:="slot")

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
