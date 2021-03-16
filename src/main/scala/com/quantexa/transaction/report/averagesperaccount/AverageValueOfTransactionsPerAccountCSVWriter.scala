package com.quantexa.transaction.report.averagesperaccount

import com.quantexa.transaction.report.common.TransactionFileWriter

object AverageValueOfTransactionsPerAccountCSVWriter {

  def writeAveragesReport(fileName: String, lines: List[TransactionAveragesPerAccount]): Unit = {
    val reportData = lines
      .map(line => "%s, %s\n".format(line.accountId, line.transactionAverages.mkString(",")))
      .mkString("")
    val header = "accountId, AA, BB, CC, DD, EE, FF, GG \n"
    TransactionFileWriter.fileWriter(fileName, header, reportData)
  }

}
