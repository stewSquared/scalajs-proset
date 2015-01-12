import utest._

import org.scalajs.jquery.jQuery

object Tests extends TestSuite {

  // Initialize App
  Proset.setupUI()

  def tests = TestSuite {
    'BoardExists {
      assert(jQuery("#game-table").length == 1)
    }

    'Card63 {
      Proset.appendCard(63)
      assert(jQuery("#game-table #card-63").length == 1)

      for (n <- Proset.DOTS) {
        assert(jQuery(s"#game-table #card-63 .dot-$n").length == 1)
      }
    }

    'Card32 {
      Proset.appendCard(32)
      assert(jQuery("#game-table #card-32").length == 1)
      assert(jQuery(s"#game-table #card-32 .dot-1").length == 1)
      for (n <- 2 to 6) {
        assert(jQuery(s"#game-table #card-32 .dot-$n").length == 0)
      }
    }

    'Card1 {
      Proset.appendCard(1)
      assert(jQuery("#game-table #card-1").length == 1)
      for (n <- 1 to 5) {
        assert(jQuery(s"#game-table #card-1 .dot-$n").length == 0)
      }
      assert(jQuery(s"#game-table #card-1 .dot-6").length == 1)
    }
  }
}
