
package utilities;

import java.io.Serializable;

public class Bill implements Serializable {

    private static final long serialVersionUID = 6529685098267757698L;
    private int Bill_ID;
    private double Amount_toPay;

    public Bill() {
        this.Bill_ID = 0;
        this.Amount_toPay = 0.00;
    }

    public Bill(int Bill_ID, double Amount_toPay) {
        this.Bill_ID = Bill_ID;
        this.Amount_toPay = Amount_toPay;
    }

    // Getter Methods

    public int getBill_ID(){
        return Bill_ID;
    }

    public double getAmount_toPay() {
        return Amount_toPay;
    }

    // Setter Methods

    public void setBill_ID(int Bill_ID){
        this.Bill_ID = Bill_ID;
    }

    public void setAmount_toPay(double amount_toPay) {
        Amount_toPay = amount_toPay;
    }

}
