import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RollingWindowTransactionReportsSpec extends AnyFlatSpec with Matchers {

  val rw = new RollingWindowTransactionReports

  "rollingWindow" should "select only 2 days" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A1", 3, "BB", 10.00),
      Transaction("T0001", "A1", 4, "AA", 10.00)
    )
    rw.rollingWindow(data, 3, 2) shouldBe Map(
      "A1" -> List(Transaction("T0001", "A1", 1, "AA", 10.00),
                   Transaction("T0001", "A1", 2, "BB", 10.00))
    )
  }

  it should "group by account, over a period of 8 days with a window of 5" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A2", 3, "CC", 10.00),
      Transaction("T0001", "A1", 4, "DD", 10.00),
      Transaction("T0001", "A2", 5, "EE", 10.00),
      Transaction("T0001", "A1", 6, "FF", 10.00),
      Transaction("T0001", "A2", 7, "GG", 10.00),
      Transaction("T0001", "A1", 8, "BB", 10.00)
    )
    rw.rollingWindow(data, 8, 5) shouldBe Map(
      "A2" -> List(Transaction("T0001", "A2", 3, "CC", 10.0), Transaction("T0001", "A2", 5, "EE", 10.0), Transaction("T0001", "A2", 7, "GG", 10.0)),
      "A1" -> List(Transaction("T0001", "A1", 4, "DD", 10.0), Transaction("T0001", "A1", 6, "FF", 10.0))
    )
  }

  "maxTransactionValuePerAccount" should "find the max transaction value" in {
    val data = Map(
      "A1" -> List(Transaction("T0001", "A2", 3, "CC", 10.0),
                   Transaction("T0001", "A2", 5, "EE", 3.00),
                   Transaction("T0001", "A2", 7, "GG", 5.00)),
      "A2" -> List(Transaction("T0001", "A1", 4, "DD", 50.0),
                   Transaction("T0001", "A1", 6, "FF", 10.0)
      )
    )
    rw.maxTransactionValuePerAccount(data) shouldBe Map("A1" -> 10.0, "A2" -> 50.0)
  }

  it should "find the max transaction value per account in a rolling window" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 30.00),
      Transaction("T0001", "A1", 2, "BB", 50.00),
      Transaction("T0001", "A2", 3, "CC", 90.00),
      Transaction("T0001", "A1", 4, "DD", 10.00),
      Transaction("T0001", "A2", 5, "EE", 100.00),
      Transaction("T0001", "A1", 6, "FF", 40.00),
      Transaction("T0001", "A2", 7, "GG", 30.00),
      Transaction("T0001", "A1", 8, "BB", 90.00)
    )
    val window = rw.rollingWindow(data, 8, 5)
    rw.maxTransactionValuePerAccount(window) shouldBe Map("A2" -> 100.0, "A1" -> 40.0)
  }

  "averageTransactionValuePerAccount" should "find the averages of each account" in {
    val data = Map(
      "A1" -> List(Transaction("T0001", "A2", 3, "CC", 10.0),
        Transaction("T0001", "A2", 5, "EE", 3.00),
        Transaction("T0001", "A2", 7, "GG", 5.00)),
      "A2" -> List(Transaction("T0001", "A1", 4, "DD", 50.0),
        Transaction("T0001", "A1", 6, "FF", 10.0)
      )
    )
     rw.averageTransactionValuePerAccount(data) shouldBe Map("A1" -> 6.0, "A2" -> 30.0)
  }
}
