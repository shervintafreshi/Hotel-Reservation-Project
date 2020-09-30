
package room;

public class PentHouse extends Room {

    private static final long serialVersionUID = 6529685098267757695L;
    static final String roomType = "Pent House";
    static final double rate = 180.00;

    public PentHouse() {
        super();
    }

    public PentHouse(int Room_ID) {
        super(Room_ID, roomType, rate);
    }

    public String toString() {
        return roomType + " Room";
    }

}
