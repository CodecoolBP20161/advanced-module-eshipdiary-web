package com.codecool.eshipdiary.service;


import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.SubType;
import com.codecool.eshipdiary.security.TenantAwarePrincipal;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.ShipRepository;
import com.codecool.eshipdiary.repository.SubTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShipRepositoryService {

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    SubTypeRepository subTypeRepository;

    @Autowired
    UserRepositoryService userRepositoryService;

    public Optional<Ship> getShipById(Long id) {
        return Optional.ofNullable(shipRepository.findOne(id));
    }

    public Iterable<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    public Iterable<Ship> getAvailableShips() {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepositoryService.getUserByUserName(tenantAwarePrincipal.getUsername()).get();
        return shipRepository.findByActiveTrueAndOnWaterFalseAndClubAndEnabledUsers(tenantAwarePrincipal.getClub(), user);
    }

    public Iterable<Ship> getAvailableShipsBySubType(SubType type) {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepositoryService.getUserByUserName(tenantAwarePrincipal.getUsername()).get();
        return shipRepository.findByActiveTrueAndOnWaterFalseAndSubTypeAndClubAndEnabledUsers(type, tenantAwarePrincipal.getClub(), user);
    }

    public void save(Ship ship) { shipRepository.save(ship); }

    public void deleteShipById(Long id) {
        if(shipRepository.findOneById(id).isPresent()) shipRepository.delete(id);
    }

    boolean isShipAvailable(Ship ship) {
        return ship.isActive() && !ship.isOnWater();
    }

    public Iterable<Ship> getAllShipsByType(ShipType type) {
        List<Ship> ships = new ArrayList<>();
        for(SubType subType : subTypeRepository.findAllByType(type)) {
            ships.addAll((List<Ship>) shipRepository.findAllBySubType(subType));
        }
        return ships;
    }
}
























