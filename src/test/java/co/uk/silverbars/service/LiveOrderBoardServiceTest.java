package co.uk.silverbars.service;

import co.uk.silverbars.domain.Order;
import co.uk.silverbars.domain.OrderType;
import co.uk.silverbars.exception.OrderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LiveOrderBoardServiceTest {

    private LiveOrderBoardService liveOrderBoardService;

    @Mock
    private OrderBoardRepository orderBoardRepository;

    @Before
    public void setup() {
        this.liveOrderBoardService = new LiveOrderBoardService(orderBoardRepository);
    }

    @Test
    public void shouldAddANewOrder() {
        final Order order = new Order("user1", 3.5, 50.00, OrderType.SELL);
        liveOrderBoardService.createAnOrder(order);
        verify(orderBoardRepository, times(1)).addOrderToBoard(order);
    }

    @Test(expected = OrderException.class)
    public void shouldThrowExceptionIfRequestIsValid() {
        final Order order = new Order("user1", 3.5, 50.00, null);
        liveOrderBoardService.createAnOrder(order);
    }

    @Test
    public void shouldCancelAndRemoveOrder() {
        final Order order = new Order("user1", 3.5, 50.00, OrderType.SELL);
        liveOrderBoardService.createAnOrder(order);
        verify(orderBoardRepository, times(1)).addOrderToBoard(order);
        when(orderBoardRepository.isOrderExists(order)).thenReturn(true);

        liveOrderBoardService.cancelOrder(order);
        verify(orderBoardRepository, times(1)).removeOrderFromBoard(order);

    }
    @Test(expected = OrderException.class)
    public void shouldThrowExceptionIfOrderNotExsitToCancelAndRemove() {

        final Order order = new Order("user1", 3.5, 55.00, OrderType.SELL);
        when(orderBoardRepository.isOrderExists(order)).thenReturn(false);

        liveOrderBoardService.cancelOrder(order);

    }

    @Test
    public void shouldReturnSummaryInformationOfLiveOrderBoardForOrderTypeBuy() {
        when(orderBoardRepository.getLiveOrders()).thenReturn(getOrders(OrderType.BUY));

        final String orderSummary = liveOrderBoardService.getOrderSummary();

        final String expectedOrderSummary = "6.0 kg for £400\n" +
                "3.0 kg for £240\n" +
                "1.5 kg for £90";

        assertThat(expectedOrderSummary, is(orderSummary));
    }

    @Test
    public void shouldReturnSummaryInformationOfLiveOrderBoardForOrderTypeSell() {
        when(orderBoardRepository.getLiveOrders()).thenReturn(getOrders(OrderType.SELL));

        final String orderSummary = liveOrderBoardService.getOrderSummary();

        final String expectedOrderSummary = "1.5 kg for £90\n" +
                "3.0 kg for £240\n" +
                "6.0 kg for £400";

        assertThat(expectedOrderSummary, is(orderSummary));
    }

    @Test
    public void shouldReturnNullWhenNoOrderOnTheBoard() {
        when(orderBoardRepository.getLiveOrders()).thenReturn(new ArrayList<Order>());

        final String orderSummary = liveOrderBoardService.getOrderSummary();

        Assert.assertNull(orderSummary);
    }


    private List<Order> getOrders(final OrderType orderType) {
        final Order order1 = new Order("user1", 4.0, 100.00, orderType);
        final Order order2 = new Order("user2", 1.5, 60.00, orderType);
        final Order order3 = new Order("user3", 2.0, 200.00, orderType);
        final Order order4 = new Order("user4", 3.0, 80.00, orderType);

        return Arrays.asList(order1, order2, order3, order4);
    }

}