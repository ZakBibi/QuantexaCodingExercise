import scala.collection.immutable.ListMap

class RollingWindowTransactionReports {

  def rollingWindow(transactions: List[Transaction], startDay: Int, window: Int): List[Transaction] = {
    transactions.filter(
        trans => trans.transactionDay >= startDay - window
          && trans.transactionDay < startDay
      )
  }

  def maxTransactionValuePerAccount(transactions: List[Transaction]): Map[(Int, String), Double] = {
    val filler = maxAndAverageTransactionFiller(transactions)
      .transform((_, value) => value.map(e => e.transactionAmount).head)

    val maxTransactionValue = transactions.groupBy(t => (t.transactionDay, t.accountId))
      .transform (
      (_, value) => value.map(e => e.transactionAmount).max
    )

    val filledTransactionValues = filler ++ maxTransactionValue

    ListMap(filledTransactionValues.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)

  }

  def averageTransactionValuePerAccount(transactions: List[Transaction]): Map[(Int, String), Double] = {
    val filler = maxAndAverageTransactionFiller(transactions)
      .transform((_, value) => value.map(e => e.transactionAmount).head)

    val transactionAverage = transactions.groupBy(t => (t.transactionDay, t.accountId))
      .transform (
      (_,value) => value.map(e => e.transactionAmount).sum / value.length
    )

    val filledTransactionValues = filler ++ transactionAverage

    ListMap(filledTransactionValues.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)

  }

  def maxAndAverageTransactionFiller(transactions: List[Transaction]): ListMap[(Int, String), List[Transaction]] = {

    val accountIds = transactions.map(t => t.accountId).toSet
    val transactionDays = transactions.map(t => t.transactionDay).toSet

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

  def totalTransactionValuePerCategoryPerAccount(transactions: List[Transaction]): Map[(Int, String, String), Double] = {
    val filler = totalTransValPerCategoryPerAccFiller(transactions)
      .transform((_, value) => value.map(e => e.transactionAmount).head)

    val totals  = transactions
      .groupBy(t => (t.transactionDay, t.accountId, t.category))
      .transform((_, value) => value.map(e => e.transactionAmount).sum)

    val filledTotals = filler ++ totals

    ListMap(filledTotals.toSeq.sortBy(r => (r._1._1, r._1._2, r._1._3)):_*)
  }

  def totalTransValPerCategoryPerAccFiller(transactions: List[Transaction]): ListMap[(Int, String, String), List[Transaction]] = {

    val accountIds = transactions.map(t => t.accountId).toSet
    val transactionDays = transactions.map(t => t.transactionDay).toSet
    val categories = transactions.map(t => t.category).toSet

    val daysAndIdKeys = for{
      accId <- accountIds
      transDay <- transactionDays
      category <- categories
    } yield (transDay, accId, category)

    val fillTransactions = daysAndIdKeys
      .toList
      .map(t => (t, List(Transaction("", "", 0, "", 0))))

    val filledMap = fillTransactions
      .groupBy(_._1)
      .transform((_,v) => v.flatMap(e => e._2))

    ListMap(filledMap.toSeq.sortBy(r => (r._1._1, r._1._2, r._1._3)):_*)

  }

}
