package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.SocialMedia;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaRepository extends AppRepository<SocialMedia, IdList> {

}