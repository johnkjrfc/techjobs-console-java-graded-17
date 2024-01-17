import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by LaunchCode
 */
public class TechJobs {

    static Scanner in = new Scanner(System.in);

    public static void main (String[] args) {

        // Initialize our field map with key/name pairs. Essentially just the headers of our CSV, template for later outputs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // initialize Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {
            String actionChoice = getUserSelection("View jobs by (type 'x' to quit):", actionChoices);

            //break if input is x in getUserSelection instance
            if (actionChoice == null) {
                break;
            //user input is list, generate next UI selection for list using columns in csv
            } else if (actionChoice.equals("list")) {
                String columnChoice = getUserSelection("List", columnChoices);

                // loops for next user input (Explain both findAll methods
                //user inputs 0 for all
                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());
                //user input another valid input
                } else {
                    ArrayList<String> results = JobData.findAll(columnChoice);

                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");

                    // Print list value in selected column
                    for (String item : results) {
                        System.out.println(item);
                    }
                }

            } else { // choice is "search"

                //print UI instructions for search
                String searchField = getUserSelection("Search by:", columnChoices);

                // What is their search term?
                System.out.println("\nSearch term:");
                String searchTerm = in.nextLine();

                if (searchField.equals("all")) {
                    printJobs(JobData.findByValue(searchTerm));
                } else {
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }
            }
        }
    }

    // ﻿Returns the key of the selected item from the choices Dictionary
    //Utility Method that sets up a menu header for a hashmap of choices for the UI
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        int choiceIdx = -1;     //User choice variable. init at -1, which is invalid, before user input received.
        Boolean validChoice = false;    //variable used as flag to see if input is valid. start false
        String[] choiceKeys = new String[choices.size()];   //stores keys of choices, utilizing .size to accommodate changes in choices.

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        int i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }
        //Do-while valid choice is false(default) to make this block execute at least once.
        do {

            System.out.println("\n" + menuHeader);

            // Print available choices index and value
            for (int j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }
            //scanner if User input is an integer, update choice idx to input
            if (in.hasNextInt()) {
                choiceIdx = in.nextInt();
                in.nextLine();
            //User enters x, signal to quit is sent
            } else {
                String line = in.nextLine();
                boolean shouldQuit = line.equals("x");
                if (shouldQuit) {
                    return null;
                }
            }

            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while(!validChoice);
        // returns the index the user selected from choiceKeys
        return choiceKeys[choiceIdx];
    }

    // Print a list of jobs
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        if (someJobs.isEmpty()) {
            System.out.print("No Results");
        } else {
            for (HashMap<String, String> job : someJobs) {
                System.out.println("\n*****");
                for (Map.Entry<String, String> jobDetail : job.entrySet()) {
                    System.out.println(jobDetail.getKey() + ": " + jobDetail.getValue());
                }
                System.out.println("*****");
            }
        }
    }
}
