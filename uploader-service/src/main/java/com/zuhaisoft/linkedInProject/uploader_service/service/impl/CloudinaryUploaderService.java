package com.zuhaisoft.linkedInProject.uploader_service.service.impl;

import com.cloudinary.Cloudinary;
import com.zuhaisoft.linkedInProject.uploader_service.service.UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryUploaderService implements UploaderService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {
        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return uploadResult.get("secure_url").toString();
        }
        catch (IOException ex){
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }
}
