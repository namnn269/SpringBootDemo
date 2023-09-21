package com.example.springbootdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @GetMapping
    public ResponseEntity<?> image() throws IOException {

        BufferedImage bImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImage.createGraphics();
        g2d.drawLine(0, 0, 300, 300);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        //

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        return new ResponseEntity<>(bImage, HttpStatus.OK);
    }


}
