package mtwilson_CSCI201L_Assignment4;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public loginServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html;charset=UTF-8");
		Cookie[] cookies = request.getCookies();
	    if (cookies != null)
	        for (Cookie cookie : cookies) {
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
	        }
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		checkLogin loginCheck = new checkLogin();
		try {
			User user = loginCheck.loginCheck(username, password);
			if(user != null) {
				 String cookieID = username; 
				 Cookie cookie = new Cookie("username", cookieID);
				 cookie.setPath(request.getContextPath());
				 response.addCookie(cookie);
				 RequestDispatcher rd=request.getRequestDispatcher("home.html");  
			     rd.include(request,response); 
				
			}
			else {
				out.print("Sorry username or password error");  
		        RequestDispatcher rd=request.getRequestDispatcher("login.html");  
		        rd.include(request,response);  
			}
		}
		catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
