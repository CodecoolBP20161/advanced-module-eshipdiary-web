package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Ship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class ShipRepositoryServiceTest {
    @Autowired
    ShipRepositoryService shipRepositoryService;

    @Test
    public void testIsShipAvailable() throws Exception {
        Ship ship = new Ship();
        ship.setActive(true);
        ship.setOnWater(false);
        Boolean available = shipRepositoryService.isShipAvailable(ship);
        assertTrue(available);
    }
}
