package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.Transaction
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ListMap

class RollingWindowTransactionReportsSpec extends AnyFlatSpec with Matchers {

  val rw = new RollingWindowTransactionReports

  "rollingWindow" should "return overlapping windows of length in days" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 2, "BB", 10.00),
      Transaction("T0001", "A1", 3, "BB", 10.00),
      Transaction("T0001", "A1", 4, "AA", 10.00)
    )
    rw.rollingWindows(data, 3) shouldBe List(
      List(
        Transaction("T0001", "A1", 1, "AA", 10.0),
        Transaction("T0001", "A1", 2, "BB", 10.0),
        Transaction("T0001", "A1", 3, "BB", 10.0)),
      List(
        Transaction("T0001", "A1", 2, "BB", 10.0),
        Transaction("T0001", "A1", 3, "BB", 10.0),
        Transaction("T0001", "A1", 4, "AA", 10.0))
    )
  }

  it should "give all days in a window" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 10.00),
      Transaction("T0001", "A1", 1, "BB", 10.00),
      Transaction("T0001", "A2", 2, "CC", 10.00),
      Transaction("T0001", "A1", 3, "DD", 10.00),
      Transaction("T0001", "A2", 3, "EE", 10.00),
      Transaction("T0001", "A2", 4, "CC", 10.00),
      Transaction("T0001", "A1", 4, "DD", 10.00),
      Transaction("T0001", "A2", 4, "CC", 10.00),
      Transaction("T0001", "A1", 5, "DD", 10.00)
    )

    rw.rollingWindows(data, 3) shouldBe List(
      List(
        Transaction("T0001", "A1", 1, "AA", 10.0),
        Transaction("T0001", "A1", 1, "BB", 10.0),
        Transaction("T0001", "A2", 2, "CC", 10.0),
        Transaction("T0001", "A1", 3, "DD", 10.0),
        Transaction("T0001", "A2", 3, "EE", 10.0)),
      List(
        Transaction("T0001", "A2", 2, "CC", 10.0),
        Transaction("T0001", "A1", 3, "DD", 10.0),
        Transaction("T0001", "A2", 3, "EE", 10.0),
        Transaction("T0001", "A2", 4, "CC", 10.0),
        Transaction("T0001", "A1", 4, "DD", 10.0),
        Transaction("T0001", "A2", 4, "CC", 10.0)),
      List(
        Transaction("T0001", "A1", 3, "DD",10.0),
        Transaction("T0001", "A2", 3, "EE",10.0),
        Transaction("T0001", "A2", 4, "CC",10.0),
        Transaction("T0001", "A1", 4, "DD",10.0),
        Transaction("T0001", "A2", 4, "CC",10.0),
        Transaction("T0001", "A1", 5, "DD",10.0))
    )
  }

  "maxTransactionValuePerAccount" should "find the max transaction value" in {
    val data = List(
      Transaction("T0001", "A1", 1, "GG", 20.0),
      Transaction("T0001", "A1", 2, "GG", 10.0),
      Transaction("T0001", "A2", 2, "EE", 50.0),
      Transaction("T0001", "A2", 3, "EE", 5.0)
    )
    rw.maxTransactionValuePerAccount(data) shouldBe ListMap(
      "A1" -> 20.0,
      "A2" -> 50.0
    )

  }

  "averageTransactionValuePerAccount" should "find the averages of each account" in {
    val data = List(
      Transaction("T0001", "A1", 1, "GG", 20.0),
      Transaction("T0001", "A1", 2, "GG", 10.0),
      Transaction("T0001", "A2", 2, "EE", 5.0),
      Transaction("T0001", "A2", 3, "EE", 10.0)
    )
    rw.averageTransactionValuePerAccount(data) shouldBe ListMap(
      "A1" -> 15.0,
      "A2" -> 7.5
    )
  }

    "totalTransactionValuePerCategoryPerAccount" should
      "find the total per transaction value and should have 0.0 for missing categories" in {

      val data = List(
        Transaction("T0001", "A1", 1, "AA", 10.00),
        Transaction("T0001", "A1", 1, "AA", 10.00),
        Transaction("T0001", "A1", 1, "BB", 5.00),
        Transaction("T0001", "A1", 1, "BB", 5.00),
        Transaction("T0001", "A2", 2, "BB", 10.00),
        Transaction("T0001", "A3", 3, "AA", 10.00)
      )

      rw.totalTransactionValuePerCategoryPerAccount(data) shouldBe ListMap(
        ("A1", "AA") -> 20.0,
        ("A1", "BB") -> 10.0,
        ("A2", "AA") -> 0.0,
        ("A2", "BB") -> 10.0,
        ("A3", "AA") -> 10.0,
        ("A3", "BB") -> 0.0
      )
    }

  "RollingWindowTransactionReports" should "create a report for a single window" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 30.00),
      Transaction("T0001", "A1", 2, "BB", 50.00),
      Transaction("T0001", "A1", 3, "CC", 90.00),
      Transaction("T0001", "A2", 4, "AA", 10.00),
      Transaction("T0001", "A2", 4, "DD", 5.00)
    )

    rw.reportForWindow(data) shouldBe ListMap(
      (5, "A1") -> List(90.0, 56.666666666666664, 30.0, 50.0, 90.0, 0.0),
      (5, "A2") -> List(10.0, 7.5, 10.0, 0.0, 0.0, 5.0)
    )
  }

  "RollingWindowTransactionReports" should "create reports for more than one window" in {
    val data = List(
      Transaction("T0001", "A1", 1, "AA", 30.00),
      Transaction("T0001", "A2", 2, "BB", 50.00),
      Transaction("T0001", "A1", 3, "AA", 5.00),
      Transaction("T0001", "A2", 4, "BB", 4.00)
    )

    rw.generateReportsForAllWindows(data, 3) shouldBe List(
      RollingWindowReports(4, "A1", List(30.0, 17.5, 35.0, 0.0)),
      RollingWindowReports(4, "A2", List(50.0, 50.0, 0.0, 50.0)),
      RollingWindowReports(5, "A1", List(5.0, 5.0, 5.0, 0.0)),
      RollingWindowReports(5, "A2", List(50.0, 27.0, 0.0, 54.0))
    )
  }

}
