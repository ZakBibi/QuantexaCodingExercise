object AverageValueOfTransactionsPerAccountApp {

  def main(args: Array[String]): Unit = {

    val transactionsFilePath = args(0)

    val transactions = TransactionFileReader.openFile(transactionsFilePath)

    val tr = new TransactionsReport

    val averages = tr.averageTransactionsPerAcc(transactions)

    val madeStr = for {
      (acc, trans) <- averages
    } yield (acc, trans.mkString(", "))

    val header = "accountId, AA, BB, CC, DD, EE, FF, GG \n"

    TransactionFileWriter.writeResultsToFile(args(1), header, madeStr)

  }

}
