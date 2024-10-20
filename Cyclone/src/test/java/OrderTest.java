import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.cyclone.Model.Client;
import com.cyclone.Model.Order;
import com.cyclone.Model.Product;
import com.cyclone.Model.User;
import com.cyclone.Model.Enum.OrderStatus;
import com.cyclone.Repository.Impl.OrderRepositoryImpl;
import com.cyclone.Service.OrderService;



public class OrderTest {
	
	 private OrderRepositoryImpl orderRepository;
	    private OrderService orderService; 
	    private Order order; 
	    private Client client; 
	    private Product product; 
	    @BeforeEach
	    public void setUp() {
	        orderRepository = mock(OrderRepositoryImpl.class);
	        orderService = new OrderService(); 
	        client = mock(Client.class); 
	        product = mock(Product.class); 
	        order = new Order();
	        order.setId(1);
	        order.setOrderDate(LocalDate.now());
	        order.setQuantity(5);
	        order.setStatus(OrderStatus.PENDING);
	        order.setClient(client); 
	        order.setProducts(Collections.singletonList(product)); 
	    }
	    
	    @Test
	    public void testAddOrder() {
	        orderService.addOrder(order);

	        verify(orderRepository).addOrder(order);
	    }
	    
	    @Test
	    public void testFindOrderById() {
	        when(orderRepository.findOrderById(1)).thenReturn(order);
	        Optional<Order> foundOrder = orderService.findOrderById(1);
	        assertTrue(foundOrder.isPresent());
	        assertEquals(order, foundOrder.get());
	    }
	    @Test
	    public void testGetAllOrders() {
	        List<Order> orderList = Collections.singletonList(order);
	        when(orderRepository.getAllOrders()).thenReturn(orderList);
	        
	        List<Order> allOrders = orderService.getAllOrders();
	        assertEquals(1, allOrders.size());
	        assertEquals(order, allOrders.get(0));
	    }

	    @Test
	    public void testModifyOrder() {
	        orderService.modifyOrder(order);
	        verify(orderRepository).modifyOrder(order);
	    }

	    @Test
	    public void testRemoveOrder() {
	        orderService.removeOrder(1);
	        verify(orderRepository).removeOrder(1);
	    }

	    @Test
	    public void testGetOrdersByClient() {
	        when(orderRepository.getOrdersByClient(1)).thenReturn(Collections.singletonList(order));
	        
	        Optional<List<Order>> orders = orderService.getOrdersByClient(1);
	        assertTrue(orders.isPresent());
	        assertEquals(1, orders.get().size());
	        assertEquals(order, orders.get().get(0));
	    }
}
