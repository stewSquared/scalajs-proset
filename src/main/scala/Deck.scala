import scala.scalajs.js.annotation.JSExport

class Deck(val cards: List[Int], numUpcards: Int) {
  def upcards = cards.take(numUpcards).toSet

  private def isProset(chosen: Set[Int]) = chosen.reduce(_ ^ _) == 0

  def remove(chosen: Set[Int]) =
    if (!chosen.isEmpty && (chosen subsetOf upcards) && isProset(chosen))
      new Deck(cards diff (chosen.toList), numUpcards)
    else
      this

  def isEmpty = upcards.size == 0
}

object Deck {
  def apply(numDots: Int) = {
    val maxVal = math.pow(2, numDots).toInt - 1
    new Deck(util.Random.shuffle(1 to maxVal).toList, numDots+1)
  }
}
