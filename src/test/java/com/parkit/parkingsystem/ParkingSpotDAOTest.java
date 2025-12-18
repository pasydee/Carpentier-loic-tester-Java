package com.parkit.parkingsystem;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();

    }

    @Test
    public void  getNextAvailableSlotTest(){
        int slot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertTrue(slot > 0);

    }
    @Test
    public void updateParkingTest() {
        int slotId = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        ParkingSpot spot = new ParkingSpot(slotId, ParkingType.CAR, false);

        boolean updated = parkingSpotDAO.updateParking(spot);
        assertTrue(updated);

        int nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertNotEquals(slotId, nextSlot);
    }

    @Test
    public void getParkingSpotTest() {

        int slotId = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertTrue(slotId > 0);

        ParkingSpot spot = parkingSpotDAO.getParkingSpot(slotId);
        assertNotNull(spot);

        assertEquals(slotId, spot.getId());
        assertEquals(ParkingType.CAR, spot.getParkingType());
        assertTrue(spot.isAvailable());
    }


}
