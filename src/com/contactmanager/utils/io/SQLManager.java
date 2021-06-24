package com.contactmanager.utils.io;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {
	public static final String SQL_CONFIG_FILE_PATH = File.separator +"configurations" + File.separator +"dataConfig.db";
	
	public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + DataStorageHandler.PATH_OF_PROGRAM + SQL_CONFIG_FILE_PATH;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
