package com.task.third.service.web.service;

import com.task.third.service.web.entity.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    void savePerson(Person person);

    void deletePerson(Person person);

    List<Person> queryByName(String name);

    List<Person> listAll();

    Optional<Person> find(Long id);
}
