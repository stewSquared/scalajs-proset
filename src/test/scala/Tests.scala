import utest._

import org.scalajs.jquery.jQuery

object Tests extends TestSuite {

  // Initialize App
  Proset.setupUI()

  def tests = TestSuite {
    'BoardExists {
      assert(jQuery("#game-table").length == 1)
    }
  }
}
