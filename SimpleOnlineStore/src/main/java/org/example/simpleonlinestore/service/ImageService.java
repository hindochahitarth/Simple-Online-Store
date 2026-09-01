package org.example.simpleonlinestore.service;

import jakarta.mail.Multipart;
import org.example.simpleonlinestore.entity.Image;
import org.example.simpleonlinestore.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.html.HTMLImageElement;

import java.io.IOException;
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository){
        this.imageRepository=imageRepository;
    }
    //create new image using uploadimage and multipartfile
    // Multipart/formdata when user upload file form postman
    public String uploadImage(MultipartFile file) throws IOException{
        Image image= Image.builder()
                .name(file.getOriginalFilename()) //'file.getOriginalFilename()' extracts the original name of the uploaded file
                .type(file.getContentType()) //'file.getContentType()' extracts the MIME type (jpg or png)
                .imageData(file.getBytes()) //'file.getBytes()' reads the raw binary payload of the image directly from memory into a byte array (byte[]).
                .build();
        imageRepository.save(image);
        return file.getOriginalFilename()+"Uploaded ";
    }
}
