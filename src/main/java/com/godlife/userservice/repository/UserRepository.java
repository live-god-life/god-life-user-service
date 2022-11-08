package com.godlife.userservice.repository;


import com.godlife.userservice.domain.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    /** Type과 식별 값으로 회원 조회 */
    Users findByTypeAndIdentifier(String type, String identifier);

    /** 닉네임으로 회원 조회 */
    Users findByNickname(String nickname);

    /** 회원 아이디로 회원 조회 */
    Users findByUserId(Long userId);
}
