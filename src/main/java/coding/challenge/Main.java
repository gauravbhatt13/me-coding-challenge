package coding.challenge;

import coding.challenge.domain.AnalysisReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void main(String... args) {
        TransactionAnalyzer transactionAnalyzer = new TransactionAnalyzer("src/main/resources/funds.csv");
        transactionAnalyzer.init(); //LOAD ALL TRANSACTIONS FROM CSV FILE.
        Scanner input = new Scanner(System.in);
        System.out.print("accountId: ");
        String accountId = input.nextLine();//READ ACCOUNT ID
        System.out.println();
        Date fromDate = null;
        Date toDate = null;
        try {
            System.out.print("from: ");
            fromDate = DATE_FORMATTER.parse(input.nextLine());//READ FROM DATE
            System.out.println();
            System.out.print("to: ");
            toDate = DATE_FORMATTER.parse(input.nextLine());//READ TO DATE
            System.out.println();
            AnalysisReport analysisReport = transactionAnalyzer.calculateBalance(accountId, fromDate, toDate);

            System.out.println("Relative balance for the period is: " + analysisReport.getBalance());
            System.out.println("Number of transactions included is: " + analysisReport.getTransactionCount());
        } catch (ParseException e) {
            System.out.println("Please input a proper date in format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
