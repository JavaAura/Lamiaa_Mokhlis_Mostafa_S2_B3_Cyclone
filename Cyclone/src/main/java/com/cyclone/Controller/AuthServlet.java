package com.cyclone.Controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.cyclone.Model.User;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Service.UserService;

/**
 * Servlet implementation class AuthServlet
 */
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private final UserService userService;

	/**
	 * Default constructor.
	 */
	public AuthServlet() {
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
		// Render the Thymeleaf template
		WebContext context = new WebContext(request, response, getServletContext());
		templateEngine.process("authentication", context, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		WebContext context = new WebContext(request, response, getServletContext());
		String page = "authentication";

		if (action.equals("login")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			User user = null;

			if (email != null && !email.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
				Optional<User> userOpt = userService.login(email, password);

				if (userOpt.isPresent()) {
					user = userOpt.get();

					// Create the session
					HttpSession session = request.getSession();
					session.setAttribute("loggedInUser", user);
					session.setAttribute("role", user.getRole());

					if (user.getRole() == Role.ADMIN) {
						response.sendRedirect(request.getContextPath() + "/user");
					} else if (user.getRole() == Role.CLIENT) {
						templateEngine.process("accueil", context, response.getWriter());
					} else {
						context.setVariable("msg", "Unknown role, cannot log in.");
						templateEngine.process(page, context, response.getWriter());
					}
				} else {
					// Incorrect login
					context.setVariable("msg", "Wrong Email or Password, Try again!!!");
					templateEngine.process(page, context, response.getWriter());
				}
			} else {
				// Missing email or password
				context.setVariable("msg", "Please enter both email and password.");
				templateEngine.process(page, context, response.getWriter());
			}
		} else if (action.equals("logout")) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			context.setVariable("msg", "logged out!!");
			templateEngine.process(page, context, response.getWriter());
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
		}
	}
}
