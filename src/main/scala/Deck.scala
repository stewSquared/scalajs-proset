import scala.scalajs.js.annotation.JSExport

class Deck(val cards: List[Int], numUpcards: Int) {
  def upcards = cards.take(numUpcards).toSet

  def remove(chosen: Set[Int]) =
    if (!chosen.isEmpty && (chosen subsetOf upcards) && proset(chosen))
      new Deck(cards diff (chosen.toList), numUpcards)
    else
      this

  def isEmpty = upcards.size == 0

  def proset(chosen: Set[Int]) = chosen.reduce(_ ^ _) == 0
}

object Deck {
  def apply(): Deck = this(6)

  def apply(numDots: Int) = {
    val maxVal = math.pow(2, numDots).toInt - 1
    new Deck(util.Random.shuffle(1 to maxVal).toList, numDots+1)
  }
}
