import utest._

import org.scalajs.jquery.jQuery

object Tests extends TestSuite {
  val NUM_DOTS = 6

  def inFreshGame(tests: Proset => Unit): Unit = {
    val game = new Proset(NUM_DOTS)
    game.setupUI()
    try {
      tests(game)
    } finally {
      game.tearDownUI()
    }
  }

  def cardDotsToBinary(card: String): String = (0 until 6)
    .map(dot => jQuery(s"#game-table $card .dot-$dot").length.toString.head)
    .mkString
    .reverse

  def tests = TestSuite {
    'SetupTeardown {
      val game = new Proset(NUM_DOTS)
      game.setupUI()
      assert(jQuery("#game-table").length == 1)
      assert(jQuery("#game-table .slot").length == 7)
      game.tearDownUI()
      assert(jQuery("#game-table").length == 0)
    }

    'Card63 (inFreshGame { game =>
      game.insert(63)
      assert(jQuery("#card-63").length == 1)
      assert(cardDotsToBinary("#card-63") == "111111")
    })

    'Card32 (inFreshGame { game =>
      game.insert(32)
      assert(jQuery("#card-32").length == 1)
      assert(cardDotsToBinary("#card-32") == "100000")
    })

    'Card1 (inFreshGame { game =>
      game.insert(1)
      assert(jQuery("#card-1").length == 1)
      assert(cardDotsToBinary("#card-1") == "000001")
    })

    'Dealing (inFreshGame { game =>
      game.deal()
      assert(jQuery("#game-table .card").length == 7)
      assert(jQuery("#game-table .card-chosen").length == 0)
    })

    'InsertCardIntoTable (inFreshGame { game =>
      game.insert(63)
      assert(jQuery(".card").length == 1)
      assert(jQuery("#card-63").length == 1)
      assert(jQuery("#game-table #slot-0 #card-63").length == 1)
    })

    'InsertMultipleCards (inFreshGame { game =>
      game.insert(63)
      game.insert(32)
      assert(jQuery("#game-table .slot .card").length == 2)
      assert(jQuery("#game-table #slot-0 #card-63").length == 1)
      assert(jQuery("#game-table #slot-1 .card").length == 1)
      assert(jQuery("#card-32").length == 1)
      assert(jQuery("#game-table #slot-2 .card").length == 0)
    })

    'Select (inFreshGame { game =>
      game.deal()
      game.select(1)
      assert(jQuery("#game-table .card-chosen").length == 1)
      game.select(1)
      game.select(2)
      assert(jQuery("#game-table .card-chosen").length == 2)
      assert(jQuery("#game-table .card").length == 7)
    })

    'Deselect (inFreshGame { game =>
      game.deal()
      game.select(1)
      game.deselect(1)
      assert(jQuery("#game-table .card-chosen").length == 0)
      assert(jQuery("#game-table .card").length == 7)
      game.deselect(2)
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
