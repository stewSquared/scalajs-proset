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

  def cardDotsToBinary(card: String
  )(implicit game: Proset): String = (0 until NUM_DOTS)
    .map(dot => jQuery(game.gameTable).find(s"$card .dot-$dot").length.toString.head)
    .mkString
    .reverse

  def tests = TestSuite {
    'SetupTeardown {
      val game = new Proset(NUM_DOTS)
      game.setupUI()
      assert(jQuery(game.gameTable).find(".slot").length == 7)
      game.tearDownUI()
    }

    'Card63 (inFreshGame { implicit game =>
      game.insert(63)
      assert(jQuery("#card-63").length == 1)
      assert(cardDotsToBinary("#card-63") == "111111")
    })

    'Card32 (inFreshGame { implicit game =>
      game.insert(32)
      assert(jQuery("#card-32").length == 1)
      assert(cardDotsToBinary("#card-32") == "100000")
    })

    'Card1 (inFreshGame { implicit game =>
      game.insert(1)
      assert(jQuery("#card-1").length == 1)
      assert(cardDotsToBinary("#card-1") == "000001")
    })

    'Dealing (inFreshGame { implicit game =>
      game.deal()
      assert(jQuery(game.gameTable).find(".card").length == 7)
      assert(jQuery(game.gameTable).find(".card-chosen").length == 0)
    })

    'InsertCardIntoTable (inFreshGame { implicit game =>
      game.insert(63)
      assert(jQuery(".card").length == 1)
      assert(jQuery("#card-63").length == 1)
      assert(jQuery(game.slots(0)).find("#card-63").length == 1)
    })

    'InsertMultipleCards (inFreshGame { implicit game =>
      game.insert(63)
      game.insert(32)
      assert(jQuery(game.gameTable).find(".slot .card").length == 2)
      assert(jQuery(game.slots(0)).find("#card-63").length == 1)
      assert(jQuery(game.slots(1)).find(".card").length == 1)
      assert(jQuery("#card-32").length == 1)
      assert(jQuery(game.slots(2)).find(".card").length == 0)
    })

    'Select (inFreshGame { implicit game =>
      game.deal()
      game.select(game.slots(1))
      assert(jQuery(game.gameTable).find(".card-chosen").length == 1)
      game.select(game.slots(1))
      game.select(game.slots(2))
      assert(jQuery(game.gameTable).find(".card-chosen").length == 2)
      assert(jQuery(game.gameTable).find(".card").length == 7)
    })

    'Deselect (inFreshGame { implicit game =>
      game.deal()
      game.select(game.slots(1))
      game.deselect(game.slots(1))
      assert(jQuery(game.gameTable).find(".card-chosen").length == 0)
      assert(jQuery(game.gameTable).find(".card").length == 7)
      game.deselect(game.slots(2))
      assert(jQuery(game.gameTable).find(".card-chosen").length == 0)
      assert(jQuery(game.gameTable).find(".card").length == 7)
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
