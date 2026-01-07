package com.railway.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.railway.model.Ticket;
import com.railway.model.Train;

public class PDFTicketGenerator {
    
    public static boolean generateTicketPDF(Ticket ticket, Train train, String outputPath) {
        try {
            // Create directory if it doesn't exist
            File file = new File(outputPath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Generate simple PDF content using basic PDF structure
            String pdfContent = generateSimplePDF(ticket, train);
            
            // Save as PDF file
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(pdfContent.getBytes("ISO-8859-1"));
            }
            
            System.out.println("[INFO] PDF ticket generated: " + outputPath);
            System.out.println("[INFO] File size: " + file.length() + " bytes");
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error generating PDF ticket: " + e.getMessage());
            return false;
        }
    }
    
    private static String generateSimplePDF(Ticket ticket, Train train) {
        StringBuilder pdf = new StringBuilder();
        
        // PDF Header
        pdf.append("%PDF-1.4\n");
        pdf.append("1 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Type /Catalog\n");
        pdf.append("/Pages 2 0 R\n");
        pdf.append(">>\n");
        pdf.append("endobj\n\n");
        
        // Pages object
        pdf.append("2 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Type /Pages\n");
        pdf.append("/Kids [3 0 R]\n");
        pdf.append("/Count 1\n");
        pdf.append(">>\n");
        pdf.append("endobj\n\n");
        
        // Page object
        pdf.append("3 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Type /Page\n");
        pdf.append("/Parent 2 0 R\n");
        pdf.append("/MediaBox [0 0 288 180]\n"); // ATVM ticket size (4" x 2.5" at 72 DPI)
        pdf.append("/Contents 4 0 R\n");
        pdf.append("/Resources <<\n");
        pdf.append("/Font <<\n");
        pdf.append("/F1 5 0 R\n");
        pdf.append("/F2 6 0 R\n");
        pdf.append(">>\n");
        pdf.append(">>\n");
        pdf.append(">>\n");
        pdf.append("endobj\n\n");
        
        // Content stream
        String contentStream = generateContentStream(ticket, train);
        pdf.append("4 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Length ").append(contentStream.length()).append("\n");
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(contentStream);
        pdf.append("endstream\n");
        pdf.append("endobj\n\n");
        
        // Font objects
        pdf.append("5 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type1\n");
        pdf.append("/BaseFont /Helvetica-Bold\n");
        pdf.append(">>\n");
        pdf.append("endobj\n\n");
        
        pdf.append("6 0 obj\n");
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type1\n");
        pdf.append("/BaseFont /Helvetica\n");
        pdf.append(">>\n");
        pdf.append("endobj\n\n");
        
        // Cross-reference table
        pdf.append("xref\n");
        pdf.append("0 7\n");
        pdf.append("0000000000 65535 f \n");
        pdf.append("0000000010 65535 n \n");
        pdf.append("0000000079 65535 n \n");
        pdf.append("0000000173 65535 n \n");
        pdf.append("0000000301 65535 n \n");
        pdf.append("0000000380 65535 n \n");
        pdf.append("0000000484 65535 n \n");
        
        // Trailer
        pdf.append("trailer\n");
        pdf.append("<<\n");
        pdf.append("/Size 7\n");
        pdf.append("/Root 1 0 R\n");
        pdf.append(">>\n");
        pdf.append("startxref\n");
        pdf.append("565\n");
        pdf.append("%%EOF\n");
        
        return pdf.toString();
    }
    
