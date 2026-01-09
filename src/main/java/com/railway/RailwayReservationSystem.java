package com.railway;

import java.util.List;
import java.util.Scanner;

import com.railway.model.Train;
import com.railway.service.ReservationService;

public class RailwayReservationSystem {
    private static final ReservationService reservationService = new ReservationService();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  RAILWAY RESERVATION SYSTEM");
        System.out.println("=================================");
        
        while (true) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    searchTrains();
                    break;
                case 2:
                    bookTicket();
                    break;
                case 3:
                    cancelTicket();
                    break;
                case 4:
                    viewTicket();
                    break;
                case 5:
                    viewPassengerTickets();
                    break;
                case 6:
                    updatePassengerDetails();
                    break;
                case 7:
                    printTicket();
                    break;
                case 8:
                    showTrainSchedule();
                    break;
                case 9:
                    calculateFare();
                    break;
                case 10:
                    showStationInfo();
                    break;
                case 11:
                    System.out.println("Thank you for using Railway Reservation System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Search Trains");
        System.out.println("2. Book Ticket");
        System.out.println("3. Cancel Ticket");
        System.out.println("4. View Ticket Details");
        System.out.println("5. View My Tickets");
        System.out.println("6. Update Passenger Details");
        System.out.println("7. Print Ticket");
        System.out.println("8. Train Schedule & Live Status");
        System.out.println("9. Fare Calculator");
        System.out.println("10. Station Information");
        System.out.println("11. Exit");
        System.out.println("================");
    }
    
    private static void searchTrains() {
        System.out.println("\n=== SEARCH TRAINS ===");
        System.out.print("Enter source station: ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination station: ");
        String destination = scanner.nextLine().trim();
        
        List<Train> trains = reservationService.searchTrains(source, destination);
        
        if (trains.isEmpty()) {
            System.out.println("No trains found for the given route!");
            return;
        }
        
        System.out.println("\n=== AVAILABLE TRAINS ===");
        System.out.printf("%-8s %-20s %-15s %-15s %-10s %-10s %-8s%n", 
                         "Train ID", "Train Name", "Departure", "Arrival", "Seats", "Fare", "Available");
        System.out.println("----------------------------------------------------------------------------------------");
        
        for (Train train : trains) {
            System.out.printf("%-8d %-20s %-15s %-15s %-10d $%-9.2f %-8d%n",
                             train.getTrainId(), train.getTrainName(), 
                             train.getDepartureTime(), train.getArrivalTime(),
                             train.getTotalSeats(), train.getFare(), train.getAvailableSeats());
        }
    }
    
    private static void bookTicket() {
        System.out.println("\n=== BOOK TICKET ===");
        int trainId = getIntInput("Enter Train ID: ");
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter passenger email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter passenger phone: ");
        String phone = scanner.nextLine().trim();
        
        // Enhanced passenger details
        int age = getIntInput("Enter passenger age: ");
        System.out.print("Enter gender (M/F/O): ");
        String gender = scanner.nextLine().trim().toUpperCase();
        
        System.out.println("\nSelect ticket class:");
        System.out.println("1. General");
        System.out.println("2. Sleeper");
        System.out.println("3. AC 3 Tier");
        System.out.println("4. AC 2 Tier");
        int classChoice = getIntInput("Enter choice (1-4): ");
        
        String ticketClass;
        switch (classChoice) {
            case 2: ticketClass = "SLEEPER"; break;
            case 3: ticketClass = "AC_3_TIER"; break;
            case 4: ticketClass = "AC_2_TIER"; break;
            default: ticketClass = "GENERAL"; break;
        }
        
        System.out.print("Enter journey date (dd-MM-yyyy) or press Enter for today: ");
        String dateInput = scanner.nextLine().trim();
        java.time.LocalDate journeyDate;
        if (dateInput.isEmpty()) {
            journeyDate = java.time.LocalDate.now();
        } else {
            try {
                journeyDate = java.time.LocalDate.parse(dateInput, 
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (Exception e) {
                System.out.println("Invalid date format, using today's date");
                journeyDate = java.time.LocalDate.now();
            }
        }
        
        int ticketId = reservationService.bookTicket(trainId, name, email, phone, age, gender, ticketClass, journeyDate);
        if (ticketId > 0) {
            System.out.println("\n=== BOOKING CONFIRMATION ===");
            reservationService.viewTicket(ticketId);
            
            System.out.print("\nWould you like to print the ticket? (y/n): ");
            String printChoice = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(printChoice) || "yes".equals(printChoice)) {
                System.out.println("\nChoose print format:");
                System.out.println("1. Console Print (Text)");
                System.out.println("2. PDF File");
                System.out.println("3. Both");
                
                int choice = getIntInput("Enter your choice (1-3): ");
                
                switch (choice) {
                    case 1:
                        reservationService.printTicket(ticketId);
                        break;
                    case 2:
                        reservationService.printTicketToPDF(ticketId);
                        break;
                    case 3:
                        reservationService.printTicket(ticketId);
                        reservationService.printTicketToPDF(ticketId);
                        break;
                    default:
                        System.out.println("Invalid choice! Printing to console...");
                        reservationService.printTicket(ticketId);
                }
            }
        }
    }
    
    private static void cancelTicket() {
        System.out.println("\n=== CANCEL TICKET ===");
        int ticketId = getIntInput("Enter Ticket ID to cancel: ");
        
        System.out.println("\nTicket details:");
        reservationService.viewTicket(ticketId);
        
        System.out.print("\nAre you sure you want to cancel this ticket? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if ("y".equals(confirm) || "yes".equals(confirm)) {
            reservationService.cancelTicket(ticketId);
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
    
    private static void viewTicket() {
        System.out.println("\n=== VIEW TICKET ===");
        int ticketId = getIntInput("Enter Ticket ID: ");
        reservationService.viewTicket(ticketId);
    }
    
    private static void viewPassengerTickets() {
        System.out.println("\n=== MY TICKETS ===");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        reservationService.viewPassengerTickets(email);
    }
    
    private static void printTicket() {
        System.out.println("\n=== PRINT TICKET ===");
        int ticketId = getIntInput("Enter Ticket ID to print: ");
        
        System.out.println("\nChoose print format:");
        System.out.println("1. Console Print (Text)");
        System.out.println("2. PDF File");
        System.out.println("3. Both");
        
        int choice = getIntInput("Enter your choice (1-3): ");
        
        switch (choice) {
            case 1:
                reservationService.printTicket(ticketId);
                break;
            case 2:
                reservationService.printTicketToPDF(ticketId);
                break;
            case 3:
                reservationService.printTicket(ticketId);
                reservationService.printTicketToPDF(ticketId);
                break;
            default:
                System.out.println("Invalid choice! Printing to console...");
                reservationService.printTicket(ticketId);
        }
    }
    
    private static void updatePassengerDetails() {
        System.out.println("\n=== UPDATE PASSENGER DETAILS ===");
        int ticketId = getIntInput("Enter Ticket ID: ");
        
        System.out.println("\nCurrent ticket details:");
        reservationService.viewTicket(ticketId);
        
        System.out.print("\nDo you want to update passenger details? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if ("y".equals(confirm) || "yes".equals(confirm)) {
            System.out.print("Enter new passenger name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter new passenger email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter new passenger phone: ");
            String phone = scanner.nextLine().trim();
            
            reservationService.updatePassengerDetails(ticketId, name, email, phone);
        } else {
            System.out.println("Update cancelled.");
        }
    }
    
    private static void showTrainSchedule() {
        System.out.println("\n=== TRAIN SCHEDULE & LIVE STATUS ===");
        System.out.print("Enter Train Number: ");
        String trainNum = scanner.nextLine().trim();
        
        System.out.println("\n=== TRAIN SCHEDULE FOR TRAIN " + trainNum + " ===");
        System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", 
                         "Station", "Station Name", "Arrival", "Departure", "Status");
        System.out.println("------------------------------------------------------------------------");
        
        // Sample schedule data based on train number
        if (trainNum.equals("1") || trainNum.toLowerCase().contains("chennai")) {
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "MAS", "Chennai Central", "--", "06:00", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "AJJ", "Arakkonam Jn", "06:45", "06:47", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "KPD", "Katpadi Jn", "07:30", "07:35", "Delayed 5 min");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "JTJ", "Jolarpettai", "09:15", "09:20", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "SA", "Salem Jn", "10:45", "10:50", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "ED", "Erode Jn", "11:45", "11:50", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "CBE", "Coimbatore Jn", "12:30", "--", "Expected");
        } else {
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "SRC", "Source Station", "--", "08:00", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "INT1", "Intermediate 1", "09:30", "09:32", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "INT2", "Intermediate 2", "11:15", "11:17", "On Time");
            System.out.printf("%-15s %-20s %-10s %-10s %-15s%n", "DEST", "Destination", "13:45", "--", "Expected");
        }
        
        System.out.println("\n[INFO] Live tracking available on IRCTC app");
        System.out.println("[INFO] SMS alerts: Send 'SPOT <PNR>' to 139");
        System.out.println("[INFO] Train Number searched: " + trainNum);
    }
    
    private static void calculateFare() {
        System.out.println("\n=== FARE CALCULATOR ===");
        System.out.print("Enter source station: ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter destination station: ");
        String destination = scanner.nextLine().trim();
        
        System.out.println("\n=== FARE BREAKDOWN FOR " + source.toUpperCase() + " TO " + destination.toUpperCase() + " ===");
        System.out.printf("%-20s %-15s %-15s %-15s%n", "Class", "Base Fare", "Taxes", "Total Fare");
        System.out.println("---------------------------------------------------------------");
        
        // Calculate distance based on route
        double distance;
        if ((source.toLowerCase().contains("chennai") && destination.toLowerCase().contains("coimbatore")) ||
            (source.toLowerCase().contains("coimbatore") && destination.toLowerCase().contains("chennai"))) {
            distance = 497; // Chennai to Coimbatore
        } else if ((source.toLowerCase().contains("chennai") && destination.toLowerCase().contains("madurai")) ||
                   (source.toLowerCase().contains("madurai") && destination.toLowerCase().contains("chennai"))) {
            distance = 462; // Chennai to Madurai
        } else {
            distance = 350; // Default distance
        }
        
        double baseFarePerKm = 0.75;
        double baseFare = distance * baseFarePerKm;
        double taxes = baseFare * 0.15;
        
        System.out.printf("%-20s Rs.%-12.2f Rs.%-12.2f Rs.%-12.2f%n", "General", baseFare, taxes, baseFare + taxes);
        System.out.printf("%-20s Rs.%-12.2f Rs.%-12.2f Rs.%-12.2f%n", "Sleeper", baseFare * 1.5, taxes * 1.5, (baseFare + taxes) * 1.5);
        System.out.printf("%-20s Rs.%-12.2f Rs.%-12.2f Rs.%-12.2f%n", "AC 3 Tier", baseFare * 2.5, taxes * 2.5, (baseFare + taxes) * 2.5);
        System.out.printf("%-20s Rs.%-12.2f Rs.%-12.2f Rs.%-12.2f%n", "AC 2 Tier", baseFare * 3.5, taxes * 3.5, (baseFare + taxes) * 3.5);
        
        System.out.println("\nRoute: " + source + " → " + destination);
        System.out.println("Distance: " + distance + " km");
        System.out.println("Note: Fares are approximate and subject to change");
    }
    
    private static void showStationInfo() {
        System.out.println("\n=== STATION INFORMATION ===");
        System.out.print("Enter station code or name: ");
        String station = scanner.nextLine().trim().toUpperCase();
        
        System.out.println("\n=== STATION DETAILS ===");
        
        // Sample station information
        if (station.contains("CHENNAI") || station.equals("MAS")) {
            System.out.println("Station Name: Chennai Central (MAS)");
            System.out.println("State: Tamil Nadu");
            System.out.println("Zone: Southern Railway");
            System.out.println("Division: Chennai");
            System.out.println("Platforms: 12");
            System.out.println("Facilities: Waiting Room, Food Court, WiFi, ATM, Parking");
            System.out.println("Contact: 044-25354151");
        } else if (station.contains("COIMBATORE") || station.equals("CBE")) {
            System.out.println("Station Name: Coimbatore Junction (CBE)");
            System.out.println("State: Tamil Nadu");
            System.out.println("Zone: Southern Railway");
            System.out.println("Division: Salem");
            System.out.println("Platforms: 6");
            System.out.println("Facilities: Waiting Room, Food Plaza, WiFi, ATM");
            System.out.println("Contact: 0422-2395555");
        } else {
            System.out.println("Station Name: " + station);
            System.out.println("State: Tamil Nadu");
            System.out.println("Zone: Southern Railway");
            System.out.println("Platforms: 4");
            System.out.println("Facilities: Basic amenities available");
            System.out.println("Contact: 139 (Railway Enquiry)");
        }
        
        System.out.println("\n=== NEARBY TRAINS ===");
        System.out.println("• Express trains: 15-20 daily");
        System.out.println("• Passenger trains: 8-10 daily");
        System.out.println("• Mail trains: 5-8 daily");
        
        System.out.println("\n[INFO] For live train status, call 139");
        System.out.println("[INFO] Station WiFi: RailWire (Free for 30 minutes)");
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}