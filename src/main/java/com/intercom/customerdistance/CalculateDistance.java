package com.intercom.customerdistance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CalculateDistance {

    private static final String CUSTOMERS_OUTPUT_FILE = "output.txt";

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String USER_ID = "user_id";
    private static final String NAME = "name";

    private static final Double MAX_DISTANCE = 100D;

    public void outputEligibleCustomers(String inputFilePath) {
        JSONObject customerEntryJson;
        HashMap<Integer, String> eligibleCustomerMap = new HashMap<>();
        try {
            File inputFile = new File(inputFilePath);
            Scanner lineReader = new Scanner(inputFile);

            System.out.println(String.format("Input file %s successfully opened.", inputFilePath));
            while (lineReader.hasNextLine()) {
                customerEntryJson = createCustomerEntryJson(lineReader.nextLine());
                if (isCustomerEligible(customerEntryJson.getString(LONGITUDE), customerEntryJson.getString(LATITUDE))) {
                    eligibleCustomerMap.put(customerEntryJson.getInt(USER_ID), customerEntryJson.getString(NAME));
                }
            }
            lineReader.close();
            generateEligibleCustomerList(eligibleCustomerMap);
        } catch (FileNotFoundException exception) {
            System.out.println(String.format("Given file %s not found.", inputFilePath));
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("An error occurred.");
            exception.printStackTrace();
        }
    }

    private JSONObject createCustomerEntryJson(String customerJson) {
        try {
            return new JSONObject(customerJson);
        } catch (JSONException exception) {
            System.out.println("Customer entry is invalid JSON format.");
            exception.printStackTrace();
            throw exception;
        }
    }

    private boolean isCustomerEligible(String longitude, String latitude) {
        final Double OFFICE_LATITUDE = Math.toRadians(53.339428);
        final Double OFFICE_LONGITUDE = Math.toRadians(-6.257664);
        Double customerLatitude = Math.toRadians(Double.parseDouble(latitude));
        Double customerLongitude = Math.toRadians(Double.parseDouble(longitude));

        // calculate first the great circle distance in radians
        Double greatCircleDistance = Math.acos(Math.sin(OFFICE_LATITUDE) * Math.sin(customerLatitude)
                + Math.cos(OFFICE_LATITUDE) * Math.cos(customerLatitude) * Math.cos(OFFICE_LONGITUDE - customerLongitude));

        // then convert back to degrees
        greatCircleDistance = Math.toDegrees(greatCircleDistance);

        // each degree on a great circle of Earth is approximately 111km
        Double distanceInKilometers = 111 * greatCircleDistance;

        return distanceInKilometers < MAX_DISTANCE;
    }

    private void generateEligibleCustomerList(Map<Integer, String> eligibleCustomers) {
        try {

            FileWriter outputEligibleCustomers = new FileWriter(CUSTOMERS_OUTPUT_FILE);
            List<Map.Entry<Integer, String>> eligibleCustomerList = new LinkedList<>(eligibleCustomers.entrySet());

            eligibleCustomerList.sort(Map.Entry.comparingByKey());

            for (Map.Entry<Integer, String> eligibleCustomer : eligibleCustomerList) {
                outputEligibleCustomers.write(String.format("Customer Id: %d\tName: %s\n", eligibleCustomer.getKey(), eligibleCustomer.getValue()));
            }

            outputEligibleCustomers.close();
            System.out.println(String.format("Eligible customers printed to %s.", CUSTOMERS_OUTPUT_FILE));
        } catch (Exception exception) {
            System.out.println("Error while outputting eligible customers.");
            exception.printStackTrace();
        }
    }
}
