package com.stn.ester.services.crud;

import com.stn.ester.entities.Announcement;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.repositories.jpa.AnnouncementRepository;
import com.stn.ester.services.SocketService;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AnnouncementService extends CrudService<Announcement, AnnouncementRepository> {

    private SocketService socketService;

    @Autowired
    public AnnouncementService(AnnouncementRepository repository,
                               SocketService socketService) {
        super(repository);
        this.socketService = socketService;
    }

    @Override
    @Transactional
    public Announcement create(Announcement o) {
        o.setUserId(SessionHelper.getUserID());
        o = super.create(o);
        this.socketService.sendAnnouncement(o);
        return o;
    }
}
