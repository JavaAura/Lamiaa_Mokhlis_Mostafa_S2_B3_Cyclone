package com.cyclone.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.cyclone.Model.Order;
import com.cyclone.Model.Product;
import com.cyclone.Model.User;
import com.cyclone.Model.Enum.OrderStatus;
import com.cyclone.Service.OrderService;
import com.cyclone.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Servlet implementation class OrderServlet
 */

public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
	private OrderService orderService;
	private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);

	//private ProductService productService;
	private UserService userService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
    	  orderService = new OrderService();
    	//  productService = new ProductService();
    	  userService = new UserService();
    }

    @Override
    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(this.getServletContext());
        templateResolver.setTemplateMode("HTML");
        templateResolver.setPrefix("/WEB-INF/templates/"); 
        templateResolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");
		 
	        if ("viewClientOrders".equals(action)) {
	        	   displayClientOrders(request, response);
	        }else if ("deleteOrder".equals(action)){
	            handleDeleteOrder(request, response);
	        }else if ("viewOrdersAdmin".equals(action)) {
	        	displayOrdersAdmin(request, response);
	        }else if ("search".equals(action)){
	        	searchOrders(request, response);
	        }
	        else {
	            showOrderButtons(request, response);
	        }
	        
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");
		 
		if ("addOrder".equals(action)) {
			handleCreateOrder(request, response);
		}else if ("updateOrder".equals(action)) {
			   handleUpdateOrder(request, response);
		}else if ("updateOrderAdmin".equals(action)) {
	        updateOrderStatus(request, response);
	    }
	}
	
	private void searchOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	        String searchWord = request.getParameter("search");

	        List<Order> matchingOrders = orderService.getOrdersByClientLastName(searchWord);

	        int page = 1;
	        String pageParam = request.getParameter("page");
	        if (pageParam != null && !pageParam.isEmpty()) {
	            try {
	                page = Integer.parseInt(pageParam);
	            } catch (NumberFormatException e) {
	                logger.warn("Invalid page parameter: {}", pageParam);
	                page = 1;
	            }
	        }
	        int pageSize = 10;
	        int offset = (page - 1) * pageSize;

	        int totalOrders = matchingOrders.size();
	        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

	        List<Order> paginatedOrders = matchingOrders.stream()
	                .skip(offset)
	                .limit(pageSize)
	                .collect(Collectors.toList());

	        List<Map<String, Object>> orderDetailsList = new ArrayList<>();
	        for (Order order : paginatedOrders) {
	            if (!order.getProducts().isEmpty()) {
	                Product product = order.getProducts().get(0);
	                double totalPrice = product.getPrice() * order.getQuantity();

	                Map<String, Object> orderDetail = new HashMap<>();
	                orderDetail.put("order", order);
	                orderDetail.put("product", product);
	                orderDetail.put("totalPrice", totalPrice);

	                orderDetailsList.add(orderDetail);
	            }
	        }

	        request.setAttribute("orderDetails", orderDetailsList);
	        request.setAttribute("currentPage", page);
	        request.setAttribute("totalPages", totalPages);
	        request.setAttribute("orderStatuses", OrderStatus.values());

	        if (matchingOrders.isEmpty()) {
	            request.setAttribute("noOrdersMessage", "No matching orders found.");
	        }

	        WebContext context = new WebContext(request, response, getServletContext());
	        context.setVariable("orderDetails", orderDetailsList);
	        context.setVariable("currentPage", page);
	        context.setVariable("totalPages", totalPages);
	        templateEngine.process("order/AdminOrdersList", context, response.getWriter());

	    } catch (NumberFormatException e) {
	        logger.error("Error parsing page parameter: {}", e.getMessage());
	        response.getWriter().write("Invalid page number. Please provide a valid number.");
	    }
	}
	
	private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  int orderId = Integer.parseInt(request.getParameter("orderId"));
		    String status = request.getParameter("status");

		    Optional<Order> optionalOrder = orderService.findOrderById(orderId);

		    if (optionalOrder.isPresent()) {
		        Order order = optionalOrder.get();
		        int quantity = order.getQuantity();
		        order.setStatus(OrderStatus.valueOf(status));

		        orderService.modifyOrder(order);
		        List<Order> orders = orderService.getAllOrders(); 
		        List<Map<String, Object>> orderDetailsList = new ArrayList<>(); 

		        for (Order o : orders) {
		            if (!o.getProducts().isEmpty()) {
		                Product product = o.getProducts().get(0); 

		                double totalPrice = product.getPrice() * o.getQuantity(); 

		                Map<String, Object> orderDetail = new HashMap<>();
		                orderDetail.put("order", order); 
		                orderDetail.put("product", product); 
		                orderDetail.put("totalPrice", totalPrice); 
		                orderDetailsList.add(orderDetail); 
		            }
		        }
		        request.setAttribute("orderDetails", orderDetailsList); 
    	        
    	        request.setAttribute("orderStatuses", OrderStatus.values());
	
    	        /*   if (OrderStatus.CANCELLED.equals(order.getStatus())) {
		            if (!order.getProducts().isEmpty()) {
		                Product product = order.getProducts().get(0);
		                product.setStock(product.getStock() + quantity);
		                
		                productService.updateProduct(product); 
		            }
		        }*/
		        request.setAttribute("message", "Order status updated successfully.");
		        WebContext context = new WebContext(request, response, getServletContext());
    	        context.setVariable("orderDetails", orderDetailsList);
    	        templateEngine.process("order/AdminOrdersList", context, response.getWriter());
		    } else {
		        request.setAttribute("errorMessage", "Order not found.");
		    }
		   
		   
	}
	
    private void displayOrdersAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	    try {
    	        
    	        int page = 1;
    	        String pageParam = request.getParameter("page");
    	        if (pageParam != null && !pageParam.isEmpty()) {
    	            try {
    	                page = Integer.parseInt(pageParam);
    	            } catch (NumberFormatException e) {
    	                logger.warn("Invalid page parameter: {}", pageParam);
    	                page = 1; 
    	            }
    	        }
    	        int pageSize = 10; 
    	        int offset = (page - 1) * pageSize;

    	        List<Order> orders = orderService.getOrdersWithPagination( offset, pageSize);
    	        int totalOrders = orderService.getTotalOrderCount(); 
    	        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
    	        
    	        List<Map<String, Object>> orderDetailsList = new ArrayList<>(); 

    	        for (Order order : orders) {
    	            if (!order.getProducts().isEmpty()) {
    	                Product product = order.getProducts().get(0); 

    	                double totalPrice = product.getPrice() * order.getQuantity(); 

    	                Map<String, Object> orderDetail = new HashMap<>();
    	                orderDetail.put("order", order); 
    	                orderDetail.put("product", product); 
    	                orderDetail.put("totalPrice", totalPrice); 
    	                
    	                orderDetailsList.add(orderDetail); 
    	            }
    	        }

    	        request.setAttribute("orderDetails", orderDetailsList); 
    	        request.setAttribute("currentPage", page);
    	        request.setAttribute("totalPages", totalPages);
    	        request.setAttribute("orderStatuses", OrderStatus.values());
    	        if (orders.isEmpty()) {
    	            request.setAttribute("noOrdersMessage", "No orders available at the moment.");
    	        }
    	        
//    	        for (Order order : orders) {
//    	            logger.info("Order details - ID: {}, Status: {}, Date: {}, Client: {} {}, Quantity: {}",
//    	                order.getId(),
//    	                order.getStatus(),
//    	                order.getOrderDate(),
//    	                order.getClient().getFirstName(),
//    	                order.getClient().getLastName(),
//    	                order.getQuantity());
//    	        }

    	        WebContext context = new WebContext(request, response, getServletContext());
    	        context.setVariable("orderDetails", orderDetailsList);
    	        context.setVariable("currentPage", page);
    	        context.setVariable("totalPages", totalPages);
    	        templateEngine.process("order/AdminOrdersList", context, response.getWriter());
    	        
    	    } catch (NumberFormatException e) {
    	    	 logger.error("Error parsing page parameter: {}", e.getMessage());
    	         response.getWriter().write("Invalid page number. Please provide a valid number."); 
    	    }  
    }
    

	private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response) {
	/*	 try {
		        int clientId = Integer.parseInt(request.getParameter("clientId"));
		        User client = userService.findUserById(clientId);

		        if (client == null) {
		            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found");
		            return;
		        }

		        Order newOrder = new Order();
		        newOrder.setClient(client); 
		        newOrder.setOrderDate(LocalDate.now());

		        String productIdStr = request.getParameter("productId"); 
		        String quantityStr = request.getParameter("quantity");

		        if (productIdStr != null && quantityStr != null) {
		            int productId = Integer.parseInt(productIdStr);
		            int quantity = Integer.parseInt(quantityStr);

		            Product product = productService.findProductById(productId);

		            if (product != null) {
		                if (quantity <= product.getStock()) {
		                    newOrder.addProduct(product); 
		                    newOrder.setQuantity(quantity); 
		                    orderService.addOrder(newOrder);
		                    
		                    product.decrementStock(quantity);
		                    
		                    productService.updateProduct(product);
		                    
		                    response.sendRedirect("orders");
		                } else {
		                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough stock available for this product");
		                }
		            } else {
		                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
		            }
		        } else {
		            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID or quantity is missing");
		        }
		    } catch (NumberFormatException e) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity or client ID");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while creating the order");
		    }*/
	}
	
	private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*  try {
		        int orderId = Integer.parseInt(request.getParameter("id"));
		        Optional<Order> optionalOrder = orderService.findOrderById(orderId);

		        if (optionalOrder.isPresent()) {
		            Order orderToDelete = optionalOrder.get();
		            
		            if ("PENDING".equals(orderToDelete.getStatus()) || "PROCESSING".equals(orderToDelete.getStatus())) {
		                List<Product> products = orderToDelete.getProducts();
		                
		                if (products != null && !products.isEmpty()) {
		                    Product product = products.get(0);
		                    int quantityToRestore = orderToDelete.getQuantity();
		                    
		                    product.incrementStock(quantityToRestore);
		                    
		                    productService.updateProduct(product);
		                    
		                    orderService.removeOrder(orderId);
		                    
		                    response.sendRedirect(request.getContextPath() + "/orders");
		                } else {
		                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "No products found for this order");
		                }
		            } else {
		                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cannot delete order with status: " + orderToDelete.getStatus());
		            }
		        } else {
		            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
		        }
		    } catch (NumberFormatException e) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the order");
		    }*/
	}
	
	private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
	 try {
		        int orderId = Integer.parseInt(request.getParameter("orderId"));
		        Optional<Order> optionalOrder = orderService.findOrderById(orderId);

		        if (optionalOrder.isPresent()) {
		            Order existingOrder = optionalOrder.get();
		            System.out.println(existingOrder.getStatus());
		            String quantityStr = request.getParameter("quantity");

		            if (quantityStr != null) {
		                int quantity = Integer.parseInt(quantityStr);
		               String existingOrderStatus=existingOrder.getStatus().name();
		               if ("PENDING".equals(existingOrderStatus) || "PROCESSING".equals(existingOrderStatus)) {
		                	  List<Product> products = existingOrder.getProducts();
		                      if (products != null && !products.isEmpty()) {
		                          Product product = products.get(0);
		                        if (quantity <= product.getStock()) {
		                            existingOrder.setQuantity(quantity);
		                            orderService.modifyOrder(existingOrder);
		                            
		                           /* product.decrementStock(quantity);
		                            
		                            productService.updateProduct(product);*/

		                            List<Order> orders = orderService.getAllOrders(); 

		                            WebContext context = new WebContext(request, response, getServletContext());
		                            context.setVariable("orders", orders);

		                            templateEngine.process("order/ordersList", context, response.getWriter());
		                        } else {
		                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough stock available");
		                        }
		                    } else {
		                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found for this order");
		                    }
		                } else {
		                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order status must be PENDING or PROCESSING to update the quantity");
		                }
		            } else {
		                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity is missing");
		            }
		        } else {
		            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
		        }
		    } catch (NumberFormatException e) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity or order ID");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the order");
		    }
	}
	
    private void showOrderButtons(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        templateEngine.process("order/orderButtons", context, response.getWriter());
    }	

    
	private void displayClientOrders(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		 String clientIdParam = request.getParameter("clientId");

	        try {
	            int clientId = Integer.parseInt(clientIdParam);
	            Optional<List<Order>> optionalOrders = orderService.getOrdersByClient(clientId);
	            List<Order> orders = optionalOrders.orElseGet(ArrayList::new);
	            
	            // Log order details (unchanged)
	            for (Order order : orders) {
	                logger.info("Order details - ID: {}, Status: {}, Date: {},Client: {} {}, Quantity: {}",
	                    order.getId(),
	                    order.getStatus(),
	                    order.getOrderDate(),
	                    order.getClient().getFirstName(),
	                    order.getClient().getLastName(),
	                    order.getQuantity());
	            }
	            logger.info("ok");

	            // Create a WebContext and add the orders to it
	            WebContext context = new WebContext(request, response, getServletContext());
	            context.setVariable("orders", orders);

	            // Process the template and write the result to the response
	            templateEngine.process("order/ordersList", context, response.getWriter());
	        } catch (NumberFormatException e) {
	            logger.warn("Invalid clientId: {}", clientIdParam);
	            response.getWriter().write("Invalid client ID.");
	        }
	}
}
