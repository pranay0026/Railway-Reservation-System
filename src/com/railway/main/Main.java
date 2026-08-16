package com.railway.main;

import java.sql.Connection;
import com.railway.ui.MainMenu;
import com.railway.util.DBConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Railway Reservation System...");
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.err.println("[Error] Failed to connect to MySQL database.");
            System.err.println("Please ensure MySQL is running on localhost:3306 and database 'railway_reservation_system' exists.");
            return;
        }

        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start();
        } catch (Exception e) {
            System.err.println("Unexpected application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Application terminated.");
        }
    }
}
