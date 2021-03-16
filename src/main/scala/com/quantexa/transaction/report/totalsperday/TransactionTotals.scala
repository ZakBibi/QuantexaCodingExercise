package com.quantexa.transaction.report.totalsperday

import com.quantexa.transaction.report.common.Transaction

case class TotalTransactionsPerDay(day: Int, total: Double)

class TransactionTotals {

  def totalTransactionsPerDay(transactions: List[Transaction]): List[TotalTransactionsPerDay] = {
    transactions
      .groupBy(_.transactionDay)
      .transform((_, value) => value.map(e => e.transactionAmount).sum)
      .toList
      .sortBy(i => i._1)
      .map(dayAndTotal => TotalTransactionsPerDay(dayAndTotal._1, dayAndTotal._2))
  }

}