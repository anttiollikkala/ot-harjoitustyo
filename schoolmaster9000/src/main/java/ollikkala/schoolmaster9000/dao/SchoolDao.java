/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author anttiollikkala
 */
public class SchoolDao {
    
    private Connection conn;
    
    public SchoolDao(Connection conn) {
        this.conn = conn;
    }
    
    public void create(String schoolName){
        try {
            PreparedStatement stmt = this.conn.prepareStatement(
                            "INSERT INTO config (config_key, config_val) VALUES (?,?)"
                    );
        stmt.setString(1, "school_name");
        stmt.setString(2, schoolName);
        stmt.execute();
        } catch (SQLException e) {
            
        }
    }
    
    public String GetSchoolName() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("SELECT config_val FROM config WHERE config_key = 'school_name'");
            ResultSet result = s.getResultSet();
            if (result.next()) {
                return result.getString(1);
            } else {
                return "";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return "";
    }
    
    public boolean install() {
        try {
            Statement s = this.conn.createStatement();
            s.execute("CREATE TABLE config ("
                    + "id INTEGER PRIMARY KEY, "
                    + "config_key TEXT UNIQUE NOT NULL, "
                    + "config_val TEXT"
                    + ")");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
