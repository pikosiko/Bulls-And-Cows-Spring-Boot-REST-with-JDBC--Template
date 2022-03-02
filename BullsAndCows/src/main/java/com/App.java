package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println("Running...");
        SpringApplication.run(App.class, args);
    }

}

//DB PASSWORD WAS LEFT BLANK -- INPUT PERSONAL PW

/*
* KNOWN ISSUES/MISSING THINGS:
* 1. Answer is displayed even if the game isn't won.  !!!FIXED!!!
* 2. If user input has a number more than once and, it is a cow, we get weird results because the number that is repeated is calculated as a cow more than once.
* 3. Timestamp has not been implemented.
* 4. Not  exception handling. --Can implement a few if there is time to spare.
* 5. Could add a DELETE method.
* 6. The method compare() could have been added to a service layer. Was first implemented within GameDaoImpl and was left there.
* 7. Tests have a problem.
* */