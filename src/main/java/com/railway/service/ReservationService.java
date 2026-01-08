package com.railway.service;

import java.util.List;

import com.railway.dao.TicketDAO;
import com.railway.dao.TrainDAO;
import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.util.PDFTicketGenerator;

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
    
    public void printTicketToPDF(int ticketId) {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return;
        }
        
        Train train = trainDAO.getTrainById(ticket.getTrainId());
        if (train == null) {
            System.out.println("Train information not found!");
            return;
        }
        
        // Generate PDF filename
        String fileName = "Ticket_" + ticket.getTicketId() + "_" + 
                         ticket.getPassengerName().replaceAll("\\s+", "_") + ".pdf";
        String outputPath = "tickets/" + fileName;
        
        // Generate PDF ticket
        if (PDFTicketGenerator.generateTicketPDF(ticket, train, outputPath)) {
            System.out.println("\n[SUCCESS] PDF ticket generated successfully!");
            System.out.println("[FILE] File saved as: " + outputPath);
            System.out.println("[PATH] Full path: " + System.getProperty("user.dir") + "/" + outputPath);
            System.out.println("[INFO] PDF ticket ready for printing");
        } else {
            System.out.println("[ERROR] Error generating PDF ticket!");
        }
    }
    
    public void printTicket(int ticketId) {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return;
        }
        
        Train train = trainDAO.getTrainById(ticket.getTrainId());
        if (train == null) {
            System.out.println("Train information not found!");
            return;
        }
        
        // Print formatted ticket
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    TAMIL NADU RAILWAY RESERVATION SYSTEM");
        System.out.println("                         Electronic Reservation Slip (ERS)");
        System.out.println("=".repeat(80));
        
        // Header section
        System.out.printf("%-25s %-30s %-20s%n", "Booked From", "Boarding At", "To");
        System.out.printf("%-25s %-30s %-20s%n", 
                         train.getSource().toUpperCase(), 
                         train.getSource().toUpperCase(), 
                         train.getDestination().toUpperCase());
        
        System.out.println("-".repeat(80));
        
        // Journey details
        System.out.printf("%-20s %-25s %-15s %-15s%n", 
                         "Start Date", "Departure", "Arrival", "Class");
        System.out.printf("%-20s %-25s %-15s %-15s%n", 
                         ticket.getBookingTime().toLocalDate().toString(),
                         train.getDepartureTime(),
                         train.getArrivalTime(),
                         "GENERAL");
        
        System.out.println("-".repeat(80));
        
        // Train and booking details
        System.out.printf("%-15s %-25s %-20s %-15s%n", 
                         "PNR", "Train No./Name", "Distance", "Booking Date");
        System.out.printf("%-15s %-25s %-20s %-15s%n", 
                         "TN" + String.format("%010d", ticketId),
                         train.getTrainId() + " / " + train.getTrainName(),
                         "-- KM",
                         ticket.getBookingTime().toLocalDate().toString());
        
        System.out.println("-".repeat(80));
        
        // Passenger details header
        System.out.println("Passenger Details");
        System.out.printf("%-5s %-25s %-10s %-15s %-20s%n", 
                         "#", "Name", "Age", "Gender", "Booking Status");
        System.out.printf("%-5s %-25s %-10s %-15s %-20s%n", 
                         "1.", ticket.getPassengerName(), "N/A", "N/A", "CONFIRMED");
        
        System.out.println("-".repeat(80));
        
        // Seat and fare details
        System.out.printf("%-20s: %-10s%n", "Seat Number", ticket.getSeatNumber());
        System.out.printf("%-20s: %-10s%n", "Contact", ticket.getPassengerPhone());
        System.out.printf("%-20s: %-10s%n", "Email", ticket.getPassengerEmail());
        
        System.out.println("-".repeat(80));
        
        // Payment details
        System.out.println("Payment Details");
        System.out.printf("%-30s: Rs. %.2f%n", "Ticket Fare", ticket.getFare());
        System.out.printf("%-30s: Rs. %.2f%n", "Convenience Fee", 0.0);
        System.out.printf("%-30s: Rs. %.2f%n", "Total Fare (all inclusive)", ticket.getFare());
        
        System.out.println("-".repeat(80));
        
        // Transaction details
        System.out.printf("Transaction ID: TN%s%010d%n", 
                         ticket.getBookingTime().toLocalDate().toString().replace("-", ""), 
                         ticketId);
        
        System.out.println("-".repeat(80));
        
        // Important notes
        System.out.println("IMPORTANT INSTRUCTIONS:");
        System.out.println("• Please carry a valid photo ID proof during journey");
        System.out.println("• Ticket is valid only for the specified train and date");
        System.out.println("• Report to station at least 30 minutes before departure");
        System.out.println("• This is a computer generated ticket and does not require signature");
        
        System.out.println("-".repeat(80));
        
        // Footer
        System.out.println("                    TAMIL NADU RAILWAY - SAFE & COMFORTABLE JOURNEY");
        System.out.println("                          Status: " + ticket.getStatus());
        System.out.println("=".repeat(80));
        
        System.out.println("\n*** HAPPY JOURNEY ***");
    }
    
    public boolean updatePassengerDetails(int ticketId, String passengerName, String passengerEmail, String passengerPhone) {
        Ticket ticket = ticketDAO.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found!");
            return false;
        }
        
        if (!"BOOKED".equals(ticket.getStatus())) {
            System.out.println("Cannot update details for cancelled ticket!");
            return false;
        }
        
        // Update passenger details
        if (ticketDAO.updatePassengerDetails(ticketId, passengerName, passengerEmail, passengerPhone)) {
            System.out.println("Passenger details updated successfully!");
            System.out.println("Updated Details:");
            System.out.println("Name: " + passengerName);
            System.out.println("Email: " + passengerEmail);
            System.out.println("Phone: " + passengerPhone);
            return true;
        } else {
            System.out.println("Error updating passenger details!");
            return false;
        }
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