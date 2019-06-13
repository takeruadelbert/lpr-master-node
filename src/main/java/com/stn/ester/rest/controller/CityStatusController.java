package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.CityStatus;
import com.stn.ester.rest.service.CityStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city_statuses")
public class CityStatusController extends AppController<CityStatusService, CityStatus> {
    @Autowired
    public CityStatusController(CityStatusService cityStatusService) {
        super(cityStatusService);
    }
}
