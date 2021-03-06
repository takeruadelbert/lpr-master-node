package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.LoginSession;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginSessionRepository extends BaseRepository<LoginSession> {

    LoginSession findByToken(String token);

    @Query(value = "select * from login_session LoginSession  left join user User on LoginSession.user_id=User.id where token=? and expire >= now() limit 1", nativeQuery = true)
    LoginSession isTokenExist(String token);
}
