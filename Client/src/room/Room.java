
package room;

import java.io.Serializable;

public abstract class Room implements Serializable {

    private static final long serialVersionUID = 6529685098267757691L;

    private int Room_ID;
    private String Room_Type;
    private double Rate;
    private boolean availability;

    Room() {
        this.Room_ID = 0;
        this.Room_Type = "";
        this.Rate = 0.00;
        this.availability = true;
    }

    Room(int Room_ID, String Room_Type, double Rate) {
        this.Room_ID = Room_ID;
        this.Room_Type = Room_Type;
        this.Rate = Rate;
        this.availability = true;
    }

    abstract public String toString();

    // Getter Methods

    public int getRoom_ID(){
        return Room_ID;
    }

    public String getRoom_Type() {
        return Room_Type;
    }

    public double getRate(){
        return Rate;
    }

    public boolean getAvailability() {
        return availability;
    }

    // setter Methods

    public void setRoom_ID(int room_ID) {
        Room_ID = room_ID;
    }

    public void setRoom_Type(String room_Type) {
        Room_Type = room_Type;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}


