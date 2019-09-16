package com.stn.ester.services.crud;

import com.stn.ester.entities.AccessLog;
import com.stn.ester.entities.AssetFile;
import com.stn.ester.entities.SystemProfile;
import com.stn.ester.core.configurations.InterceptorConfig;
import com.stn.ester.core.interceptors.AccessLogInterceptor;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.helpers.GlobalFunctionHelper;
import com.stn.ester.repositories.jpa.AccessLogRepository;
import com.stn.ester.repositories.jpa.AssetFileRepository;
import com.stn.ester.repositories.jpa.SystemProfileRepository;
import com.stn.ester.services.base.CrudService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class AssetFileService extends CrudService {

    @Autowired
    private AssetFileRepository assetFileRepository;
    @Autowired
    private SystemProfileRepository systemProfileRepository;
    @Value("${ester.asset.root}")
    private String assetRootPath;

    @Value("${ester.asset.temp}")
    private String assetTempPath;

    private String parentDirectory = new File(System.getProperty("user.dir")).getParent() != null ? new File(System.getProperty("user.dir")).getParent() : new File(System.getProperty("user.dir")).toString();

    @Value("${ester.asset.default}")
    private String assetDefault;
    @Autowired
    private ResourceLoader resourceLoader;
    private static final String DS = "/";
    public static Long defaultProfilePictureID;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    public AssetFileService(AssetFileRepository assetFileRepository) {
        super(assetFileRepository);
    }

    @Transactional
    public Object uploadFile(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        List<AssetFile> data = new ArrayList<>();
        try {
            if (files.length != 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
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
                        String pathFile = DS + this.assetTempPath + DS + filename + "." + ext;

                        GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + this.assetTempPath);

                        FileOutputStream fileOutputStream = new FileOutputStream(this.parentDirectory + DS + pathFile);
                        fileOutputStream.write(file.getBytes());
                        fileOutputStream.close();

                        AssetFile assetFile = new AssetFile(pathFile, filename, ext);
                        data.add((AssetFile) super.create(assetFile));

                        Long accessLogId = AccessLogInterceptor.accessLogId.get();
                        Long assetFileId = assetFile.getId();
                        updateAccessLog(accessLogId, assetFileId);
                    } else {
                        System.out.println("NULL");
                    }
                }
                result.put("status", HttpStatus.OK.value());
                result.put("message", "File(s) has been uploaded successfully.");
                result.put("data", data);
            } else {
                String message = "No File Uploaded.";
                result.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
                result.put("message", message);
                return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
            result.put("message", ex.getMessage());
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        } finally {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
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
                super.create(assetFile);

                Long accessLogId = AccessLogInterceptor.accessLogId.get();
                Long assetFileId = assetFile.getId();
                updateAccessLog(accessLogId, assetFileId);

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

    public Object getFile(String token, boolean is_download) {
        Map<String, Object> result = new HashMap<>();
        Optional<AssetFile> assetFile = this.assetFileRepository.findByToken(token);
        if (!assetFile.equals(Optional.empty())) {
            int is_default = assetFile.get().getIsDefault();
            String path = assetFile.get().getPath();
            path = DS + path;
            HttpHeaders headers = new HttpHeaders();
            String filename = is_default == 1 ? path : this.parentDirectory + path;
            try {
                InputStream inputFile = is_default == 1 ? getClass().getResourceAsStream(filename) : new FileInputStream(filename);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = IOUtils.toByteArray(inputFile);
                outputStream.write(buffer, 0, buffer.length);
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
                e.printStackTrace();
                result.put("status", HttpStatus.NOT_FOUND.value());
                result.put("message", e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } else {
            result.put("status", HttpStatus.NOT_FOUND.value());
            result.put("message", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public AssetFile moveTempDirToPermanentDir(String token, String assetDir) {
        try {
            AssetFile file = this.assetFileRepository.findByToken(token).get();
            if (!file.equals(Optional.empty())) {
                long asset_file_id = file.getId();
                String path = this.assetRootPath + DS + assetDir;

                // create if the path doesn't exists
                GlobalFunctionHelper.autoCreateDir(this.parentDirectory + DS + path);

                String from = this.parentDirectory + file.getPath();
                String targetFileName = this.assetRootPath + DS + assetDir + DS + file.getName() + '.' + file.getExtension();
                String to = this.parentDirectory + DS + targetFileName;
                // move file from folder "temp"
                Files.copy(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Paths.get(from));
//                Files.move(Paths.get(from), Paths.get(to), StandardCopyOption.REPLACE_EXISTING);

                // update path data of asset file
                file.setId(asset_file_id);
                file.setPath(targetFileName);
                return (AssetFile) super.update(file.getId(), file);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void addDefaultProfilePicture() {
        try {
            Optional<AssetFile> existingDefaultPP = this.assetFileRepository.findByNameAndExtension("default-pp", "png");
            if (existingDefaultPP.equals(Optional.empty())) {
                String defaultProfilePicturePath = this.assetDefault + DS + "profile_picture" + DS + "default-pp.png";
                URL url = getClass().getClassLoader().getResource(defaultProfilePicturePath);

                // add default profile picture to Asset File table
                String filename = FilenameUtils.getName(url.getPath());
                defaultProfilePictureID = this.saveAssetFile(defaultProfilePicturePath, filename);
            } else {
                defaultProfilePictureID = existingDefaultPP.get().getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void addDefaultSystemProfileIfDoesntExist() {
        SystemProfile existingSystemProfile = this.systemProfileRepository.findFirstByIdIsNotNull();
        if (existingSystemProfile == null) {
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
                URL url = getClass().getClassLoader().getResource(defaultLogoPath);
                String filename = FilenameUtils.getName(url.getPath());
                Long logo_id = this.saveAssetFile(defaultLogoPath, filename);

                // save default data
                SystemProfile systemProfile = new SystemProfile(address, telephone, name, shortname, header, email, website, logo_id);
                super.create(systemProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Transactional
    public Long saveAssetFile(String filepath, String filename) {
        AssetFile assetFile = new AssetFile(filepath, GlobalFunctionHelper.getNameFile(filename), GlobalFunctionHelper.getExtensionFile(filename));
        assetFile.setAssetFileToDefault();
        super.create(assetFile);
        return assetFile.getId();
    }

    private void updateAccessLog(Long accessLogId, Long assetFileId) {
        if ((accessLogId != null && assetFileId != null) && InterceptorConfig.accessLogEnabled) {
            // fetch data thread of Access Log ID and insert asset file ID according its access log ID
            AccessLog accessLog = this.accessLogRepository.findById(accessLogId).get();
            if (accessLog != null) {
                accessLog.setUploadFileId(assetFileId);
                this.accessLogService.update(accessLogId, accessLog);
            }

            // remove thread if it's done
            AccessLogInterceptor.accessLogId.remove();
        }
    }

    public byte[] getFile(String token) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        AssetFile assetFile = this.assetFileRepository.findByToken(token).get();
        if (assetFile != null) {
            int isDefault = assetFile.getIsDefault();
            String path = DS + assetFile.getPath();
            String filename = isDefault == 1 ? path : this.parentDirectory + path;
            try {
                InputStream inputStream = isDefault == 1 ? getClass().getResourceAsStream(filename) : new FileInputStream(filename);
                byte[] buffer = IOUtils.toByteArray(inputStream);
                byteArrayOutputStream.write(buffer, 0, buffer.length);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
