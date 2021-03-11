import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TransactionFileReaderSpec extends AnyFlatSpec with Matchers {

  it should "open file and return list of transactions" in {
    val result = List (
      Transaction("T0001", "A27", 1, "GG", 338.11),
      Transaction("T0002", "A5", 1, "BB", 677.89),
      Transaction("T0003", "A32", 1, "DD", 499.86),
      Transaction("T0004", "A42", 1, "DD", 801.81)
    )
    TransactionFileReader.openFile("src/test/resources/test-file-reader.txt") shouldBe result
  }

}
