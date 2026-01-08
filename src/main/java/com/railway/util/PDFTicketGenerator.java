package com.railway.util;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.railway.model.Ticket;
import com.railway.model.Train;

public class PDFTicketGenerator {

    private static final float PAGE_WIDTH = 432;
    private static final float PAGE_HEIGHT = 288;

    public static boolean generateTicketPDF(Ticket ticket, Train train, String outputFile) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT));
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                float y = PAGE_HEIGHT - 30;

                // ===== TITLE =====
                write(cs, PDType1Font.HELVETICA_BOLD, 16, 20, y, "INDIAN RAILWAYS - E TICKET");
                y -= 30;

                // ===== TICKET INFO =====
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "PNR Number : " + ticket.getTicketId());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Passenger  : " + ticket.getPassengerName());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Seat No    : " + ticket.getSeatNumber());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Status     : " + ticket.getStatus());
                y -= 25;

                // ===== TRAIN DETAILS =====
                write(cs, PDType1Font.HELVETICA_BOLD, 12, 20, y, "Train Details");
                y -= 18;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Train No   : " + train.getTrainId());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Train Name : " + train.getTrainName());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "From       : " + train.getSource());
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "To         : " + train.getDestination());
                y -= 25;

                // ===== DATE & FARE =====
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Journey Date : " +
                        ticket.getBookingTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                y -= 15;
                write(cs, PDType1Font.HELVETICA, 11, 20, y, "Fare         : Rs. " + ticket.getFare());

                // ===== FOOTER =====
                write(cs, PDType1Font.HELVETICA_OBLIQUE, 10, 20, 30,
                        "Please carry a valid photo ID. Safe Journey!");
            }

            document.save(outputFile);
            System.out.println("PDF generated successfully: " + outputFile);
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