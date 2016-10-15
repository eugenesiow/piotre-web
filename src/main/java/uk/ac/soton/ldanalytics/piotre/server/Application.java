package uk.ac.soton.ldanalytics.piotre.server;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.redirect;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;
import static spark.debug.DebugScreen.enableDebugScreen;

import org.sql2o.Sql2o;
import org.zeromq.ZMQ;

import com.beust.jcommander.JCommander;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import uk.ac.soton.ldanalytics.piotre.server.app.AppController;
import uk.ac.soton.ldanalytics.piotre.server.app.AppDao;
import uk.ac.soton.ldanalytics.piotre.server.data.DataController;
import uk.ac.soton.ldanalytics.piotre.server.data.DataDao;
import uk.ac.soton.ldanalytics.piotre.server.index.IndexController;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.MappingController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.MappingDao;
import uk.ac.soton.ldanalytics.piotre.server.metadata.MetadataController;
import uk.ac.soton.ldanalytics.piotre.server.model.Model;
import uk.ac.soton.ldanalytics.piotre.server.overview.OverviewController;
import uk.ac.soton.ldanalytics.piotre.server.query.QueryController;
import uk.ac.soton.ldanalytics.piotre.server.query.QueryDao;
import uk.ac.soton.ldanalytics.piotre.server.query.QueryListener;
import uk.ac.soton.ldanalytics.piotre.server.user.UserDao;
import uk.ac.soton.ldanalytics.piotre.server.util.Filters;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class Application {

    // Declare dependencies
    public static UserDao userDao;
    public static DataDao dataDao;
    public static AppDao appDao;
    public static MappingDao mappingDao;
    public static QueryDao queryDao;
    public static EPServiceProvider epService;
    public static ZMQ.Context context;

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
        queryDao = new QueryDao(sql2o);

        // Configure Spark
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();
        
        //Load Streaming Engine
        Configuration engineConfig = new Configuration();
        epService = EPServiceProviderManager.getDefaultProvider(engineConfig);
        context = ZMQ.context(1); //Prepare ZMQ context
        QueryController.SetupStreams(epService);
        webSocket("/events", EventsWebSocket.class);
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.bind("tcp://localhost:6000");
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        String stmtStr = "SELECT\n" + 
        		"       environmental.insideTemp AS currentTemp, \n" +
        		"		environmental.insideHumidity AS currentHumidity, \n" + 
        		"		environmental.windSpeed AS currentWindSpeed, \n" + 
        		"		environmental.windGust AS currentWindGust, \n" + 
        		"		environmental.windGustDirectionDegrees AS currentWindDirection \n" + 
        		"   FROM\n" + 
        		"        environmental.std:lastevent()";
//        EPStatement statement = epService.getEPAdministrator().createEPL(stmtStr);
//        statement.addListener(new QueryListener("tempQuery",sender));
//        stmtStr = "    SELECT\n" + 
//        		"        avg(meter.RealPowerWatts) as averagePower, meter.MeterName as meterName \n" + 
//        		"   FROM\n" + 
//        		"        meter.win:time(30 sec)" +
//        		"	GROUP BY\n" + 
//        		"		meter.MeterName" +
//        		"	HAVING avg(meter.RealPowerWatts) > 0";
//		EPStatement hstatement = epService.getEPAdministrator().createEPL(stmtStr);
//		hstatement.addListener(new QueryListener("meterQuery",sender));
        		
		stmtStr = "SELECT\n" + 
				"	motion.MotionSensorName as roomName,\n" + 
				"	sum(motion.MotionOrNoMotion) as totalMotion\n" + 
				"FROM\n" + 
				"	motion.win:time_batch(10 sec)\n" + 
				"GROUP BY\n" + 
				"	motion.MotionSensorName\n" + 
				"HAVING sum(motion.MotionOrNoMotion) > 0";
		EPStatement mstatement = epService.getEPAdministrator().createEPL(stmtStr);
		mstatement.addListener(new QueryListener("motionQuery",sender));
        
        

        // Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);

        redirect.get("/", Path.Web.INDEX);
        
        // Set up routes
        get(Path.Web.OVERVIEW,			OverviewController.serveOverviewPage);
        get(Path.Web.INDEX,				IndexController.serveIndexPage);
        get(Path.Web.DATA,				DataController.fetchData);
        get(Path.Web.DATUM,				DataController.fetchDatum);
        get(Path.Web.METADATA,			MetadataController.fetchMetadata);
        get(Path.Web.DATA_ADD,			DataController.addData);
        get(Path.Web.APPS,				AppController.fetchApps);
        get(Path.Web.APPS_ADD,			AppController.addApps);
        get(Path.Web.APP,				AppController.fetchApp);
        get(Path.Web.QUERY_STORE,		QueryController.serveQueryStorePage);
        get(Path.Web.QUERY_STREAM,		QueryController.serveQueryStreamPage);
        get(Path.Web.MAPPINGS,			MappingController.fetchMappings);
        get(Path.Web.MAPPING,			MappingController.editMapping);
        get(Path.Web.LOGIN,				LoginController.serveLoginPage);
        get(Path.Web.ACCOUNT,			LoginController.serveAccountPage);
        post(Path.Web.LOGIN,			LoginController.handleLoginPost);
        post(Path.Web.DATA_ADD,			DataController.handleAddDataPost);
        post(Path.Web.APPS_ADD,			AppController.handleAddAppPost);
        post(Path.Web.DATUM,			DataController.handleUpdateDataPost);
        post(Path.Web.SPARQL,			QueryController.handleSparqlPost);
        post(Path.Web.QUERY_STORE,		QueryController.handleQueryStorePost);
        post(Path.Web.APP,				AppController.handleUpdateAppPost);
        post(Path.Web.LOGOUT,			LoginController.handleLogoutPost);
        post(Path.Web.MAPPING_TRANSLATE,MappingController.translateMapping);
        post(Path.Web.MAPPING_SAVE,		MappingController.saveMapping);
        get("*",                    	ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);

    }

}
