
package utilities;

import guest.Guest;
import room.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Reservation implements Serializable {

    private static final long serialVersionUID = 6529685098267757697L;
    private int Book_ID;
    private int Booking_Period;
    private Date Book_date;
    private Date Check_in;
    private Date Check_out;
    private Guest client;
    private ArrayList<Room> rooms;
    private Bill bill;

    public Reservation() {
        this.Book_ID = 0;
        this.Booking_Period = 0;
        this.Book_date = null;
        this.Check_in = null;
        this.Check_out = null;
        this.client = null;
        this.rooms = null;
        this.bill = null;
    }

    public Reservation(int Book_ID, int Booking_Period, Date Book_date, Date Check_in, Date Check_out) {
        this.Book_ID = Book_ID;
        this.Booking_Period = Booking_Period;
        this.Book_date = Book_date;
        this.Check_in = Check_in;
        this.Check_out = Check_out;
        this.client = null;
        this.rooms = null;
        this.bill = null;
    }

// Getter Methods

    public int getBook_ID() { return Book_ID; }

    public int getBooking_Period() { return Booking_Period; }

    public Date getBook_date() {
        return Book_date;
    }

    public Date getCheck_in() {
        return Check_in;
    }

    public Date getCheck_out() {
        return Check_out;
    }

    public Guest getClient() {
        return client;
    }

    public ArrayList<Room> getRooms() { return rooms; }

    public Bill getBill() { return bill; }

    // Setter Methods

    public void setBook_ID(int book_ID) {
        Book_ID = book_ID;
    }

    public void setBooking_Period(int booking_Period) { Booking_Period = booking_Period; }

    public void setBook_date(Date book_date) { Book_date = book_date; }

    public void setCheck_in(Date check_in) {
        Check_in = check_in;
    }

    public void setCheck_out(Date check_out) {
        Check_out = check_out;
    }

    public void setClient(Guest client) {
        this.client = client;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public void setBill(Bill bill) { this.bill = bill; }
}
