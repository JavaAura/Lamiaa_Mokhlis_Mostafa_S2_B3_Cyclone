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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession(false);
		WebContext context = new WebContext(request, response, getServletContext());

		if (action == null) {
			action = "list";
		}

		if (session != null) {
			switch (action) {
			case "edit":
				showEditForm(request, response);
				break;
			case "list":
				listUsers(request, response);
				break;
			case "search":
				searchUser(request, response);
				break;
			case "delete":
				deleteUser(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
				break;
			}
		} else {
			context.setVariable("msg", "please log in first!!");
			templateEngine.process("authentication", context, response.getWriter());
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {
		case "insert":
			insertUser(request, response);
			break;
		case "update":
			updateUser(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
			break;
		}
	}

	protected void insertUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	protected void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebContext context = new WebContext(request, response, getServletContext());
		String page = "admin/userManagement";

		Optional<List<User>> usersOpt = userService.getAllUsers();

		if (usersOpt.isPresent()) {
			List<User> admins = usersOpt.get().stream().filter(user -> user.getRole() == Role.ADMIN)
					.collect(Collectors.toList());

			List<User> clients = usersOpt.get().stream().filter(user -> user.getRole() == Role.CLIENT)
					.collect(Collectors.toList());

			context.setVariable("admins", admins);
			context.setVariable("clients", clients);

			templateEngine.process(page, context, response.getWriter());
		} else {
			context.setVariable("admins", null);
			context.setVariable("clients", null);
			context.setVariable("emptyTable", "no users found");
			templateEngine.process(page, context, response.getWriter());
		}

	}

	protected void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDStr = request.getParameter("userID");
		int userID = Integer.parseInt(userIDStr);

		Optional<User> userOpt = userService.getUserById(userID);
		WebContext context = new WebContext(request, response, getServletContext());

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			context.setVariable("user", user);

			if (user.getRole() == Role.ADMIN) {
				context.setVariable("isAdmin", true);
			} else if (user.getRole() == Role.CLIENT) {
				context.setVariable("isClient", true);
			}

			templateEngine.process("admin/editUser", context, response.getWriter());
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
		}
	}

	protected void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDStr = request.getParameter("userID");
		int userID = Integer.parseInt(userIDStr);

		Optional<User> userOpt = userService.getUserById(userID);
		WebContext context = new WebContext(request, response, getServletContext());
		String page = "admin/userManagement";

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			// Update common fields
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			user.setEmail(request.getParameter("email"));

			// Update role-specific fields
			String roleString = request.getParameter("role");
			if ("admin".equalsIgnoreCase(roleString)) {
				((Admin) user).setAccessLevel(request.getParameter("accessLevel"));
				user.setRole(Role.ADMIN);
			} else if ("client".equalsIgnoreCase(roleString)) {
				((Client) user).setDeliveryAddress(request.getParameter("deliveryAddress"));
				((Client) user).setPaymentMethod(request.getParameter("paymentMethod"));
				user.setRole(Role.CLIENT);
			} else {
				context.setVariable("error", "Invalid role");
//	            templateEngine.process(page, context, response.getWriter());
				listUsers(request, response);
				return;
			}

			String password = request.getParameter("password");
			if (password != null && !password.isEmpty()) {
				user.setPassword(PasswordUtils.hashPassword(password));
			}

			try {
				userService.updateUser(user);
				listUsers(request, response);
			} catch (Exception e) {
				context.setVariable("error", "Error while updating user");
//	            templateEngine.process(page, context, response.getWriter());
				listUsers(request, response);
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
		}
	}
	
	protected void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    String userID = request.getParameter("userID");

	    if (userID != null && !userID.isEmpty()) {
	        try {
	            Integer userId = Integer.parseInt(userID);

	            boolean deleted = userService.deleteUser(userId);

	            if (deleted) {
	            	listUsers(request, response);
	            } else {
	                WebContext context = new WebContext(request, response, getServletContext());
	                context.setVariable("error", "User not found or could not be deleted.");
	                listUsers(request, response);
	            }
	        } catch (NumberFormatException e) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
	        }
	    } else {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user ID");
	    }
	}

	protected void searchUser(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String keyword = request.getParameter("searchInput");

	    if (keyword == null || keyword.trim().isEmpty()) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search name is missing or empty.");
	        return;
	    }

	    Optional<List<User>> usersOpt = userService.searchUsersByName(keyword);

	    WebContext context = new WebContext(request, response, getServletContext());
	    String page = "admin/userManagement";

	    if (!usersOpt.isPresent()) {
	        context.setVariable("emptyTable", "No users found.");
	    } else {
	        List<User> admins = usersOpt.get().stream()
	                .filter(user -> user.getRole() == Role.ADMIN)
	                .collect(Collectors.toList());
	        List<User> clients = usersOpt.get().stream()
	                .filter(user -> user.getRole() == Role.CLIENT)
	                .collect(Collectors.toList());

	        context.setVariable("admins", admins);
	        context.setVariable("clients", clients);
	    }

	    templateEngine.process(page, context, response.getWriter());
	}


}
