
package guest;

import java.io.Serializable;

public class Guest implements Serializable {

    private static final long serialVersionUID = 6529685098267757696L;
    private int Guest_ID;
    private String Title;
    private String First_name;
    private String Last_name;
    private String Address;
    private long Phone;
    private String Email;

    public Guest() {
        this.Guest_ID = 0;
        this.Phone = 0;
        this.Title = null;
        this.First_name = null;
        this.Last_name = null;
        this.Address = null;
        this.Email = null;
    }

    public Guest(int Guest_ID, String Title, String First_name, String Last_name, String Address, long Phone, String Email) {
        this.Guest_ID = Guest_ID;
        this.Title = Title;
        this.First_name = First_name;
        this.Last_name = Last_name;
        this.Address = Address;
        this.Phone = Phone;
        this.Email = Email;
    }

    // Getter Methods

    public int getGuest_ID() {
        return Guest_ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getFirst_name() {
        return First_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public String getAddress() {
        return Address;
    }

    public long getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    // Setter Methods

    public void setGuest_ID(int guest_ID) {
        Guest_ID = guest_ID;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPhone(long phone) {
        Phone = phone;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
