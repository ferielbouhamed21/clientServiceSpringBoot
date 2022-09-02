package com.example.demo.services.impl;

import com.example.demo.services.facade.FileUpload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service()
public class ZohoDeskService {
    private final String scope = "Desk.tickets.ALL";
    private static String access_token;
    private static String refresh_token;
    @Autowired
    private JsonFileService fileService;
    @Autowired
    private FileUpload fileUpload;

    public void getAuthToken(String code) throws Exception {
        Map credentials = fileService.getFromJson("credentials.json");
        Object client_id = credentials.get("client_id");
        Object client_secret = credentials.get("client_secret");

        String url = "https://accounts.zoho.com/oauth/v2/token?code=" + code + "&client_id=" + client_id.toString() + "&client_secret=" + client_secret.toString() + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String resultAsJsonStr = restTemplate.postForObject(url, "", String.class);
        JsonNode jsonNode = mapper.readTree(resultAsJsonStr);
        refresh_token = jsonNode.get("refresh_token").asText();
        access_token = jsonNode.get("access_token").asText();
        fileService.saveToJson("credentials.json", "refresh_token", refresh_token);
        fileService.saveToJson("credentials.json", "access_token", access_token);
        System.out.println(refresh_token);
        System.out.println(access_token);
    }

    public String getAccessToken() throws Exception {
        Map credentials = fileService.getFromJson("credentials.json");
        Object client_id = credentials.get("client_id");
        Object client_secret = credentials.get("client_secret");
        Object refresh_token = credentials.get("refresh_token");
        String url = "https://accounts.zoho.com/oauth/v2/token?refresh_token=" + refresh_token.toString() + "&client_id=" + client_id.toString() + "&scope=" + scope + "&client_secret=" + client_secret.toString() + "&grant_type=refresh_token";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String resultAsJsonStr = restTemplate.postForObject(url, "", String.class);
        JsonNode jsonNode = mapper.readTree(resultAsJsonStr);
        access_token = jsonNode.get("access_token").asText();
        fileService.saveToJson("credentials.json", "access_token", access_token);
        return access_token;
    }


    public String createTicket(Map<String, Object> ticket) throws Exception {
        String url = "https://desk.zoho.com/api/v1/tickets";
        String json = new ObjectMapper().writeValueAsString(ticket);
        HttpHeaders headers = new HttpHeaders();
        System.out.println(json);
        headers.add("Authorization", "Zoho-oauthtoken " + getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        //headers.add("orgId", "783870836");
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        RestTemplate restTemplate = new RestTemplate();
        String responseEntity = restTemplate.postForObject(url, request, String.class).toString();
        Map<String, Object> response = new ObjectMapper().readValue(responseEntity, HashMap.class);
        String id = response.get("id").toString();
        System.out.println("id: " + id);
        return id;
    }

    public String attachFileToATicket(String ticketId, MultipartFile file) throws Exception {

        String url = "https://desk.zoho.com/api/v1/tickets/" + ticketId + "/attachments";

        LinkedMultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        fileMap.add("Content-disposition", "form-data; name=file; filename=" + file.getOriginalFilename());
        HttpEntity<byte[]> doc = new HttpEntity<>(file.getBytes(), fileMap);

        LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
        multipartReqMap.add("file", doc);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Zoho-oauthtoken " + getAccessToken());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Accept", "*/*");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multipartReqMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.exchange(url, HttpMethod.POST, request, String.class).getBody();
        String uuid = UUID.randomUUID().toString();
        fileUpload.putObject(uuid, file);
        return result;
    }

    //get aLL tickets
    public JsonNode getAllTickets() throws Exception {
        String url = "https://desk.zoho.com/api/v1/tickets";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Zoho-oauthtoken " + getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "*/*");
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(result);
        return jsonNode;
    }

}
