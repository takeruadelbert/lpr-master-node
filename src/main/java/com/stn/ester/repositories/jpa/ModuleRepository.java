package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameOption;
import com.stn.ester.entities.Module;
import com.stn.ester.entities.enumerate.RequestMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends BaseRepository<Module>, RepositoryListTrait<IdNameOption> {

    List<Module> findAllByRequestMethodAndName(RequestMethod requestMethod, String name);

    List<Module> findAllByName(String name);

}
