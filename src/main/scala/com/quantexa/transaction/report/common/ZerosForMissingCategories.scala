package com.quantexa.transaction.report.common

import scala.collection.immutable.ListMap

object ZerosForMissingCategories {

  def generate(transactions: List[Transaction]): ListMap[(String, String), Double] = {
    val byAccountId = transactions.map(t => t.accountId).toSet
    val byTransactionCategory = transactions.map(t => t.category).toSet

    val categoryAndIdKeys = for {
      accId <- byAccountId
      transCategory <- byTransactionCategory
    } yield (accId, transCategory) -> 0.0d

    ListMap(categoryAndIdKeys.toSeq.sortBy(key => (key._1._1, key._1._2)):_*)
  }

}
