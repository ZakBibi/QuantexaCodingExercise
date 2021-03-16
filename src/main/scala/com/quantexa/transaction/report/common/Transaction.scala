package com.quantexa.transaction.report.common

case class Transaction(transactionId: String, accountId: String, transactionDay: Int, category: String, transactionAmount: Double)
