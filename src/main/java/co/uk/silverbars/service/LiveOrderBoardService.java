package co.uk.silverbars.service;

import co.uk.silverbars.domain.Order;
import co.uk.silverbars.domain.OrderSummary;
import co.uk.silverbars.domain.OrderType;
import co.uk.silverbars.exception.OrderException;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;

public class LiveOrderBoardService {

    private final OrderBoardRepository orderBoardRepository;

    public LiveOrderBoardService(final OrderBoardRepository orderBoardRepository) {
        this.orderBoardRepository = orderBoardRepository;
    }

    public void createAnOrder(final Order order) {
        if (order == null || !order.isValid()) {
            throw new OrderException("Invalid order request");
        }
        orderBoardRepository.addOrderToBoard(order);
    }

    public void cancelOrder(final Order order) {
        if (orderBoardRepository.isOrderExists(order)) {
            orderBoardRepository.removeOrderFromBoard(order);
        } else {
           throw new OrderException("Order not exists on live order board");
        }
    }

    public String getOrderSummary() {

        final List<Order> liveOrders = orderBoardRepository.getLiveOrders();

        final Map<Double, OrderSummary> summaryMap = new HashMap<>();
        final StringBuilder orderSummaryBuilder = new StringBuilder();

        prepareOrderSummary(liveOrders, summaryMap);

        final List<OrderSummary> orderSummaries = summaryMap.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());

        final List<OrderSummary> sortAndFilterSummaryForSellOrderType = sortSellSummaryList(orderSummaries);
        final List<OrderSummary> sortAndFilterSummaryForBuyOrderType = sortBuySummaryList(orderSummaries);

        final Iterable<OrderSummary> iterable = Iterables.unmodifiableIterable(
                        Iterables.concat(sortAndFilterSummaryForSellOrderType, sortAndFilterSummaryForBuyOrderType));

        iterable.forEach(orderSummary -> orderSummaryBuilder.append(orderSummary).append("\n"));

        if (orderSummaryBuilder.length() > 0) {
            return orderSummaryBuilder.toString().trim();
        }

        return null;
    }

    private List<OrderSummary> sortSellSummaryList(final List<OrderSummary> orderSummaries) {

        final List<OrderSummary> sellOrder = orderSummaries.stream().filter(orderSummary -> orderSummary.getOrderType().equals(OrderType.SELL)).collect(Collectors.toList());
        Collections.sort(sellOrder, (o1, o2) -> o1.getTotalPrice().compareTo(o2.getTotalPrice()));
        return sellOrder;
    }
    private List<OrderSummary> sortBuySummaryList(final List<OrderSummary> orderSummaries) {

        final List<OrderSummary> buyOrder = orderSummaries.stream().filter(orderSummary -> orderSummary.getOrderType().equals(OrderType.BUY)).collect(Collectors.toList());
        Collections.sort(buyOrder, (o1, o2) -> o2.getTotalPrice().compareTo(o1.getTotalPrice()));
        return buyOrder;
    }

    private void prepareOrderSummary(final List<Order> liveOrders, final Map<Double, OrderSummary> summaryMap) {
        liveOrders.stream().forEach(order -> {
            if (summaryMap.get(order.getTotalCost()) == null) {
                summaryMap.put(order.getTotalCost(), new OrderSummary(order.getQuantityInKg(), order.getTotalCost(), order.getOrderType()));
            } else {
                final OrderSummary orderSummary = summaryMap.get(order.getTotalCost());
                OrderSummary updateOrderSummary = new OrderSummary(orderSummary.getTotalQuantity() + order.getQuantityInKg(),
                        order.getTotalCost(), order.getOrderType());
                summaryMap.put(order.getTotalCost(), updateOrderSummary);
            }
        });
    }
}
