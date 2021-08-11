package mtwilson_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/homeBuyServlet")
public class homeBuyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public homeBuyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ticker = request.getParameter("ticker");
		PrintWriter out = response.getWriter();
		String quantity = request.getParameter("quantity");
		String ask = request.getParameter("ask");
		double askPrice = Double.valueOf(ask);
		int quant = Integer.valueOf(quantity);
		Cookie[] cookies = request.getCookies();
		String username = "";
		if(cookies.length > 1) {
			username = cookies[1].getValue();
		}
		else {
			username = cookies[0].getValue();
		}
		
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Connection con = null;      
			 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment4?user=root&password=Matthewwilson033!");
			 PreparedStatement statement = con.prepareStatement("SELECT balance FROM Users WHERE username = ?");
			 statement.setString(1, username);
			 ResultSet result = statement.executeQuery();
			 double balance = 0;
			 while(result.next()) {
				 balance = result.getInt("balance");
			 }
			 double totalBuy = quant * askPrice;
			 if(balance < totalBuy) {
				 out.print("FAILED: Purchase not possible");
				 out.flush();
			 }
			 else {
				 //update balance and insert stock into portfolio
				 balance -= quant * askPrice;
				 PreparedStatement ps = con.prepareStatement("INSERT INTO Portfolio (username, ticker, askPrice, quantity, total) VALUES(?,?,?,?,?)");
				 ps.setString(1, username);
				 ps.setString(2, ticker);
				 ps.setDouble(3, askPrice);
				 ps.setInt(4, quant);
				 ps.setDouble(5, askPrice*quant);
				 ps.executeUpdate();
				 
				 PreparedStatement p = con.prepareStatement("UPDATE Users SET balance=? WHERE username=? AND ID <> 0;");
				 p.setDouble(1, balance);
				 p.setString(2, username);
				 p.executeUpdate();
				 if(quant > 1) {
					 out.print("SUCCESS: Executed purchase of " + quant + " shares of "+ ticker + " for $" + (quant*askPrice));
				 }
				 else {
					 out.print("SUCCESS: Executed purchase of " + quant + " share of "+ ticker + " for $" + (quant*askPrice));
				 }
			 }
			 
		 }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
		 catch(ClassNotFoundException e) {
			 e.printStackTrace();
		 }
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