    private static String generateContentStream(Ticket ticket, Train train) {
        StringBuilder content = new StringBuilder();
        
        // Draw border and background similar to ATVM ticket
        content.append("q\n"); // Save graphics state
        
        // Orange header background (like ATVM ticket)
        content.append("1 0.5 0 rg\n"); // Orange color
        content.append("0 150 288 30 re\n"); // Header rectangle
        content.append("f\n"); // Fill
        
        // White background for main content
        content.append("1 1 1 rg\n"); // White
        content.append("0 0 288 150 re\n"); // Main content area
        content.append("f\n"); // Fill
        
        // Border
        content.append("0 0 0 RG\n"); // Black stroke color
        content.append("1 w\n"); // Line width
        content.append("0 0 288 180 re\n"); // Rectangle border
        content.append("S\n"); // Stroke
        
        content.append("Q\n"); // Restore graphics state
        
        content.append("BT\n");
        
        // Header - White text on orange background
        content.append("1 1 1 rg\n"); // White color
        content.append("/F1 9 Tf\n"); // Smaller font
        content.append("5 162 Td\n"); // Adjusted position
        content.append("(TAMIL NADU RAILWAY) Tj\n");
        
        content.append("100 0 Td\n"); // Move right for PNR
        content.append("/F2 8 Tf\n"); // Smaller font for PNR
        content.append("(PNR: TN").append(String.format("%010d", ticket.getTicketId())).append(") Tj\n");
        
        // Journey type and UTS number
        content.append("-100 -15 Td\n"); // Move back to left and down
        content.append("0 0 0 rg\n"); // Black text
        content.append("/F2 7 Tf\n");
        content.append("(JOURNEY TICKET) Tj\n");
        
        content.append("120 0 Td\n"); // Move right
        content.append("(UTS NO: TN").append(String.format("%06d", ticket.getTicketId())).append(") Tj\n");
        
        // Route information - Black text
        content.append("-120 -12 Td\n"); // Move back to left and down
        content.append("/F1 8 Tf\n");
        content.append("(").append(train.getSource().toUpperCase()).append(" TO ").append(train.getDestination().toUpperCase()).append(") Tj\n");
        
        // Train details
        content.append("0 -11 Td\n");
        content.append("/F2 6 Tf\n"); // Smaller font
        content.append("(Train: ").append(train.getTrainId()).append("/").append(train.getTrainName()).append(") Tj\n");
        
        // Passenger info - split into two lines if name is long
        content.append("0 -10 Td\n");
        String passengerName = ticket.getPassengerName();
        if (passengerName.length() > 25) {
            content.append("(Passenger: ").append(passengerName.substring(0, 25)).append(") Tj\n");
            content.append("0 -8 Td\n");
            content.append("(").append(passengerName.substring(25)).append(") Tj\n");
        } else {
            content.append("(Passenger: ").append(passengerName).append(") Tj\n");
            content.append("0 -8 Td\n"); // Extra space for consistency
        }
        
        // Seat and class info
        content.append("0 -9 Td\n");
        content.append("(Seat: ").append(ticket.getSeatNumber()).append("  Class: GENERAL) Tj\n");
        
        // Fare and timing on same line
        content.append("0.8 0 0 rg\n"); // Red for fare
        content.append("0 -10 Td\n");
        content.append("/F1 7 Tf\n");
        content.append("(Fare: Rs. ").append(String.format("%.2f", ticket.getFare())).append(") Tj\n");
        
        content.append("0 0 0 rg\n"); // Black
        content.append("80 0 Td\n"); // Move right
        content.append("(Dep: ").append(train.getDepartureTime()).append(") Tj\n");
        
        // Date and status on same line
        content.append("-80 -9 Td\n"); // Move back to left and down
        content.append("/F2 6 Tf\n");
        content.append("(Date: ").append(ticket.getBookingTime().toLocalDate().toString()).append(") Tj\n");
        
        // Status with color
        if ("BOOKED".equals(ticket.getStatus())) {
            content.append("0 0.6 0 rg\n"); // Green for BOOKED
        } else {
            content.append("0.8 0 0 rg\n"); // Red for CANCELLED
        }
        content.append("80 0 Td\n"); // Move right
        content.append("(Status: ").append(ticket.getStatus()).append(") Tj\n");
        
        // Transaction details
        content.append("0 0 0 rg\n"); // Black
        content.append("-80 -9 Td\n"); // Move back to left and down
        content.append("(TXN: TN").append(ticket.getBookingTime().toLocalDate().toString().replace("-", "")).append(String.format("%06d", ticket.getTicketId())).append(") Tj\n");
        
        // Phone number
        content.append("0 -8 Td\n");
        content.append("(Contact: ").append(ticket.getPassengerPhone()).append(") Tj\n");
        
        // Footer - Important note
        content.append("0.4 0.4 0.4 rg\n"); // Gray
        content.append("0 -9 Td\n");
        content.append("/F2 5 Tf\n"); // Very small font
        content.append("(Carry valid ID proof. Report 30 min before departure.) Tj\n");
        
        // Happy Journey
        content.append("0 0.5 1 rg\n"); // Blue
        content.append("0 -7 Td\n");
        content.append("/F1 6 Tf\n");
        content.append("(*** HAPPY JOURNEY - TAMIL NADU RAILWAY ***) Tj\n");
        
        content.append("ET\n");
        
        return content.toString();
    }
}