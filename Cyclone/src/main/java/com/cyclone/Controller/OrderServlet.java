package com.cyclone.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.cyclone.Model.Enum.OrderStatus;
import com.cyclone.Service.OrderService;

/**
 * Servlet implementation class OrderServlet
 */

public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
	private OrderService orderService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
    	  orderService = new OrderService();
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
	        }else {
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
		}
	}

	private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response) {
	/*	OrderService orderService = new OrderService(); 
		int clientId = Integer.parseInt(request.getParameter("clientId"));
		  UserService userService = new UserService();
	      client = userService.findUserById(clientId);
	      Order newOrder = new Order();
	        newOrder.setClient(client); 
	        newOrder.setOrderDate(LocalDate.now());
	        String productIdStr = request.getParameter("productId"); 
	        String quantityStr = request.getParameter("quantity");
	        if (productIdStr != null && quantityStr != null) {
	            int productId = Integer.parseInt(productIdStr);
	            int quantity = Integer.parseInt(quantityStr);
	            ProductService productService = new ProductService(); 
	            Product product = productService.findProductById(productId);
	            if (product != null) {
	                newOrder.setProduct(product); 
	                newOrder.setQuantity(quantity); 

	                OrderService orderService = new OrderService();
	                orderService.addOrder(newOrder);

	            }else {
	                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
	            }
	        }else {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID or quantity is missing");
	        }*/
	}
	
	private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    try {
	        int orderId = Integer.parseInt(request.getParameter("id"));
	        orderService.removeOrder(orderId);
	        response.sendRedirect(request.getContextPath() + "/orders");
	    } catch (NumberFormatException e) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
	    }
	}
	
	private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 try {
		        int orderId = Integer.parseInt(request.getParameter("orderId"));
		        Optional<Order> optionalOrder = orderService.findOrderById(orderId);

		        if (optionalOrder.isPresent()) {
		            Order existingOrder = optionalOrder.get();
		            String quantityStr = request.getParameter("quantity");

		            if (quantityStr != null) {
		                int quantity = Integer.parseInt(quantityStr);
		                if ("PENDING".equals(existingOrder.getStatus()) || "PROCESSING".equals(existingOrder.getStatus())) {
		                	  List<Product> products = existingOrder.getProducts();
		                      if (products != null && !products.isEmpty()) {
		                          Product product = products.get(0);
		                        if (quantity <= product.getStock()) {
		                            existingOrder.setQuantity(quantity);
		                            orderService.modifyOrder(existingOrder);
		                            response.sendRedirect("orders"); 
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
		        request.setAttribute("orders", orders);
		        request.getRequestDispatcher("/WEB-INF/templates/order/ordersList.html").forward(request, response);
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid clientId: " + clientIdParam);
		        response.getWriter().write("Invalid client ID.");
		    }
	}
}
