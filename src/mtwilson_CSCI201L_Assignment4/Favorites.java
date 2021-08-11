package mtwilson_CSCI201L_Assignment4;



public class Favorites {
	private String ticker;
	private String name;
	private double lastPrice;
	private double prevClose;
	
	public void setTicker(String t) {
		this.ticker=t;
	}
	public String getTicker() {
		return this.ticker;
	}
	public void setName(String t) {
		this.name=t;
	}
	public String getName() {
		return this.name;
	}
	public void setLastPrice(double t) {
		this.lastPrice=t;
	}
	public Double getLastPrice() {
		return this.lastPrice;
	}
	public void setPrevClose(double t) {
		this.prevClose=t;
	}
	public Double getPrevClose() {
		return this.prevClose;
	}
	
	public String toString() {
		return ticker + " " + name + " " + lastPrice + " " + prevClose;
	}

}
