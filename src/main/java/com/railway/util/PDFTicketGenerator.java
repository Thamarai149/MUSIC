package com.railway.util;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.railway.model.Ticket;
import com.railway.model.Train;

public class PDFTicketGenerator {

    private static final float PAGE_WIDTH = 595;   // A4 width (original size)
    private static final float PAGE_HEIGHT = 842;  // A4 height (original size)

    public static boolean generateTicketPDF(Ticket ticket, Train train, String outputFile) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT));
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                
                // ===== ADD IRCTC LOGO BACKGROUND =====
                try {
                    File logoFile = new File("src/irctc.png");
                    if (logoFile.exists()) {
                        PDImageXObject logoImage = PDImageXObject.createFromFile("src/irctc.png", document);
                        
                        // Add logo as watermark in background (center, large and light)
                        float logoWidth = 150;
                        float logoHeight = 150;
                        float logoX = (PAGE_WIDTH - logoWidth) / 2;
                        float logoY = (PAGE_HEIGHT - logoHeight) / 2;
                        
                        // Draw large background logo (watermark effect)
                        cs.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight);
                        
                        // Add small logo in header (top right)
                        cs.drawImage(logoImage, 450, PAGE_HEIGHT - 80, 50, 50);
                    }
                } catch (IOException e) {
                    System.out.println("Warning: Could not load IRCTC logo - " + e.getMessage());
                }
                
                float y = PAGE_HEIGHT - 50;

                // ===== HEADER =====
                write(cs, PDType1Font.HELVETICA_BOLD, 20, 50, y, "INDIAN RAILWAYS");
                write(cs, PDType1Font.HELVETICA_BOLD, 16, 350, y, "E-TICKET");
                y -= 30;
                
                // Divider line
                cs.moveTo(50, y);
                cs.lineTo(PAGE_WIDTH - 50, y);
                cs.stroke();
                y -= 40;

                // ===== TICKET INFO SECTION =====
                write(cs, PDType1Font.HELVETICA_BOLD, 14, 50, y, "PASSENGER DETAILS");
                y -= 25;
                
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "PNR Number : " + ticket.getTicketId());
                y -= 20;
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Passenger Name : " + ticket.getPassengerName());
                y -= 20;
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Seat Number : " + ticket.getSeatNumber());
                y -= 20;
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Booking Status : " + ticket.getStatus());
                y -= 40;

                // ===== TRAIN DETAILS SECTION =====
                write(cs, PDType1Font.HELVETICA_BOLD, 14, 50, y, "TRAIN DETAILS");
                y -= 25;
                
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Train Number : " + train.getTrainId());
                write(cs, PDType1Font.HELVETICA, 12, 300, y, "Train Name : " + train.getTrainName());
                y -= 20;
                
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "From Station : " + train.getSource());
                write(cs, PDType1Font.HELVETICA, 12, 300, y, "To Station : " + train.getDestination());
                y -= 20;
                
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Departure Time : " + train.getDepartureTime());
                write(cs, PDType1Font.HELVETICA, 12, 300, y, "Arrival Time : " + train.getArrivalTime());
                y -= 40;

                // ===== JOURNEY & FARE SECTION =====
                write(cs, PDType1Font.HELVETICA_BOLD, 14, 50, y, "JOURNEY & FARE DETAILS");
                y -= 25;
                
                write(cs, PDType1Font.HELVETICA, 12, 50, y, "Journey Date : " +
                        ticket.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                write(cs, PDType1Font.HELVETICA, 12, 300, y, "Booking Date : " +
                        ticket.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                y -= 20;
                
                write(cs, PDType1Font.HELVETICA_BOLD, 12, 50, y, "Total Fare : Rs. " + ticket.getFare());
                y -= 60;

                // ===== IMPORTANT INSTRUCTIONS =====
                write(cs, PDType1Font.HELVETICA_BOLD, 12, 50, y, "IMPORTANT INSTRUCTIONS:");
                y -= 20;
                write(cs, PDType1Font.HELVETICA, 10, 50, y, "• Please carry a valid photo ID proof during journey");
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 10, 50, y, "• Report at the station at least 30 minutes before departure");
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 10, 50, y, "• This ticket is valid only for the date and train mentioned above");
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 10, 50, y, "• Smoking and consumption of alcohol is prohibited in trains");
                y -= 40;

                // ===== FOOTER =====
                // Bottom divider line
                cs.moveTo(50, 100);
                cs.lineTo(PAGE_WIDTH - 50, 100);
                cs.stroke();
                
                write(cs, PDType1Font.HELVETICA_OBLIQUE, 10, 50, 80,
                        "For any queries, visit www.irctc.co.in | Customer Care: 139");
                write(cs, PDType1Font.HELVETICA_BOLD, 10, 400, 80, "SAFE JOURNEY!");
            }

            document.save(outputFile);
            System.out.println("Original size PDF with IRCTC logo generated successfully: " + outputFile);
            return true;

        } catch (IOException e) {
            System.err.println("PDF generation failed: " + e.getMessage());
            return false;
        }
    }

    // ===== Utility method for safe text writing =====
    private static void write(PDPageContentStream cs, PDType1Font font, int fontSize,
                            float x, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }
}