package mtwilson_CSCI201L_Assignment4;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.group.Response;

import java.sql.*;

/**
 * Servlet implementation class favoritesServlet
 */
@WebServlet("/favoritesServlet")
public class favoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public favoritesServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ticker = request.getParameter("ticker");
		PrintWriter out = response.getWriter();
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
			 PreparedStatement statement = con.prepareStatement("SELECT * FROM Favorites WHERE username = ? and ticker = ?");
			 statement.setString(1, username);
			 statement.setString(2, ticker);
			 ResultSet result = statement.executeQuery();
			 if(result.next()) {
				out.println("This ticker is already in favorites");
				/*
				RequestDispatcher rd=request.getRequestDispatcher("register.html");  
		        rd.include(request,response);  	
		        */
			 }
			 else {
				 PreparedStatement ps = con.prepareStatement("INSERT INTO Favorites (username, ticker) VALUES(?,?)");
				 ps.setString(1, username);
				 ps.setString(2, ticker);
				 ps.executeUpdate();
				 
				 Statement sta = con.createStatement(); 
				 String query = "SELECT * FROM Favorites WHERE username='"+username+"'";
			     ResultSet res = sta.executeQuery(query);
			     while (res.next()) {
			         String user = res.getString("username");
			         String tick = res.getString("ticker");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
