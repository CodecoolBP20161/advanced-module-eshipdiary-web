package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.SubType;
import com.codecool.eshipdiary.repository.SubTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class SubTypeRepositoryService {

    @Autowired
    private SubTypeRepository subTypeRepository;

    public Optional<SubType> getSubTypeById(Long id) {
        return subTypeRepository.findOneById(id);
    }

    public Iterable<SubType> getAllSubType() {
        return subTypeRepository.findAll();
    }

    public void save(SubType SubType) { subTypeRepository.save(SubType); }

    public void deleteSubTypeById(Long id) {
        if(subTypeRepository.findOneById(id).isPresent()) subTypeRepository.delete(id);
    }
}
