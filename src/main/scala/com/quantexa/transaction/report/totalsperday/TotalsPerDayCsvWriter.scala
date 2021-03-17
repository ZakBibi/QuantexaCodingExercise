package com.quantexa.transaction.report.totalsperday

import com.quantexa.transaction.report.common.TransactionReportFileWriter

object TotalsPerDayCsvWriter {

  def writeTotalsReport(fileName: String, lines: List[TotalTransactionsPerDay]): Unit = {
    val reportData = lines.map(line => "%s, %s\n".format(line.day.toString, line.total.toString)).mkString("")
    val header = "Day, Total \n"
    TransactionReportFileWriter.writeTo(fileName, header, reportData)
  }

}
