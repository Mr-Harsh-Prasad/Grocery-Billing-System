package Application.models;

public class BillItem {

    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double discount;
    private double gst;

    public BillItem(
            int productId,
            String productName,
            int quantity,
            double unitPrice,
            double discount,
            double gst) {

        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.gst = gst;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getGst() {
        return quantity * unitPrice * 0.05;
    }

    public double getTotal() {
        double subtotal = quantity * unitPrice;
        double taxableAmount = subtotal - discount;
        return taxableAmount + getGst();
    }
}