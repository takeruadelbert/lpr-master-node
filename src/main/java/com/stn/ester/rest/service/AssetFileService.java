package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class AssetFileService extends AppService {

    @Autowired
    private AssetFileRepository assetFileRepository;

    @Value("${ester.asset.root}")
    private String assetRootPath;

    @Value("${ester.asset.temp}")
    private String assetTempPath;

    private String parentDirectory = new File(System.getProperty("user.dir")).getParent() != null ? new File(System.getProperty("user.dir")).getParent() : new File(System.getProperty("user.dir")).toString();

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
                    String pathFile = DS + this.assetTempPath + DS + filename + "." + ext;

                    GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + this.assetTempPath);

                    FileOutputStream fileOutputStream = new FileOutputStream(this.parentDirectory + DS + pathFile);
                    fileOutputStream.write(file.getBytes());
                    fileOutputStream.close();

                    AssetFile assetFile = new AssetFile(pathFile, filename, ext);
                    assetFile.setToken(GlobalFunctionHelper.generateToken());
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
                GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + this.assetTempPath);

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
                String path = DS + this.assetTempPath + DS + filename;
                String pathfile = this.parentDirectory + DS + this.assetTempPath + DS + filename;
                FileOutputStream fileOutputStream = new FileOutputStream(pathfile);
                byte[] fileByteArray = Base64.getDecoder().decode(GlobalFunctionHelper.getRawDataFromEncodedBase64(encoded_file));
                fileOutputStream.write(fileByteArray);

                // save decoded file to database
                AssetFile assetFile = new AssetFile(path, name, ext);
                assetFile.setToken(GlobalFunctionHelper.generateToken());
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

    public Object getFile(String path, boolean is_download) {
        path = DS + path;
        Map<String, Object> result = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        String filename = this.parentDirectory + path;

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
            if (is_download) {
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

    public Long moveTempDirToPermanentDir(String token, String assetDir) {
        Long result = null;
        try {
            Optional<AssetFile> file = this.assetFileRepository.findByToken(token);
            if (!file.equals(Optional.empty())) {
                long asset_file_id = file.get().getId();
                String path = this.assetRootPath + DS + assetDir;

                // create if the path doesn't exists
                GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + path);

                String from = this.parentDirectory + file.get().getPath();
                String to = from.replace("temp", this.assetRootPath + DS + assetDir);

                // move file from folder "temp"
                Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Paths.get(from));
//                Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);

                // update path data of asset file
                file.get().setId(asset_file_id);
                file.get().setPath(to.replace(this.parentDirectory, ""));
                this.assetFileRepository.save(file.get());
                return asset_file_id;
            } else {
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
    }
}
