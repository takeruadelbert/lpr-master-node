package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.exception.UnauthorizedException;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AppController<UserService, User> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService=userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map login(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> loginToken = service.login(payload.get("username"), payload.get("password"), session);
        return loginToken;
    }

    @RequestMapping(value = "/heartbeat")
    public Object isValid(@RequestHeader("access-token") String accessToken) {
        LoginSession loginSession=this.userService.tokenHeartbeat(accessToken);
        if (loginSession==null){
            throw new UnauthorizedException();
        }else{
            return loginSession;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/gif")) {
            File convertFile = new File(System.getProperty("user.dir") + "\\assets\\image\\" + file.getOriginalFilename()); //set path directory
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(file.getBytes());
            fout.close();
            return "Image is successfully uploaded";
        } else {
            return "Image not uploaded";
        }
    }

}