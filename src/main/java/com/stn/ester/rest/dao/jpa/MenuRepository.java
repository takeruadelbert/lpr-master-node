package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.Menu;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuRepository extends AppRepository<Menu,Long, IdLabelList> {

    List<Menu> findAllByParentMenuId(Long parentMenuId);

    List<Menu> findAllByIdInAndParentMenuIdOrderByOrderingNumber(Set<Long> id,Long parentId);

    List<Menu> findAllByParentMenuIdIsNull();
}
