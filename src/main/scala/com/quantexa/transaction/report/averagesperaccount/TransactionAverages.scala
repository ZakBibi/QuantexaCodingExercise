package com.quantexa.transaction.report.averagesperaccount

import com.quantexa.transaction.report.common.ZerosForMissingCategories
import com.quantexa.transaction.report.common.Transaction

import scala.collection.immutable.ListMap

case class TransactionAveragesPerAccount(accountId: String, averages: List[Double])

class TransactionAverages {

  def averageTransactionsPerAcc(transactions: List[Transaction]): List[TransactionAveragesPerAccount] = {
    val filledAverages = ZerosForMissingCategories.generate(transactions) ++ generateAverages(transactions)

    val sortedAverages = ListMap(filledAverages.toSeq.sortBy(key => (key._1._1, key._1._2)):_*)

    sortAveragesByAccountId(sortedAverages)
  }

  private def generateAverages(transactions: List[Transaction]): Map[(String, String), Double] = {
    transactions
      .groupBy(trans => (trans.accountId, trans.category))
      .transform((_, value) => value.map(e => e.transactionAmount).sum / value.length)
  }

  private def sortAveragesByAccountId(sortedAverages: ListMap[(String, String), Double]): List[TransactionAveragesPerAccount] = {
    discardCategoryKey(sortedAverages)
      .toList
      .sortBy(_._1)
      .map(accountAndAverages =>
        TransactionAveragesPerAccount(accountId = accountAndAverages._1, averages = accountAndAverages._2)
      )
  }

  private def discardCategoryKey(sortedAverages: ListMap[(String, String), Double]): Map[String, List[Double]] = {
    sortedAverages.toList
      .groupBy(keyOfAccIdAndCategory => keyOfAccIdAndCategory._1._1)
      .transform((_, value) => value.map(_._2))
  }

}
