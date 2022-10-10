package com.godlife.userservice.repository;


import com.godlife.userservice.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /** Type과 식별 값으로 회원 조회 */
    UserEntity findByTypeAndIdentifier(String type, String identifier);

    /** 닉네임으로 회원 조회 */
    UserEntity findByNickname(String nickname);
}
