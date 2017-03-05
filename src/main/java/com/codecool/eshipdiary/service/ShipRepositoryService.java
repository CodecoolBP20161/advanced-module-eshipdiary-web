package com.codecool.eshipdiary.service;


import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShipRepositoryService {

    @Autowired
    ShipRepository shipRepository;

    public Optional<Ship> getShipById(long id) {
        return Optional.ofNullable(shipRepository.findOne(id));
    }

    public Optional<Ship> getShipByName(String name) {
        return shipRepository.findOneByName(name);
    }

    public Optional<Ship> getShipByType(String type) {
        return shipRepository.findOneByShipType(type);
    }

    public Optional<Ship> getShipByCategory(String category) {
        return shipRepository.findOneByCategory(category);
    }

    public Iterable<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    public void create(Ship ship) { shipRepository.save(ship); }

    public void deleteShipById(Long id) {
        shipRepository.delete(shipRepository.findOne(id));
    }

}
























