package com.quantexa.transaction.report.averagesperaccount

import com.quantexa.transaction.report.common.TransactionReportFileWriter

object AverageValueOfTransactionsPerAccountCSVWriter {

  def writeAveragesReport(fileName: String, lines: List[TransactionAveragesPerAccount]): Unit = {
    val reportData = lines
      .map(line => "%s, %s\n".format(line.accountId, line.averages.mkString(",")))
      .mkString("")
    val header = "accountId, AA, BB, CC, DD, EE, FF, GG \n"
    TransactionReportFileWriter.writeTo(fileName, header, reportData)
  }

}
