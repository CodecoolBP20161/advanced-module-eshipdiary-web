package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.repository.ShipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShipTypeRepositoryService {

    @Autowired
    private ShipTypeRepository shipTypeRepository;

    public Optional<ShipType> getShipTypeById(long id) {
        return shipTypeRepository.findOneById(id);
    }

    public Iterable<ShipType> getAllShipType() {
        return shipTypeRepository.findAll();
    }

    public void save(ShipType shipType) { shipTypeRepository.save(shipType); }

    public void deleteShipTypeById(Long id) {
        if(shipTypeRepository.findOneById(id).isPresent()) shipTypeRepository.delete(id);
    }

}