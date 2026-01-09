package com.railway.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int trainId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private int passengerAge;
    private String passengerGender; // M, F, O
    private String idProofType; // AADHAR, PAN, PASSPORT, DRIVING_LICENSE
    private String idProofNumber;
    private String seatNumber; // Changed to String for formats like "S1-23", "A1-45"
    private String coachNumber; // S1, A1, B2, etc.
    private String berthType; // LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER
    private String ticketClass; // GENERAL, SLEEPER, AC_3_TIER, AC_2_TIER, AC_1_TIER
    private double baseFare;
    private double taxes;
    private double totalFare;
    private LocalDateTime bookingTime;
    private LocalDate journeyDate;
    private String status; // BOOKED, CANCELLED, CONFIRMED, WAITING, RAC
    private String pnrNumber; // 10-digit PNR
    private String bookingSource; // ONLINE, COUNTER, MOBILE_APP
    private String paymentMode; // CARD, UPI, NET_BANKING, CASH
    private String transactionId;
    private boolean isInsured;
    private double insuranceAmount;
    private String specialRequests; // WHEELCHAIR, MEAL, etc.
    
    public Ticket() {}
    
    public Ticket(int trainId, String passengerName, String passengerEmail, 
                  String passengerPhone, int passengerAge, String passengerGender,
                  String seatNumber, String coachNumber, String ticketClass, 
                  double baseFare, LocalDate journeyDate) {
        this.trainId = trainId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.passengerAge = passengerAge;
        this.passengerGender = passengerGender;
        this.seatNumber = seatNumber;
        this.coachNumber = coachNumber;
        this.ticketClass = ticketClass;
        this.baseFare = baseFare;
        this.taxes = baseFare * 0.15; // 15% taxes
        this.totalFare = baseFare + taxes;
        this.bookingTime = LocalDateTime.now();
        this.journeyDate = journeyDate;
        this.status = "BOOKED";
        this.pnrNumber = generatePNR();
        this.bookingSource = "ONLINE";
        this.paymentMode = "CARD";
        this.transactionId = "TXN" + System.currentTimeMillis() % 1000000;
        this.isInsured = false;
        this.insuranceAmount = 0.0;
        this.idProofType = "AADHAR";
        this.berthType = "LOWER";
    }
    
    private String generatePNR() {
        return String.format("%010d", (long)(Math.random() * 10000000000L));
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
    
    public int getPassengerAge() { return passengerAge; }
    public void setPassengerAge(int passengerAge) { this.passengerAge = passengerAge; }
    
    public String getPassengerGender() { return passengerGender; }
    public void setPassengerGender(String passengerGender) { this.passengerGender = passengerGender; }
    
    public String getIdProofType() { return idProofType; }
    public void setIdProofType(String idProofType) { this.idProofType = idProofType; }
    
    public String getIdProofNumber() { return idProofNumber; }
    public void setIdProofNumber(String idProofNumber) { this.idProofNumber = idProofNumber; }
    
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    
    public String getCoachNumber() { return coachNumber; }
    public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }
    
    public String getBerthType() { return berthType; }
    public void setBerthType(String berthType) { this.berthType = berthType; }
    
    public String getTicketClass() { return ticketClass; }
    public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }
    
    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }
    
    public double getTaxes() { return taxes; }
    public void setTaxes(double taxes) { this.taxes = taxes; }
    
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    
    // Legacy method for backward compatibility
    public double getFare() { return totalFare; }
    public void setFare(double fare) { this.totalFare = fare; }
    
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    
    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }
    
    public String getBookingSource() { return bookingSource; }
    public void setBookingSource(String bookingSource) { this.bookingSource = bookingSource; }
    
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public boolean isInsured() { return isInsured; }
    public void setInsured(boolean insured) { isInsured = insured; }
    
    public double getInsuranceAmount() { return insuranceAmount; }
    public void setInsuranceAmount(double insuranceAmount) { this.insuranceAmount = insuranceAmount; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}