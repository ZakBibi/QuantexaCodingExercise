package com.quantexa.transaction.report.common

import com.quantexa.transaction.report.common.ZerosForMissingCategories.generate
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.ListMap

class ZerosForMissingCategoriesSpec extends AnyFlatSpec with Matchers {

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
      ("A1", "AA") -> 0.0,
      ("A1", "BB") -> 0.0,
      ("A1", "CC") -> 0.0,
      ("A1", "DD") -> 0.0,
      ("A1", "EE") -> 0.0,
      ("A1", "FF") -> 0.0,
      ("A1", "GG") -> 0.0,
      ("A2", "AA") -> 0.0,
      ("A2", "BB") -> 0.0,
      ("A2", "CC") -> 0.0,
      ("A2", "DD") -> 0.0,
      ("A2", "EE") -> 0.0,
      ("A2", "FF") -> 0.0,
      ("A2", "GG") -> 0.0
    )

    generate(data) shouldBe filler

  }

}
