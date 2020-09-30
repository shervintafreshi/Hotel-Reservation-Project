package room;

import java.io.Serializable;

public class DeluxRoom extends Room implements Serializable {

    private static final long serialVersionUID = 6529685098267757693L;
    static final String roomType = "Delux";
    static final double rate = 250.00;

    public DeluxRoom() {
        super();
    }

    public DeluxRoom(int Room_ID) {
        super(Room_ID, roomType, rate);
    }

    public String toString() {
        return roomType + " Room";
    }

}
