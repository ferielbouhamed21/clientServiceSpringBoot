package com.example.demo.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service()
public class ZohoDeskService {
    //private String code = "1000.8bb5dabd8d996c44fc01c60228b2837a.4a20be7928d7730082f07d1fa55eacd8";
   // private String client_id = "1000.B905WHUHL2678ACMZCXY986HX48APA";
   // private String client_secret = "ef6e432f28188fe09c5d4842f3fff4f4caa9669db5";
    private final String scope = "Desk.tickets.ALL";
    private static String access_token;
    private static String refresh_token;
    @Autowired
    private FileService fileService;

    public void getAuthToken(String code) throws Exception {
        Map credentials = fileService.getFromJson("credentials.json");
        Object client_id =credentials.get("client_id");
        Object client_secret =credentials.get("client_secret");

        String url = "https://accounts.zoho.com/oauth/v2/token?code=" + code + "&client_id=" + client_id.toString() + "&client_secret=" + client_secret.toString() + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String resultAsJsonStr = restTemplate.postForObject(url, "", String.class);
        JsonNode jsonNode = mapper.readTree(resultAsJsonStr);
        refresh_token = jsonNode.get("refresh_token").asText();
        access_token = jsonNode.get("access_token").asText();
        fileService.saveToFile(access_token,"access_token.txt");
        fileService.saveToFile(refresh_token,"refresh_token.txt");
        System.out.println(refresh_token);
        System.out.println(access_token);
    }

    public void getAccessToken() throws Exception {
        Map credentials = fileService.getFromJson("credentials.json");
        Object client_id =credentials.get("client_id");
        Object client_secret =credentials.get("client_secret");
        String url = "https://accounts.zoho.com/oauth/v2/token?refresh_token=" + refresh_token + "&client_id=" + client_id.toString() + "&scope=" + scope + "&client_secret=" + client_secret.toString() + "&grant_type=refresh_token";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String resultAsJsonStr = restTemplate.postForObject(url, "", String.class);
        JsonNode jsonNode = mapper.readTree(resultAsJsonStr);
        access_token = jsonNode.get("access_token").asText();
        fileService.saveToFile(access_token,"access_token.txt");
    }

    public String createTicket(Map<String, String> ticket) throws Exception {
        String url = "https://desk.zoho.com/api/v1/tickets";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Zoho-oauthtoken " + access_token);
        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(ticket, headers);
        RestTemplate restTemplate = new RestTemplate();
        String resultAsJsonStr = restTemplate.postForObject(url, request, String.class);
        return resultAsJsonStr;
    }

}
