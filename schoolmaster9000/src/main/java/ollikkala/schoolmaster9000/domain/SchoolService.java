/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author anttiollikkala
 */
public class SchoolService {
 
    private Connection connection;
    
    public SchoolService(Connection connection) {
        this.connection = connection;
        this.init();
    }
    
    private void init() {
        try {
            Statement stmt = this.connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Options (key TEXT UNIQUE NOT NULL, value TEXT)");
        } catch (SQLException e) {
            System.out.println("Cannot initialize database: " + e.getMessage());
            System.exit(1);
        }
        
    }
    
}
