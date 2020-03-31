/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import ui.UI;

/**
 *
 * @author anttiollikkala
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner in = new Scanner(System.in);
        
        Connection yhteys = null;
        try {
            Class.forName("org.sqlite.JDBC");
            yhteys = DriverManager.getConnection("jdbc:sqlite:tietokanta.db");
        } catch (Exception e) {
            System.out.println("Error while connecting to database: " + e.getMessage());
            System.exit(1);
        }
        
        UI ui = new UI(in);
        ui.Start();
    }
}
