package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.service.FileService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

@RestController
@RequestMapping("/files")
public class FileController extends AppController<FileService, File> {

    @Autowired
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) { super(fileService);}

    @RequestMapping(value = "/singleFileUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadSingleFile(@RequestParam("file") MultipartFile file) throws IOException {

        String msg = "";
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\" + file.getOriginalFilename(); //set path directory
        if (file.getSize() < 1000000) {
            try(FileOutputStream fout = new FileOutputStream(path)) {
                fout.write(file.getBytes());
                fout.close();

                msg = msg + "File is successfully uploaded.";
            } catch (FileNotFoundException e) {
                msg = msg + "File not found" + e;
                e.printStackTrace();
            } catch (IOException ioe) {
                msg = msg + ioe.getMessage();
                ioe.printStackTrace();
            }
        } else {
            throw new Error("Maximum upload size exceeded 10MB.");
        }
        return msg;
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {

        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/gif")) {
            //serv image to resource folder
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename(); //set path directory
            FileOutputStream fout = new FileOutputStream(path);
            fout.write(file.getBytes());
            fout.close();
            //end

            String msg = "";
            String base64Image = "";
            String imagePath = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename();

            try (FileInputStream imageInFile = new FileInputStream(imagePath)) {
                // Reading a Image file from file system
                byte[] imageData;
                int maxByteImage = 1000000;

                imageData = new byte[maxByteImage];
                imageInFile.read(imageData);
                base64Image = Base64.getEncoder().encodeToString(imageData);

                File files = new File();
                files.name = file.getOriginalFilename();
                System.out.println("extension" + files.name);
                files.extension = file.getContentType().substring(6);
                System.out.println("extension" + files.extension);
                files.base64Image = base64Image;
                files.url = "http://localhost:8080/files/image/" + file.getOriginalFilename();
                fileService.save(files);

                msg = msg + "Image is successfully uploaded.";
            } catch (FileNotFoundException e) {
                msg = msg + "Image not found" + e;
                e.printStackTrace();
            } catch (IOException ioe) {
                msg = msg + ioe.getMessage();
                ioe.printStackTrace();
            }
            return msg;
        } else {
            throw new Error("Image not uploaded.");
        }
    }

    @RequestMapping("/image/{name}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable("name") String name) throws IOException {

        String filename = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + name;
        HttpHeaders headers = new HttpHeaders();
        try (InputStream inputImage = new FileInputStream(filename))  {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int l = inputImage.read(buffer);
            while(l >= 0) {
                outputStream.write(buffer, 0, l);
                l = inputImage.read(buffer);
            }

            String getExtension = name;
            int index = getExtension.indexOf( '.' );
            String extension = getExtension.substring(getExtension.indexOf( '.' )+1, getExtension.length());

            headers.set("Content-Type", "image/" + extension);
            headers.set("Content-Disposition", "inline; filename=\"" + name + "");
            return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
    }
}
