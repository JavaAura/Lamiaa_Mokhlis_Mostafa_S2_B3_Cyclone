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
	        }/*else if ("".equals(action)){
	   
	        }*/else {
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
