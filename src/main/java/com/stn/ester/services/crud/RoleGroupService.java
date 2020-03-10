package com.stn.ester.services.crud;

import com.stn.ester.entities.RoleGroup;
import com.stn.ester.repositories.jpa.RoleGroupRepository;
import com.stn.ester.services.base.CrudService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Log4j2
public class RoleGroupService extends CrudService<RoleGroup, RoleGroupRepository> {

    @Autowired
    public RoleGroupService(RoleGroupRepository roleGroupRepository) {
        super(roleGroupRepository);
    }

    @Scheduled(fixedDelay = 43_000_000, initialDelay = 1_000)
    protected void deleteBroken() {
        Collection<RoleGroup> roleGroups = currentEntityRepository.findAllByUserIdIsNullAndRoleIdIsNull();
        currentEntityRepository.deleteAll(roleGroups);
        log.info(String.format("Cleaned %d unused rolegroup", roleGroups.size()));
    }
}
