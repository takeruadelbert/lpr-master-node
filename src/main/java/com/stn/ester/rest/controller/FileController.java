package com.stn.ester.rest.controller;

import com.stn.ester.rest.dao.jpa.FileRepository;
import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import com.stn.ester.rest.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController extends AppController<FileService, File> {

    private static String directory_upload = "\\assets\\uploads\\";
    private static String directory_upload_file = "\\assets\\uploads\\file\\";
    private static String directory_upload_profile_picture = "\\assets\\uploads\\profile_picture\\";
    private static String directory_upload_base64 = "\\assets\\uploads\\base64\\";
    private static String directory_upload_file_linux = "/assets/uploads/file/";
    private static String directory_upload_profile_picture_linux = "/assets/uploads/profile_picture/";
    private static String directory_upload_base64_linux = "/assets/uploads/base64/";
    private static String url_file = "/files/get/file/";
    private static String url_profile_picture = "/files/get/profile_picture/";
    private static String protocol = "http://";

    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private GlobalFunctionHelper globalFunctionHelper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    public FileController(FileService fileService) { super(fileService);}

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile[] files) throws IOException {
        //get operation system
        if (globalFunctionHelper.getOS().equals("Linux")) {directory_upload_file = directory_upload_file_linux;}
        for(MultipartFile file : files) {
            String setFile = System.getProperty("user.dir") + directory_upload_file + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
            Optional<File> checkNameFileIfExist = fileRepository.findByName(globalFunctionHelper.getNameFile(file.getOriginalFilename()));
            if (!checkNameFileIfExist.equals(Optional.empty())) {
                setFile = System.getProperty("user.dir") + directory_upload_file + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow() + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(setFile)) {
                fileOutputStream.write(file.getBytes());
                fileOutputStream.close();

                File voData = new File();
                voData.name = globalFunctionHelper.getNameFile(file.getOriginalFilename());
                voData.url = protocol + request.getServerName() + ":" + request.getServerPort() + url_file + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                if (!checkNameFileIfExist.equals(Optional.empty())) {
                    voData.name = globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow();
                    voData.url = protocol + request.getServerName() + ":" + request.getServerPort() + url_file + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow() + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                }
                voData.extension = globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                fileService.Create(voData);

            } catch (FileNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (IOException ioe) {
                return new ResponseEntity<>(ioe.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("File is successfully uploaded.", HttpStatus.OK);
    }

    @RequestMapping(value = "/upload_photo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/gif") || file.getContentType().equals("image/bmp")) {
            //get operation system
            if (globalFunctionHelper.getOS().equals("Linux")) {directory_upload_profile_picture = directory_upload_profile_picture_linux;}
            //serv image to assets folder
            String setFile = System.getProperty("user.dir") + directory_upload_profile_picture + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
            Optional<File> checkNameFileIfExist = fileRepository.findByName(globalFunctionHelper.getNameFile(file.getOriginalFilename()));
            //if name is exist can't override file
            if (!checkNameFileIfExist.equals(Optional.empty())) {
                setFile = System.getProperty("user.dir") + directory_upload_profile_picture + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow() + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
            }
            try(FileOutputStream fileOutputStream = new FileOutputStream(setFile)) {
                fileOutputStream.write(file.getBytes());
                fileOutputStream.close();

                File voData = new File();
                voData.name = globalFunctionHelper.getNameFile(file.getOriginalFilename());
                voData.url = protocol + request.getServerName() + ":" + request.getServerPort() + url_profile_picture + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                if (!checkNameFileIfExist.equals(Optional.empty())) {
                    voData.name = globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow();
                    voData.url = protocol + request.getServerName() + ":" + request.getServerPort() + url_profile_picture + globalFunctionHelper.getNameFile(file.getOriginalFilename()) + "-" + globalFunctionHelper.getTimeNow() + globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                }
                voData.extension = globalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                fileService.Create(voData);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return new ResponseEntity(ioe.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("File is successfully uploaded.", HttpStatus.OK);
        } else {
            return new ResponseEntity("File is not a valid image. Only JPG, JPEG, PNG, GIF and BMP files are allowed.", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/upload_base64", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> uploadPhotoBase64(@RequestParam(value = "encodeBase64") String encodeBase64) throws IOException {
        //get operation system
        if (globalFunctionHelper.getOS().equals("Linux")) {directory_upload_base64 = directory_upload_base64_linux;}
        //decode base64 string
        String imagePath = System.getProperty("user.dir") + directory_upload_base64 + globalFunctionHelper.getTimeNow() + globalFunctionHelper.getExtensionFromBase64(encodeBase64);
        try (FileOutputStream imageOutFile = new FileOutputStream(imagePath)) {
            byte[] imageByteArray = Base64.getDecoder().decode(globalFunctionHelper.removeDataFromBase64Two(encodeBase64));
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new ResponseEntity(ioe.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("File is successfully uploaded.", HttpStatus.OK);
    }

    @RequestMapping("/get/{directory_name}/{name}")
    public ResponseEntity<byte[]> getFile(@PathVariable("directory_name") String directory_name, @PathVariable("name") String name) throws IOException {
        //get directory based on operation system
        if (globalFunctionHelper.getOS().equals("Linux")) {
            if (directory_name.equals("file")) {directory_upload = directory_upload_file_linux;}
            if (directory_name.equals("profile_picture")) {directory_upload = directory_upload_profile_picture_linux;}
            if (directory_name.equals("base64")) {directory_upload = directory_upload_base64_linux;}
        } else {
            if (directory_name.equals("file")) {directory_upload = directory_upload_file;}
            if (directory_name.equals("profile_picture")) {directory_upload = directory_upload_profile_picture;}
            if (directory_name.equals("base64")) {directory_upload = directory_upload_base64;}
        }

        HttpHeaders headers = new HttpHeaders();
        String filename = System.getProperty("user.dir") + directory_upload + name;
        try (InputStream inputFile = new FileInputStream(filename)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = Files.readAllBytes(Paths.get(filename));
            int l = inputFile.read(buffer);
            while(l >= 0) {
                outputStream.write(buffer, 0, l);
                l = inputFile.read(buffer);
            }
            String extension = globalFunctionHelper.getExtensionImage(name);
            if (extension.matches("(.*)jpg(.*)") || extension.matches("(.*)jpeg(.*)") || extension.matches("(.*)png(.*)") || extension.matches("(.*)gif(.*)") || extension.matches("(.*)bmp(.*)")) {
                headers.set("Content-Type", "image/" + extension);
                headers.set("Content-Disposition", "inline; filename=\"" + name + "");
            } else {
                headers.set("Content-Type", "application/x-javascript; charset=utf-8");
                headers.set("Content-Disposition", "attachment; filename=\"" + name + "");
            }
            return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
    }
}
