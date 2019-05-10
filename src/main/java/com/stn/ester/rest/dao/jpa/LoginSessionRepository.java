package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.LoginSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoginSessionRepository extends PagingAndSortingRepository<LoginSession,Long> {

    LoginSession findByToken(String token);

    @Query(value="select * from login_session LoginSession  left join user User on LoginSession.user_id=User.id where token=? and expire >= now() limit 1",nativeQuery = true)
    LoginSession isTokenExist(String token);
}
