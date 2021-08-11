package mtwilson_CSCI201L_Assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class checkLogin {
	public User loginCheck(String username, String password) throws SQLException,
    ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = null;      
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment4?user=root&password=Matthewwilson033!");

		PreparedStatement statement = con.prepareStatement("SELECT * FROM Users WHERE username = ? and password = ?");
		statement.setString(1, username);
        statement.setString(2, password);
        
        ResultSet result = statement.executeQuery();
        User user = null;
        if(result.next()) {
        	user = new User();
        	user.setUsername(result.getString("username"));
        }
        con.close();
        result.close();
        statement.close();
        return user;
        
	}
}
