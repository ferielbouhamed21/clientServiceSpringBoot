package com.example.demo.services.facade;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileUpload {

    public void putObject(String objectId,MultipartFile file) throws Exception;
    public InputStream getObject( String fileId);

}
