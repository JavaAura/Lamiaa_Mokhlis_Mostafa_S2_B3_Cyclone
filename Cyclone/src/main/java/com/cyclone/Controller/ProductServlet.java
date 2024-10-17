package com.cyclone.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.context.WebContext;

import com.cyclone.Model.Product;
import com.cyclone.Service.ProductService;
import com.cyclone.Service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ProductServlet
 */

public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private TemplateEngine templateEngine;

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
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getAllProducts(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Handle POST requests if needed, or just call doGet
		doGet(request, response);
	}

	public void getAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> products = productService.getAllProducts();
		
		WebContext context = new WebContext(request, response, getServletContext());
		
		if (products == null || products.isEmpty()) {
			context.setVariable("errorMessage", "No products available.");
		} else {
			context.setVariable("products", products);

			for (Product product : products) {
				logger.info("Product: " + product.getName());
			}
		}
		
		templateEngine.process("Liste-produits", context, response.getWriter());
	}

}
