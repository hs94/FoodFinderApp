package cs499.examples.semesterproject;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class AndroidFoodBankOwnerServlet extends HttpServlet
{
	boolean debug = false;
	
	private String sqlliteDbUrl;

    @Override
    public void init(ServletConfig config) throws ServletException 
    {
        sqlliteDbUrl = config.getInitParameter("sqlliteDbUrl");
    }
	
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{  
	   res.setContentType ("TEXT/HTML");
	   PrintWriter out = res.getWriter ();
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Food Bank Data </h1> </body>");
		   out.println("</html");
	   }
	}
	
   public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (!debug)
		   res.setContentType("application/json");
	   else if (debug)
		   res.setContentType ("TEXT/HTML");
	   PrintWriter out = res.getWriter ();
	   String action = req.getParameter("action");
	   if (debug)
		   out.println("doPost Action: " + action);
	   if (action.equals("registerfoodplace"))
		  registerFoodPlace(out, req, res);
	   else if (action.equals("enterorganization"))
	   {
		   registerOrganization(out,req,res);
	   }
	   else if (action.equals("searchplaces"))
	   {
		   searchPlaces (out,req,res);
	   }
	   else if (action.equals("authenticate"))
	   {
		   authenticateUser (out,req,res);
	   }
	   else if (action.equals("additems"))
	   {
		   addItemsToPlace (out,req,res);
	   }
	   else if (action.equals("searchitems"))
	   {
		   searchItems (out,req,res);
	   }
	   else if (action.equals("requestitems"))
	   {
		   requestItem (out,req,res);
	   }
	   else if (action.equals("viewrequests"))
	   {
		   viewRequests (out,req,res);
	   }
   }
   
   public void viewRequests (PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
		Statement s = null;
	   	ResultSet rs = null;
		//int queryExecuted = 0;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   
	   try
	   {
		   s = c.createStatement();
		   System.out.println("ownername: " + req.getParameter("ownerName"));
		   
		   query = "select sender, organization.name as org_name ,itemname from request, foodplace, organization where " +
		   		"request.sender=organization.member and request.placename=foodplace.name and " +
		   		"foodplace.ownername=" + "'" + req.getParameter("ownerName") + "'";
		   rs = s.executeQuery(query);
		   
		   if (debug)
			   out.println("query: " + query);
		   rs = s.executeQuery(query);
		   if (rs != null)
		   {
			   System.out.println("Creating JSON....");
			  JSONArray jsonArray = this.convertToJSON(rs);
			  out.print(jsonArray);
			  out.flush();
		   }
		   else
		   {
			   out.println("Hey! Your resultset is null or empty");
			   out.flush();
		   }
		    c.close();
			s.close();
			
			out.println("</body> </html> ");
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
   }
   
   public void requestItem (PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Search Food Bank Data </h1>");
	   }
	   	Statement s = null;
	   	//ResultSet rs = null;
		int queryExecuted = 0;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   
	   try
	   {
		   String[] selectedItems = null;
		   System.out.println("senderName: " + req.getParameter("senderName"));
		   System.out.println("placeName: " + req.getParameter("placeName"));
		   System.out.println("selectedItems: " + req.getParameter("selectedItems"));
		   if (req.getParameter("selectedItems") != null)
		   {
			   selectedItems = req.getParameter("selectedItems").split(",");
		   }
		   if (selectedItems != null && selectedItems.length > 0)
		   {
			   s = c.createStatement();
			   for (int i = 0; i < selectedItems.length; i ++)
			   {
				   query = "insert into request (sender, placename, itemname)"
						   + " values (" + "'" + req.getParameter("senderName")
						   + "'" + ", " + "'" + req.getParameter("placeName")
						   + "'" + ", " + "'" + selectedItems[i]
						   + "'" + ")"; 
				   queryExecuted = s.executeUpdate(query);
			   }
			   c.close();
				s.close();
				out.println("Data inserted into db");
				out.println("</body> </html> ");
				System.out.println("Selected items inserted into db");
		   }
		   else
		   {
			   System.out.println("No selected items");
		   }
	   }
	   
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
   }
   
   public void searchItems (PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Search Food Bank Data </h1>");
	   }
	   	Statement s = null;
	   	ResultSet rs = null;
		//int queryExecuted = 0;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   
	   try
	   {
		   System.out.println(req.getParameter("placeName"));
		   s = c.createStatement();
		   query = "select * from items where foodplacename=" + "'" + req.getParameter("placeName")
				   + "'";
				   
		   if (debug)
			   out.println("query: " + query);
		   rs = s.executeQuery(query);
		   if (rs != null)
		   {
			   System.out.println("Creating JSON....");
			  JSONArray jsonArray = this.convertToJSON(rs);
			  out.print(jsonArray);
			  out.flush();
		   }
		   else
		   {
			   out.println("Hey! Your resultset is null or empty");
			   out.flush();
		   }
		    c.close();
			s.close();
			
			out.println("</body> </html> ");
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
   }
   
   public void addItemsToPlace(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Food Bank Data </h1>");
	   }
	   
	   Statement s = null;
	   	ResultSet rs = null;
	   	int queryExecuted = 0;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   
	   try
	   {
		   s = c.createStatement();
		   query = "select * from foodplace where ownername=" + "'" + req.getParameter("ownername") + "'";
		   if (debug)
			   out.println("query: " + query);
		   rs = s.executeQuery(query);
		   if (debug)
		   {
			   out.println("username: " + req.getParameter("ownername") + "<br/>");
			   out.println("itemName: " + req.getParameter("itemName") + "<br/>");
			   out.println("itemQuantity: " + req.getParameter("itemQuantity") + "<br/>");
			   out.println("Restaurant name: " + rs.getString("name") + "<br/>");
		   }
		   query = "insert into items (foodplacename, itemtype, itemname) values ("
				   + "'" + rs.getString("name") + "'" + ", " + req.getParameter("itemQuantity")
				   + ", " + "'" + req.getParameter("itemName") + "'" + ")"; 
		   queryExecuted = s.executeUpdate(query);
		   c.close();
			s.close();
			out.println("Data inserted into db");
			out.println("</body> </html> ");
			System.out.println("Data inserted into db");
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
   }
   
   public void authenticateUser(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Food Bank Data </h1>");
	   }
	   	Statement s = null;
	   	ResultSet rs = null;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   try
	   {
		   s = c.createStatement();
		   query = "select * from foodappuser where username=" + "'" + req.getParameter("loginUser") + "'";
		   if (debug)
			   out.println("query: " + query);
		   rs = s.executeQuery(query);
		  JSONArray jsonArray = this.convertToJSON(rs);
		  out.print(jsonArray);
		  out.flush();
		  
		  c.close();
			s.close();
			out.println("</body> </html> ");
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
	   
   }
   
   public JSONArray convertToJSON( ResultSet rs ) throws SQLException, JSONException
  {
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = rs.getMetaData();

    while(rs.next()) 
    {
      int numColumns = rsmd.getColumnCount();
      JSONObject obj = new JSONObject();

      for (int i=1; i<numColumns+1; i++) 
      {
        String column_name = rsmd.getColumnName(i);

        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
         obj.put(column_name, rs.getArray(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
         obj.put(column_name, rs.getBoolean(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
         obj.put(column_name, rs.getBlob(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
         obj.put(column_name, rs.getDouble(column_name)); 
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
         obj.put(column_name, rs.getFloat(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
         obj.put(column_name, rs.getNString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
         obj.put(column_name, rs.getString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
         obj.put(column_name, rs.getDate(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
        obj.put(column_name, rs.getTimestamp(column_name));   
        }
        else{
         obj.put(column_name, rs.getObject(column_name));
        }
      }

      json.put(obj);
    }

    return json;
  }
   public void searchPlaces(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   if (debug)
	   {
		   out.println("<html> <head> <title> FoodBank Test </title> </head>");
		   out.println("<body> <h1> Food Bank Data </h1>");
	   }
	   	Statement s = null;
	   	ResultSet rs = null;
		int queryExecuted = 0;
		String query = "";
		Connection c = getDBConnection();
		
	   if (c == null)
	   {
		   if (debug)
			   out.println("Could not connect to DB");
		   System.out.println("Could not connect to DB");
		   return;
	   }
	   
	   try
	   {
		   s = c.createStatement();
		   if (req.getParameter("searchCity").trim().equals("") && !req.getParameter("searchZip").trim().equals(""))
			   query = "select * from foodplace where zip=" + req.getParameter("searchZip");
		   else if (!req.getParameter("searchCity").trim().equals("") && req.getParameter("searchZip").trim().equals(""))
			   query = "select * from foodplace where city=" + "'" + req.getParameter("searchCity") + "'";
		   else
		   {
			   query = "select * from foodplace where zip=" + req.getParameter("searchZip")
			   			+ " and city=" + "'" + req.getParameter("searchCity") + "'";
		   }
		   if (debug)
			   out.println("query: " + query);
		   rs = s.executeQuery(query);
		   
		  JSONArray jsonArray = this.convertToJSON(rs);
		  out.print(jsonArray);
		  out.flush();
		  
		  c.close();
			s.close();
			
			out.println("</body> </html> ");
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace();
	   }
   }	
   
   
   public Connection getDBConnection()
   {
	   Connection c = null;
	   try 
		{
			//Class.forName("oracle.jdbc.driver.OracleDriver");
		   Class.forName("org.sqlite.JDBC");
		   String dbPath = "";
		   //c = DriverManager.getConnection("jdbc:sqlite:/Users/haaris/Workspace/SQLite/sqlite-shell-win32-x86-3080100/foodbank.db");
		   //c = DriverManager.getConnection("jdbc:sqlite:/Users/haaris/apache-tomcat-7.0.47/webapps/foodapp/foodbank.db");
		   c = DriverManager.getConnection(sqlliteDbUrl);

		   return c;
		} 
		
		catch (Exception e) 
		{
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return null;
		}
   }
   public void registerFoodPlace(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   Statement s = null;
		int queryExecuted = 0;
		String query = "";
	   	  Connection c = getDBConnection();
	   	  if (c == null)
		   {
			   out.println("Could not connect to DB");
			   System.out.println("Could not connect to DB");
			   return;
		   }
		   	  out.println("<html> <head> </head> <body> ");
		      out.println("Place name: " + req.getParameter("foodPlaceName"));
		      out.println("Place address: " + req.getParameter("foodPlaceStreetAddress")
		                        + ", " + req.getParameter("foodPlaceCity") + "," 
		                        + req.getParameter("foodPlaceState") + 
		                        req.getParameter("foodPlaceZip"));
		      System.out.println("Place name: " + req.getParameter("foodPlaceName"));
		      System.out.println("Place address: " + req.getParameter("foodPlaceStreetAddress")
		                        + ", " + req.getParameter("foodPlaceCity") + "," 
	                        + req.getParameter("foodPlaceState") + 
	                        req.getParameter("foodPlaceZip"));
	      
	      
			
			try
			{
				//c = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521/XE", "system", "admin");
				//c = DriverManager.getConnection("jdbc:oracle:thin:@10.159.226.169:1521/XE", "system", "admin");
				
				s = c.createStatement();
				
				query = "insert into foodappuser (username, password, usertype) values (" + "'"+ req.getParameter("foodPlaceOwnerName") + "'" 
						+ ", " + "'" + req.getParameter("foodPlaceOwnerPassword") + "'" + ", 'owner')";
				out.println("query: " + query);
				queryExecuted = s.executeUpdate(query);
				
				query = "insert into foodplace (ownername, name, streetaddress, city, state, zip, phonenumber)"
						+ "values (" +"'"+ req.getParameter("foodPlaceOwnerName") + "'" 
						+ ", " + "'" + req.getParameter("foodPlaceName") + "'" +", '" + req.getParameter("foodPlaceStreetAddress") + "', '" 
						+ req.getParameter("foodPlaceCity")
						+ "'" + ", '" + req.getParameter("foodPlaceState") + "'" + ", " + req.getParameter("foodPlaceZip") + ", "
						+ req.getParameter("foodPlacePhone") + ")";
				out.println("query: " + query);
				queryExecuted = s.executeUpdate(query);
				
				c.close();
				s.close();
				out.println("Data inserted into db");
				out.println("</body> </html> ");
				System.out.println("Data inserted into db");
			}
			catch (SQLException e) 
			{
				out.println("Exception: could not insert data" + e.getMessage());
				out.println("</body> </html> ");
				e.printStackTrace();
				return;
			}
   }
   
   public void registerOrganization(PrintWriter out, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
	   Statement s = null;
		String query = "";
		int queryExecuted = 0;
	   	  Connection c = getDBConnection();
	   	  if (c == null)
		   {
			   out.println("Could not connect to DB");
			   System.out.println("Could not connect to DB");
			   return;
		   }
	      
			
			try
			{
				//c = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521/XE", "system", "admin");
				//c = DriverManager.getConnection("jdbc:oracle:thin:@10.159.226.169:1521/XE", "system", "admin");
				
				s = c.createStatement();
				
				query = "insert into foodappuser (username, password, usertype) values (" + "'"+ req.getParameter("memberName") + "'" 
						+ ", " + "'" + req.getParameter("memberPassword") + "'" + ", 'organization')";
				out.println("query: " + query);
				queryExecuted = s.executeUpdate(query);
				
				query = "insert into organization (member, name) values (" + "'" 
						+req.getParameter("memberName")+"', " + "'" + 
						req.getParameter("name")+"')";
				out.println("query: " + query);
				queryExecuted = s.executeUpdate(query);
				
				c.close();
				s.close();
				out.println("Data inserted into db");
				out.println("</body> </html> ");
				System.out.println("Data inserted into db");
			}
			catch (SQLException e) 
			{
				out.println("Exception: could not insert data" + e.getMessage());
				out.println("</body> </html> ");
				e.printStackTrace();
				return;
			}
   }
  
}