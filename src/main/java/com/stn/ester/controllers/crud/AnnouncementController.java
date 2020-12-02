package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Announcement;
import com.stn.ester.services.crud.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController extends CrudController<AnnouncementService, Announcement> {

    @Autowired
    public AnnouncementController(AnnouncementService service) {
        super(service);
    }
}
