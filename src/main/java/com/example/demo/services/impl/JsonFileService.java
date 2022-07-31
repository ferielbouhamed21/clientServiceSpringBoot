package com.example.demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class JsonFileService {
    public void saveToFile(String str, String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try{
                FileWriter writer = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(writer);
                bw.write(str);
                bw.close();
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public String getFromFile(String str, String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("file does not exist");
        }
        else {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String line = br.readLine();
                while(line != null){
                    line = br.readLine();
                        return line;
                }
                br.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return "empty file";
    }


    public Map getFromJson(String filename) throws IOException {

             Map readValue = new ObjectMapper().readValue(new File(filename),Map.class);

        return readValue;
    }

    public void saveToJson(String filename, String key, String value) throws IOException{
        Map readValue = new ObjectMapper().readValue(new File(filename),Map.class);
        Object client_id =readValue.get("client_id");
        Object client_secret =readValue.get("client_secret");
        Object refresh_token =readValue.get("refresh_token");
        Object access_token =readValue.get("access_token");
        Map<String, String> obj = new HashMap<>();
        obj.put("client_id", client_id.toString());
        obj.put("client_secret", client_secret.toString());
        if(key.equals("access_token")){
            obj.put("access_token",value);
            obj.put("refresh_token",refresh_token.toString());
        }
        else if (key.equals("refresh_token")){
            obj.put("refresh_token",value);
            obj.put("access_token",access_token.toString());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(new File(filename), obj);
    }

}
