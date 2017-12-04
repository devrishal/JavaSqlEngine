package com.rishal.valueObjects;
/**
Value object file to hold the  CSV Data
**/
public class TableData {
	private String title;
	private int brand;
	private int store;
	private double price;
	private boolean in_stock;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getBrand() {
		return brand;
	}
	public void setBrand(int brand) {
		this.brand = brand;
	}
	public int getStore() {
		return store;
	}
	public void setStore(int store) {
		this.store = store;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isIn_stock() {
		return in_stock;
	}
	public void setIn_stock(boolean in_stock) {
		this.in_stock = in_stock;
	}
	public String selectStar()
	{
		return ""+getTitle()+","+getBrand()+","+getStore()+","+getPrice()+","+isIn_stock();
	}
	
}
