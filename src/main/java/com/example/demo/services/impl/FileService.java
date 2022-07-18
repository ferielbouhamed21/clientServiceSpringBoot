package com.example.demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.util.Map;

@Service
public class FileService {
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
        //try (FileReader reader = new FileReader(filename)) {
            //Read JSON file
          //  Object obj = jsonParser.parse(reader);

             Map readValue = new ObjectMapper().readValue(new File(filename),Map.class);

        return readValue;
    }


}
