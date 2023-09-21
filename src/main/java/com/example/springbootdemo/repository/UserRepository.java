package com.example.springbootdemo.repository;

import com.example.springbootdemo.dto.ReqParam;
import com.example.springbootdemo.dto.UserDtoResponse;
import com.example.springbootdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, BaseRepository {

    @Query(" SELECT new com.example.springbootdemo.dto.UserDtoResponse" +
            " (u.id, u.email, (SELECT MAX(u2.age) FROM User u2 WHERE u2.id = u.id)) " +
            " FROM User u ")
    List<UserDtoResponse> findUserDtoSubQueryInSelect();

//    @Query(" SELECT new com.example.springbootdemo.dto.UserDtoResponse" +
//            " (u.id, u.email, (SELECT MAX(u2.age) FROM User u2 WHERE u2.id = u.id)) " +
//            " FROM ( SELECT u3 " +
//            "        FROM User u3 ) AS u ")
//    List<UserDtoResponse> findUserDtoSubQueryInFrom();

    @Query(value = " SELECT u FROM User u WHERE u.address LIKE " +
            " %?#{[0].concat([1]).trim.substring(0, [0].length +[1].length -1)}% ")
    List<User> findByAddressWithoutParamName(String address,
                                             String add);

    @Query(value = " SELECT u FROM User u WHERE u.address LIKE " +
            " %:#{#address.concat(#add).trim.substring(0, #address.length + #add.length -1)}% ")
    List<User> findByAddressWithParamName(String address,
                                          String add);


    // method dưới không hoạt động, [0] ko hoạt động với object
    @Query(value = " SELECT u FROM User u WHERE u.address LIKE " +
            " %?#{[0].address.concat([0].add).trim.substring(0, #[0].address.length + #[0].add.length -1)}% ")
    List<User> findByAddressWithReqBodyNoName(ReqParam req);

    @Query(value = " SELECT u FROM User u " +
            " WHERE u.address LIKE " +
            " %:#{#req.address.concat(#req.add).trim.substring(0, #req.address.length + #req.add.length + -1)}% ")
    List<User> findByAddressWithReqBody(ReqParam req);

    @Query(value = " SELECT u.id, u.address, u.created_at FROM users u WHERE u.address LIKE " +
            " %:#{#req.address.concat(#req.add).trim.substring(0, #req.address.length + #req.add.length -1)}% ",
            nativeQuery = true)
    List<Map<String, Object>> findByAddressWithReqBodyNative(ReqParam req);

    @Query(value = " SELECT new com.example.springbootdemo.dto.UserDtoResponse(u.age, u.email, 555)FROM User u " +
            " UNION " +
            " SELECT new com.example.springbootdemo.dto.UserDtoResponse(u.id, u.address, u.age) FROM User u ")
    List<UserDtoResponse> findStringWithUnionJpa();

}
