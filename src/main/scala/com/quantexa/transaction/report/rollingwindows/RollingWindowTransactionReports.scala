package com.quantexa.transaction.report.rollingwindows

import com.quantexa.transaction.report.common.ZerosForMissingCategories
import com.quantexa.transaction.report.common.Transaction
import scala.collection.immutable.ListMap

case class RollingWindowReports(day: Int, accountId: String, reportData: List[Double])

class RollingWindowTransactionReports {

  private[rollingwindows] def rollingWindows(transactions: List[Transaction], windowLengthInDays: Int): List[List[Transaction]] = {
    val days = transactions.map(t => t.transactionDay).distinct.sorted
    val daysInWindow = days.sliding(windowLengthInDays).toList
    val transInWindows = daysInWindow.map { window =>
      transactions.filter {
        t => window.contains(t.transactionDay)
      }
    }
    transInWindows
  }

  private[rollingwindows] def maxTransactionValuePerAccount(transactionWindow: List[Transaction]): ListMap[String, Double] = {
    val maxTransactionValue = transactionWindow
      .groupBy(t => t.accountId)
      .transform ((_, value) =>
        value.map(e => e.transactionAmount).max
      )

    ListMap(maxTransactionValue.toSeq.sortBy(_._1):_*)

  }

  private[rollingwindows] def averageTransactionValuePerAccount(transactionWindow: List[Transaction]): ListMap[String, Double] = {
    val transactionAverage = transactionWindow
      .groupBy(t => t.accountId)
      .transform ((_,value) =>
        value.map(e => e.transactionAmount).sum / value.length
      )

    ListMap(transactionAverage.toSeq.sortBy(_._1):_*)

  }

  private[rollingwindows] def totalTransactionValuePerCategoryPerAccount(transactionWindow: List[Transaction]): ListMap[(String, String), Double] = {
    val filler = ZerosForMissingCategories.generate(transactionWindow)

    val totals = transactionWindow
      .groupBy(trans => (trans.accountId, trans.category))
      .transform((_, value) =>
        value.map(trans => trans.transactionAmount).sum
      )

    val filledTotals = filler ++ totals

    ListMap(filledTotals.toSeq.sortBy(keyTuple => (keyTuple._1._1, keyTuple._1._2)):_*)

  }

  private[rollingwindows] def reportForWindow(transactionWindow: List[Transaction]): ListMap[(Int, String), List[Double]] = {
    val byAccountId = transactionWindow.groupBy(t => t.accountId)
    val currentDay = transactionWindow.map(_.transactionDay).max + 1

    val sortedTransactions = ListMap(byAccountId.toSeq.sortBy(_._1):_*)

    val maxAndAvgByAccount = sortedTransactions
      .transform((_, value) =>
      {
        val max = maxTransactionValuePerAccount(value)
        val avg = averageTransactionValuePerAccount(value)

        max.values ++ avg.values
      }
    )

    val totalsByCategory = totalTransactionValuePerCategoryPerAccount(transactionWindow)
      .groupBy(_._1._1)
      .transform((_, value) =>
        value.values.toList
      )

    val sortedTotals = ListMap(totalsByCategory.toSeq.sortBy(_._1):_*)

    val maxAndAvgAndTotals = maxAndAvgByAccount.toList ++ sortedTotals.toList

    val merged = maxAndAvgAndTotals
      .groupBy(_._1)
      .transform(
      (_, value) => value.flatMap(_._2)
    )

    val windowReport = ListMap(merged.toSeq.sortBy(key => key._1):_*)

    ListMap( windowReport.keySet.map{ accountId =>
      (currentDay, accountId) -> windowReport(accountId)
    }.toSeq.sortBy(_._1):_*)

  }

  def generateReportsForAllWindows(transactions: List[Transaction], daysInWindow: Int): List[RollingWindowReports] = {
    val transactionWindows = rollingWindows(transactions, daysInWindow)
    transactionWindows.flatMap(t => reportForWindow(t))
      .map(t => RollingWindowReports(day = t._1._1, accountId = t._1._2, reportData = t._2))

  }

}
