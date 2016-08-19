package uk.ac.soton.ldanalytics.piotre.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.debug.DebugScreen.enableDebugScreen;

import org.sql2o.Sql2o;

import uk.ac.soton.ldanalytics.piotre.server.app.AppController;
import uk.ac.soton.ldanalytics.piotre.server.app.AppDao;
import uk.ac.soton.ldanalytics.piotre.server.data.DataController;
import uk.ac.soton.ldanalytics.piotre.server.data.DataDao;
import uk.ac.soton.ldanalytics.piotre.server.index.IndexController;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.MappingController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.MappingDao;
import uk.ac.soton.ldanalytics.piotre.server.model.Model;
import uk.ac.soton.ldanalytics.piotre.server.user.UserDao;
import uk.ac.soton.ldanalytics.piotre.server.util.Filters;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

import com.beust.jcommander.JCommander;

public class Application {

    // Declare dependencies
    public static UserDao userDao;
    public static DataDao dataDao;
    public static AppDao appDao;
    public static MappingDao mappingDao;

    public static void main(String[] args) {
    	CommandLineOptions options = new CommandLineOptions();
        new JCommander(options, args);
    	
    	//DB connection
    	Sql2o sql2o = new Sql2o("jdbc:h2:" + options.db, options.username, options.password);
    	Model.prepareDB(sql2o);
    	
        // Instantiate your dependencies
        userDao = new UserDao();
        dataDao = new DataDao(sql2o);
        appDao = new AppDao(sql2o);
        mappingDao = new MappingDao(sql2o);

        // Configure Spark
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.INDEX,			IndexController.serveIndexPage);
        get(Path.Web.DATA,			DataController.fetchData);
        get(Path.Web.DATUM,			DataController.fetchDatum);
        get(Path.Web.DATA_ADD,		DataController.addData);
        get(Path.Web.APPS,			AppController.fetchApps);
        get(Path.Web.APPS_ADD,		AppController.addApps);
        get(Path.Web.APP,			AppController.fetchApp);
        get(Path.Web.MAPPINGS,		MappingController.fetchMappings);
        get(Path.Web.LOGIN,			LoginController.serveLoginPage);
        post(Path.Web.LOGIN,		LoginController.handleLoginPost);
        post(Path.Web.DATA_ADD,		DataController.handleAddDataPost);
        post(Path.Web.APPS_ADD,		AppController.handleAddAppPost);
        post(Path.Web.DATUM,		DataController.handleUpdateDataPost);
        post(Path.Web.APP,			AppController.handleUpdateAppPost);
        post(Path.Web.LOGOUT,		LoginController.handleLogoutPost);
        get("*",                    ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);

    }

}
