package com.railway.dao;

import com.railway.config.DatabaseConfig;
import com.railway.model.Train;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {
    
    public List<Train> searchTrains(String source, String destination) {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains WHERE source = ? AND destination = ? AND available_seats > 0";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, source);
            stmt.setString(2, destination);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("fare")
                );
                trains.add(train);
            }
        } catch (SQLException e) {
            System.err.println("Error searching trains: " + e.getMessage());
        }
        
        return trains;
    }
    
    public Train getTrainById(int trainId) {
        String sql = "SELECT * FROM trains WHERE train_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("fare")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting train: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean updateAvailableSeats(int trainId, int seatsToReduce) {
        String sql = "UPDATE trains SET available_seats = available_seats - ? WHERE train_id = ? AND available_seats >= ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatsToReduce);
            stmt.setInt(2, trainId);
            stmt.setInt(3, seatsToReduce);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating seats: " + e.getMessage());
            return false;
        }
    }
    
    public boolean increaseAvailableSeats(int trainId, int seatsToAdd) {
        String sql = "UPDATE trains SET available_seats = available_seats + ? WHERE train_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatsToAdd);
            stmt.setInt(2, trainId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error increasing seats: " + e.getMessage());
            return false;
        }
    }
}