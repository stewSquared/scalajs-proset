import utest._

import org.scalajs.jquery.jQuery

object Tests extends TestSuite {

  def inFreshGame(tests: => Unit): Unit = {
    Proset.setupUI()
    try {
      tests
    } finally {
      Proset.tearDownUI()
    }
  }

  def cardDotsToBinary(card: String): String = (0 until 6)
    .map(dot => jQuery(s"#game-table $card .dot-$dot").length.toString.head)
    .mkString
    .reverse

  def tests = TestSuite {
    'SetupTeardown {
      Proset.setupUI()
      assert(jQuery("#game-table").length == 1)
      assert(jQuery("#game-table .slot").length == 7)
      Proset.tearDownUI()
      assert(jQuery("#game-table").length == 0)
    }

    'Card63 (inFreshGame {
      Proset.insert(63)
      assert(jQuery("#card-63").length == 1)
      assert(cardDotsToBinary("#card-63") == "111111")
    })

    'Card32 (inFreshGame {
      Proset.insert(32)
      assert(jQuery("#card-32").length == 1)
      assert(cardDotsToBinary("#card-32") == "100000")
    })

    'Card1 (inFreshGame {
      Proset.insert(1)
      assert(jQuery("#card-1").length == 1)
      assert(cardDotsToBinary("#card-1") == "000001")
    })

    'Dealing (inFreshGame{
      Proset.deal()
      assert(jQuery("#game-table .card").length == 7)
      assert(jQuery("#game-table .card-chosen").length == 0)
    })

    'InsertCardIntoTable (inFreshGame{
      Proset.insert(63)
      assert(jQuery(".card").length == 1)
      assert(jQuery("#card-63").length == 1)
      assert(jQuery("#game-table #slot-1 #card-63").length == 1)
    })

    'InsertMultipleCards (inFreshGame{
      Proset.insert(63)
      Proset.insert(32)
      assert(jQuery("#game-table .slot .card").length == 2)
      assert(jQuery("#game-table #slot-1 #card-63").length == 1)
      assert(jQuery("#game-table #slot-2 .card").length == 1)
      assert(jQuery("#card-32").length == 1)
      assert(jQuery("#game-table #slot-3 .card").length == 0)
    })

    'Select (inFreshGame{
      Proset.deal()
      Proset.select(1)
      assert(jQuery("#game-table .card-chosen").length == 1)
      Proset.select(1)
      Proset.select(2)
      assert(jQuery("#game-table .card-chosen").length == 2)
      assert(jQuery("#game-table .card").length == 7)
    })

    'Deselect (inFreshGame{
      Proset.deal()
      Proset.select(1)
      Proset.deselect(1)
      assert(jQuery("#game-table .card-chosen").length == 0)
      assert(jQuery("#game-table .card").length == 7)
      Proset.deselect(2)
      assert(jQuery("#game-table .card-chosen").length == 0)
      assert(jQuery("#game-table .card").length == 7)
    })

    'Upcards {
      val d = Deck(6)
      assert(d.upcards.toSet.size == 7)
    }

    'Removal {
      val d = new Deck((1 to 7).toList, 4)
      assert(d.upcards == Set(1,2,3,4))
      assert(d.remove(Set(1,2,3)).upcards == Set(4,5,6,7))
    }

    'RemovalFailsWhenChosenAreNotProset {
      val d = new Deck((1 to 7).toList, 4)
      assert(d.remove(Set(2,3)).upcards == Set(1,2,3,4))
    }
  }
}
