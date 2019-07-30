package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuRepository extends AppRepository<Menu,IdLabelList> {

    List<Menu> findAllByParentMenuId(Long parentMenuId);

    List<Menu> findAllByIdInAndParentMenuIdOrderByOrderingNumber(Set<Long> id,Long parentId);

    List<Menu> findAllByParentMenuIdIsNull();

    Menu findById(long id);
}
