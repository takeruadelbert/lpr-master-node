package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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

    @Value("${ester.asset.default}")
    private String assetDefault;
    @Autowired
    private ResourceLoader resourceLoader;
    private static final String DS = File.separator;
    public static Long defaultProfilePictureID;

    @Autowired
    public AssetFileService(AssetFileRepository assetFileRepository) {
        super(AssetFile.unique_name);
        super.repositories.put(AssetFile.unique_name, assetFileRepository);
    }

    @Transactional
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
                    data.add((AssetFile) super.create(assetFile));
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

    @Transactional
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
                super.create(assetFile);

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

    @Transactional
    public Long moveTempDirToPermanentDir(String token, String assetDir) {
        Long result = null;
        try {
            AssetFile file = this.assetFileRepository.findByToken(token).get();
            if (!file.equals(Optional.empty())) {
                long asset_file_id = file.getId();
                String path = this.assetRootPath + DS + assetDir;

                // create if the path doesn't exists
                GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + path);

                String from = this.parentDirectory + file.getPath();
                String to = from.replace("temp", this.assetRootPath + DS + assetDir);
                // move file from folder "temp"
                Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Paths.get(from));
//                Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);

                // update path data of asset file
                file.setId(asset_file_id);
                file.setPath(to.replace(this.parentDirectory, ""));
                super.update(file.getId(), file);
                return asset_file_id;
            } else {
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
    }

    @Transactional
    public void addDefaultProfilePicture() {
        try {
            Optional<AssetFile> existingDefaultPP = this.assetFileRepository.findByNameAndExtension("default-pp", "png");
            if (existingDefaultPP.equals(Optional.empty())) {
                String defaultProfilePicturePath = this.assetDefault + DS + "profile_picture" + DS + "default-pp.png";
                Resource resource = resourceLoader.getResource("classpath:" + defaultProfilePicturePath);
                String realDefaultPPPath = resource.getFile().getAbsolutePath();
                realDefaultPPPath = realDefaultPPPath.replace(this.parentDirectory, "");

                // add default profile picture to Asset File table
                String filename = resource.getFilename();
                defaultProfilePictureID = this.saveAssetFile(realDefaultPPPath, filename);
            } else {
                defaultProfilePictureID = existingDefaultPP.get().getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void addDefaultSystemProfile() {
        try {
            String address = "Jalan Sawah Kurung No. 4A";
            String email = "suryateknologi@yahoo.co.id";
            String header = "<h1>Test</h1>";
            String name = "Surya Teknologi Nasional";
            String shortname = "STN";
            String telephone = "022 123456789";
            String website = "http://suryateknologi.co.id/";

            // add default logo
            String defaultLogoPath = this.assetDefault + DS + "system_profile" + DS + "stn.png";
            Resource resource = resourceLoader.getResource("classpath:" + defaultLogoPath);
            String realDefaultLogoPath = resource.getFile().getAbsolutePath();
            realDefaultLogoPath = realDefaultLogoPath.replace(this.parentDirectory, "");
            String filename = resource.getFilename();
            Long logo_id = this.saveAssetFile(realDefaultLogoPath, filename);

            // save default data
            SystemProfile systemProfile = new SystemProfile(address, telephone, name, shortname, header, email, website, logo_id);
            super.create(systemProfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public Long saveAssetFile(String filepath, String filename) {
        AssetFile assetFile = new AssetFile(filepath, GlobalFunctionHelper.getNameFile(filename), GlobalFunctionHelper.getExtensionFile(filename));
        super.create(assetFile);
        return assetFile.getId();
    }
}
