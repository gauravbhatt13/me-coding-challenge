package coding.challenge;

import coding.challenge.domain.AnalysisReport;
import coding.challenge.domain.Transaction;
import coding.challenge.domain.TransactionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionAnalyzer {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String transactionFile = null;
    private List<Transaction> transactions = new ArrayList<>();

    public TransactionAnalyzer(String fileName) {
        this.transactionFile = fileName;
    }

    public void init() {
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(this.transactionFile));
            fileScanner.nextLine(); //SKIPPING FILE HEADERS
            while (fileScanner.hasNextLine()) {
                addTransaction(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please make sure that the transaction file is in classpath.");
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
    }
    private void addTransaction(String s) {
        String[] row = s.split(",");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(row[0]);
        transaction.setFromAccountId(row[1]);
        transaction.setToAccountId(row[2]);
        try {
            transaction.setCreatedAt(DATE_FORMATTER.parse(row[3]));
        } catch (ParseException e) {
            System.out.println("Invalid createdAt date.");
        }
        transaction.setAmount(Double.valueOf(row[4]));
        transaction.setTransactionType(TransactionType.valueOf(row[5]));
        if (row.length > 6) {
            transaction.setRelatedTransaction(row[6]);
        }
        this.transactions.add(transaction);
    }

    public AnalysisReport calculateBalance(String accountId, Date fromDate, Date toDate) {
        //BELOW STREAM FILTERS TRANSACTIONS WITH TYPE 'PAYMENT' AND THAT FALL BETWEEN FROM AND TO DATE
        Map<String, Transaction> accountTransactions = transactions.stream().filter(t -> (t.getFromAccountId().equalsIgnoreCase(accountId) ||
                t.getToAccountId().equalsIgnoreCase(accountId)) && t.getTransactionType() == TransactionType.PAYMENT && t.getCreatedAt().after(fromDate) && t.getCreatedAt().before(toDate)).collect(Collectors.toMap(Transaction::getTransactionId, Function.identity()));

        //BELOW STREAM RETRIEVES LIST OF REVERSAL TRANSACTIONS AFTER FROM DATE
        List<String> reversals = transactions.stream().filter(t -> t.getCreatedAt().after(fromDate) && t.getTransactionType() == TransactionType.REVERSAL).
                map(Transaction::getRelatedTransaction).collect(Collectors.toCollection(ArrayList::new));

        //BELOW CODE WILL REMOVE ALL REVERSAL TRANSACTIONS FROM THE FILTERED PAYMENT TRANSACTION LIST
        accountTransactions.keySet().removeIf(entry -> reversals.contains(entry));

        Double relativeBalance = 0.0;
        for (Map.Entry<String, Transaction> transaction : accountTransactions.entrySet()) {
            if (transaction.getValue().getFromAccountId().equalsIgnoreCase(accountId)) {
                relativeBalance = relativeBalance - transaction.getValue().getAmount();
            } else if (transaction.getValue().getToAccountId().equalsIgnoreCase(accountId)) {
                relativeBalance = relativeBalance + transaction.getValue().getAmount();
            }
        }
        AnalysisReport analysisReport = new AnalysisReport();
        analysisReport.setBalance(relativeBalance < 0 ? String.valueOf(relativeBalance).replace("-", "-$") : "$".concat(String.valueOf(relativeBalance)));
        analysisReport.setTransactionCount(accountTransactions.size());
        return analysisReport;
    }
}
