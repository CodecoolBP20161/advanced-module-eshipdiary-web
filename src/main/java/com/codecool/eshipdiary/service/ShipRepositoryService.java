package com.codecool.eshipdiary.service;


import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.SubType;
import com.codecool.eshipdiary.model.TenantAwarePrincipal;
import com.codecool.eshipdiary.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ShipRepositoryService {

    @Autowired
    ShipRepository shipRepository;

    public Optional<Ship> getShipById(Long id) {
        return Optional.ofNullable(shipRepository.findOne(id));
    }

    public Iterable<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    public Iterable<Ship> getAvailableShips() {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return shipRepository.findByActiveTrueAndOnWaterFalseAndClub(tenantAwarePrincipal.getClub());
    }

    public Iterable<Ship> getAvailableShipsBySubType(SubType type) {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return shipRepository.findByActiveTrueAndOnWaterFalseAndSubTypeAndClub(type, tenantAwarePrincipal.getClub());
    }

    public void save(Ship ship) { shipRepository.save(ship); }

    public void deleteShipById(Long id) {
        if(shipRepository.findOneById(id).isPresent()) shipRepository.delete(id);
    }

}
























