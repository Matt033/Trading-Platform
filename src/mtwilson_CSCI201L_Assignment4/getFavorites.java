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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Vector;

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




@WebServlet("/getFavorites")
public class getFavorites extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public getFavorites() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType("text/html;charset=UTF-8");
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
		
		Comparator<Favorites> StockComparator = new Comparator<Favorites>() {

			public int compare(Favorites s1, Favorites s2) {
			   String Stock1 = s1.getTicker().toUpperCase();
			   String Stock2 = s2.getTicker().toUpperCase();

			   return Stock1.compareTo(Stock2);
		    }
		};
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = null;      
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment4?user=root&password=Matthewwilson033!");
			 Statement sta = con.createStatement(); 
			 //String query = "SELECT * FROM Favorites WHERE username='"+username+"'";
			 String query = "SELECT ticker FROM Favorites WHERE username='"+username+"'";
	
		     ResultSet res = sta.executeQuery(query);
		     Vector<String> tickers = new Vector<String>();
		     while (res.next()) {
		         String tick = res.getString("ticker");
		         tickers.add(tick);
		    
		     }
		     
		     
		     Vector<Favorites> favorites = new Vector<Favorites>();
		     for(int i = 0; i < tickers.size(); i++){
		    	 Favorites fav = new Favorites();
		    	 HttpURLConnection urlConnection = null;
			     HttpURLConnection urlConnection3 = null;
			     StringBuilder result = new StringBuilder();
			     StringBuilder result3 = new StringBuilder();
		    	 fav.setTicker(tickers.get(i));
		    	 URL url = new URL("https://api.tiingo.com/tiingo/daily/"+tickers.get(i)+"?token=8ecdb1205642201096c240df4540937923211b4c\n");
		    	 URL url3 = new URL("https://api.tiingo.com/iex/?tickers="+tickers.get(i)+"&token=8ecdb1205642201096c240df4540937923211b4c\n");
		    	 urlConnection = (HttpURLConnection) url.openConnection();
		    	 urlConnection3 = (HttpURLConnection) url3.openConnection();
		    	 InputStream ins = new BufferedInputStream(urlConnection.getInputStream());
		    	 InputStream ins3 = new BufferedInputStream(urlConnection3.getInputStream());
		    	 BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		    	 BufferedReader reader3 = new BufferedReader(new InputStreamReader(ins3));
		    	 String line;
		    	 String line3;
		    	 while ((line = reader.readLine()) != null) {
		             result.append(line);
		         }
		    	 while ((line3 = reader3.readLine()) != null) {
		             result3.append(line3);
		         }
		    	 String lines = result.toString();
		    	 String lines3 = result3.toString();
		    	 lines3=lines3.replace("[", "");
		         lines3=lines3.replace("]", "");
		         lines=lines.replace("[", "");
		         lines=lines.replace("]", "");
		    	 JSONObject json = new JSONObject(lines);
		    	 JSONObject json3 = new JSONObject(lines3);
		    	 fav.setName(json.getString("name"));
		    	 fav.setLastPrice(json3.getDouble("tngoLast"));
		    	 fav.setPrevClose(json3.getDouble("prevClose"));
		    	 favorites.add(fav);
		     }
		     favorites.sort(StockComparator);
		     JSONArray array = new JSONArray();
		     for(int i = 0; i < favorites.size(); i++) {
		    	 JSONObject object = new JSONObject();
			     object.append("ticker", favorites.get(i).getTicker());
			     object.append("name", favorites.get(i).getName());
			     object.append("last", favorites.get(i).getLastPrice());
			     object.append("prev", favorites.get(i).getPrevClose());
			     array.put(object);
		     }
		     out.print(array);
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
