package com.biz.iolist.exec;

import com.biz.iolist.service.ProductService;

public class ProEx_03 {

	public static void main(String[] args) {

		ProductService ps = new ProductService();
		ps.deletePRO();
		ps.updatePRO();
	}

}
