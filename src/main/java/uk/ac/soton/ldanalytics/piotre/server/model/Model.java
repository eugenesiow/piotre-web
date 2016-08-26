package uk.ac.soton.ldanalytics.piotre.server.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import uk.ac.soton.ldanalytics.piotre.server.app.App;
import uk.ac.soton.ldanalytics.piotre.server.data.Data;
import uk.ac.soton.ldanalytics.piotre.server.data.Data.DataType;
import uk.ac.soton.ldanalytics.piotre.server.mapping.Mapping;
import uk.ac.soton.ldanalytics.piotre.server.metadata.MetadataItem;
import uk.ac.soton.ldanalytics.piotre.server.metadata.SchemaItem;
import uk.ac.soton.ldanalytics.piotre.server.relation.Relation;

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
			//create user table and add admin
			conn.createQuery("CREATE TABLE user (username varchar,salt varchar,hashedPassword varchar);")
            	.executeUpdate();
			conn.createQuery("insert into user(username, salt, hashedPassword) VALUES (:username, :salt, :hashedPassword)")
        		.addParameter("username",adminName)
        		.addParameter("salt",adminSalt)
        		.addParameter("hashedPassword",adminHashedPassword)
        		.executeUpdate();
			
			//Add metadata schema store
			conn.createQuery("CREATE TABLE metadata_schema (category varchar, name varchar,type varchar,sortorder int,data varchar);")
    			.executeUpdate();
			String dataCategory = "data";
			String storeType = "store";
			List<SchemaItem> schema = new ArrayList<SchemaItem>();
			schema.add(new SchemaItem(dataCategory,"jdbc_url",storeType,0,"{\"caption\":\"JDBC URL\",\"type\":\"text\",\"placeholder\":\"e.g. jdbc:postgresql://host:port/database\"}"));
			schema.add(new SchemaItem(dataCategory,"username",storeType,1,"{\"caption\":\"Username\",\"type\":\"text\",\"placeholder\":\"e.g. sa\"}"));
			schema.add(new SchemaItem(dataCategory,"password",storeType,2,"{\"caption\":\"Password\",\"type\":\"text\",\"placeholder\":\"e.g. sa_password\"}"));
			storeType = "stream";
			schema.add(new SchemaItem(dataCategory,"stream_uri",storeType,0,"{\"caption\":\"Stream URI\",\"type\":\"text\",\"placeholder\":\"e.g. http://www.cwi.nl/SRBench/observations\"}"));
			schema.forEach((schemaItem)->conn.createQuery("insert into metadata_schema(category, name, type, sortorder, data) VALUES (:category, :name, :type, :sortorder, :data)")
					.bind(schemaItem)
					.executeUpdate());
			
			//Add metadata store
			conn.createQuery("CREATE TABLE metadata (itemId uuid, name varchar,data varchar);")
    			.executeUpdate();
			
			//create sample data
			List<Data> data = new ArrayList<Data>();
			UUID sampleStoreId = UUID.randomUUID();
			UUID sampleStreamId = UUID.randomUUID();			
			data.add(new Data(sampleStoreId,"A Sample Store",adminName,"An example H2 relational database with sample weather data and corresponding mappings.",DataType.STORE));
			data.add(new Data(sampleStreamId,"A Sample Stream",adminName,"An example stream from a smart home and corresponding mappings.",DataType.STREAM));
			data.forEach((datum) -> conn.createQuery("insert into data(id, name, author, description, type) VALUES (:id, :name, :author, :description, :type)")
            	.addParameter("id",datum.getId())
            	.addParameter("name",datum.getName())
            	.addParameter("author",datum.getAuthor())
            	.addParameter("description",datum.getDescription())
            	.addParameter("type",datum.getType().toString())
            	.executeUpdate());
			
			//create sample metadata for the data
			List<MetadataItem> metadata = new ArrayList<MetadataItem>();
			metadata.add(new MetadataItem(sampleStoreId,"jdbc_url","jdbc:h2:./smarthome"));
			metadata.add(new MetadataItem(sampleStoreId,"username","sa"));
			metadata.add(new MetadataItem(sampleStoreId,"password",""));
			metadata.add(new MetadataItem(sampleStreamId,"stream_uri","http://www.cwi.nl/SRBench/observations"));
			metadata.forEach((metadatum) -> conn.createQuery("insert into metadata(itemId, name, data) VALUES (:itemId, :name, :data)")
	            	.bind(metadatum)
	            	.executeUpdate());
			
			//create apps table and add samples
			conn.createQuery("CREATE TABLE apps (id uuid primary key,name varchar,author varchar,description varchar,uri varchar);")
	        	.executeUpdate();
			List<App> apps = new ArrayList<App>();
			UUID sampleStoreAppId = UUID.randomUUID();
			UUID sampleStreamAppId = UUID.randomUUID();			
			apps.add(new App(sampleStoreAppId,"Smart Home Historical Analytics",adminName,"Visual Analytics for Historical Smart Home Data","http://pi.webobservatory.me/"));
			apps.add(new App(sampleStreamAppId,"Smart Home Real-time Dashboard",adminName,"An example, highly extensible, real-time dashboard based on FreeBoard.","http://freeboard.io/"));
			apps.forEach((app) -> conn.createQuery("insert into apps(id, name, author, description, uri) VALUES (:id, :name, :author, :description, :uri)")
            	.bind(app)
            	.executeUpdate());
			
			//create mappings table and add sample mappings
			conn.createQuery("CREATE TABLE mappings (id uuid primary key,name varchar,author varchar,uri varchar,content clob,format clob);")
        		.executeUpdate();
			String smarthomeEnvironmentContent = FileUtils.readFileToString(new File(Model.class.getClassLoader().getResource("mappings/smarthome_environment.nt").getFile()));
			String smarthomeMeterContent = FileUtils.readFileToString(new File(Model.class.getClassLoader().getResource("mappings/smarthome_meter.nt").getFile()));
			List<Mapping> mappings = new ArrayList<Mapping>();
			UUID sampleSmarthomeEnvironmentId = UUID.randomUUID();
			UUID sampleSmarthomeMeterId = UUID.randomUUID();
			mappings.add(new Mapping(sampleSmarthomeEnvironmentId,"smarthome_environment",adminName,"http://iot.soton.ac.uk/smarthome/environment#",smarthomeEnvironmentContent,null));
			mappings.add(new Mapping(sampleSmarthomeMeterId,"smarthome_meter",adminName,"http://iot.soton.ac.uk/smarthome/meter#",smarthomeMeterContent,null));
			mappings.forEach((mapping) -> conn.createQuery("insert into mappings(id, name, author, uri, content,format) VALUES (:id, :name, :author, :uri, :content, :format)")
            	.bind(mapping)
            	.executeUpdate());
			
			//create mappings-data relations table and add sample relations
			conn.createQuery("CREATE TABLE rel_mappings_data (id1 uuid,id2 uuid);")
    			.executeUpdate();
			List<Relation> relations = new ArrayList<Relation>();
			relations.add(new Relation(sampleSmarthomeEnvironmentId,sampleStoreId));
			relations.forEach((relation) -> conn.createQuery("insert into rel_mappings_data(id1, id2) VALUES (:id1, :id2)")
	            	.bind(relation)
	            	.executeUpdate());
			
			//create apps-data relations table and add sample relations
			conn.createQuery("CREATE TABLE rel_apps_data (id1 uuid,id2 uuid);")
    			.executeUpdate();
			
			conn.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
