/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kmcgh15
 */
public class Rolodex {
    
    private static ObservableList<Customer> allCustomer = FXCollections.observableArrayList();
    private static int customerIDCount = 2;
   
    
     public static void addCustomer(Customer customer) {
         allCustomer.add(customer);
         
     }
     
          
     public static int getCustomerIDnum() {
        customerIDCount++;
        return customerIDCount;
    }
     
    
     
     public static void updateCustomer(int index, Customer selectedCustomer) {
         allCustomer.set(index, selectedCustomer);
         
     }
     
    
     
     public static void deleteCustomer(Customer selectedCustomer) {
         allCustomer.remove(selectedCustomer);
         
     }
     
    
     
     public static ObservableList<Customer> getAllCustomer() {
        return allCustomer; 
     }
    
}
