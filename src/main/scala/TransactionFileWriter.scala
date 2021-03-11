import java.io._

object TransactionFileWriter {

  def writeResultsToFile(fileName: String, header: String, lines: List[(Any, Any)]): Unit = {
    val l: String = lines.map(tuple => "%s, %s".format(tuple._1, tuple._2)).mkString("\n")
    val file = new File(fileName)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(header)
    for (line <- l) {
      bw.write(line)
    }
    bw.close()
  }

}
