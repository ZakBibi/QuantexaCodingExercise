import scala.collection.immutable.ListMap

class TransactionTotals {

  def totalTransactionsPerDay(transactions: List[Transaction]): List[(Int, Double)] = {
    transactions
      .groupBy(_.transactionDay)
      .transform((_, value) => value.map(e => e.transactionAmount).sum)
      .toList
      .sortBy(i => i._1)
  }

}