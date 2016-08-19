package uk.ac.soton.ldanalytics.piotre.server.app;

import java.util.List;
import java.util.UUID;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class AppDao {
	private Sql2o sql2o;
	
	public AppDao(Sql2o sql2o) {
		this.sql2o = sql2o;
	}
	
	public Iterable<App> getAllApps() {
		try (Connection conn = sql2o.open()) {
            List<App> apps = conn.createQuery("select * from apps")
                    .executeAndFetch(App.class);
            return apps;
        }
    }
	
	public App getApp(String id) {
		try (Connection conn = sql2o.open()) {
			List<App> app = conn.createQuery("select * from apps where id='"+id+"'")
	                .executeAndFetch(App.class);
	        return app.get(0);
		}
	}
	
	public boolean updateApp(String strId, String name, String author, String description, String uri) {
		UUID id = UUID.fromString(strId);
		try (Connection conn = sql2o.open()) {
			conn.createQuery("update apps set name=:name,author=:author,description=:description,uri=:uri where id=:id")
				.bind(new App(id,name,author,description,uri))
				.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	};
	
	public boolean addApp(String name, String author, String description, String uri) {
		UUID id = UUID.randomUUID();
		try (Connection conn = sql2o.open()) {
			conn.createQuery("insert into apps(id, name, author, description, uri) VALUES (:id, :name, :author, :description, :uri)")
				.bind(new App(id,name,author,description,uri))
				.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	};
}
