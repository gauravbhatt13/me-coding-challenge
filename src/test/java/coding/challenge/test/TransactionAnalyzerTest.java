package coding.challenge.test;

import coding.challenge.TransactionAnalyzer;
import coding.challenge.domain.AnalysisReport;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TransactionAnalyzerTest {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Test
    public void testPositiveFunds() {
        TransactionAnalyzer transactionAnalyzer =
                new TransactionAnalyzer("src/test/resources/test-funds.csv");
        transactionAnalyzer.init();
        try {
            AnalysisReport analysisReport = transactionAnalyzer.calculateBalance("ACC334455",
                    DATE_FORMATTER.parse("20/10/2018 12:00:00"),
                    DATE_FORMATTER.parse("21/10/2018 11:00:00"));
            Assert.assertEquals("$23.0", analysisReport.getBalance());
            Assert.assertEquals(3, analysisReport.getTransactionCount());
        } catch (ParseException e) {
            System.out.println("Please enter date in proper format: dd/MM/yyyy HH:mm:ss");
        }
    }

    @Test
    public void testNegativeFunds() {
        TransactionAnalyzer transactionAnalyzer =
                new TransactionAnalyzer("src/test/resources/test-funds.csv");
        transactionAnalyzer.init();
        try {
            AnalysisReport analysisReport = transactionAnalyzer.calculateBalance("ACC334455",
                    DATE_FORMATTER.parse("20/10/2018 12:00:00"),
                    DATE_FORMATTER.parse("20/10/2018 19:00:00"));
            Assert.assertEquals("-$25.0", analysisReport.getBalance());
            Assert.assertEquals(1, analysisReport.getTransactionCount());
        } catch (ParseException e) {
            System.out.println("Please enter date in proper format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
