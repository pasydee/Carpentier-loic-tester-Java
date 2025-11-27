package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (double) (outTime.getTime() - inTime.getTime()) / (1000 * 60 *60);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (duration <= ((double) 30 / 60) ){
                    ticket.setPrice(0);
                }
                else{
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                }

                break;
            }
            case BIKE: {
                if (duration <= ((double) 30 / 60) ){
                    ticket.setPrice(0);
                }
                else {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                }

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}