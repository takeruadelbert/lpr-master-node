package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameList;
import com.stn.ester.entities.Module;
import com.stn.ester.entities.enumerate.RequestMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends AppRepository<Module>, RepositoryListTrait<IdNameList> {

    List<Module> findAllByRequestMethodAndName(RequestMethod requestMethod, String name);

    List<Module> findAllByName(String name);

}
