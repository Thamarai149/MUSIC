package com.railway.util;

import com.railway.model.Ticket;
import com.railway.model.Train;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFTicketGenerator {
    
    public static boolean generateTicketPDF(Ticket ticket, Train train, String outputPath) {
        try {
            // Create HTML content for the ticket
            String htmlContent = generateHTMLTicket(ticket, train);
            
            // Save as HTML file (can be converted to PDF by browser)
            String htmlPath = outputPath.replace(".pdf", ".html");
            
            try (FileWriter writer = new FileWriter(htmlPath)) {
                writer.write(htmlContent);
            }
            
            System.out.println("[INFO] HTML ticket generated: " + htmlPath);
            System.out.println("[INFO] You can open this file in a browser and print as PDF");
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error generating ticket file: " + e.getMessage());
            return false;
        }
    }
    
    private static String generateHTMLTicket(Ticket ticket, Train train) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<title>Railway Ticket - ").append(ticket.getTicketId()).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
        html.append(".ticket { background: white; border: 2px solid #0066cc; padding: 20px; max-width: 800px; margin: 0 auto; }\n");
        html.append(".header { text-align: center; color: #0066cc; border-bottom: 2px solid #0066cc; padding-bottom: 10px; margin-bottom: 20px; }\n");
        html.append(".section { margin: 15px 0; }\n");
        html.append(".section-title { background: #0066cc; color: white; padding: 8px; font-weight: bold; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin: 10px 0; }\n");
        html.append("td, th { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("th { background-color: #e6f2ff; }\n");
        html.append(".important { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; margin: 15px 0; }\n");
        html.append(".footer { text-align: center; color: #0066cc; font-weight: bold; margin-top: 20px; }\n");
        html.append("@media print { body { background: white; } .ticket { border: none; } }\n");
        html.append("</style>\n</head>\n<body>\n");
        
        html.append("<div class='ticket'>\n");
        
        // Header
        html.append("<div class='header'>\n");
        html.append("<h1>TAMIL NADU RAILWAY RESERVATION SYSTEM</h1>\n");
        html.append("<h2>Electronic Reservation Slip (ERS)</h2>\n");
        html.append("</div>\n");
        
        // Journey Information
        html.append("<div class='section'>\n");
        html.append("<div class='section-title'>JOURNEY DETAILS</div>\n");
        html.append("<table>\n");
        html.append("<tr><th>From</th><th>To</th><th>Class</th><th>Date</th></tr>\n");
        html.append("<tr><td>").append(train.getSource().toUpperCase()).append("</td>");
        html.append("<td>").append(train.getDestination().toUpperCase()).append("</td>");
        html.append("<td>GENERAL</td>");
        html.append("<td>").append(ticket.getBookingTime().toLocalDate().toString()).append("</td></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Train Details
        html.append("<div class='section'>\n");
        html.append("<div class='section-title'>TRAIN INFORMATION</div>\n");
        html.append("<table>\n");
        html.append("<tr><th>Train No./Name</th><th>Departure</th><th>Arrival</th><th>PNR</th></tr>\n");
        html.append("<tr><td>").append(train.getTrainId()).append(" / ").append(train.getTrainName()).append("</td>");
        html.append("<td>").append(train.getDepartureTime()).append("</td>");
        html.append("<td>").append(train.getArrivalTime()).append("</td>");
        html.append("<td>TN").append(String.format("%010d", ticket.getTicketId())).append("</td></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Passenger Details
        html.append("<div class='section'>\n");
        html.append("<div class='section-title'>PASSENGER DETAILS</div>\n");
        html.append("<table>\n");
        html.append("<tr><th>Name</th><th>Email</th><th>Phone</th><th>Seat No.</th></tr>\n");
        html.append("<tr><td>").append(ticket.getPassengerName()).append("</td>");
        html.append("<td>").append(ticket.getPassengerEmail()).append("</td>");
        html.append("<td>").append(ticket.getPassengerPhone()).append("</td>");
        html.append("<td>").append(ticket.getSeatNumber()).append("</td></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Payment Details
        html.append("<div class='section'>\n");
        html.append("<div class='section-title'>PAYMENT DETAILS</div>\n");
        html.append("<table>\n");
        html.append("<tr><th>Description</th><th>Amount</th></tr>\n");
        html.append("<tr><td>Ticket Fare</td><td>Rs. ").append(String.format("%.2f", ticket.getFare())).append("</td></tr>\n");
        html.append("<tr><td>Convenience Fee</td><td>Rs. 0.00</td></tr>\n");
        html.append("<tr><th>Total Amount</th><th>Rs. ").append(String.format("%.2f", ticket.getFare())).append("</th></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Transaction Details
        html.append("<div class='section'>\n");
        html.append("<div class='section-title'>TRANSACTION DETAILS</div>\n");
        html.append("<table>\n");
        html.append("<tr><td><strong>Transaction ID:</strong></td><td>TN")
               .append(ticket.getBookingTime().toLocalDate().toString().replace("-", ""))
               .append(String.format("%010d", ticket.getTicketId())).append("</td></tr>\n");
        html.append("<tr><td><strong>Booking Time:</strong></td><td>")
               .append(ticket.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
               .append("</td></tr>\n");
        html.append("<tr><td><strong>Status:</strong></td><td>").append(ticket.getStatus()).append("</td></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Important Instructions
        html.append("<div class='important'>\n");
        html.append("<h3>IMPORTANT INSTRUCTIONS:</h3>\n");
        html.append("<ul>\n");
        html.append("<li>Please carry a valid photo ID proof during journey</li>\n");
        html.append("<li>Ticket is valid only for the specified train and date</li>\n");
        html.append("<li>Report to station at least 30 minutes before departure</li>\n");
        html.append("<li>This is a computer generated ticket and does not require signature</li>\n");
        html.append("<li>Keep this ticket safe until the end of your journey</li>\n");
        html.append("</ul>\n");
        html.append("</div>\n");
        
        // Footer
        html.append("<div class='footer'>\n");
        html.append("<h2>TAMIL NADU RAILWAY - SAFE & COMFORTABLE JOURNEY</h2>\n");
        html.append("<h3 style='color: green;'>*** HAPPY JOURNEY ***</h3>\n");
        html.append("</div>\n");
        
        html.append("</div>\n</body>\n</html>");
        
        return html.toString();
    }
}