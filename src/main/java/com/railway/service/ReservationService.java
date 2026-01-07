package com.railway.service;

import java.util.List;

import com.railway.dao.TicketDAO;
import com.railway.dao.TrainDAO;
import com.railway.model.Ticket;
import com.railway.model.Train;

public class ReservationService {
    private final TrainDAO trainDAO;
    private final TicketDAO ticketDAO;
    
    public ReservationService() {
        this.trainDAO = new TrainDAO();
        this.ticketDAO = new TicketDAO();
    }
    
    public List<Train> searchTrains(String source, String destination) {
        return trainDAO.searchTrains(source, destination);
    }
    
    public int bookTicket(int trainId, String passengerName, String passengerEmail, String passengerPhone) {
        Train train = trainDAO.getTrainById(trainId);
        if (train == null) {
            System.out.println("Train not found!");
            return -1;
        }
        
        if (train.getAvailableSeats() <= 0) {
            System.out.println("No seats available!");
            return -1;
        }
        
        // Get next available seat number
        int seatNumber = ticketDAO.getNextAvailableSeat(trainId);
        
        // Create ticket
        Ticket ticket = new Ticket(trainId, passengerName, passengerEmail, passengerPhone, seatNumber, train.getFare());
        
        // Book ticket in database
        int ticketId = ticketDAO.bookTicket(ticket);
        
        if (ticketId > 0) {
            // Update available seats
            if (trainDAO.updateAvailableSeats(trainId, 1)) {
                System.out.println("Ticket booked successfully!");
                System.out.println("Ticket ID: " + ticketId);
                System.out.println("Seat Number: " + seatNumber);
                System.out.println("Fare: $" + train.getFare());
                return ticketId;
            } else {
                System.out.println("Error updating seat availability!");
                return -1;
            }
        } else {
            System.out.println("Error booking ticket!");
            return -1;
        }
    }
    
    public boolean cancelTicket(int ticketId) {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return false;
        }
        
        if (!"BOOKED".equals(ticket.getStatus())) {
            System.out.println("Ticket is already cancelled!");
            return false;
        }
        
        // Cancel ticket
        if (ticketDAO.cancelTicket(ticketId)) {
            // Increase available seats
            if (trainDAO.increaseAvailableSeats(ticket.getTrainId(), 1)) {
                System.out.println("Ticket cancelled successfully!");
                return true;
            } else {
                System.out.println("Ticket cancelled but error updating seat availability!");
                return true;
            }
        } else {
            System.out.println("Error cancelling ticket!");
            return false;
        }
    }
    
    public void viewTicket(int ticketId) {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return;
        }
        
        Train train = trainDAO.getTrainById(ticket.getTrainId());
        
        System.out.println("\n=== TICKET DETAILS ===");
        System.out.println("Ticket ID: " + ticket.getTicketId());
        System.out.println("Train: " + (train != null ? train.getTrainName() : "Unknown"));
        System.out.println("Route: " + (train != null ? train.getSource() + " -> " + train.getDestination() : "Unknown"));
        System.out.println("Passenger: " + ticket.getPassengerName());
        System.out.println("Email: " + ticket.getPassengerEmail());
        System.out.println("Phone: " + ticket.getPassengerPhone());
        System.out.println("Seat Number: " + ticket.getSeatNumber());
        System.out.println("Fare: $" + ticket.getFare());
        System.out.println("Booking Time: " + ticket.getBookingTime());
        System.out.println("Status: " + ticket.getStatus());
        if (train != null) {
            System.out.println("Departure: " + train.getDepartureTime());
            System.out.println("Arrival: " + train.getArrivalTime());
        }
        System.out.println("=====================");
    }
    
    public void viewPassengerTickets(String passengerEmail) {
        List<Ticket> tickets = ticketDAO.getTicketsByPassenger(passengerEmail);
        
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for this email!");
            return;
        }
        
        System.out.println("\n=== YOUR TICKETS ===");
        for (Ticket ticket : tickets) {
            Train train = trainDAO.getTrainById(ticket.getTrainId());
            System.out.println("Ticket ID: " + ticket.getTicketId() + 
                             " | Train: " + (train != null ? train.getTrainName() : "Unknown") +
                             " | Seat: " + ticket.getSeatNumber() + 
                             " | Status: " + ticket.getStatus());
        }
        System.out.println("===================");
    }
}