package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
