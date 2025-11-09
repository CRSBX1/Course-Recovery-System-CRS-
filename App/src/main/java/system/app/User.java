/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.util.Map;
import java.util.List;
import java.security.SecureRandom;
/**
 *
 * @author Lenovo
 */
public class User {
    private String userID;
    private String name;
    private String email;
    private String password;
    private String role;
    
    public boolean login(String username, String Password, Map<String,List<String>> database){
        for(String i:  database.keySet()){
            if(database.get(i).get(0) == username & database.get(i).get(2) == Password){
                return true;
            }
        }
        return false;
    }
    
    public StringBuilder resetPassword(){
        String collection = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder reset = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for(int i=0;i<6;i++){
            int index = random.nextInt(collection.length());
            reset.append(collection.charAt(index));
        }
        //Send email notification here
        return reset; //give password for verification
    }
}
