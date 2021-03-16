package com.quantexa.transaction.report.common

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TransactionFileReaderSpec extends AnyFlatSpec with Matchers {

  it should "open file and return list of transactions" in {
    val result = List (
      Transaction("transactionId1", "accountId1", 1, "category1", 1.0),
      Transaction("transactionId2", "accountId2", 2, "category2", 2.0),
      Transaction("transactionId3", "accountId3", 3, "category3", 3.0),
      Transaction("transactionId4", "accountId4", 4, "category4", 4.0)
    )
    TransactionFileReader.from("src/test/resources/test-file-reader.txt") shouldBe result
  }

}
