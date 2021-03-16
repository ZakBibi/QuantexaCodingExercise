package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.TransactionFileReader

object RollingWindowApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.openFile(transactionsFilePath)

    val rw = new RollingWindowTransactionReports

    val rollingWindowReports = rw.generateReportsForAllWindows(transactions, 5)

    RollingWindowCSVWriter.writeRollingWindowReport(args(1), rollingWindowReports)

  }

}
