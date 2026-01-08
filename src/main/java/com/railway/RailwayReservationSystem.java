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
        System.out.println("8. Exit");
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
        
        int ticketId = reservationService.bookTicket(trainId, name, email, phone);
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