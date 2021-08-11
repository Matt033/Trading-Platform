package mtwilson_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public registerServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String google = request.getParameter("google");
		System.out.println(google);
		if(google == null) {
			System.out.println("no google");
		}
		else {
			System.out.println("yes google");
		}
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        username = username.replaceAll("\\s", "");
        String password = request.getParameter("password");
        Cookie[] cookies = request.getCookies();
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = null;      
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment4?user=root&password=Matthewwilson033!");
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Users WHERE email = ? OR username=?");
			statement.setString(1, email);
			statement.setString(2, username);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				if(google != null) {
					String cookieID = username; 
					System.out.println(username);
					Cookie cookie = new Cookie("username", cookieID);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
					//out.print("success");
					RequestDispatcher rd=request.getRequestDispatcher("home.html");  
			        rd.include(request,response);  	
				}
				else {
					out.println("This email or username has already been used");
					RequestDispatcher rd=request.getRequestDispatcher("login.html");  
			        rd.include(request,response); 
				}
			}
			else {
				 PreparedStatement ps = con.prepareStatement("INSERT INTO Users (username, password, email, balance) VALUES(?,?,?,?)");
				 if(google == null) {
					 ps.setString(1, username);
				 }
				 else {
					ps.setString(1, cookies[0].getValue());
				 }
				 ps.setString(2, password);
				 ps.setString(3, email);
				 ps.setInt(4, 50000);
				 ps.executeUpdate();
				 String cookieID = username; 
				 Cookie cookie = new Cookie("username", cookieID);
				 cookie.setPath(request.getContextPath());
				 response.addCookie(cookie);
				 System.out.println("success");
				 //out.print("success");
				 
				 RequestDispatcher rd=request.getRequestDispatcher("home.html");  
			     rd.include(request,response);  	
			}
			
        }
        catch(SQLException e){
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
