package de.bxservice.bxpos.logic.print;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.bxservice.bxpos.logic.model.pos.POSOrder;
import de.bxservice.bxpos.logic.model.pos.POSOrderLine;

/**
 * Created by Diego Ruiz on 21/04/16.
 */
public class CPCLPrinter extends AbstractPOSPrinter {

    public CPCLPrinter(POSOrder order) {
        super(order);
    }


    @Override
    public String print() {

        /*return String.format("! 0 200 200 600 1\r\n" +
                "PW 550\r\n" +
                "TONE 0\r\n" +
                "SPEED 2\r\n" +
                "ON-FEED IGNORE\r\n" +
                "NO-PACE\r\n" +
                "POSTFEED 50\r\n" +
                "JOURNAL\r\n" +
                //"BOX 0 0 376 797 5\r\n" +
                "T 5 1 25 20 Type:\r\n" +
                "T 5 1 90 20 %s\r\n" +
                "T 5 1 25 75 Server:\r\n" +
                "T 5 1 110 75 %s\r\n" +
                "T 5 1 25 130 Guest:\r\n" +
                "T 5 1 100 130 %s\r\n" +
                "T 5 1 300 20 Table\r\n" +
                "T 5 3 300 75 %s\r\n" +
                "LINE 25 175 530 175 1\r\n" +
                "T 5 0 35 421 Address:\r\n" +
                "T 5 0 35 342 Last Name:\r\n" +
                "T 5 0 35 257 First Name:\r\n" +
                "T 5 0 35 175 Plate #:\r\n" +
                "T 5 0 64 290 %s\r\n" +
                "T 5 0 64 374 %s\r\n" +
                "T 5 0 64 466 %s\r\n" +
                "T 5 0 64 558 %s\r\n" +
                "FORM \r\n\r\n"+
                "PRINT\r\n", new Object[] { "Dine-in", "Garden", "5", "Table 1","Carlos", "Ruiz", "AAA 2356", "None" });

                /*return "! U1 JOURNAL\r\n" +
                "! U1 SETLP 5 2 24\r\n" +
                "Order #: "+ order.getOrderId() +"\r\n" +
                "Type: Dine-in\r\n" +
                "Server: Garden\r\n" +
                "Guests: "+ order.getGuestNumber() +"\r\n" +
                //"! U1 LINE 25 175 530 175 1\r\n" +
                "! U1 SETLP 5 0 24\r\n" +

                "! U1 SETLP 7 0 24\r\n" +
                order.getOrderedLines().get(0).getQtyOrdered() + "  " + order.getOrderedLines().get(0).getProduct().getProductName() + "\r\n" +
                order.getOrderedLines().get(1).getQtyOrdered() + "  " + order.getOrderedLines().get(1).getProduct().getProductName() + "\r\n" +
                "! U1 PRESENT-AT\r\n" +
                "! U1 PRINT\r\n";*/

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        StringBuilder ticket = new StringBuilder();

        ticket.append("! U1 JOURNAL\r\n");
        ticket.append("! U1 SETLP 5 3 86\r\n");
        ticket.append("\r\n");
        ticket.append("Order #: "+ order.getOrderId() +"\r\n");
        ticket.append( "! U1 SETLP 7 0 24\r\n");
        ticket.append( "Type: Dine-in\r\n");
        ticket.append("Server: Garden\r\n");
        ticket.append( "Guests: "+ order.getGuestNumber() +"\r\n");
        ticket.append("\r\n");
        ticket.append( "! U1 SETLP 5 2 46\r\n");
        for(POSOrderLine line : order.getOrderedLines())
            ticket.append(line.getQtyOrdered() + "  " + line.getProduct().getProductName() + "\r\n");
        ticket.append("\r\n");
        ticket.append( "! U1 SETLP 7 0 24\r\n");
        ticket.append(dateFormat.format(cal.getTime())+"\r\n");//2014/08/06 16:00:22
        ticket.append("! U1 PRESENT-AT\r\n");
        ticket.append("! U1 PRINT\r\n");

        return ticket.toString();

    }

    public void sendCommand(String paramString) {

    }

    @Override
    public String printKitchen() {
        return null;
    }

    @Override
    public String printBar() {
        return null;
    }

    @Override
    public String printReceipt() {
        return null;
    }
}
