import scala.collection.immutable.ListMap

class TransactionsReport {

  def totalTransactionsPerDay(transactions: List[Transaction]): List[(Int, Double)] = {
    transactions
      .groupBy(_.transactionDay)
      .transform((_, value) => value.map(e => e.transactionAmount).sum)
      .toList
      .sortBy(i => i._1)
  }

  def averageTransactionsPerAcc(transactions: List[Transaction]): List[(String, List[Double])] = {
    val filler = createFiller(transactions)
      .transform((_, value) => value.map(e => e.transactionAmount).head)

    val averages = transactions
      .groupBy(trans => (trans.accountId, trans.category))
      .transform((_, value) => value.map(e => e.transactionAmount).sum / value.length)

    val filledAverages: ListMap[(String, String), Double] = filler ++ averages

    val sortedAvg = ListMap(filledAverages.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)

    sortedAvg.toList
      .groupBy(e => e._1._1)
      .transform((_, value) => value.map(e => e._2))
      .toList
      .sortBy(i => i._1)
  }

  def createFiller(transactions: List[Transaction]): ListMap[(String, String), List[Transaction]] = {
    val byTransactionCategory = transactions.groupBy(trans => trans.category)
    val byAccountId = transactions.groupBy(trans => trans.accountId)

    val accountIds = byAccountId.keySet
    val transactionCategories = byTransactionCategory.keySet

    val categoryAndIdKeys = for {
      accId <- accountIds
      transCategory <- transactionCategories
    } yield (accId, transCategory)

    val fillTransactions = categoryAndIdKeys
      .toList
      .map(e => (e, List(Transaction("", "", 0, "", 0))))

    val filledMap = fillTransactions
      .groupBy(_._1)
      .transform((_, v) => v.flatMap(e => e._2))

    ListMap(filledMap.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)
  }

}

case class Transaction(transactionId: String, accountId: String, transactionDay: Int, category: String, transactionAmount: Double)
