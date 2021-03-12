import scala.collection.immutable.ListMap

class RollingWindowTransactionReports {

  def rollingWindow(transactions: List[Transaction], startDay: Int, window: Int): List[Transaction] = {
    transactions.filter(
        trans => trans.transactionDay >= startDay - window
          && trans.transactionDay < startDay
      )
  }

  def maxTransactionValuePerAccount(transactions: List[Transaction]): Map[(Int, String), Double] = {
    val filler = maxTransactionFiller(transactions)
      .transform((_, value) => value.map(e => e.transactionAmount).head)

    val maxTransactionValue = transactions.groupBy(t => (t.transactionDay, t.accountId))
      .transform (
      (_, value) => value.map(e => e.transactionAmount).max
    )

    val filledTransactionValues = filler ++ maxTransactionValue

    ListMap(filledTransactionValues.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)

  }

  def maxTransactionFiller(transactions: List[Transaction]): ListMap[(Int, String), List[Transaction]] = {
    val byTransactionDay = transactions.groupBy(trans => trans.transactionDay)
    val byAccountId = transactions.groupBy(trans => trans.accountId)

    val transactionDays = byTransactionDay.keySet
    val accountIds = byAccountId.keySet

    val daysAndIdKeys = for{
      accId <- accountIds
      transDay <- transactionDays
    } yield (transDay, accId)

    val fillTransactions = daysAndIdKeys
      .toList
      .map(t => (t, List(Transaction("", "", 0, "", 0))))

    val filledMap = fillTransactions
      .groupBy(_._1)
      .transform((_,v) => v.flatMap(e => e._2))

    ListMap(filledMap.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)

  }

  def averageTransactionValuePerAccount(transactions: List[Transaction]): Map[(Int, String), Double] = {
    transactions.groupBy(t => (t.transactionDay, t.accountId))
      .transform (
      (_,value) => value.map(e => e.transactionAmount).sum / value.length
    )
  }

  def totalTransactionValuePerCategoryPerAccount(transactions: List[Transaction]): Map[(Int, String, String), Double] = {
    transactions
      .groupBy(t => (t.transactionDay, t.accountId, t.category))
      .transform((_, value) => value.map(e => e.transactionAmount).sum)
  }

}
