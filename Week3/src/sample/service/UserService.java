package sample.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class UserService {
    private final String filePath =
            "Week3/Data/users.txt";

    public boolean isAuthorized(String userName, String password){
        try(
                BufferedReader reader = new BufferedReader(new FileReader(filePath))
        ){
            String line;
            while ((line=reader.readLine()) != null){

                String[] parts = line.split(",");

                if(parts[0].equalsIgnoreCase(userName.toLowerCase()) && parts[1].equals(password)){
                    return true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;

    }

    public boolean userNameExists(String userName){
        try(
                BufferedReader reader = new BufferedReader(new FileReader(filePath))
        ){
            String line;
            while ((line=reader.readLine()) != null){

                String[] parts = line.split(",");
                if (parts[0].equalsIgnoreCase(userName.toLowerCase())){
                    return true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;

    }

    public boolean registerNewUser(String userName, String password){

        userName = userName.toLowerCase();
        if (userNameExists(userName)){
            return false;
        }

        try(
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))
        ){
            writer.write(userName + "," + password);
            writer.newLine();

            return true;
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;

    }

}
