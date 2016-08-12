package uk.ac.soton.ldanalytics.piotre.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import uk.ac.soton.ldanalytics.piotre.server.data.Data;
import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;

public class Model {	
	public static void prepareDB(Sql2o sql2o) {
		//test if db has been created
		try (Connection conn = sql2o.beginTransaction()) {
			conn.createQuery("CREATE TABLE data (id uuid primary key,name varchar,author varchar, description varchar, type varchar);")
            	.executeUpdate();
			conn.commit();
		} catch(Sql2oException e) {
			//if tables already exist, just exit the prep
			return;
		}
		
		String adminName = "admin";
		String adminPassword = "password";
		String adminSalt = BCrypt.gensalt();
		String adminHashedPassword = BCrypt.hashpw(adminPassword,adminSalt);
		
		try (Connection conn = sql2o.beginTransaction()) {
			conn.createQuery("CREATE TABLE user (username varchar,salt varchar,hashedPassword varchar);")
            	.executeUpdate();
			conn.createQuery("insert into user(username, salt, hashedPassword) VALUES (:username, :salt, :hashedPassword)")
        		.addParameter("username",adminName)
        		.addParameter("salt",adminSalt)
        		.addParameter("hashedPassword",adminHashedPassword)
        		.executeUpdate();
			
			List<Data> data = new ArrayList<Data>();
			data.add(new Data(UUID.randomUUID(),"A Sample Store",adminName,"An example H2 relational database with sample weather data and corresponding mappings.",DataType.STORE));
			data.add(new Data(UUID.randomUUID(),"A Sample Stream",adminName,"An example stream from a smart home and corresponding mappings.",DataType.STREAM));
			data.forEach((datum) -> conn.createQuery("insert into data(id, name, author, description, type) VALUES (:id, :name, :author, :description, :type)")
            	.addParameter("id",datum.getId())
            	.addParameter("name",datum.getName())
            	.addParameter("author",datum.getAuthor())
            	.addParameter("description",datum.getDescription())
            	.addParameter("type",datum.getType().toString())
            	.executeUpdate());
			conn.commit();
		}
	}
}
