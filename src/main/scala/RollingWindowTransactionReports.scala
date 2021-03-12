class RollingWindowTransactionReports {

  def rollingWindow(transactions: List[Transaction], startDay: Int, window: Int): Map[(Int, String), List[Transaction]] = {
    transactions.filter(
        trans => trans.transactionDay >= startDay - window
          && trans.transactionDay < startDay
      ).groupBy(t => (t.transactionDay, t.accountId))
  }

  def maxTransactionValuePerAccount(transactions: Map[(Int, String), List[Transaction]]): Map[(Int, String), Double] = {
    transactions.transform (
      (_, value) => value.map(e => e.transactionAmount).max
    )
  }

  def averageTransactionValuePerAccount(transactions: Map[(Int, String), List[Transaction]]): Map[(Int, String), Double] = {
    transactions.transform (
      (_,value) => value.map(e => e.transactionAmount).sum / value.length
    )
  }



}
