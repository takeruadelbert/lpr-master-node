package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.ProfilePicture;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePictureRespository extends PagingAndSortingRepository<ProfilePicture, Long> {
    Optional<ProfilePicture> findByAvatar(String avatar);
}
