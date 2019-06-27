package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.helper.DateTimeHelper;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
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

                // replace all whitespace characters to none
                filename = filename.replaceAll(" ", "");
                Optional<AssetFile> temp = this.assetFileRepository.findByName(filename);

                /*
                 check if uploaded file(s) already exist in database or with the same name.
                 If so, added suffix timestamp (milliseconds) from uploaded file.
                 */
                if (!temp.equals(Optional.empty())) {
                    filename += DateTimeHelper.getCurrentTimeStamp();
                }

                // store file into asset using FileOutputStream
                try {
                    String pathFile = environment.getProperty("ester.parent-directory");

                    /*
                     check if required directory is exist.
                     If it doesn't exists, then program creates the directory automatically
                     */
                    File checkFile = new File(System.getProperty("user.dir") + DS + pathFile);
                    if (!checkFile.exists()) {
                        checkFile.mkdir();
                    }

                    String ext = GlobalFunctionHelper.getExtensionFile(file.getOriginalFilename());
                    FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + DS + pathFile + DS + filename + ext);
                    fileOutputStream.write(file.getBytes());
                    fileOutputStream.close();

                    AssetFile assetFile = new AssetFile(pathFile, filename, ext);
                    data.add(this.assetFileRepository.save(assetFile));
                } catch (Exception ex) {
                    result.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                    result.put("message", ex.getMessage());
                    return result;
                }
            }
            result.put("status", HttpStatus.OK.value());
            result.put("message", "File(s) has been uploaded successfully.");
            result.put("data", data);
        }
        return result;
    }
}
