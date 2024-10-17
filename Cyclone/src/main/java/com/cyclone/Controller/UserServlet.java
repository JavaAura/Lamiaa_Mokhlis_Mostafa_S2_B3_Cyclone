package com.cyclone.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.cyclone.Model.Admin;
import com.cyclone.Model.Client;
import com.cyclone.Model.User;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Service.UserService;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
    private final UserService userService;

    /**
     * Default constructor. 
     */
    public UserServlet() {
    	this.userService = new UserService();
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

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

        switch (action) {
            case "insert":
                insertUser(request, response);
                break;
            case "update":
//                updateArticle(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
	}
	
	protected void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Retrieve form parameters
	    String firstName = request.getParameter("firstName");
	    String lastName = request.getParameter("lastName");
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    String roleString = request.getParameter("role");
	    String accessLevel = request.getParameter("accessLevel"); 
	    String deliveryAddress = request.getParameter("deliveryAddress"); 
	    String paymentMethod = request.getParameter("paymentMethod");
	    
	    WebContext context = new WebContext(request, response, getServletContext());
	    String page = "admin/userManagement";

	    // Create the appropriate User based on the role
	    User user;
	    if ("admin".equalsIgnoreCase(roleString)) {
	        // Create Admin
	        user = new Admin();
	        ((Admin) user).setAccessLevel(accessLevel);
	        user.setRole(Role.ADMIN);
	    } else if ("client".equalsIgnoreCase(roleString)) {
	        // Create Client
	        user = new Client();
	        ((Client) user).setDeliveryAddress(deliveryAddress);
	        ((Client) user).setPaymentMethod(paymentMethod);
	        user.setRole(Role.CLIENT);
	    } else {
	    	context.setVariable("error", "Invalid role");
		    templateEngine.process(page, context, response.getWriter());
	        return;
	    }

	    user.setFirstName(firstName);
	    user.setLastName(lastName);
	    user.setEmail(email);
	    user.setPassword(password);

	    try {
	        userService.createUser(user);
	        context.setVariable("message", "User created successfully");
	        context.setVariable("user", user);
	        templateEngine.process(page, context, response.getWriter());
	    } catch (Exception e) {
	    	context.setVariable("error", "Error while saving user");
		    templateEngine.process(page, context, response.getWriter());
	    }
	}

}
