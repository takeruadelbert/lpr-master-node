package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.Menu;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MenuRepository extends PagingAndSortingRepository<Menu,Long> {

    Set<Menu> findByParentMenuId(Long id);
}
