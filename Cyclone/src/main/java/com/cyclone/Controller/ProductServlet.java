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
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Servlet implementation class ProductServlet
 */

public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private Validator validator;

	private ProductService productService;
	private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductServlet() {
		this.productService = new ProductService();
	}

	/**
	 * Initializes the servlet, setting up the Thymeleaf template engine and the validator.
	 */
	@Override
	public void init() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(this.getServletContext());
		templateResolver.setTemplateMode("HTML");
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
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
		logger.info("Action: " + action);

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "list":
			getAllProducts(request, response);
			break;
		case "showForm":
			showAddProductForm(request, response);
			break;
		case "showFormUpdate":
			showUpdateProductForm(request, response);
			break;
		default:
			getAllProducts(request, response);
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
		logger.info("Action: " + action);

		switch (action) {
		case "add":
			addProduct(request, response);
			break;
		case "update":
			updateProduct(request, response);
			break;
		case "delete":
			deleteProduct(request, response);
			break;
		default:
			getAllProducts(request, response);
			break;
		}
	}

	/**
	 * Retrieves all products and displays them in a list.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the GET could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the GET request
	 */
	public void getAllProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Product> products = productService.getAllProducts();

		WebContext context = new WebContext(request, response, getServletContext());

		if (products == null || products.isEmpty()) {
			context.setVariable("errorMessage", "No products available.");
		} else {
			context.setVariable("products", products);

			for (Product product : products) {
				logger.info("admin/Product: " + product.getName());
			}
		}

		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("text/html; charset=UTF-8");
		templateEngine.process("admin/product-list", context, response.getWriter());
	}

	/**
	 * Adds a new product to the database.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the POST could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the POST request
	 */
	public void addProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("addProduct");
		int stock = 0;
		double price = 0;
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String priceStr = request.getParameter("price");
		String stockStr = request.getParameter("stock");

		if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty()
				|| priceStr == null || priceStr.trim().isEmpty() || stockStr == null || stockStr.trim().isEmpty()) {

			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("errorMessage", "All fields are required and cannot be empty");
			Product product = new Product();
			product.setName(name);
			product.setDescription(description);
			context.setVariable("product", product);
			templateEngine.process("admin/productform", context, response.getWriter());
			return;
		}

		try {
			price = Double.parseDouble(priceStr.trim());
			stock = Integer.parseInt(stockStr.trim());

			Product product = new Product();
			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setStock(stock);

			Set<ConstraintViolation<Product>> violations = validator.validate(product);

			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Product> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}

				WebContext context = new WebContext(request, response, getServletContext());
				context.setVariable("product", product);
				context.setVariable("errors", errors);
				context.setVariable("errorMessage", "Please correct the following errors:");
				templateEngine.process("admin/productform", context, response.getWriter());
				return;
			}

			boolean resultat = productService.addProduct(product);
			if (resultat) {
				doGet(request, response);
			} else {
				WebContext context = new WebContext(request, response, getServletContext());
				context.setVariable("errorMessage", "Failed to add product");
				context.setVariable("product", product);
				templateEngine.process("admin/productform", context, response.getWriter());
			}
		} catch (NumberFormatException e) {
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("errorMessage", "Invalid price or stock value. Please enter valid numbers.");
			templateEngine.process("admin/productform", context, response.getWriter());
		}
	}

	/**
	 * Displays the form for adding a new product.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the GET could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the GET request
	 */
	public void showAddProductForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebContext context = new WebContext(request, response, getServletContext());
		templateEngine.process("admin/productform", context, response.getWriter());
	}

	/**
	 * Displays the form for updating an existing product.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the GET could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the GET request
	 */
	public void showUpdateProductForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Optional<Product> product = productService.getProductById(id);
		if (product.isPresent()) {
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("product", product.get());
			templateEngine.process("admin/productUpdate", context, response.getWriter());
		}
	}

	/**
	 * Updates an existing product in the database.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the POST could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the POST request
	 */
	public void updateProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Optional<Product> optionalProduct = productService.getProductById(id);

		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String priceStr = request.getParameter("price");
			String stockStr = request.getParameter("stock");

			if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty()
					|| priceStr == null || priceStr.trim().isEmpty() || stockStr == null || stockStr.trim().isEmpty()) {

				WebContext context = new WebContext(request, response, getServletContext());
				context.setVariable("errorMessage", "All fields are required and cannot be empty");
				context.setVariable("product", product);
				templateEngine.process("admin/productUpdate", context, response.getWriter());
				return;
			}

			try {
				double price = Double.parseDouble(priceStr.trim());
				int stock = Integer.parseInt(stockStr.trim());

				product.setName(name);
				product.setDescription(description);
				product.setPrice(price);
				product.setStock(stock);

				Set<ConstraintViolation<Product>> violations = validator.validate(product);
				logger.info("Violations: " + violations);

				if (!violations.isEmpty()) {
					Map<String, String> errors = new HashMap<>();
					for (ConstraintViolation<Product> violation : violations) {
						logger.info("Violation: " + violation.getPropertyPath().toString() + " - "
								+ violation.getMessage());
						errors.put(violation.getPropertyPath().toString(), violation.getMessage());
					}

					WebContext context = new WebContext(request, response, getServletContext());
					context.setVariable("product", product);
					context.setVariable("errors", errors);
					templateEngine.process("admin/productUpdate", context, response.getWriter());
					return;
				}

				boolean result = productService.updateProduct(product);
				if (result) {
					doGet(request, response);
				} else {
					WebContext context = new WebContext(request, response, getServletContext());
					context.setVariable("errorMessage", "Failed to update product");
					context.setVariable("product", product);
					templateEngine.process("admin/productUpdate", context, response.getWriter());
				}
			} catch (NumberFormatException e) {
				WebContext context = new WebContext(request, response, getServletContext());
				context.setVariable("errorMessage", "Invalid price or stock value. Please enter valid numbers.");
				context.setVariable("product", product);
				templateEngine.process("admin/productUpdate", context, response.getWriter());
			}
		} else {
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("errorMessage", "Product not found");
			templateEngine.process("admin/productUpdate", context, response.getWriter());
		}
	}

	/**
	 * Deletes a product from the database.
	 * @param request The HTTP servlet request
	 * @param response The HTTP servlet response
	 * @throws ServletException If the request for the POST could not be handled
	 * @throws IOException If an input or output error is detected when the servlet handles the POST request
	 */
	public void deleteProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		boolean result = productService.deleteProduct(id);
		if (result) {
			doGet(request, response);
		} else {
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("errorMessage", "Failed to delete product");
			templateEngine.process("admin/Liste-produits", context, response.getWriter());
		}
	}
}
