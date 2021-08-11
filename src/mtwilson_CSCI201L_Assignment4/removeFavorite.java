package mtwilson_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class removeFavorite
 */
@WebServlet("/removeFavorite")
public class removeFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public removeFavorite() {
        super();
        // TODO Auto-generated constructor stub
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
			PreparedStatement ps = con.prepareStatement("DELETE FROM Favorites WHERE username=? and ticker=?");
			ps.setString(1, username);
			ps.setString(2, ticker);
			ps.executeUpdate();
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
