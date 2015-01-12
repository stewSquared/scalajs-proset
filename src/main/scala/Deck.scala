class Deck(val cards: List[Int], numUpcards: Int) {
  def upcards = cards.take(numUpcards)
}

object Deck {
  def apply(): Deck = this(6)

  def apply(numDots: Int) = {
    val maxVal = math.pow(2, numDots).toInt - 1
    new Deck(util.Random.shuffle(1 to maxVal).toList, numDots+1)
  }
}
