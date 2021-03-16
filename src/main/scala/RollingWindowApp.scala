object RollingWindowApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.openFile(transactionsFilePath)

    val rw = new RollingWindowTransactionReports

    val rollingWindowReports = rw.completeReport(transactions, 5)

    val madeStr = for {
      (acc, trans) <- rollingWindowReports
    } yield (acc._1, acc._2, trans.mkString(", "))

    val header = "Day, Account ID, Max, Average, AA Total, BB Total, CC Total, DD Total, EE Total, FF total, GG Total \n"

    TransactionFileWriter.writeRollingWindowReportToFile(args(1), header, madeStr)

  }

}
