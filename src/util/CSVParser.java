package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Client;
import domain.Transaction;

import enumeration.Currency;

import exception.CSVParserException;
import exception.ErrorCode;

public class CSVParser {

    // clients and transactions .csv files used
    private static String CLIENTS_FILE = "csv_files/clients.csv";
    private static String TRANSACTIONS_FILE = "csv_files/transactions.csv";

    // csv file separator
    private static String SEPARATOR = ",";

    // method to get clients data from csv file into a list of Client (not used for the current task)
    public List<Client> getClientsList() {

        List<Client> clientsList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String line = "";

        try {
            bufferedReader = new BufferedReader(new FileReader(CLIENTS_FILE));

            // skip first line
            String headerLine = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {

                String[] info = line.split(SEPARATOR);
                Client client = new Client(info[0], info[1], new BigDecimal(info[2]));
                clientsList.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return clientsList;
    }

    // method to get clients data from csv file into a map of Client with clients email used as map key
    public Map<String, Client> getClientsMap() throws CSVParserException {

        Map<String, Client> clientsMap = new HashMap<String, Client>();
        BufferedReader bufferedReader = null;
        String line = "";

        try {
            bufferedReader = new BufferedReader(new FileReader(CLIENTS_FILE));

            // skip first line
            String headerLine = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {

                String[] info = line.split(SEPARATOR);
                Client client = new Client(info[0], info[1], new BigDecimal(info[2]));
                clientsMap.put(client.getEmail(), client);
            }
        } catch (FileNotFoundException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_READ_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_CLOSE_ERROR);
                }
            }
        }

        return clientsMap;
    }

    // method to get transactions data from csv file into a list of Transaction (not used for the current task)
    public List<Transaction> getTransactions() throws CSVParserException {

        List<Transaction> transactionsList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String line = "";

        try {
            bufferedReader = new BufferedReader(new FileReader(TRANSACTIONS_FILE));

            // skip first line
            String headerLine = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {

                String[] info = line.split(SEPARATOR);
                Transaction transaction = new Transaction(Integer.parseInt(info[0]), info[1], Currency.valueOf(info[2]),
                        new BigDecimal(info[3]));
                transactionsList.add(transaction);
            }
        } catch (FileNotFoundException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_READ_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_CLOSE_ERROR);
                }
            }
        }

        return transactionsList;
    }

    // method that processes transactions csv file and generates new collection of Client with updated balances
    public Collection<Client> executeTransactions(final Map<String, Client> clientsMap) throws CSVParserException {

        BufferedReader bufferedReader = null;
        String line = "";

        try {
            bufferedReader = new BufferedReader(new FileReader(TRANSACTIONS_FILE));

            BigDecimal amount = new BigDecimal("0");

            // skip first line
            String headerLine = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {

                String[] info = line.split(SEPARATOR);
                Transaction transaction = new Transaction(Integer.parseInt(info[0]), info[1], Currency.valueOf(info[2]),
                        new BigDecimal(info[3]));

                // if the currency of the transaction is USD it will be converted to EUR with the current known
                // conversion rate. After the result of the division is round to two decimal places
                amount = (transaction.getCurrency() == Currency.USD)
                    ? transaction.getAmount().divide(Currency.getEUR_USD_CONVERSION_RATE(), 2, RoundingMode.HALF_UP)
                    : transaction.getAmount();

                // update the balance of the transaction's client
                clientsMap.get(transaction.getEmail()).updateBalance(amount);
            }
        } catch (FileNotFoundException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_READ_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_CLOSE_ERROR);
                }
            }
        }

        return clientsMap.values();
    }

    // method that generates new clients_balance.csv file in the filePath passed as parameter
    public void generateClientsFile(final String filePath, final Collection<Client> clients) throws CSVParserException {

        try {

            // generate current date to add to filename
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            StringBuffer fileName = new StringBuffer(filePath);
            fileName.append("clients_balance_").append(currentDate).append(".csv");

            FileWriter writer = new FileWriter(fileName.toString());

            // clients columns information
            writer.append("email");
            writer.append(SEPARATOR);
            writer.append("name");
            writer.append(SEPARATOR);
            writer.append("balance");
            writer.append('\n');

            for (Client client : clients) {
                writer.append(client.getEmail());
                writer.append(SEPARATOR);
                writer.append(client.getName());
                writer.append(SEPARATOR);
                writer.append(client.getBalance().toString());
                writer.append('\n');
            }

            writer.flush();
            writer.close();

            System.out.println("***");
            System.out.println("CSV CLIENTS FILE GENERATED SUCCESSFULLY!");
            System.out.println("***");

        } catch (IOException e) {
            throw new CSVParserException(e.getMessage(), ErrorCode.CSV_FILE_CREATE_ERROR);
        }
    }
}
