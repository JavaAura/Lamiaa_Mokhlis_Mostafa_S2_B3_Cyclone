package com.cyclone.Util;

import java.util.List;

import com.cyclone.Model.Product;
import com.cyclone.Service.ProductService;

public class Main {

	public static void main(String[] args) {

        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            System.out.println(product.getName());
        }
		

		
	}

}
