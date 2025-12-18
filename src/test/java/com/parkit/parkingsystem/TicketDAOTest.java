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

public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;

    @BeforeAll
    private static void setUp() throws Exception{
        ticket = new Ticket();
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ticket.setInTime(inTime);

    }
    @Test
    public void saveTicketTest(){

        assertTrue(ticketDAO.saveTicket(ticket));

        Ticket saveTicket = ticketDAO.getTicket("ABCDEF");
        assertEquals(ticket.getPrice(),saveTicket.getPrice());
        assertEquals(ticket.getVehicleRegNumber(),saveTicket.getVehicleRegNumber());
        long diff = Math.abs(ticket.getInTime().getTime() - saveTicket.getInTime().getTime()); assertTrue(diff < 1000);
        assertTrue(diff < 1000);
        assertEquals(ticket.getParkingSpot(),saveTicket.getParkingSpot());

    }
    @Test
    public void updateTicketTest(){

        assertTrue(ticketDAO.saveTicket(ticket));

        Ticket saved = ticketDAO.getTicket("abcdef");
        ticket.setId(saved.getId());
        ticket.setPrice(2.1);
        ticket.setOutTime(new Date());

        assertTrue(ticketDAO.updateTicket(ticket));


        Ticket updateTicket = ticketDAO.getTicket("ABCDEF");
        assertEquals(2.1,updateTicket.getPrice());
        assertNotNull(updateTicket.getOutTime());

        long diff = Math.abs(ticket.getOutTime().getTime() - updateTicket.getOutTime().getTime());
        assertTrue(diff < 1000);
    }
    @Test
    public void getNbTicketTest() {
        dataBasePrepareService.clearDataBaseEntries();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        for (int i = 0; i < 2; i++) {
            Ticket ticket = new Ticket();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setPrice(1.0 + i);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 - i * 30) * 60 * 1000));
            ticket.setOutTime(new Date());
            assertTrue(ticketDAO.saveTicket(ticket));
        }

        int count = ticketDAO.getNbTicket("ABCDEF");
        assertEquals(2, count);
    }


}
