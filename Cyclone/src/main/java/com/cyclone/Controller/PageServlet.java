package com.cyclone.Controller;

import java.io.IOException;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.context.WebContext;

import com.cyclone.Model.Product;
import com.cyclone.Service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;


/**
 * Servlet implementation class ProductServlet
 */

public class PageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	private ProductService productService;
	private static final Logger logger = LoggerFactory.getLogger(PageServlet.class);
	private TemplateEngine templateEngine;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PageServlet() {
		this.productService = new ProductService();
	}

	/**
	 * Initializes the servlet, setting up the Thymeleaf template engine and the validator.
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(this.getServletContext());
		templateResolver.setTemplateMode("HTML");
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	/**
	 * Handles GET requests to the servlet.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the GET could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the GET request
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getParameter("action");
		if (action == null) {
			action = "home";
		}

		switch (action) {
			case "home":
				home(request, response);
				break;
			case "about":
				about(request, response);
				break;
			case "contact":
				contact(request, response);
				break;
			default:
				home(request, response);
				break;
		}

		
	}

	/**
	 * Handles POST requests to the servlet.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the POST could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the POST request
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getParameter("action");
	
		switch (action) {
			case "SubmitContact":
				submitContact(request, response);
				break;
			default:
				response.sendRedirect(request.getContextPath() + "/page?action=Home");
				break;
		}
	}

    private void home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        templateEngine.process("home", context, response.getWriter());
    }

    private void about(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        templateEngine.process("about", context, response.getWriter());
    }

    private void contact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        templateEngine.process("contact", context, response.getWriter());
    }

    private void submitContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        templateEngine.process("submitContact", context, response.getWriter());
    }

}
