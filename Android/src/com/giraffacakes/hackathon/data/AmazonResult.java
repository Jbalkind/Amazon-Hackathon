package com.giraffacakes.hackathon.data;

import java.util.List;

public class AmazonResult extends Result {
	private List<Product> products;
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
