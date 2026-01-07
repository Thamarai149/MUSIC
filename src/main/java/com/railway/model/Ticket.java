package com.railway.model;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int trainId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private int seatNumber;
    private double fare;
    private LocalDateTime bookingTime;
    private String status; // BOOKED, CANCELLED
    
    public Ticket() {}
    
    public Ticket(int trainId, String passengerName, String passengerEmail, 
                  String passengerPhone, int seatNumber, double fare) {
        this.trainId = trainId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.seatNumber = seatNumber;
        this.fare = fare;
        this.bookingTime = LocalDateTime.now();
        this.status = "BOOKED";
    }
    
    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    
    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }
    
    public String getPassengerPhone() { return passengerPhone; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }
    
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    
    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
    
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}