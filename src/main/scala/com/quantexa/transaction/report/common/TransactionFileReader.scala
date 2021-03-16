package com.quantexa.transaction.report.common

import scala.io.Source

object TransactionFileReader {

  def from(fileName: String): List[Transaction] = {
    val transactionFile = Source.fromFile(fileName)
    try {
      val transactionLines = transactionFile.getLines().drop(1)
      transactionLines.map { line =>
        val split = line.split(',')
        Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
      }.toList
    } finally {
      transactionFile.close
    }
  }

}
