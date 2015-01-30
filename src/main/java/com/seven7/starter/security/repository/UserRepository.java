package com.seven7.starter.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seven7.starter.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
