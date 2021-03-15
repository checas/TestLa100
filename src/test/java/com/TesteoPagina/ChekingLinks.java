package com.TesteoPagina;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ChekingLinks {
	
	private WebElement link;
	
	public ChekingLinks(WebElement link) {
		this.link = link;
	}
	
	public boolean checkingPageLinks(WebElement link) {
		String url = "";
		Boolean salida = false;
		HttpURLConnection httpConection = null;
		int responseCode = 200;
		link.getAttribute("href");
		if(url == null || url.isEmpty()) {
			System.out.println(url + "url is not configured or it is empty");
			salida = false;
		} 	
		try {
			httpConection = (HttpURLConnection)(new URL(url).openConnection());
			httpConection.setRequestMethod("HEAD");
			httpConection.connect();
			responseCode = httpConection.getResponseCode();
			if(responseCode > 400) {
				if(responseCode != 405) {
					System.out.println("ERROR BROKEN LINK: "+ responseCode + "-- " + url);
					salida = false;
				}
			} else {
				System.out.println("VALID LINK: -- " + url);
				salida = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return salida;
	}
}
