package com.example.springbootdemo;

import com.example.springbootdemo.dto.UserDtoResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final EntityManager entityManager;

    public List<UserDtoResponse> findUserDtoSubQueryInSelectDao() {
        String sql = " SELECT new com.example.springbootdemo.dto.UserDtoResponse" +
                " (u.id, u.email, (SELECT MAX(u2.age) FROM User u2 WHERE u2.id = u.id)) " +
                " FROM User u ";
        return entityManager.createQuery(sql, UserDtoResponse.class).getResultList();
    }

    public List<UserDtoResponse> findUserDtoSubQueryInFromDao() {
        // ERROR, not allow to use subQuery in FROM CLAUSE, but in spring boot >= 3.0 we can do
        String sql = " SELECT new com.example.springbootdemo.dto.UserDtoResponse " +
                " (subq.id, subq.email, (SELECT MAX(u2.age) FROM User u2 WHERE u2.id = subq.id)) " +
                " FROM User user, (SELECT u3.id AS id, u3.email AS email FROM User u3)  subq " +
                " WHERE user.id = subq.id ";
        return entityManager.createQuery(sql, UserDtoResponse.class).getResultList();
    }


}
