package com.stn.ester.controllers.function;

import com.stn.ester.controllers.base.SecuredController;
import com.stn.ester.dto.entity.NotificationDTO;
import com.stn.ester.entities.Notification;
import com.stn.ester.helpers.SearchAndFilterHelper;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class NotificationController extends SecuredController {

    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/notifications/feed/me", method = RequestMethod.GET)
    public Map<String, Object> getNotification() {
        Map<String, Object> result = new HashMap<>();
        Collection<Notification> notifications = notificationService.getNotificationFeedByUserId(SessionHelper.getUserID());
        List<NotificationDTO> notificationDTOS = notifications.stream().map(
                n -> {
                    NotificationDTO notificationDTO = new NotificationDTO(n);
                    return notificationDTO;
                }).collect(Collectors.toList());
        Collections.sort(notificationDTOS);
        result.put("notifications", notificationDTOS);
        result.put("unseen", notificationService.countUnseenNotificationByUserId(SessionHelper.getUserID()));
        return result;
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/notifications/me", method = RequestMethod.GET)
    public Page<NotificationDTO> indexByCurrentLoginUser(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size, @RequestParam(value = "search", required = false) String search) throws UnsupportedEncodingException {
        if (search != null) {
            search = URLDecoder.decode(search, StandardCharsets.UTF_8.toString());
        }
        Specification<Notification> spec = SearchAndFilterHelper.resolveSpecification(search);
        Page<Notification> result = notificationService.indexByUserId(SessionHelper.getUserID(), page, size, spec);
        return result.map(this::convertToNotificationDTO);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/notifications/seen/{id}", method = RequestMethod.POST)
    public void setHasSeen(@PathVariable Long id) {
        notificationService.setToHasSeen(id);
    }

    private NotificationDTO convertToNotificationDTO(Notification notification) {
        return new NotificationDTO(notification);
    }
}
