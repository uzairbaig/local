package co.uk.silverbars.service;

import co.uk.silverbars.domain.Order;

import java.util.List;

public class OrderBoardRepository {

    private final List<Order> liveOrders;

    public OrderBoardRepository(List<Order> liveOrders) {
        this.liveOrders = liveOrders;
    }

    public void addOrderToBoard(final Order order) {
        liveOrders.add(order);
    }

    public void removeOrderFromBoard(final Order order) {
        liveOrders.remove(order);
    }

    public boolean isOrderExists(final Order order) {
        return liveOrders.contains(order);
    }

    public List<Order> getLiveOrders() {
        return liveOrders;
    }
}

