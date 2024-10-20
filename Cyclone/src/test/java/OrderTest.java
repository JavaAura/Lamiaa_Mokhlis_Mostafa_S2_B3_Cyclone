import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collections;

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
}
