package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.ShipSize;
import com.codecool.eshipdiary.repository.ShipSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShipSizeRepositoryService {

    @Autowired
    private ShipSizeRepository shipSizeRepository;

    public Optional<ShipSize> getShipSizeById(long id) {
        return shipSizeRepository.findOneById(id);
    }

    public Iterable<ShipSize> getAllShipSize() {
        return shipSizeRepository.findAll();
    }

    public void save(ShipSize shipSize) { shipSizeRepository.save(shipSize); }

    public void deleteShipSizeById(Long id) {
        if(shipSizeRepository.findOneById(id).isPresent()) shipSizeRepository.delete(id);
    }

}