import java.io._

object TransactionFileWriter {

  def writeTotalAndAverageReports(fileName: String, header: String, lines: List[(Any, Any)]): Unit = {
    val reportData = lines.map(tuple => "%s, %s".format(tuple._1, tuple._2)).mkString("\n")
    writeReportData(fileName, header, reportData)
  }

  def writeRollingWindowReport(fileName: String, header: String, lines: List[(Any, Any, Any)]): Unit = {
    val reportData = lines.map(tuple => "%s, %s, %s".format(tuple._1, tuple._2, tuple._3)).mkString("\n")
    writeReportData(fileName, header, reportData)
  }

  private def writeReportData(fileName: String, header: String, reportData: String): Unit = {
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(header)
    for (line <- reportData) {
      bw.write(line)
    }
    bw.close()
  }
}
