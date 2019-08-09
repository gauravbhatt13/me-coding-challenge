# Coding challenge
The coding challenge app reads csv files with transactions and gives relative account balance based on input date and time. The application is a maven project with JUNIT as dependency mentioned in the pom.xml.

## Design
> TransactionAnalyzer class takes filename as input in constructor and sets its transactionFile property.
> An init call on the analyzer loads all the transactions from the csv file.
> calculateBalance call will return AnalysisReport class object with calculated relative balance.

### Steps to run
> Run Main.java
> Provide account ID as mentioned in the input file. (Id is case sensitive)
> Provide fromDate and toDate in 'dd/MM/yyyy HH:mm:ss' format.

### Steps to run testcases
> Run TransactionAnalyzerTest.java present in the src/test/java/coding/challenge/test folder.