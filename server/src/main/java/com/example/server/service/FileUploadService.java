package com.example.server.service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileUploadService {
    
    @Value("${do.storage.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String upload(MultipartFile file) throws IOException {

        Map<String, String> userDate = new HashMap<>();
        userDate.put("name", "Kenneth");
        userDate.put("uploadTime", Instant.now().toString());
        userDate.put("originalFilename", file.getOriginalFilename());
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userDate);

        String key = UUID.randomUUID().toString().substring(0,8);

        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(), ".");

        int count = 0;
        String filenameExt = "";
        String finalFileUpload = "";

        while(tk.hasMoreTokens()){
            if (count == 1) {
                filenameExt = tk.nextToken();
                break;
            }
            count++;
        }

        if (filenameExt.equals("blob")) {
            finalFileUpload = filenameExt + "png";
        }

        PutObjectRequest putRequest = new PutObjectRequest(
            bucketName, "myobject/%s.%s".formatted(key,finalFileUpload), 
            file.getInputStream(),
            metadata);

        putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);
        return "myobject/%s.%s".formatted(key, finalFileUpload);
        

    }
}
