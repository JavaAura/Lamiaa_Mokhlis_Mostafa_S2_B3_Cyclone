package com.cyclone.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.cyclone.Model.Admin;
import com.cyclone.Model.Client;
import com.cyclone.Model.User;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Service.UserService;
import com.cyclone.Util.PasswordUtils;

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
    	HttpSession session = request.getSession(false);
    	WebContext context = new WebContext(request, response, getServletContext());
    	
    	if (session != null) { 
    		listUsers(request, response);
	    } else {
	        context.setVariable("msg", "please log in first!!");
	        templateEngine.process("authentication", context, response.getWriter());
	    }
    	
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
	    user.setPassword(PasswordUtils.hashPassword(password));

	    try {
	        userService.createUser(user);
	        listUsers(request, response);
	    } catch (Exception e) {
	    	context.setVariable("error", "Error while saving user");
		    templateEngine.process(page, context, response.getWriter());
	    }
	}
	
	protected void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		WebContext context = new WebContext(request, response, getServletContext());
	    String page = "admin/userManagement";
	    
	    Optional<List<User>> usersOpt = userService.getAllUsers();

	    if(usersOpt.isPresent()) {
	    	List<User> admins = usersOpt.get().stream()
                    .filter(user -> user.getRole() == Role.ADMIN)
                    .collect(Collectors.toList());
                    
	    	List<User> clients = usersOpt.get().stream()
                     .filter(user -> user.getRole() == Role.CLIENT)
                     .collect(Collectors.toList());

	    	context.setVariable("admins", admins);
	    	context.setVariable("clients", clients);
	    	
	    	

	    	templateEngine.process(page, context, response.getWriter());
	    }else {
	    	context.setVariable("admins", null);
	    	context.setVariable("clients", null);
	    	context.setVariable("emptyTable", "no users found");
	    	templateEngine.process(page, context, response.getWriter());
	    }
        
	}

}
