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

    // ===== THERMAL PRINTER SIZE (80mm) =====
    private static final float PAGE_WIDTH = 226;   // 80mm
    private static final float PAGE_HEIGHT = 1000; // large height (auto cut)

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
                        
                        // Add logo as watermark in background (center, light)
                        float logoWidth = 80;
                        float logoHeight = 80;
                        float logoX = (PAGE_WIDTH - logoWidth) / 2;
                        float logoY = PAGE_HEIGHT / 2;
                        
                        // Draw background logo (watermark effect)
                        cs.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight);
                        
                        // Add small logo in header
                        cs.drawImage(logoImage, PAGE_WIDTH - 35, PAGE_HEIGHT - 50, 25, 25);
                    }
                } catch (IOException e) {
                    System.out.println("Warning: Could not load IRCTC logo - " + e.getMessage());
                }
                
                float y = PAGE_HEIGHT - 20;

                // ===== HEADER =====
                writeCentered(cs, PDType1Font.HELVETICA_BOLD, 14, y, "INDIAN RAILWAYS");
                y -= 18;
                writeCentered(cs, PDType1Font.HELVETICA, 9, y, "ELECTRONIC TRAIN TICKET");
                y -= 20;
                drawLine(cs, y);
                y -= 15;

                // ===== TICKET DETAILS =====
                write(cs, 10, y, "PNR : " + (ticket.getPnrNumber() != null ? ticket.getPnrNumber() : ticket.getTicketId())); 
                y -= 14;
                write(cs, 10, y, "Status : " + ticket.getStatus()); 
                y -= 14;
                drawLine(cs, y);
                y -= 15;

                // ===== PASSENGER =====
                writeAutoScale(cs, PDType1Font.HELVETICA_BOLD, 11, 8, 10, y,
                        "Passenger: " + ticket.getPassengerName());
                y -= 14;
                write(cs, 10, y, "Age: " + ticket.getPassengerAge() + " | Gender: " + ticket.getPassengerGender());
                y -= 14;
                write(cs, 10, y, "Seat: " + ticket.getCoachNumber() + "-" + ticket.getSeatNumber());
                y -= 14;
                write(cs, 10, y, "Class: " + ticket.getTicketClass() + " | " + ticket.getBerthType());
                y -= 14;
                drawLine(cs, y);
                y -= 15;

                // ===== TRAIN DETAILS =====
                writeAutoScale(cs, PDType1Font.HELVETICA_BOLD, 11, 8, 10, y,
                        train.getSource() + " TO " + train.getDestination());
                y -= 14;
                writeAutoScale(cs, PDType1Font.HELVETICA, 10, 8, 10, y,
                        "Train: " + train.getTrainId() + " - " + train.getTrainName());
                y -= 14;
                write(cs, 10, y, "Journey: " + (ticket.getJourneyDate() != null ? 
                        ticket.getJourneyDate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")) :
                        ticket.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                y -= 14;
                write(cs, 10, y, "Base: Rs." + ticket.getBaseFare() + " + Tax: Rs." + ticket.getTaxes());
                y -= 14;
                write(cs, 10, y, "Total Fare: Rs. " + ticket.getTotalFare());
                y -= 20;
                drawLine(cs, y);
                y -= 18;

                // ===== FOOTER =====
                writeCentered(cs, PDType1Font.HELVETICA_OBLIQUE, 9, y, "Carry " + ticket.getIdProofType() + " ID");
                y -= 12;
                writeCentered(cs, PDType1Font.HELVETICA_OBLIQUE, 9, y, "SAFE JOURNEY");
                y -= 12;
                writeCentered(cs, PDType1Font.HELVETICA, 7, y, "Booked via " + ticket.getBookingSource());
                y -= 10;
                writeCentered(cs, PDType1Font.HELVETICA, 7, y, "IRCTC - Indian Railways");
            }

            document.save(outputFile);
            System.out.println("Thermal Ticket PDF with IRCTC logo generated: " + outputFile);
            return true;

        } catch (IOException e) {
            System.err.println("PDF generation failed: " + e.getMessage());
            return false;
        }
    }

    // ===== BASIC WRITE =====
    private static void write(PDPageContentStream cs, int fontSize, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, fontSize);
        cs.newLineAtOffset(10, y);
        cs.showText(text);
        cs.endText();
    }

    // ===== CENTER ALIGN =====
    private static void writeCentered(PDPageContentStream cs, PDType1Font font, int fontSize, 
                                    float y, String text) throws IOException {
        float textWidth = (font.getStringWidth(text) / 1000) * fontSize;
        float x = (PAGE_WIDTH - textWidth) / 2;
        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    // ===== AUTO SCALE TEXT =====
    private static void writeAutoScale(PDPageContentStream cs, PDType1Font font, float maxSize, 
                                     float minSize, float x, float y, String text) throws IOException {
        float size = maxSize;
        float maxWidth = PAGE_WIDTH - 20;
        
        while (size > minSize) {
            float width = (font.getStringWidth(text) / 1000) * size;
            if (width <= maxWidth) break;
            size -= 0.5f;
        }
        
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    // ===== SEPARATOR LINE =====
    private static void drawLine(PDPageContentStream cs, float y) throws IOException {
        cs.moveTo(10, y);
        cs.lineTo(PAGE_WIDTH - 10, y);
        cs.stroke();
    }
}