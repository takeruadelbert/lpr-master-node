package com.stn.ester.rest.dao.jpa;

import java.util.Optional;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.dao.jpa.projections.NameLabelList;
import com.stn.ester.rest.domain.Gender;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends AppRepository<Gender, NameLabelList> {
    Optional<Gender> findByLabel(String label);
}