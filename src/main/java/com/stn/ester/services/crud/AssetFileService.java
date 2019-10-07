package com.stn.ester.services.crud;

import com.stn.ester.core.search.AppSpecification;
import com.stn.ester.core.search.util.SearchOperation;
import com.stn.ester.core.search.util.SpecSearchCriteria;
import com.stn.ester.entities.AssetFile;
import com.stn.ester.entities.SystemProfile;
import com.stn.ester.helpers.DateTimeHelper;
import com.stn.ester.helpers.FileHelper;
import com.stn.ester.helpers.GlobalFunctionHelper;
import com.stn.ester.repositories.jpa.AssetFileRepository;
import com.stn.ester.repositories.jpa.SystemProfileRepository;
import com.stn.ester.services.base.CrudService;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssetFileService extends CrudService<AssetFile, AssetFileRepository> {

    @Autowired
    private AssetFileRepository assetFileRepository;
    @Autowired
    private SystemProfileRepository systemProfileRepository;
    @Value("${ester.asset.root}")
    private String assetRootPath;

    @Value("${ester.asset.temp}")
    private String assetTempPath;

    @Value("${ester.file.temp-file-max-age}")
    private Integer TEMP_FILE_MAX_AGE;

    private String parentDirectory = new File(System.getProperty("user.dir")).getParent() != null ? new File(System.getProperty("user.dir")).getParent() : new File(System.getProperty("user.dir")).toString();

    @Value("${ester.asset.default}")
    private String assetDefault;
    @Autowired
    private ResourceLoader resourceLoader;
    private static final String DS = "/";
    public static Long defaultProfilePictureID;

    @Autowired
    public AssetFileService(AssetFileRepository assetFileRepository) {
        super(assetFileRepository);
    }

    @PostConstruct
    public void createDefaultDir() {
        FileHelper.autoCreateDir(this.parentDirectory + DS + this.assetTempPath);
    }

    @Override
    public AssetFile create(AssetFile o) {
        String token;
        do {
            token = GlobalFunctionHelper.generateToken();
        } while (currentEntityRepository.findByToken(token).isPresent());
        o.setToken(token);
        return super.create(o);
    }

    @Transactional
    public Object uploadFile(MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        List<AssetFile> data = new ArrayList<>();
        try {
            if (files.length != 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        FileAttribute fileAttribute = new FileAttribute(file.getOriginalFilename());

                        FileOutputStream fileOutputStream = new FileOutputStream(fileAttribute.getFileAbsolutePath());
                        fileOutputStream.write(file.getBytes());
                        fileOutputStream.close();

                        data.add((AssetFile) this.create(fileAttribute.asAssetFile()));
                    } else {
                        System.out.println("NULL");
                    }
                }
                result.put("status", HttpStatus.OK.value());
                result.put("message", "File(s) has been uploaded successfully.");
                result.put("data", data);
                return new ResponseEntity<>(result, HttpStatus.OK);
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
        }
    }

    @Transactional
    public Object uploadEncodedFile(String filename, String encoded_file) {
        Map<String, Object> result = new HashMap<>();

        // decode it first
        if (!encoded_file.isEmpty()) {
            try {
                FileAttribute fileAttribute = new FileAttribute(filename);

                FileOutputStream fileOutputStream = new FileOutputStream(fileAttribute.getFileAbsolutePath());
                byte[] fileByteArray = Base64.getDecoder().decode(FileHelper.getRawDataFromEncodedBase64(encoded_file));
                fileOutputStream.write(fileByteArray);
                fileOutputStream.close();

                result.put("data", this.create(fileAttribute.asAssetFile()));
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

    @Transactional
    public Object uploadViaUrl(URL url) {
        Map<String, Object> result = new HashMap<>();
        try {
            FileAttribute fileAttribute = new FileAttribute(url);

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(fileAttribute.getFileAbsolutePath());
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();

            result.put("data", this.create(fileAttribute.asAssetFile()));
            result.put("status", HttpStatus.OK.value());
            result.put("message", "Encoded file has successfully been uploaded.");
        } catch (IOException ex) {
            result.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
            result.put("message", ex.getMessage());
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        } finally {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
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
                String extension = FileHelper.getExtensionFile(name);
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
                FileHelper.autoCreateDir(this.parentDirectory + DS + path);

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
                systemProfileRepository.save(systemProfile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Transactional
    public Long saveAssetFile(String filepath, String filename) {
        AssetFile assetFile = new AssetFile(filepath, FileHelper.getNameFile(filename), FileHelper.getExtensionFile(filename));
        assetFile.setAssetFileToDefault();
        this.create(assetFile);
        return assetFile.getId();
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

    @Scheduled(fixedDelay = 43_200_000, initialDelay = 1_000_000)
    public void scheduledCleanTempFile() {
        String dayAgo = DateTimeHelper.convertToDateTimeString(LocalDateTime.now().minusDays(TEMP_FILE_MAX_AGE));
        AppSpecification<AssetFile> assetFileAppSpecification = new AppSpecification<>(new SpecSearchCriteria(null, "path", SearchOperation.STARTS_WITH, DS + assetTempPath));
        AppSpecification<AssetFile> assetFileAppSpecificationAge = new AppSpecification<>(new SpecSearchCriteria(null, "createdDate", SearchOperation.LESS_THAN_OR_EQUAL, dayAgo));
        Specification<AssetFile> assetFileAppSpecificationNullToken = (Specification<AssetFile>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("token"));
        Collection<AssetFile> assetFiles = currentEntityRepository.findAll(Specification.where(assetFileAppSpecification).and(assetFileAppSpecificationAge).and(assetFileAppSpecificationNullToken));
        int cleanCount = 0;
        for (AssetFile assetFile : assetFiles) {
            try {
                if (Files.deleteIfExists(Paths.get(this.parentDirectory + assetFile.getPath()))) {
                    cleanCount++;
                    assetFile.setToken(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Temp File Cleaner : %d file(s) have been clean up.", cleanCount);
        currentEntityRepository.saveAll(assetFiles);
    }

    @Data
    private class FileAttribute {
        String name;
        String ext;

        public FileAttribute(String filename) {
            name = com.google.common.io.Files.getNameWithoutExtension(filename).trim();
            ext = com.google.common.io.Files.getFileExtension(filename);
            appendFilenameIfExist();
        }

        public FileAttribute(URL url) {
            name = com.google.common.io.Files.getNameWithoutExtension(url.getFile());
            ext = FileHelper.removeRequestParamFromExtension(com.google.common.io.Files.getFileExtension(url.getFile()));
            appendFilenameIfExist();
        }

        public String getFullname() {
            return name + "." + ext;
        }

        public String getFileTargetPath() {
            return DS + assetTempPath + DS + name + "." + ext;
        }

        public String getFileAbsolutePath() {
            return parentDirectory + DS + this.getFileTargetPath();
        }

        private void appendFilenameIfExist() {
            Optional<AssetFile> temp = assetFileRepository.findByNameAndExtension(name, ext);

            if (!temp.equals(Optional.empty())) {
                name += DateTimeHelper.getCurrentTimeStamp();
            }
        }

        public AssetFile asAssetFile() {
            return new AssetFile(getFileTargetPath(), getName(), getExt());
        }
    }
}
