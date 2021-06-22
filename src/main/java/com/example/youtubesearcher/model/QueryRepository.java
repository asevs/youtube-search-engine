package com.example.youtubesearcher.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface QueryRepository extends JpaRepository<Query, String> {
    @Modifying
    @Transactional
    void deleteByCreatedAtBefore(Date expiryDate);
}
