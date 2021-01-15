/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author kmcgh15
 */
// interacts with user table/ current user
// userId (int) , userName , password , active (tinyint(4)) , createDate , createdBy , lastUpdate , lastUpdatedBy
public class User {

    private static int userId; //auto incremented in database
    private static String username;
    private static String password;

    public User() {
        userId = 0;
        username = null;
        password = null;

    }

    //constructor
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    //getters
    public static int getUserId() {
        return userId;
    }

    public static String getUsername() {
        return username;
    }

    public User(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    //setters
    public static void setUserId(int userId) {
        User.userId = userId;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

}
