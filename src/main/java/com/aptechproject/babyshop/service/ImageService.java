package com.aptechproject.babyshop.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

@Service
public class ImageService {
    
    public byte[] convertToWebP(MultipartFile file) throws IOException {
        // 1. we want to read the raw file
        // 2. Configure the writer that would configure the file to webP
        // 3. Convert and return the bytes

        // 1
        ImmutableImage image = ImmutableImage.loader().fromBytes(file.getBytes());
        // 2
        WebpWriter writer = WebpWriter.DEFAULT.withQ(80); // 80% quality compression
        // 3
        return image.bytes(writer);
    }
}
