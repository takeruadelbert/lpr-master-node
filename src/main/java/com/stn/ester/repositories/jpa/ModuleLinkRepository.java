package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.ModuleLink;
import com.stn.ester.entities.enumerate.RequestMethod;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleLinkRepository extends BaseRepository<ModuleLink> {
    List<ModuleLink> findAllByRequestMethodAndName(RequestMethod requestMethod, String name);
}
