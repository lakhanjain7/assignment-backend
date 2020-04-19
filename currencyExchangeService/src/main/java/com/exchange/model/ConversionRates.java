package com.exchange.model;

import java.io.Serializable;
import java.util.HashMap;

public class ConversionRates implements Serializable{
	private String base;
	private String date;
	private HashMap<String, String> rates;
	
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public HashMap<String, String> getRates() {
		return rates;
	}
	public void setRates(HashMap<String, String> rates) {
		this.rates = rates;
	}
	
}
