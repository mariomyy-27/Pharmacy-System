package sample.model;

public class Sale {

    private String medicineName;
    private double price;
    private int amountSold;

    public Sale(String medicineName, double price, int amountSold) {
        this.medicineName = medicineName;
        this.price = price;
        this.amountSold = amountSold;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public double getTotalPrice() {
        return price * amountSold;
    }
}