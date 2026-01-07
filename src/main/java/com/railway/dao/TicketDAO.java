package com.railway.dao;

import com.railway.config.DatabaseConfig;
import com.railway.model.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    
    public int bookTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (train_id, passenger_name, passenger_email, passenger_phone, seat_number, fare, booking_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, ticket.getTrainId());
            stmt.setString(2, ticket.getPassengerName());
            stmt.setString(3, ticket.getPassengerEmail());
            stmt.setString(4, ticket.getPassengerPhone());
            stmt.setInt(5, ticket.getSeatNumber());
            stmt.setDouble(6, ticket.getFare());
            stmt.setTimestamp(7, Timestamp.valueOf(ticket.getBookingTime()));
            stmt.setString(8, ticket.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error booking ticket: " + e.getMessage());
        }
        
        return -1;
    }
    
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setTrainId(rs.getInt("train_id"));
                ticket.setPassengerName(rs.getString("passenger_name"));
                ticket.setPassengerEmail(rs.getString("passenger_email"));
                ticket.setPassengerPhone(rs.getString("passenger_phone"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setFare(rs.getDouble("fare"));
                ticket.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
                ticket.setStatus(rs.getString("status"));
                return ticket;
            }
        } catch (SQLException e) {
            System.err.println("Error getting ticket: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean cancelTicket(int ticketId) {
        String sql = "UPDATE tickets SET status = 'CANCELLED' WHERE ticket_id = ? AND status = 'BOOKED'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling ticket: " + e.getMessage());
            return false;
        }
    }
    
    public List<Ticket> getTicketsByPassenger(String passengerEmail) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE passenger_email = ? ORDER BY booking_time DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, passengerEmail);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setTrainId(rs.getInt("train_id"));
                ticket.setPassengerName(rs.getString("passenger_name"));
                ticket.setPassengerEmail(rs.getString("passenger_email"));
                ticket.setPassengerPhone(rs.getString("passenger_phone"));
                ticket.setSeatNumber(rs.getInt("seat_number"));
                ticket.setFare(rs.getDouble("fare"));
                ticket.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
                ticket.setStatus(rs.getString("status"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting passenger tickets: " + e.getMessage());
        }
        
        return tickets;
    }
    
    public boolean updatePassengerDetails(int ticketId, String passengerName, String passengerEmail, String passengerPhone) {
        String sql = "UPDATE tickets SET passenger_name = ?, passenger_email = ?, passenger_phone = ? WHERE ticket_id = ? AND status = 'BOOKED'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, passengerName);
            stmt.setString(2, passengerEmail);
            stmt.setString(3, passengerPhone);
            stmt.setInt(4, ticketId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating passenger details: " + e.getMessage());
            return false;
        }
    }
    
    public int getNextAvailableSeat(int trainId) {
        String sql = "SELECT COALESCE(MIN(seat_number + 1), 1) as next_seat FROM tickets WHERE train_id = ? AND status = 'BOOKED' AND seat_number + 1 NOT IN (SELECT seat_number FROM tickets WHERE train_id = ? AND status = 'BOOKED')";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, trainId);
            stmt.setInt(2, trainId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("next_seat");
            }
        } catch (SQLException e) {
            System.err.println("Error getting next seat: " + e.getMessage());
        }
        
        return 1; // Default to seat 1 if no seats are booked
    }
}