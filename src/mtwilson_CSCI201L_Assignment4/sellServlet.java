package mtwilson_CSCI201L_Assignment4;

import java.io.Console;
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

/**
 * Servlet implementation class sellServlet
 */
@WebServlet("/sellServlet")
public class sellServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public sellServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ticker = request.getParameter("ticker");
		String quantity = request.getParameter("quantity");
		String ask = request.getParameter("last");
		PrintWriter out = response.getWriter();
		double last = Double.valueOf(ask);
		int quants = Integer.valueOf(quantity);
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
			 PreparedStatement statement = con.prepareStatement("SELECT * FROM Portfolio WHERE username = ? AND ticker = ?",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			 statement.setString(1, username);
			 statement.setString(2, ticker);
			 ResultSet result = statement.executeQuery();
			 while(result.next() && quant != 0) {
				 int rowQuant = result.getInt("quantity");
				 double totalCost = result.getDouble("total");
				 double dbask = result.getDouble("askPrice");
				 if(quant-rowQuant > 0) {
					 result.deleteRow();
					 quant -= rowQuant;
				 }
				 if(quant-rowQuant == 0) {
					 result.deleteRow();
					 break;
				 }
				 else {
					 rowQuant -= quant;
					 totalCost -= (quant * dbask);
					 result.updateInt("quantity", rowQuant);
					 result.updateDouble("total", totalCost);
					 result.updateRow();
					 quant = 0; 
				 }
			 }
			 
			 if(quants > 1) {
				 out.print("SUCCESS: Executed sale of " + quants + " shares of "+ ticker + " for $" + (quants*last));
			 }
			 else {
				 out.print("SUCCESS: Executed sale of " + quants + " share of "+ ticker + " for $" + (quants*last));
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
