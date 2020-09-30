
package room;

import java.io.Serializable;

public class DoubleRoom extends Room implements Serializable {

    private static final long serialVersionUID = 6529685098267757692L;
    static final String roomType = "Double";
    static final double rate = 180.00;

    public DoubleRoom() {
        super();
    }

    public DoubleRoom(int Room_ID) {
        super(Room_ID, roomType, rate);
    }

    public String toString() {
        return roomType + " Room";
    }

}

