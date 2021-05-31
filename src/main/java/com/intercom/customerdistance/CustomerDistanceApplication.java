package com.intercom.customerdistance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStreamReader;
import java.util.Scanner;

@SpringBootApplication
public class CustomerDistanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerDistanceApplication.class, args);
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("Please enter the name of the customer file in resources:");
        CalculateDistanceImpl calculateDistanceImpl = new CalculateDistanceImpl();
        calculateDistanceImpl.generateEligibleCustomerFile(scanner.nextLine());
    }
}
