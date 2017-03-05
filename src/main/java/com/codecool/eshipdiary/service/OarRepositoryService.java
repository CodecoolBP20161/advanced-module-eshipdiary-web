package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.repository.OarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OarRepositoryService {

    @Autowired
    private OarRepository oarRepository;

    public Optional<Oar> getOarById(long id) {
        return Optional.ofNullable(oarRepository.findOne(id));
    }

    public Iterable<Oar> getAllOars() {
        return oarRepository.findAll();
    }

    public void create(Oar oar) { oarRepository.save(oar); }

    public void deleteOarById(Long id) { oarRepository.delete(oarRepository.findOne(id)); }

}