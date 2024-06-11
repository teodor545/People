package com.task.third.service.web.repository;

import com.task.third.service.web.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Mail, Long> {
}
