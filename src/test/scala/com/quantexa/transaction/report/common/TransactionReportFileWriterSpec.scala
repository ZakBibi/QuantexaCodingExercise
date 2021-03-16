package com.quantexa.transaction.report.common

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.sys.process._

class TransactionReportFileWriterSpec extends AnyFlatSpec with Matchers {

  def compareFiles(firstFileName: String, secondFileName: String): Int = {
    val cmd = s"diff $firstFileName $secondFileName"
    cmd!
  }

  it should "save list of strings to file" in {
    val data = List("A", "B", "C").mkString("")
    val header = "first, second\n"
    TransactionReportFileWriter.writeTo("target/file-writer-test", header, data)
    compareFiles("target/file-writer-test", "src/test/resources/file-writer-true.txt") shouldBe 0
  }

  it should "return 1 when files do not match" in {
    val data = List("A", "B", "C", "D").mkString("")
    val header = "first, second\n"
    TransactionReportFileWriter.writeTo("target/wrong-string-test", header, data)
    compareFiles("target/wrong-string-test", "src/test/resources/file-writer-true.txt") shouldBe 1
  }

}
