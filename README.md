# Customer Proximity App
## Description
Java 8 Springboot application that reads in customers file and outputs customers within 100km of Dublin Office.

## Requirements
* Java 8
* Java IDE (IntelliJ, Eclipse, etc)

## How to Run Application
* Clone the project from GitHub.
* Open the project in your preferred Java IDE, for me I use IntelliJ Community Edition.
* Build the project to download and install dependencies.
* Run CustomerDistanceApplication and it will prompt the user for a file name. Enter 'customers.txt'.
    * Note this file can be replaced with any other as long as it is located in /resources/customerrecords.
* If the file is valid it will generate 'output.txt' into project's root directory.
    * This output file will contain the list of eligible customers in ascending order by their ID.
* It will then prompt the user for another file.
* To exit the application, type 'exit'.

## How to Run Tests
* Follow the steps to run the application above.
* The tests are located in CalculateDistanceImplTest.
* You can run them from there and it will indicate if they have all passed.
* If any fail, the stacktrace as to why they failed will be logged.

## Details
I chose SpringBoot as it is simple and easy to setup allowing for me to quickly get to work with the solution. Spring Initializr will build a project for you with all the dependencies that you need making the experience very straight forward.
\
\
The application allows for mutiple customer records to be processed without having to restart the application for each one. The application was designed so that it would only process valid customer records but not crash if invalids were provided. Proper logging was done with all methods including obfuscation of developer related information such as stacktraces. All this was done to keep in mind that a requirement for this application was to be production ready.
\
\
An adherence to clean code and conventional standards was followed in the development to allow for easy readability and clarity, found here (https://www.oracle.com/java/technologies/javase/codeconventions-contents.html).
\
\
The formula I have used to calculcate the distances between customers and the office is the first formula found here (https://en.wikipedia.org/wiki/Great-circle_distance#Formulae) as is required from the specification.
\
\
Testing is done with Junit and Mockito, I have created test files that are used to ensure that my application has proper error handling, (such as if the customers record file is invalid).
