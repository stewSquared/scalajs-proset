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
  }
}
