package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Ship;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@ContextConfiguration
//@WebAppConfiguration
public class ShipRepositoryServiceTest {
    @Autowired
    ShipRepositoryService shipRepositoryService;

    @Test
    public void testIsShipAvailable_itIs() throws Exception {
        Ship ship = new Ship();
        ship.setActive(true);
        ship.setOnWater(false);
        Boolean available = shipRepositoryService.isShipAvailable(ship);
        assertTrue(available);
    }

    @Test
    public void testIsShipAvailable_isTaken() throws Exception {
        Ship ship = new Ship();
        ship.setActive(true);
        ship.setOnWater(true);
        Boolean available = shipRepositoryService.isShipAvailable(ship);
        assertFalse(available);
    }

    @Test
    public void testIsShipAvailable_itInactive() throws Exception {
        Ship ship = new Ship();
        ship.setActive(false);
        ship.setOnWater(false);
        Boolean available = shipRepositoryService.isShipAvailable(ship);
        assertFalse(available);
    }
}
