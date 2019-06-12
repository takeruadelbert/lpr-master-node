package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.File;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, Long> {
    Optional<File> findByFile(String file);
}