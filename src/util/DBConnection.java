/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author kmcgh15
 */
public class DBConnection {
     // jdbc URL parts
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ipaddress = "//wgudb.ucertify.com/U05mx3";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendor + ipaddress;
    
    // Driver and Connection Interface reference
    private static final String mysqlJDBCDriver = "com.mysql.jdbc.Driver";
    public static Connection conn;
    
    private static final String userName = "U05mx3"; // Username
    
    private static final String password = "53688548931"; // Password
    
    public static Connection makeConnection() 
    {
        try{
            Class.forName(mysqlJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void closeConnections() 
    {
        try {
        conn.close();
        System.out.println("Connection closed!");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }    
    

}
