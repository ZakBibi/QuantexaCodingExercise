import java.io._

object TransactionFileWriter {

  def writeTotalAndAverageReportsToFile(fileName: String, header: String, lines: List[(Any, Any)]): Unit = {
    val reportData = lines.map(tuple => "%s, %s".format(tuple._1, tuple._2)).mkString("\n")
    writeLines(fileName, header, reportData)
  }

  def writeRollingWindowReportToFile(fileName: String, header: String, lines: List[(Any, Any, Any)]): Unit = {
    val reportData = lines.map(tuple => "%s, %s, %s".format(tuple._1, tuple._2, tuple._3)).mkString("\n")
    writeLines(fileName, header, reportData)
  }

  private def writeLines(fileName: String, header: String, reportData: String): Unit = {
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(header)
    for (line <- reportData) {
      bw.write(line)
    }
    bw.close()
  }
}
