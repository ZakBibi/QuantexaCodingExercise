import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ListMap

class RollingWindowTransactionReportsSpec extends AnyFlatSpec with Matchers {

  val rw = new RollingWindowTransactionReports

  "rollingWindow" should "select only 2 days" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A1", 3, "BB", 10.00),
      Transaction("T0001", "A1", 4, "AA", 10.00)
    )
    rw.rollingWindow(data, 3, 2) shouldBe List(Transaction("T0001", "A1", 1, "AA", 10.0), Transaction("T0001", "A1", 2, "BB", 10.0))
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
    rw.rollingWindow(data, 8, 5) shouldBe List(
      Transaction("T0001", "A2", 3, "CC", 10.0),
      Transaction("T0001", "A1", 4, "DD", 10.0),
      Transaction("T0001", "A2", 5, "EE", 10.0),
      Transaction("T0001", "A1", 6, "FF", 10.0),
      Transaction("T0001", "A2", 7, "GG", 10.0)
    )
  }

  "dataFillers" should "fill in the empty data for max and average transactions" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A2", 1, "AA", 10.00),
      Transaction("T0001", "A3", 2, "BB", 10.00)
    )

    rw.maxAndAverageTransactionFiller(data) shouldBe ListMap(
      (1, "A1") -> List(Transaction("", "", 0, "", 0.0)),
      (1, "A2") -> List(Transaction("", "", 0, "", 0.0)),
      (1, "A3") -> List(Transaction("", "", 0, "", 0.0)),
      (2, "A1") -> List(Transaction("", "", 0, "", 0.0)),
      (2, "A2") -> List(Transaction("", "", 0, "", 0.0)),
      (2, "A3") -> List(Transaction("", "", 0, "", 0.0))
    )

  }

  it should "fill in the empty data for total transactions" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A2", 1, "AA", 10.00),
      Transaction("T0001", "A3", 2, "BB", 10.00)
    )

    rw.totalTransValPerCategoryPerAccFiller(data) shouldBe ListMap(
      (1, "A1", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (1, "A1", "BB") -> List(Transaction("", "", 0, "",0.0)),
      (1, "A2", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (1, "A2", "BB") -> List(Transaction("", "", 0, "",0.0)),
      (1, "A3", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (1, "A3", "BB") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A1", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A1", "BB") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A2", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A2", "BB") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A3", "AA") -> List(Transaction("", "", 0, "",0.0)),
      (2, "A3", "BB") -> List(Transaction("", "", 0, "",0.0))
    )

  }

  "maxTransactionValuePerAccount" should "find the max transaction value" in {
    val data = List(Transaction("T0001", "A2", 7, "GG", 10.0),
                    Transaction("T0001", "A2", 5, "EE", 50.0)
                    )
    rw.maxTransactionValuePerAccount(data) shouldBe ListMap(
      (5, "A2") -> 50.0,
      (7, "A2") -> 10.0)
  }

  it should "find the max transaction value per account in a rolling window" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 30.00),
      Transaction("T0001", "A1", 1, "BB", 50.00),
      Transaction("T0001", "A2", 2, "CC", 90.00),
      Transaction("T0001", "A1", 3, "DD", 10.00),
      Transaction("T0001", "A2", 4, "EE", 100.00)
    )
    val windowedData = rw.rollingWindow(data, 4, 4)
    rw.maxTransactionValuePerAccount(windowedData) shouldBe ListMap(
      (1, "A1") -> 50.0,
      (1, "A2") -> 0.0,
      (2, "A1") -> 0.0,
      (2, "A2") -> 90.0,
      (3, "A1") -> 10.0,
      (3, "A2") -> 0.0)
  }

  "averageTransactionValuePerAccount" should "find the averages of each account" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 30.00),
      Transaction("T0001", "A1", 2, "BB", 50.00),
      Transaction("T0001", "A2", 3, "CC", 90.00),
      Transaction("T0001", "A1", 4, "DD", 10.00),
      Transaction("T0001", "A2", 5, "EE", 100.00),
      Transaction("T0001", "A1", 6, "FF", 40.00),
      Transaction("T0001", "A2", 7, "GG", 30.00),
      Transaction("T0001", "A1", 8, "BB", 90.00)
    )
    val windowedData = rw.rollingWindow(data, 8, 5)
    rw.averageTransactionValuePerAccount(windowedData) shouldBe ListMap(
      (3, "A1") -> 0.0,
      (3, "A2") -> 90.0,
      (4, "A1") -> 10.0,
      (4, "A2") -> 0.0,
      (5, "A1") -> 0.0,
      (5, "A2") -> 100.0,
      (6, "A1") -> 40.0,
      (6, "A2") -> 0.0,
      (7, "A1") -> 0.0,
      (7, "A2") -> 30.0
    )
  }

    "totalTransactionValuePerCategoryPerAccount" should "find the total per transaction value" in {
      val data = List(
        Transaction("T0001", "A1", 1, "AA", 10.00),
        Transaction("T0001", "A1", 1, "AA", 10.00),
        Transaction("T0001", "A1", 1, "BB", 5.00),
        Transaction("T0001", "A1", 1, "BB", 5.00),
        Transaction("T0001", "A2", 2, "BB", 10.00),
        Transaction("T0001", "A3", 3, "AA", 10.00)
      )

      rw.totalTransactionValuePerCategoryPerAccount(data) shouldBe ListMap(
        (1, "A1", "AA") -> 20.0,
        (1, "A1", "BB") -> 10.0,
        (1, "A2", "AA") -> 0.0,
        (1, "A2", "BB") -> 0.0,
        (1, "A3", "AA") -> 0.0,
        (1, "A3", "BB") -> 0.0,
        (2, "A1", "AA") -> 0.0,
        (2, "A1", "BB") -> 0.0,
        (2, "A2", "AA") -> 0.0,
        (2, "A2", "BB") -> 10.0,
        (2, "A3", "AA") -> 0.0,
        (2, "A3", "BB") -> 0.0,
        (3, "A1", "AA") -> 0.0,
        (3, "A1", "BB") -> 0.0,
        (3, "A2", "AA") -> 0.0,
        (3, "A2", "BB") -> 0.0,
        (3, "A3", "AA") -> 10.0,
        (3, "A3", "BB") -> 0.0)
    }

}
