package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdNameList;
import com.stn.ester.rest.domain.Module;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ModuleRepository extends AppRepository<Module,IdNameList> {

    List<Module> findAllByRequestMethodAndName(RequestMethod requestMethod, String name);

    List<Module> findAllByName(String name);

}
