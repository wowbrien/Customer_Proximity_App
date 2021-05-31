package com.intercom.customerdistance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStreamReader;
import java.util.Scanner;

@SpringBootApplication
public class CustomerDistanceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CustomerDistanceApplication.class, args);
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        processCustomerFileInput();
    }

    private void processCustomerFileInput() {
        final String EXIT = "exit";
        final String CUSTOMERS_RECORD_PATH = "src/main/resources/customerrecords/";
        String userInput;
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        CalculateDistance calculateDistance = new CalculateDistanceImpl();

        while (true) {
            System.out.println("Please enter the name of the customer file in resources:");
            userInput = scanner.nextLine();
            if (userInput.equals(EXIT)) {
                System.out.println("Thank you for using this application!");
                System.exit(0);
            }
            calculateDistance.generateEligibleCustomerFile(CUSTOMERS_RECORD_PATH + userInput);
        }
    }
}
