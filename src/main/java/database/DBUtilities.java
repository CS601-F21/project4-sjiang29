package database;


import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Reference: CS 601 example code
 * Utility class to read db config file
 */
public class DBUtilities {

    public static final String configFileName = "dbConfig.json";

    /**
     * Read in the configuration file.
     * @return
     */
    public static DBConfig readConfig() {

        DBConfig config = null;
        Gson gson = new Gson();
        try {
            config = gson.fromJson(new FileReader(configFileName), DBConfig.class);
        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return config;
    }


}

