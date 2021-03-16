package com.quantexa.transaction.report.totalsperday

import com.quantexa.transaction.report.common.TransactionFileReader

object TotalTransactionsPerDayApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.openFile(transactionsFilePath)

    val tr = new TransactionTotals

    val totals = tr.totalTransactionsPerDay(transactions)

    TotalsPerDayCSVWriter.writeTotalsReport(args(1), totals)

  }

}
