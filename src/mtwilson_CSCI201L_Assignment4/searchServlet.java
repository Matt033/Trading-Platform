package mtwilson_CSCI201L_Assignment4;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.mysql.cj.xdevapi.JsonArray;
import java.lang.Object;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class searchServlet
 */
@WebServlet("/searchServlet")
public class searchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public searchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String ticker = request.getParameter("searchword");
		HttpURLConnection urlConnection = null;
		HttpURLConnection urlConnection2 = null;
		HttpURLConnection urlConnection3 = null;
		
		StringBuilder result = new StringBuilder();
		StringBuilder result2 = new StringBuilder();
		StringBuilder result3 = new StringBuilder();
		try {
			URL url = new URL("https://api.tiingo.com/tiingo/daily/"+ticker+"/prices?token=8ecdb1205642201096c240df4540937923211b4c\n"
					+ "");
			URL url2 = new URL("https://api.tiingo.com/tiingo/daily/"+ticker+"?token=8ecdb1205642201096c240df4540937923211b4c\n");
			URL url3 = new URL("https://api.tiingo.com/iex/?tickers="+ticker+"&token=8ecdb1205642201096c240df4540937923211b4c\n");
			
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection2 = (HttpURLConnection) url2.openConnection();
			urlConnection3 = (HttpURLConnection) url3.openConnection();
	
		
		
		InputStream ins = new BufferedInputStream(urlConnection.getInputStream());
		InputStream ins2 = new BufferedInputStream(urlConnection2.getInputStream());
		InputStream ins3 = new BufferedInputStream(urlConnection3.getInputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(ins2));
		BufferedReader reader3 = new BufferedReader(new InputStreamReader(ins3));
		String line;
		String line2;
		String line3;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
       
        while ((line2 = reader2.readLine()) != null) {
            result2.append(line2);
        }
        while ((line3 = reader3.readLine()) != null) {
            result3.append(line3);
        }
        String lines2 = result2.toString();
        String lines = result.toString();
        String lines3 = result3.toString();
        lines=lines.replace("[", "");
        lines=lines.replace("]", "");
        lines3=lines3.replace("[", "");
        lines3=lines3.replace("]", "");
        lines3 =lines3.replace("last", "newlast");
        lines3 =lines3.replace("high", "newhigh");
        lines3 =lines3.replace("low", "newlow");
        lines3 =lines3.replace("open", "newopen");
        lines3 =lines3.replace("ticker", "newticker");
        lines3 =lines3.replace("volume", "newvolume");
        

			JSONObject json = new JSONObject(lines2);
			JSONObject json2 = new JSONObject(lines);
			JSONObject json3 = new JSONObject(lines3);
			System.out.println(json3);
			Iterator<String> keys = json2.keys();
			Iterator<String> keys2 = json3.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				json.append(key,json2.get(key));
			}
			while(keys2.hasNext()) {
				String key = keys2.next();
				json.append(key,json3.get(key));
			}
			json.append("error", "false");
			out.print(json);
	        out.flush();
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JSONObject j = new JSONObject();
			try {
				j.append("error","invalid ticker cannot perform search");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(j);
			out.flush();
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
