package com.example.server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.service.FileUploadService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Controller
public class FileUploadController {
    
    @Autowired
    private FileUploadService fileSvc;

    @PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(
        @RequestPart MultipartFile imageFile,
        @RequestPart String title,
        @RequestPart String complain
    ) {

        String key;
        JsonObject jsonPayload= null;

        try {
            key = this.fileSvc.upload(imageFile);
            jsonPayload = Json.createObjectBuilder()
            .add("image-key", key)
            .build();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.ok(jsonPayload.toString());
    } 


}
