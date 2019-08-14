package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.ModuleLink;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleLinkRepository extends JpaRepository<ModuleLink, Long> {
    List<ModuleLink> findAllByRequestMethodAndName(RequestMethod requestMethod, String name);
}
