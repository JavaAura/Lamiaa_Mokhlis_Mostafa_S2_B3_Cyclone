package com.cyclone.Controller;

import java.io.IOException;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
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
			default:
				getAllProducts(request, response);
				break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		logger.info("Action: " + action);
		
		switch (action) {
			case "add":
				addProduct(request, response);
				break;
			default:
				getAllProducts(request, response);
				break;
		}
	}

	public void getAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		templateEngine.process("admin/Liste-produits", context, response.getWriter());
	}

	public void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("addProduct");
		int stock = 0;
		double price = 0;
		String name = request.getParameter("name");	
		String description = request.getParameter("description");
		String priceStr = request.getParameter("price");
		String stockStr = request.getParameter("stock");

		// Vérifier si les champs requis sont présents et non vides
		if (name == null || name.trim().isEmpty() || 
			description == null || description.trim().isEmpty() || 
			priceStr == null || priceStr.trim().isEmpty() || 
			stockStr == null || stockStr.trim().isEmpty()) {
			
			// Gérer les paramètres manquants ou vides
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
				// Convertir price et stock en utilisant trim() pour éliminer les espaces
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
			// Gérer le format de nombre invalide
			WebContext context = new WebContext(request, response, getServletContext());
			context.setVariable("errorMessage", "Invalid price or stock value. Please enter valid numbers.");
			templateEngine.process("admin/productform", context, response.getWriter());
		}
	}
	public void showAddProductForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebContext context = new WebContext(request, response, getServletContext());
		templateEngine.process("admin/productform", context, response.getWriter());
	}
}
