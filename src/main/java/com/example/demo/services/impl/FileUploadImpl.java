package com.example.demo.services.impl;

import com.example.demo.services.facade.FileUpload;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileUploadImpl implements FileUpload {

    private static String bucketName="data";
    private static  String endpoint ="http://172.22.224.1:9000";
    private static  int port =9000;
    private static  boolean secure =false;
    private static  String accessKey ="hd92j67z92dHJpRK";
    private static  String secretKey ="rMzIlW9gbFkVeRl8SWVkrx0DPffFgKEh";
    private static MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint , port , secure)
                .credentials(accessKey, secretKey)
                .build();

    public void putObject(String objectId, MultipartFile file) throws Exception {

        //String mimeType = file.getOriginalFilename().split("\\.")[1];
        String mimeType = file.getContentType();
        InputStream is = file.getInputStream();

        PutObjectArgs.Builder putObjectArgs = PutObjectArgs.builder();

        if (mimeType != null) putObjectArgs.contentType(mimeType);

        try {
            minioClient.putObject(

                    putObjectArgs.bucket(bucketName).object(objectId)

                            .stream(is, -1, 10485760).build()

            );
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getObject( String fileId) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileId).build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
