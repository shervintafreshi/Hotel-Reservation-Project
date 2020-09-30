package room;

import java.io.Serializable;

public class SingleRoom extends Room implements Serializable {

    private static final long serialVersionUID = 6529685098267757694L;
    static final String roomType = "Single";
    static final double rate = 120.00;

    public SingleRoom() {
        super();
    }

    public SingleRoom(int Room_ID) {
        super(Room_ID, roomType,rate);
    }

    public String toString() {
        return roomType + " Room";
    }
}
