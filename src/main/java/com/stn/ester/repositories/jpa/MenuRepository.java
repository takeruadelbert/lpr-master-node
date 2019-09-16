package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelList;
import com.stn.ester.entities.Menu;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface MenuRepository extends AppRepository<Menu>, RepositoryListTrait<IdLabelList> {

    List<Menu> findAllByParentMenuId(Long parentMenuId);

    List<Menu> findAllByIdInAndParentMenuIdOrderByOrderingNumber(Set<Long> id,Long parentId);

    List<Menu> findAllByParentMenuIdIsNull();

    List<Menu> findAllByModuleId(Long moduleId);

    List<Menu> findAllByModuleIdIn(Collection<Long> moduleIds);

    Menu findById(long id);
}
