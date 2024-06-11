package com.task.third.service.web.service;

import com.task.third.service.web.entity.Person;
import com.task.third.service.web.repository.PeopleRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// currently just a wrapper around the repository
@AllArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    private PeopleRepository peopleRepository;

    @Override
    public void savePerson(Person person) {
        LOGGER.debug("savePerson called");
        peopleRepository.save(person);
    }

    @Override
    public void deletePerson(Person person) {
        LOGGER.debug("deletePerson called");
        peopleRepository.delete(person);
    }

    @Override
    public List<Person> queryByName(String name) {
        LOGGER.debug("queryByName called");
        return peopleRepository.findByFullNameStartsWithIgnoreCase(name);
    }

    @Override
    public List<Person> listAll() {
        LOGGER.debug("listAll called");
        return peopleRepository.findAll();
    }

    @Override
    public Optional<Person> find(Long id) {
        LOGGER.debug("find called");
        return peopleRepository.findById(id);
    }
}
