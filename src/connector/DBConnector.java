package connector;

import java.sql.*;


public class DBConnector {
	private String user;
	private String password;
	private String sid;
	private String host;
	private String url;
	
	
	private Connection oracleConn;
	
	public DBConnector(String user, String password, String sid, String host){
		this.user = user;
		this.password = password;
		this.sid = sid;
		this.host = host;		
	}
	
	public void intialization(){
		url = "jdbc:oracle:thin:@" + host + ":" + sid;		
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            oracleConn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }		
	}
	
	public ResultSet query(String query){
        ResultSet resultSet = null;
        try {
            Statement statement;
            statement = oracleConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
	
	public void finalize(){
        try {
        	oracleConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
