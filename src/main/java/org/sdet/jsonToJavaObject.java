package org.sdet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pojo.CustomerInfo;

import java.io.File;

public class jsonToJavaObject {
    public static void main(String[] args) {
        String jsonPath = "/Users/pragadeesh/Downloads/Java/SQLToAPI/resources/customerInfo/customerInfo_1.json";
        backToJavaObject(jsonPath);
    }

    private static void backToJavaObject(String jsonPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerInfo customerInfo = objectMapper.readValue(new File(jsonPath), CustomerInfo.class);
            System.out.println(customerInfo.getCourseName());
        } catch (Exception e) {
            e.getCause();
        }
    }


}
