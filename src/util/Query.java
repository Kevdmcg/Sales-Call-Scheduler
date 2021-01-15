/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import static util.DBConnection.conn;

/**
 *
 * @author kmcgh15
 */
public class Query {
    
private Connection con;
private static PreparedStatement ps;
private static ResultSet rs;
private static String query;

    public static void makeQuery(String q){
        query =q;
        try{
            ps=(PreparedStatement) conn.prepareStatement(q);
            // determine query execution
            if(query.toLowerCase().startsWith("select"))
                rs=ps.executeQuery(q);
             if(query.toLowerCase().startsWith("delete")||query.toLowerCase().startsWith("insert")||query.toLowerCase().startsWith("update"))
                ps.executeUpdate(q);
            
        }
        catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }
    public static ResultSet getResult(){
        return rs;
    } 
    
    
}
