package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.TransactionFileReader

object RollingWindowApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.from(transactionsFilePath)

    val rw = new RollingWindowTransactionReports

    val rollingWindowReports = rw.generateReportsForAllWindows(transactions, 5)

    RollingWindowCvsWriter.writeRollingWindowReport(args(1), rollingWindowReports)

  }

}
