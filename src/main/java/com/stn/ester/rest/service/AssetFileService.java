package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AssetFileService extends AppService {

    @Autowired
    private AssetFileRepository assetFileRepository;

    @Autowired
    private Environment environment;

    private final String DS = File.separator;

    @Autowired
    public AssetFileService(AssetFileRepository assetFileRepository) {
        super(AssetFile.unique_name);
        super.repositories.put(AssetFile.unique_name, assetFileRepository);
    }

    public Object uploadFile(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        List<AssetFile> data = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String filename = GlobalFunctionHelper.getNameFile(file.getOriginalFilename());
                String ext = GlobalFunctionHelper.getExtensionFile(file.getOriginalFilename());

                // replace all whitespace characters to none
                filename = filename.replaceAll(" ", "");
                Optional<AssetFile> temp = this.assetFileRepository.findByNameAndExtension(filename, ext);

                /*
                 check if uploaded file(s) already exist in database or with the same name.
                 If so, added suffix timestamp (milliseconds) from uploaded file.
                 */
                if (!temp.equals(Optional.empty())) {
                    filename += DateTimeHelper.getCurrentTimeStamp();
                }

                // store file into asset using FileOutputStream
                try {
                    String pathFile = DS + environment.getProperty("ester.parent-directory") + DS + filename + "." + ext;

                    this.autoCreateAssetDir();

                    FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + DS + pathFile);
                    fileOutputStream.write(file.getBytes());
                    fileOutputStream.close();

                    AssetFile assetFile = new AssetFile(pathFile, filename, ext);
                    data.add(this.assetFileRepository.save(assetFile));
                } catch (Exception ex) {
                    result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.put("message", ex.getMessage());
                    return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            result.put("status", HttpStatus.OK.value());
            result.put("message", "File(s) has been uploaded successfully.");
            result.put("data", data);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public Object uploadEncodedFile(String filename, String encoded_file) {
        Map<String, Object> result = new HashMap<>();

        // decode it first
        if (!encoded_file.isEmpty()) {
            List<MultipartFile> decoded_files = new ArrayList<>();
            try {
                this.autoCreateAssetDir();

                String name = GlobalFunctionHelper.getNameFile(filename);
                String ext = GlobalFunctionHelper.getExtensionFile(filename);

                // check if uploaded file is already exists
                Optional<AssetFile> file = this.assetFileRepository.findByNameAndExtension(name, ext);

                /*
                 check if uploaded file(s) already exist in database or with the same name.
                 If so, added suffix timestamp (milliseconds) from uploaded file.
                 */
                if (!file.equals(Optional.empty())) {
                    name += DateTimeHelper.getCurrentTimeStamp();
                    filename = name + DateTimeHelper.getCurrentTimeStamp() + "." + ext;
                }
                String path = DS + this.environment.getProperty("ester.parent-directory") + DS + filename;
                String pathfile = System.getProperty("user.dir") + DS + this.environment.getProperty("ester.parent-directory") + DS + filename;
                FileOutputStream fileOutputStream = new FileOutputStream(pathfile);
                byte[] fileByteArray = Base64.getDecoder().decode(GlobalFunctionHelper.getRawDataFromEncodedBase64(encoded_file));
                fileOutputStream.write(fileByteArray);

                // save decoded file to database
                AssetFile assetFile = new AssetFile(path, name, ext);
                this.assetFileRepository.save(assetFile);

                result.put("data", assetFile);
                result.put("status", HttpStatus.OK.value());
                result.put("message", "Encoded file has successfully been uploaded.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } catch (Exception ex) {
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
                result.put("message", ex.getMessage());
                return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        result.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
        result.put("message", "Invalid File.");
        return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
    }

    /*
      check if required directory is exist.
      If it doesn't exists, then program creates the directory automatically
    */
    private void autoCreateAssetDir() {
        File assetDir = new File(System.getProperty("user.dir") + DS + this.environment.getProperty("ester.parent-directory"));
        if (!assetDir.exists()) {
            assetDir.mkdir();
        }
    }

    public Object getFile(String path, boolean is_download) {
        path = DS + path;
        Map<String,Object> result = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        String filename = System.getProperty("user.dir") + path;
        try (InputStream inputFile = new FileInputStream(filename)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = Files.readAllBytes(Paths.get(filename));
            int l = inputFile.read(buffer);
            while (l >= 0) {
                outputStream.write(buffer, 0, l);
                l = inputFile.read(buffer);
            }
            String[] temp = path.split("/");
            String name = temp[temp.length - 1];
            String extension = GlobalFunctionHelper.getExtensionFile(name);
            if(is_download) {
                headers.set("Content-Type", "application/x-javascript; charset=utf-8");
                headers.set("Content-Disposition", "attachment; filename=\"" + name + "");
            } else {
                if (extension.matches("(.*)jpg(.*)") || extension.matches("(.*)jpeg(.*)") || extension.matches("(.*)png(.*)") || extension.matches("(.*)gif(.*)") || extension.matches("(.*)bmp(.*)")) {
                    headers.set("Content-Type", "image/" + extension);
                    headers.set("Content-Disposition", "inline; filename=\"" + name + "");
                } else {
                    headers.set("Content-Type", "application/x-javascript; charset=utf-8");
                    headers.set("Content-Disposition", "attachment; filename=\"" + name + "");
                }
            }
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            result.put("status", HttpStatus.NOT_FOUND.value());
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
