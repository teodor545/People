package com.task.third.service.web.repository;

import com.task.third.service.web.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {

    List<Person> findByFullNameStartsWithIgnoreCase(String name);
}
