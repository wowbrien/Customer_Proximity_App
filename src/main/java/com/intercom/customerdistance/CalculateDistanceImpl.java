package com.intercom.customerdistance;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CalculateDistanceImpl implements CalculateDistance {

    private Logger logger = LoggerFactory.getLogger(CalculateDistanceImpl.class);

    /**
     * Generates file containing all eligible customers for free food and drink
     *
     * @param inputFileName the name of the input customers file
     */
    public void generateEligibleCustomerFile(String inputFileName) {
        final String CUSTOMERS_OUTPUT_FILE = "output.txt";

        try {
            FileWriter outputEligibleCustomers = new FileWriter(CUSTOMERS_OUTPUT_FILE);

            HashMap<Integer, String> customerMap = generateCustomerMap(inputFileName);

            if (!customerMap.isEmpty()) {
                for (Map.Entry<Integer, String> eligibleCustomer : sortCustomerListAscending(customerMap)) {
                    outputEligibleCustomers.write(String.format("Customer Id: %d\tName: %s\n", eligibleCustomer.getKey(), eligibleCustomer.getValue()));
                }
                outputEligibleCustomers.close();
                logger.info(String.format("Eligible customers printed to %s.", CUSTOMERS_OUTPUT_FILE));
            }
        } catch (IOException e) {
            logger.error(String.format("Error outputting eligible customers to %s.", CUSTOMERS_OUTPUT_FILE));
        }
    }

    /**
     * Generates customer map based on input file given with key(user_id), value(name)
     *
     * @param inputFilePath the file path of the customers located in resources
     * @return returns map of eligible customers for free food & drink
     */
    private HashMap<Integer, String> generateCustomerMap(String inputFilePath) {
        final String LATITUDE = "latitude";
        final String LONGITUDE = "longitude";
        final String USER_ID = "user_id";
        final String NAME = "name";

        HashMap<Integer, String> eligibleCustomerMap = new HashMap<>();
        JSONObject customerEntryJson;
        File inputFile = new File(inputFilePath);

        try {
            Scanner lineReader = new Scanner(inputFile);

            logger.info(String.format("Input file %s successfully opened.", inputFile.getName()));
            while (lineReader.hasNextLine()) {
                customerEntryJson = new JSONObject(lineReader.nextLine());
                if (isCustomerEligible(customerEntryJson.getString(LONGITUDE), customerEntryJson.getString(LATITUDE))) {
                    eligibleCustomerMap.put(customerEntryJson.getInt(USER_ID), customerEntryJson.getString(NAME));
                }
            }
            lineReader.close();

        } catch (JSONException exception) {
            logger.error(String.format("Given file %s has unexpected JSON format.", inputFile.getName()));
        } catch (FileNotFoundException exception) {
            logger.error(String.format("Given file %s not found in resources.", inputFile.getName()));
        }
        return eligibleCustomerMap;
    }

    /**
     * Checks if the given customer is eligible for free food & drink by calculating if they are within 100km of the dublin office
     *
     * @param longitude the longitude of the given customer
     * @param latitude  the latitude of the given customer
     * @return boolean if the customer is eligible for free food & drink, i.e. within 100km of the dublin office
     */
    private boolean isCustomerEligible(String longitude, String latitude) {
        Double customerLatitude = Math.toRadians(Double.parseDouble(latitude));
        Double customerLongitude = Math.toRadians(Double.parseDouble(longitude));
        final Double OFFICE_LATITUDE = Math.toRadians(53.339428);
        final Double OFFICE_LONGITUDE = Math.toRadians(-6.257664);
        final Double MAX_DISTANCE = 100D;

        // calculate first the great circle distance in radians
        Double greatCircleDistance = Math.acos(Math.sin(OFFICE_LATITUDE) * Math.sin(customerLatitude)
                + Math.cos(OFFICE_LATITUDE) * Math.cos(customerLatitude) * Math.cos(OFFICE_LONGITUDE - customerLongitude));

        // then convert back to degrees
        greatCircleDistance = Math.toDegrees(greatCircleDistance);

        // each degree on a great circle of Earth is approximately 111km
        Double distanceInKilometers = 111 * greatCircleDistance;

        return distanceInKilometers < MAX_DISTANCE;
    }


    /**
     * @param eligibleCustomers map of all eligible customers
     * @return sorted list of customers in ascending order
     */
    private List<Map.Entry<Integer, String>> sortCustomerListAscending(Map<Integer, String> eligibleCustomers) {
        List<Map.Entry<Integer, String>> eligibleCustomerList = new LinkedList<>(eligibleCustomers.entrySet());
        eligibleCustomerList.sort(Map.Entry.comparingByKey());
        return eligibleCustomerList;
    }
}
