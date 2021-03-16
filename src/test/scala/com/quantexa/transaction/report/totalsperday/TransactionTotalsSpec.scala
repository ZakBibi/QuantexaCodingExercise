package com.quantexa.transaction.report.totalsperday

import com.quantexa.transaction.report.common.Transaction
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TransactionTotalsSpec extends AnyFlatSpec with Matchers {

  val tr = new TransactionTotals

  "totalTransactionsPerDay" should "get total transactions per day" in {
    val data = List (
      Transaction("T0001", "A1", 1, "GG", 200.00),
      Transaction("T0001", "A2", 1, "GG", 100.00),
      Transaction("T0003", "A3", 2, "BB", 50.00)
    )
    tr.totalTransactionsPerDay(data) shouldBe List(
      TotalTransactionsPerDay(1, 300.00),
      TotalTransactionsPerDay(2, 50.00))
  }
}
