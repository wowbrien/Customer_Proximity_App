package com.intercom.customerdistance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerDistanceApplication {

    private static final String CUSTOMERS_INPUT_FILE = "customers.txt";


    public static void main(String[] args) {
        SpringApplication.run(CustomerDistanceApplication.class, args);
        CalculateDistance calculateDistance = new CalculateDistance();
        calculateDistance.outputEligibleCustomers(CUSTOMERS_INPUT_FILE);
    }

}
