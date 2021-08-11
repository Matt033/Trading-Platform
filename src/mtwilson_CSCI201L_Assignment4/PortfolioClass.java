package mtwilson_CSCI201L_Assignment4;

public class PortfolioClass {
	String ticker;
	Double totalCost;
	int quantity;
	String name;
	Double last;
	Double prevClose;
	
	public PortfolioClass(String ticker, Double totalCost, int quantity) {
		this.ticker = ticker;
		this.totalCost = totalCost;
		this.quantity = quantity;
		this.name = "";
		this.last = 0.0;
		this.prevClose = 0.0;
	}

}
