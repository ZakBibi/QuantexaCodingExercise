object TotalTransactionsPerDayApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.openFile(transactionsFilePath)

    val tr = new TransactionTotals

    val totals = tr.totalTransactionsPerDay(transactions)

    val header = "day, total \n"

    TransactionFileWriter.writeTotalAndAverageReportsToFile(args(1), header, totals)

  }

}
