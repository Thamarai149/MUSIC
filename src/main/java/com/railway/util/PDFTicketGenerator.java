package com.railway.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.railway.model.Ticket;
import com.railway.model.Train;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFTicketGenerator {
    
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(0, 102, 204);
    private static final DeviceRgb LIGHT_BLUE = new DeviceRgb(230, 240, 255);
    
    public static boolean generateTicketPDF(Ticket ticket, Train train, String outputPath) {
        try {
            // Create directory if it doesn't exist
            File file = new File(outputPath);
            file.getParentFile().mkdirs();
            
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Header
            addHeader(document);
            
            // Journey Information
            addJourneyInfo(document, ticket, train);
            
            // Passenger Details
            addPassengerDetails(document, ticket);
            
            // Payment Details
            addPaymentDetails(document, ticket);
            
            // Footer with Instructions
            addFooter(document, ticket);
            
            document.close();
            return true;
            
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return false;
        }
    }
    
    private static void addHeader(Document document) {
        // Main Header
        Paragraph header = new Paragraph("TAMIL NADU RAILWAY RESERVATION SYSTEM")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR);
        document.add(header);
        
        Paragraph subHeader = new Paragraph("Electronic Reservation Slip (ERS)")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY);
        document.add(subHeader);
        
        document.add(new Paragraph("\n"));
    }
    
    private static void addJourneyInfo(Document document, Ticket ticket, Train train) {
        // Journey Details Table
        Table journeyTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        
        // Header row
        journeyTable.addHeaderCell(createHeaderCell("From"));
        journeyTable.addHeaderCell(createHeaderCell("To"));
        journeyTable.addHeaderCell(createHeaderCell("Class"));
        
        // Data row
        journeyTable.addCell(createDataCell(train.getSource().toUpperCase()));
        journeyTable.addCell(createDataCell(train.getDestination().toUpperCase()));
        journeyTable.addCell(createDataCell("GENERAL"));
        
        document.add(journeyTable);
        document.add(new Paragraph("\n"));
        
        // Train and Time Details
        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        
        detailsTable.addHeaderCell(createHeaderCell("Train No./Name"));
        detailsTable.addHeaderCell(createHeaderCell("Departure"));
        detailsTable.addHeaderCell(createHeaderCell("Arrival"));
        detailsTable.addHeaderCell(createHeaderCell("Date"));
        
        detailsTable.addCell(createDataCell(train.getTrainId() + " / " + train.getTrainName()));
        detailsTable.addCell(createDataCell(train.getDepartureTime().toString()));
        detailsTable.addCell(createDataCell(train.getArrivalTime().toString()));
        detailsTable.addCell(createDataCell(ticket.getBookingTime().toLocalDate().toString()));
        
        document.add(detailsTable);
        document.add(new Paragraph("\n"));
    }
    
    private static void addPassengerDetails(Document document, Ticket ticket) {
        Paragraph passengerHeader = new Paragraph("PASSENGER DETAILS")
                .setFontSize(14)
                .setBold()
                .setFontColor(HEADER_COLOR);
        document.add(passengerHeader);
        
        Table passengerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        
        passengerTable.addHeaderCell(createHeaderCell("Name"));
        passengerTable.addHeaderCell(createHeaderCell("Email"));
        passengerTable.addHeaderCell(createHeaderCell("Phone"));
        passengerTable.addHeaderCell(createHeaderCell("Seat No."));
        
        passengerTable.addCell(createDataCell(ticket.getPassengerName()));
        passengerTable.addCell(createDataCell(ticket.getPassengerEmail()));
        passengerTable.addCell(createDataCell(ticket.getPassengerPhone()));
        passengerTable.addCell(createDataCell(String.valueOf(ticket.getSeatNumber())));
        
        document.add(passengerTable);
        document.add(new Paragraph("\n"));
    }
    
    private static void addPaymentDetails(Document document, Ticket ticket) {
        Paragraph paymentHeader = new Paragraph("PAYMENT DETAILS")
                .setFontSize(14)
                .setBold()
                .setFontColor(HEADER_COLOR);
        document.add(paymentHeader);
        
        Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        
        paymentTable.addCell(createDataCell("Ticket Fare"));
        paymentTable.addCell(createDataCell("Rs. " + String.format("%.2f", ticket.getFare())));
        
        paymentTable.addCell(createDataCell("Convenience Fee"));
        paymentTable.addCell(createDataCell("Rs. 0.00"));
        
        paymentTable.addCell(createHeaderCell("Total Amount"));
        paymentTable.addCell(createHeaderCell("Rs. " + String.format("%.2f", ticket.getFare())));
        
        document.add(paymentTable);
        document.add(new Paragraph("\n"));
        
        // PNR and Transaction Details
        Table transactionTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        
        transactionTable.addCell(createDataCell("PNR Number"));
        transactionTable.addCell(createDataCell("TN" + String.format("%010d", ticket.getTicketId())));
        
        transactionTable.addCell(createDataCell("Transaction ID"));
        transactionTable.addCell(createDataCell("TN" + 
                ticket.getBookingTime().toLocalDate().toString().replace("-", "") + 
                String.format("%010d", ticket.getTicketId())));
        
        transactionTable.addCell(createDataCell("Booking Time"));
        transactionTable.addCell(createDataCell(ticket.getBookingTime().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        
        transactionTable.addCell(createDataCell("Status"));
        transactionTable.addCell(createDataCell(ticket.getStatus()));
        
        document.add(transactionTable);
        document.add(new Paragraph("\n"));
    }
    
    private static void addFooter(Document document, Ticket ticket) {
        Paragraph instructionsHeader = new Paragraph("IMPORTANT INSTRUCTIONS")
                .setFontSize(12)
                .setBold()
                .setFontColor(HEADER_COLOR);
        document.add(instructionsHeader);
        
        String[] instructions = {
            "• Please carry a valid photo ID proof during journey",
            "• Ticket is valid only for the specified train and date",
            "• Report to station at least 30 minutes before departure",
            "• This is a computer generated ticket and does not require signature",
            "• Keep this ticket safe until the end of your journey"
        };
        
        for (String instruction : instructions) {
            Paragraph p = new Paragraph(instruction)
                    .setFontSize(10)
                    .setMarginLeft(10);
            document.add(p);
        }
        
        document.add(new Paragraph("\n"));
        
        Paragraph footer = new Paragraph("TAMIL NADU RAILWAY - SAFE & COMFORTABLE JOURNEY")
                .setFontSize(12)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR);
        document.add(footer);
        
        Paragraph happyJourney = new Paragraph("*** HAPPY JOURNEY ***")
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GREEN);
        document.add(happyJourney);
    }
    
    private static Cell createHeaderCell(String content) {
        return new Cell()
                .add(new Paragraph(content).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setPadding(8);
    }
    
    private static Cell createDataCell(String content) {
        return new Cell()
                .add(new Paragraph(content))
                .setBackgroundColor(LIGHT_BLUE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER)
                .setPadding(6);
    }
}