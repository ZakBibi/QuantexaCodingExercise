package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.ZerosForMissingCategories
import com.quantexa.transaction.report.common.Transaction

import scala.collection.immutable.ListMap

case class RollingWindowReports(day: Int, accountId: String, reportData: List[Double])

class RollingWindowTransactionReports {

  def rollingWindows(transactions: List[Transaction], daysInWindow: Int): List[List[Transaction]] = {
    val days = transactions.map(t => t.transactionDay).distinct.sorted
    val d = days.sliding(daysInWindow).toList
    val transInWindows = d.map{
      e => transactions.filter {
        t => e.contains(t.transactionDay)
      }
    }
    transInWindows
  }

  def maxTransactionValuePerAccount(transactionWindow: List[Transaction]): ListMap[String, Double] = {
    val maxTransactionValue = transactionWindow.groupBy(t => t.accountId)
      .transform (
      (_, value) => value.map(e => e.transactionAmount).max
      )

    ListMap(maxTransactionValue.toSeq.sortBy(r => r._1):_*)

  }

  def averageTransactionValuePerAccount(transactionWindow: List[Transaction]): ListMap[String, Double] = {
    val transactionAverage = transactionWindow.groupBy(t => t.accountId)
      .transform (
      (_,value) => value.map(e => e.transactionAmount).sum / value.length
      )

    ListMap(transactionAverage.toSeq.sortBy(r => r._1):_*)

  }

  def totalTransactionValuePerCategoryPerAccount(transactionWindow: List[Transaction]): ListMap[(String, String), Double] = {
    val filler = ZerosForMissingCategories.generate(transactionWindow)

    val totals = transactionWindow
      .groupBy(t => (t.accountId, t.category))
      .transform((_, value) => value.map(e => e.transactionAmount).sum)

    val filledTotals = filler ++ totals

    ListMap(filledTotals.toSeq.sortBy(r => (r._1._1, r._1._2)):_*)
  }

  def reportForWindow(transactionWindow: List[Transaction]): ListMap[(Int, String), List[Double]] = {

    val currentDay = transactionWindow.map(_.transactionDay).max + 1

    val byAccountId = transactionWindow.groupBy(t => t.accountId)

    val sorted = ListMap(byAccountId.toSeq.sortBy(r => r._1):_*)

    val maxAndAvgByAccount = sorted.transform(
      (_, value) => {
        val max = maxTransactionValuePerAccount(value)
        val avg = averageTransactionValuePerAccount(value)

        max.values ++ avg.values
      }
    )

    val totalsByCategory = totalTransactionValuePerCategoryPerAccount(transactionWindow)
      .groupBy(_._1._1)
      .transform(
        (_, value) => value.values.toList
      )

    val sortedTotals = ListMap(totalsByCategory.toSeq.sortBy(_._1):_*)

    val maxAndAvgAndTotals = maxAndAvgByAccount.toList ++ sortedTotals.toList

    val merged = maxAndAvgAndTotals
      .groupBy(_._1)
      .transform(
      (_, value) => value.flatMap(e => e._2)
    )

    val windowReport = ListMap(merged.toSeq.sortBy(r => r._1):_*)

    ListMap( windowReport.keySet.map{ e =>
      (currentDay, e) -> windowReport(e)
    }.toSeq.sortBy(r => r._1):_*)

  }

  def completeReport(transactions: List[Transaction], daysInWindow: Int): List[RollingWindowReports] = {
    val transactionWindows = rollingWindows(transactions, daysInWindow)
    transactionWindows.flatMap(t => reportForWindow(t))
      .map(t => RollingWindowReports(t._1._1, t._1._2, t._2))
  }

}
