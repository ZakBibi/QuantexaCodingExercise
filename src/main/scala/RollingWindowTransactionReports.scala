class RollingWindowTransactionReports {

  def rollingWindow(transactions: List[Transaction], startDay: Int, window: Int): Map[String, List[Transaction]] = {
    transactions.filter(
        trans => trans.transactionDay >= startDay - window
          && trans.transactionDay < startDay
      ).groupBy(_.accountId)
  }

  def maxTransactionValuePerAccount(transactions: Map[String, List[Transaction]]): Map[String, Double] = {
    transactions.transform(
      (_, value) => value.map(e => e.transactionAmount).max
    )
  }

  def averageTransactionValuePerAccount(transactions: Map[String, List[Transaction]]): Map[String, Double] = {
    transactions.transform (
      (_,value) => value.map(e => e.transactionAmount).sum / value.length
    )
  }



}
