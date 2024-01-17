import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
//Public makes this accessible in the rest of program. imports data from csv, stores utility methods for searches
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";  //private static final, so it CANNOT be changed anywhere else
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;  //declare ArrayList of HashMap objects, allJobs

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    //utility method that lists by field
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();
        //for row in alljobs,
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        Collections.sort(values);
        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();
//        Collections.sort(allJobs);
        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    //User selects search by a column
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //for row in all jobs
        for (HashMap<String, String> row : allJobs) {
            //retrieve aValue from each row in the selected column
            String aValue = row.get(column);
            //if aValue matches value (case insensitive), add that row to jobs array
            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    //User selects 'search' and 'all'
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        // load data, if not already loaded
        loadData();
        //declare empty array for our search matches
        ArrayList<HashMap<String, String>> searchMatches = new ArrayList<>();


        //for row in allJobs
        for (HashMap<String, String> row : allJobs) {
            //iterate through keys in allJobs
            for (String key : row.keySet()) {
                String columnValue = row.get(key);

                if (columnValue != null) {
                    //check if columnValue contains value
                    if (columnValue.toLowerCase().contains(value.toLowerCase())) {
                        //check that searchMatches doesn't already have this row, if not row is added
                        if (!searchMatches.contains(row)) {
                            searchMatches.add(row);
                            break;
                        }
                    }

                }
            }
        }


        return searchMatches;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }
        //!isDataLoaded, attempts to format csv. try avoids IO exceptions
        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE); //read
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in); //first record in 'in' used as header
            List<CSVRecord> records = parser.getRecords(); //retrieve all records
            Integer numberOfColumns = records.get(0).size(); //determine no. columns in 'in'

            //string array. retrieve map<String, Int>. grab the keys. create array from a new String length of numcolumns
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]); //columns to array

            //init all jobs as empty arrayList which will store a hashMap of each job in csv
            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            //for record in records
            for (CSVRecord record : records) {
                //init a new empty hashMap
                HashMap<String, String> newJob = new HashMap<>();
                //for header in headers [],
                for (String headerLabel : headers) {
                    //put the header as key, and record @ header column as value in newJob
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
