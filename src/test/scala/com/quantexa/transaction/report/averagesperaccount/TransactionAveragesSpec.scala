package com.quantexa.transaction.report.averagesperaccount

import com.quantexa.transaction.report.common.Transaction
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ListMap

class TransactionAveragesSpec extends AnyFlatSpec with Matchers {

  val ta = new TransactionAverages

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
    ta.averageTransactionsPerAcc(data) shouldBe List(
      TransactionAveragesPerAccount("A1", List(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0))
    )
  }

  it should "get averages and fill in missing data" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 1, "BB", 10.00),
      Transaction("T0001", "A2", 1, "BB", 10.00),
      Transaction("T0001", "A3", 1, "AA", 10.00),
      Transaction("T0001", "A3", 1, "CC", 10.00)
    )

    ta.averageTransactionsPerAcc(data) shouldBe List(
      TransactionAveragesPerAccount("A1", List(10.0, 10.0, 0.0)),
      TransactionAveragesPerAccount("A2", List(0.0, 10.0, 0.0)),
      TransactionAveragesPerAccount("A3", List(10.0, 0.0, 10.0)))

  }


}
