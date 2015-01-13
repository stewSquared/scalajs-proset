import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.{jQuery => $}

object Proset extends JSApp {
  val DOTS = 1 to 6
  var deck = Deck()

  def main(): Unit = {
    $(setupUI _)
    $(deal _)
  }

  def setupUI(): Unit = {
    $("body").append(View.gameTable.render)
    $("#game-table")
      .dblclick(submit _)
    (1 to 7) foreach { n =>
      $(s"#slot-$n")
        .click(toggle(n) _)
    }
  }

  def tearDownUI(): Unit = {
    $("#game-table").remove()
  }

  def insert(card: Int): Unit =
    $("#game-table .slot:empty:first").append(View.card(card).render)

  def deal(): Unit = deck.upcards
    .filter(n => $(s"#card-$n").length == 0)
    .map(insert _)

  @JSExport
  def toggle(slot: Int)(): Unit = {
    if ($(s"#slot-$slot .card-chosen").length == 0) {
      select(slot)
    } else {
      deselect(slot)
    }
  }

  def select(slot: Int): Unit = {
    $(s"#game-table #slot-$slot .card")
      .attr("class", "card card-chosen")
  }

  def deselect(slot: Int): Unit =
    $(s"#game-table #slot-$slot .card")
      .attr("class", "card")

  @JSExport
  def submit(): Unit = {
    val chosen: Set[Int] = (1 to 7)
      .filter(n => $(s"#slot-$n .card-chosen").length > 0)
      .map(n => $(s"#slot-$n .card-chosen").attr("id").split('-').last.toInt)
      .toSet
    val newDeck = deck.remove(chosen)
    if (newDeck.upcards != deck.upcards) {
      this.deck = newDeck
      $(".card-chosen").remove()
      deal()
    } else {
      (1 to 7) foreach deselect
    }
  }

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
