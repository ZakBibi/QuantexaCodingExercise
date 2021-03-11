import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ListMap

class TransactionAveragesSpec extends AnyFlatSpec with Matchers {

  val ta = new TransactionAverages

  "createFiller" should "make filler data" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 1, "BB", 10.00),
      Transaction("T0001", "A1", 1, "CC", 10.00),
      Transaction("T0001", "A1", 1, "DD", 10.00),
      Transaction("T0001", "A1", 1, "EE", 10.00),
      Transaction("T0001", "A1", 1, "FF", 10.00),
      Transaction("T0001", "A1", 1, "GG", 10.00),
      Transaction("T0001", "A2", 1, "BB", 10.00)
    )

    val filler = ListMap(
      ("A1", "AA") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "BB") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "CC") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "DD") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "EE") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "FF") -> List(Transaction("", "", 0, "", 0.0)),
      ("A1", "GG") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "AA") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "BB") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "CC") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "DD") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "EE") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "FF") -> List(Transaction("", "", 0, "", 0.0)),
      ("A2", "GG") -> List(Transaction("", "", 0, "", 0.0))
    )

    ta.createFiller(data) shouldBe filler

  }

  "averageTransactionsPerAcc" should "get average per account for seven transaction types" in {
    val data = List (
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 1, "BB", 10.00),
      Transaction("T0001", "A1", 1, "CC", 10.00),
      Transaction("T0003", "A1", 2, "DD", 10.00),
      Transaction("T0003", "A1", 2, "EE", 10.00),
      Transaction("T0003", "A1", 2, "FF", 10.00),
      Transaction("T0003", "A1", 2, "GG", 10.00)
    )
    ta.averageTransactionsPerAcc(data) shouldBe List(("A1", List(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0)))
  }

  it should "get averages and fill in missing data" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 1, "BB", 10.00),
      Transaction("T0001", "A2", 1, "BB", 10.00),
      Transaction("T0001", "A3", 1, "AA", 10.00)
    )

    ta.averageTransactionsPerAcc(data) shouldBe List(
      ("A1", List(10.0, 10.0)),
      ("A2", List(0.0, 10.0)),
      ("A3", List(10.0, 0.0)))

  }


}
