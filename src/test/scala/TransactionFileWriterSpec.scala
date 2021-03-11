import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sys.process._

class TransactionFileWriterSpec extends AnyFlatSpec with Matchers {

  def compareFiles(firstFileName: String, secondFileName: String): Int = {
    val cmd = s"diff $firstFileName $secondFileName"
    cmd!
  }

  it should "save list of int tuples to file" in {
    val data = List((2, 3), (4, 5), (6, 7))
    val header = "first, second\n"
    TransactionFileWriter.writeResultsToFile("target/int-tuples-test", header, data)
    compareFiles("target/int-tuples-test", "src/test/resources/int-tuples-true") shouldBe 0
  }

  it should "save list of str and int tuples to file" in {
    val data = List(("A", 3), ("B", 5), ("C", 7))
    val header = "first, second\n"
    TransactionFileWriter.writeResultsToFile("target/mixed-tuples-test", header, data)
    compareFiles("target/mixed-tuples-test", "src/test/resources/mixed-tuples-true") shouldBe 0
  }

  it should "return 1 when files do not match" in {
    val data = List(("A", 3), ("B", 5), ("C", 7))
    val header = "first, second\n"
    TransactionFileWriter.writeResultsToFile("target/mixed-tuples-test", header, data)
    compareFiles("target/mixed-tuples-test", "src/test/resources/int-tuples-true") shouldBe 1
  }

}
