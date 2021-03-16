package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.TransactionReportFileWriter

object RollingWindowCSVWriter {

  def writeRollingWindowReport(fileName: String, lines: List[RollingWindowReports]): Unit = {
    val reportData = lines
      .map(line => "%s, %s, %s\n"
        .format(line.day.toString, line.accountId, line.reportData.mkString(","))).mkString("")
    val header = "Day, Account ID, Max, Average, AA Total, BB Total, CC Total, DD Total, EE Total, FF total, GG Total \n"
    TransactionReportFileWriter.writeTo(fileName, header, reportData)
  }

}
