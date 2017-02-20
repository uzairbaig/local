package co.uk.silverbars.domain;

public class OrderSummary {

    private Double totalQuantity;

    private Double totalPrice;

    private OrderType orderType;

    public OrderSummary(Double totalQuantity, Double totalPrice, OrderType orderType) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.orderType = orderType;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public String toString() {
        return String.format("%.1f kg for £%.0f", totalQuantity, totalPrice);
    }
}
