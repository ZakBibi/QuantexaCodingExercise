package com.quantexa.transaction.report.common

import java.io.{BufferedWriter, File, FileWriter}

object TransactionReportFileWriter {

  def writeTo(fileName: String, header: String, reportData: String): Unit = {
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(header)
    for (line <- reportData) {
      bw.write(line)
    }
    bw.close()
  }
}
