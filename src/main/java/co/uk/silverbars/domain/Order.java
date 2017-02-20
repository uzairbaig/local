package co.uk.silverbars.domain;

import org.apache.commons.lang3.StringUtils;

public class Order {

    private String userId;

    private Double quantityInKg;

    private Double pricePerKg;

    private OrderType orderType;

    public Order(String userId, Double quantity, Double pricePerUnit, OrderType orderType) {
        this.userId = userId;
        this.quantityInKg = quantity;
        this.pricePerKg = pricePerUnit;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public Double getQuantityInKg() {
        return quantityInKg;
    }

    public Double getPricePerKg() {
        return pricePerKg;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Double getTotalCost() {
        return this.quantityInKg * this.getPricePerKg();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (userId != null ? !userId.equals(order.userId) : order.userId != null) return false;
        if (quantityInKg != null ? !quantityInKg.equals(order.quantityInKg) : order.quantityInKg != null) return false;
        if (pricePerKg != null ? !pricePerKg.equals(order.pricePerKg) : order.pricePerKg != null) return false;
        return orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (quantityInKg != null ? quantityInKg.hashCode() : 0);
        result = 31 * result + (pricePerKg != null ? pricePerKg.hashCode() : 0);
        result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
        return result;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(userId) && quantityInKg != null && pricePerKg != null && orderType != null;
    }
}
