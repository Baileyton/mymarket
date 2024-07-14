package com.mymarket.repository;

import com.mymarket.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<Member, Long> {

}