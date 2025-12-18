package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket,boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        double duration = (double) (outTime.getTime() - inTime.getTime()) / (1000 * 60 *60);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (duration <= ((double) 30 / 60) ){
                    ticket.setPrice(0);
                }
                else{
                    if (discount) {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR*0.95);
                    }
                    else{
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    }

                }

                break;
            }
            case BIKE: {
                if (duration <= ((double) 30 / 60) ){
                    ticket.setPrice(0);
                }
                else {
                    if (discount) {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR*0.95);
                    }
                    else{
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    }

                }

                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
    public void calculateFare(Ticket ticket){
        calculateFare(ticket, false);
    }
}