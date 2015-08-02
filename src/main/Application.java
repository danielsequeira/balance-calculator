package main;

import java.util.Collection;
import java.util.Map;

import domain.Client;

import exception.CSVParserException;

import util.CSVParser;

public class Application {

    public static void main(final String[] args) throws CSVParserException {
    	
    	CSVParser csvParser = new CSVParser();
        Map<String, Client> clientsMap;

        // get clients data into a Map from defined clients .csv file in CSVPaser class
        clientsMap = csvParser.getClientsMap();

        // read transactions .csv file defined in CSVParser class and return clients collection with updated balances
        Collection<Client> clients = csvParser.executeTransactions(clientsMap);

        // generate clients .csv file with updated balances
        csvParser.generateClientsFile("csv_files/", clients);
    }
}
