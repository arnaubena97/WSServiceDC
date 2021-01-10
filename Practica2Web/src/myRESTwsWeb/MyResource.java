package myRESTwsWeb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("")
public class MyResource{
	@Path("/text")
    @POST
    public String getExam(String[] insert) throws NamingException, SQLException {
		String strResultado = "";
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null) {
            Connection connection = ds.getConnection();
            PreparedStatement preparedStatement;
            
            if(insert[0].equals("I")) {
            	preparedStatement = connection.prepareStatement("SELECT * FROM public.exam WHERE idexam =" + insert[1]);
            } else {
            	preparedStatement = connection.prepareStatement("SELECT * FROM public.exam WHERE description LIKE '%" + insert[1] + "%'");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
            	strResultado += resultSet.getString("idexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("description");
            	strResultado += " - ";
            	strResultado += resultSet.getString("dataexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("timeexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("locationexam");
                strResultado += "?";
        	}
            return strResultado;
        }
        return "ERROR";
    }
	
	@Path("/storeexams")
    @POST
    public String getExams(String insert) throws NamingException, SQLException {
		String strResultado = "";
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null) {
            Connection connection = ds.getConnection();
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("SELECT * FROM public.exam");
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
            	strResultado += resultSet.getString("idexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("description");
            	strResultado += " - ";
            	strResultado += resultSet.getString("dataexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("timeexam");
            	strResultado += " - ";
            	strResultado += resultSet.getString("locationexam");
                strResultado += "?";
        	}
            return strResultado;
        }
        return "ERROR";
    }
	
	@Path("/storegrade")
    @POST
    public String getGrades(String insert) throws NamingException, SQLException {
		String strResultado = "";
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null) {
            Connection connection = ds.getConnection();
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            
            if(insert.equals("")){
            	preparedStatement = connection.prepareStatement("SELECT * FROM public.grades");
            } else {
            	preparedStatement = connection.prepareStatement("SELECT iduser FROM public.users WHERE code=" + insert);
            	resultSet = preparedStatement.executeQuery();
            	resultSet.next();
            	preparedStatement = connection.prepareStatement("SELECT * FROM public.grades WHERE idstudent=" + resultSet.getString("iduser"));
            }
            
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()) {
            	strResultado += " idgrade: ";
            	strResultado += resultSet.getString("idgrade");
            	strResultado += ", idexam: ";
            	strResultado += resultSet.getString("idexam");
            	strResultado += ", idstudent: ";
            	strResultado += resultSet.getString("idstudent");
            	strResultado += ", grade: ";
            	strResultado += resultSet.getString("grade");
                strResultado += "?";
        	}
            return strResultado;
        }
        return "ERROR";
    }
	
	@Path("/validate")
    @POST
    public String validate(String user) throws NamingException, SQLException {
		InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	PreparedStatement st = connection.prepareStatement("SELECT profile FROM public.users WHERE code=" + user);
        	ResultSet resultSet = st.executeQuery();
        	
        	if(!resultSet.next()) {
        		return "false";
        	} else {
        		return resultSet.getString("profile");
        	}
        } else {
        	return "false";
        }
    }
	
	@POST
    @Path("/adduser")
    public void addUser(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	Statement st = connection.createStatement();
        	st.executeUpdate("INSERT INTO public.users (iduser, code, profile) VALUES (DEFAULT, '" + insert[1] + "', '" + insert[2] + "')");
            connection.close();
            st.close();
        }
    }
	
	@POST
    @Path("/post")
    public String crearExam(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	PreparedStatement pst = connection.prepareStatement("SELECT idexam FROM public.exam WHERE description='" + insert[1] + "' AND dataexam='"
        			+ insert[2] + "' AND timeexam='" + insert[3] + "' AND locationexam='" + insert[4] + "'");
        	ResultSet resultSet = pst.executeQuery();
        	
        	if(!resultSet.next()) {
	        	connection = ds.getConnection();
	        	Statement st = connection.createStatement();
	        	st.executeUpdate("INSERT INTO public.exam (idexam, description, dataexam, timeexam, locationexam) "
	            		+ "VALUES (DEFAULT, '" + insert[1] + "', '" + insert[2] + "', '" + insert[3] + "', '" + insert[4] + "')");
	            connection.close();
	            st.close();
	            return "Exam added succesfully";
        	} else {
        		return "Exam already exists..";
        	}
        } else {
        	return "ERROR";
        }
    }
	
	@POST
    @Path("/addgrades")
    public String addGrades(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	PreparedStatement pst = connection.prepareStatement("SELECT idgrade FROM public.grades WHERE (idexam=" + insert[1] + "AND idstudent=" + insert[2] + ")");
        	PreparedStatement examExist = connection.prepareStatement("SELECT idexam FROM public.exam WHERE idexam=" + insert[1]);
        	PreparedStatement studentExist = connection.prepareStatement("SELECT iduser FROM public.users WHERE iduser=" + insert[2]);
        	ResultSet resultSet = pst.executeQuery();
        	ResultSet resultSet1 = examExist.executeQuery();
        	ResultSet resultSet2 = studentExist.executeQuery();
        	
        	if(!resultSet.next() && resultSet1.next() && resultSet2.next()) {
        		connection = ds.getConnection();
            	Statement st = connection.createStatement();
            	st.executeUpdate("INSERT INTO public.grades(idgrade, idexam, idstudent, grade) VALUES(DEFAULT, '" + insert[1] + "', '" + insert[2] + "', '" + insert[3] + "')");
                connection.close();
                st.close();
                return "Add grade successful!";
        	} else {
        		return "This grade already exists";
        	}
        }
        return "ERROR IN ADD GRADES";
    }
	
	@POST
    @Path("/mod")
    public void modifyExam(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	Statement st = connection.createStatement();
        	st.executeUpdate("UPDATE public.exam SET description='" + insert[1] + "', dataexam='" + insert[2] + "', timeexam='" + insert[3] + "', locationexam='" + insert[4] + "' WHERE idexam=" + insert[0]);
            connection.close();
            st.close();
        }
    }
	
	@POST
    @Path("/moddesc")
    public void modifyDescription(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	Statement st = connection.createStatement();
        	st.executeUpdate("UPDATE public.exam SET description='" + insert[1] + "' WHERE idexam=" + insert[0]);
            connection.close();
            st.close();
        }
    }
	
	@POST
    @Path("/del")
    public void deleteExam(String[] insert) throws NamingException, SQLException  {
    	InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource) cxt.lookup("java:/PostgresXADS");
        if(ds != null){
        	Connection connection = ds.getConnection();
        	Statement st = connection.createStatement();
        	st.executeUpdate("DELETE FROM public.exam WHERE idexam=" + insert[0] + " AND " + insert[0] + " NOT IN (SELECT idexam FROM public.grades)");
            connection.close();
            st.close();
        }
    }
}