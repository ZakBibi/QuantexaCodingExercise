package com.quantexa.transaction.report.averagesperaccount

import com.quantexa.transaction.report.common.TransactionFileReader

object AverageValueOfTransactionsPerAccountApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.from(transactionsFilePath)

    val ta = new TransactionAverages

    val averages = ta.averageTransactionsPerAcc(transactions)

    AverageValueOfTransactionsPerAccountCsvWriter.writeAveragesReport(args(1), averages)

  }

}
