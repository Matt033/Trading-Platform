package mtwilson_CSCI201L_Assignment4;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;

/**
 * Servlet implementation class getPortfolio
 */
@WebServlet("/getPortfolio")
public class getPortfolio extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public getPortfolio() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Cookie[] cookies = request.getCookies();
		String username = "";
		if(cookies.length > 1) {
			username = cookies[1].getValue();
		}
		else {
			username = cookies[0].getValue();
		}
		
		Map<String, PortfolioClass> portfolioInfo = new HashMap<String, PortfolioClass>();
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 Connection con = null;      
			 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment4?user=root&password=Matthewwilson033!");
			 PreparedStatement statement = con.prepareStatement("SELECT * FROM Portfolio WHERE username = ?");
			 statement.setString(1, username);
			 ResultSet result = statement.executeQuery();
			 while(result.next()) {
				 String ticker = result.getString("ticker");
				 Double askPrice = result.getDouble("askPrice");
				 int quantity = result.getInt("quantity");
				 Double totalCost = result.getDouble("total");
				 if(!portfolioInfo.containsKey(ticker)) {
					 PortfolioClass object = new PortfolioClass(ticker, totalCost, quantity);
					 portfolioInfo.put(ticker, object);
				 }
				 else {
					 portfolioInfo.get(ticker).quantity += quantity;
					 portfolioInfo.get(ticker).totalCost += totalCost;
				 }
			 }
			
			 PreparedStatement state = con.prepareStatement("SELECT balance FROM Users WHERE username = ?");
			 state.setString(1, username);
			 ResultSet res = state.executeQuery();
			 Double balance = 0.0;
			 while(res.next()) {
				 balance = res.getDouble("balance");
			 }
			 JSONArray portArray = new JSONArray();
			 for(Map.Entry<String,PortfolioClass> entry : portfolioInfo.entrySet()) {
	            String ticker = entry.getKey();
	            HttpURLConnection urlConnection = null;
			     HttpURLConnection urlConnection3 = null;
			     StringBuilder results = new StringBuilder();
			     StringBuilder result3 = new StringBuilder();
	
		    	 URL url = new URL("https://api.tiingo.com/tiingo/daily/"+ticker+"?token=8ecdb1205642201096c240df4540937923211b4c\n");
		    	 URL url3 = new URL("https://api.tiingo.com/iex/?tickers="+ticker+"&token=8ecdb1205642201096c240df4540937923211b4c\n");
		    	 urlConnection = (HttpURLConnection) url.openConnection();
		    	 urlConnection3 = (HttpURLConnection) url3.openConnection();
		    	 InputStream ins = new BufferedInputStream(urlConnection.getInputStream());
		    	 InputStream ins3 = new BufferedInputStream(urlConnection3.getInputStream());
		    	 BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		    	 BufferedReader reader3 = new BufferedReader(new InputStreamReader(ins3));
		    	 String line;
		    	 String line3;
		    	 while ((line = reader.readLine()) != null) {
		             results.append(line);
		         }
		    	 while ((line3 = reader3.readLine()) != null) {
		             result3.append(line3);
		         }
		    	 String lines = results.toString();
		    	 String lines3 = result3.toString();
		    	 lines3=lines3.replace("[", "");
		         lines3=lines3.replace("]", "");
		         lines=lines.replace("[", "");
		         lines=lines.replace("]", "");
		    	 JSONObject json = new JSONObject(lines);
		    	 JSONObject json3 = new JSONObject(lines3);
		    	 portfolioInfo.get(ticker).name = json.getString("name");
		    	 portfolioInfo.get(ticker).prevClose = json3.getDouble("prevClose");
		    	 portfolioInfo.get(ticker).last = json3.getDouble("tngoLast");
		    	 JSONObject portObject = new JSONObject();
		    	 portObject.append("ticker", ticker);
		    	 portObject.append("name", json.getString("name"));
		    	 portObject.append("prevClose", json3.getDouble("prevClose"));
		    	 portObject.append("last", json3.getDouble("tngoLast"));
		    	 portObject.append("quantity", entry.getValue().quantity);
		    	 portObject.append("totalCost", entry.getValue().totalCost);
		    	 portObject.append("askPrice", json3.get("askPrice"));
		    	 portObject.append("bidPrice", json3.get("bidPrice"));
		    	 portArray.put(portObject);
		    }
			JSONObject balanced = new JSONObject();
			balanced.append("balance", balance);
			portArray.put(balanced);
			out.print(portArray);
			out.flush();
		 }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
		 catch(ClassNotFoundException e) {
			 e.printStackTrace();
		 }
		 catch(JSONException e) {
			 e.printStackTrace();
		 }
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
