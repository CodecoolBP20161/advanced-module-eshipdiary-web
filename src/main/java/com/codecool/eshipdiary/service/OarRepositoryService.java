package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.security.TenantAwarePrincipal;
import com.codecool.eshipdiary.repository.OarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OarRepositoryService {

    @Autowired
    private OarRepository oarRepository;

    public Optional<Oar> getOarById(long id) {
        return oarRepository.findOneById(id);
    }

    public Iterable<Oar> getAllOars() {
        return oarRepository.findAll();
    }

    public Iterable<Oar> getAvailableOars() {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return oarRepository.findByActiveTrueAndOnWaterFalseAndClub(tenantAwarePrincipal.getClub());
    }

    public Iterable<Oar> getAvailableOarsByType(ShipType type) {
        TenantAwarePrincipal tenantAwarePrincipal = (TenantAwarePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return oarRepository.findByActiveTrueAndOnWaterFalseAndTypeAndClub(type, tenantAwarePrincipal.getClub());
    }

    public Iterable<Oar> getAllOarsByType(ShipType type) {
        return oarRepository.findAllByType(type);
    }
    public void save(Oar oar) { oarRepository.save(oar); }

    public void deleteOarById(Long id) {
        if(oarRepository.findOneById(id).isPresent()) oarRepository.delete(id);
    }

    public List<Oar> availableOarsFrom(List<Oar> oars) {
        List<Oar> availables = new ArrayList<>();
        for (Oar oar : oars) {
            if (oarIsAvailable(oar)) {
                availables.add(oar);
            }
        }
        return availables;
    }

    public boolean oarIsAvailable(Oar oar) {
        if (oar.isActive() && !oar.isOnWater()) {
            return true;
        }
        return false;
    }

}