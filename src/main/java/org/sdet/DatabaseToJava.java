package org.sdet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pojo.CustomerInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseToJava {
    public static void main(String[] args) {
        String sqlQuery = "select * from CustomerInfo where location ='Asia';";
        List<CustomerInfo> customerJson = connectToDatabase("jdbc:mysql://localhost:3306/Business", "root", "******", sqlQuery);
        //If you want seprate json files for the database rows
        javaObjectToSingleJson(customerJson);
        //if you want single multiple array json file for all the rows in the result
        javaObjectToJsonArray(customerJson);
    }

    public static List<CustomerInfo> connectToDatabase(String url, String username, String password, String query) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<CustomerInfo> customerJson = new ArrayList<>();
            while (resultSet.next()) {
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setCourseName(resultSet.getString(1));
                customerInfo.setPurchasedDate(resultSet.getString(2));
                customerInfo.setAmount(resultSet.getInt(3));
                customerInfo.setLocation(resultSet.getString(4));
                customerJson.add(customerInfo);
            }
            System.out.println("Completed generating seperate json files for each record in the result set");
            return customerJson;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.getCause();
            }
        }
    }

    public static void javaObjectToSingleJson(List<CustomerInfo> customerJson) {

        try {
            for (int i = 1; i < customerJson.size(); i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(new File("path" + i + ".json"), customerJson.get(i));
            }
        } catch (IOException e) {
            e.getCause();
        }
    }

    public static void javaObjectToJsonArray(List<CustomerInfo> customerJson) {
        Gson gson = new Gson();
        FileWriter fileWriter = null;
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 1; i < customerJson.size(); i++) {
                /*to convert single jsons to array of jsons*/
                //convert all the object in customerinfo array to json using gson and add the jsons into array of jsons.
                String customer = gson.toJson(customerJson.get(i));
                jsonArray.put(customer);
            }
            //inserting the json array again inside the jsonobject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", jsonArray);
            String escapeCharactersRemoved = jsonObject.toString().replace("\\", "");
            String modifiedString = escapeCharactersRemoved.replace("\"{", "{");
            String customerInfoArray = modifiedString.replace("}\"", "}");
            fileWriter = new FileWriter("path");
            fileWriter.write(customerInfoArray);
            fileWriter.flush();
            System.out.println("Successfully copied output to json file!!");
            System.out.println("Json File is generated");
        } catch (IOException e) {
            e.getCause();
        }
    }
}
