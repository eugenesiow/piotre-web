package uk.ac.soton.ldanalytics.piotre.server.query;

import static uk.ac.soton.ldanalytics.piotre.server.Application.dataDao;
import static uk.ac.soton.ldanalytics.piotre.server.Application.queryDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.resultSetToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getParamId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryQuery;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.velocity.tools.generic.DisplayTool;
import org.zeromq.ZMQ.Socket;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.data.Schema;
import uk.ac.soton.ldanalytics.piotre.server.data.StreamReceiver;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.Mapping;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class QueryController {
	public static Route fetchQueryStreams = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("queries", queryDao.getAllStreamQueries());
            return ViewUtil.render(request, model, Path.Template.QUERY_STREAMS, Path.PageNames.QUERY_STREAM);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(queryDao.getAllStreamQueries());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
	
	public static Route serveQueryStorePage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		HashMap<String, Object> model = new HashMap<>();
        model.put("data", dataDao.getAllData());
        return ViewUtil.render(request, model, Path.Template.QUERY_STORE, Path.PageNames.QUERY_STORE);
	};
	
	public static Route serveQueryStreamPage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		String id = getParamId(request);
		HashMap<String, Object> model = new HashMap<>();
        if(id.equals("add")) {
        	model.put("query", new QueryStreaming(UUID.randomUUID(),"Register Stream Query",""));
        	model.put("data", dataDao.getAllData());
        	model.put("mode", "add");
        }
        else {
        	QueryStreaming query = queryDao.getStreamQuery(id);
        	model.put("query", query);
        	model.put("mode", "edit");
        	model.put("data", dataDao.getDatum(query.getDataStream().toString()));
        }
        return ViewUtil.render(request, model, Path.Template.QUERY_STREAM, Path.PageNames.QUERY_STREAM);
    };
    public static Route handleQueryStorePost = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	String dataId = getQueryId(request);
    	Iterable<Mapping> mappings = dataDao.getMappingsByDatum(dataId);
    	Map<String,String> metadata = dataDao.getMetadata(dataId);
    	ResultsSet results = queryDao.queryStore(metadata, mappings,getQueryQuery(request));
    	if (clientAcceptsJson(request)) {
    		return resultSetToJson(results);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
    public static Route handleSparqlPost = (Request request, Response response) -> {
    	String dataId = getParamId(request);
    	Iterable<Mapping> mappings = dataDao.getMappingsByDatum(dataId);
    	Map<String,String> metadata = dataDao.getMetadata(dataId);
    	ResultsSet results = queryDao.queryStore(metadata, mappings,getQueryQuery(request));
    	if (clientAcceptsJson(request)) {
    		return resultSetToJson(results);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
    
    public static void SetupStreams(EPServiceProvider epService) {
    	Map<String,StreamReceiver> existingStreams = new HashMap<String,StreamReceiver>();
    	for(Schema schema:dataDao.getAllStreamSchema()) {
    		StreamReceiver srcReceiver = existingStreams.get(schema.getDataStreamUri());
    		if(srcReceiver == null) {
    			srcReceiver = new StreamReceiver(schema.getDataStreamUri());
    			existingStreams.put(schema.getDataStreamUri(), srcReceiver);
    			(new Thread(srcReceiver)).start();
    		}
//    		System.out.println(schema.getName() + " " + schema.getContent());
    		loadSchema(schema.getName(),schema.getContent(),epService);
        }
    }
    
    public static void RegisterStreams(EPServiceProvider epService, Socket sender) {
    	for(QueryStreaming query:queryDao.getAllStreamQueries()) {
    		EPStatement statement = epService.getEPAdministrator().createEPL(query.getEpl());
            statement.addListener(new QueryListener(query.getName(),sender));
    	}
    }
    
    public static void loadSchema(String name, String content, EPServiceProvider epService) {
		Map<String,Object> dataSchema = new LinkedHashMap<String,Object>();
		Scanner scanner = new Scanner(content);
		while(scanner.hasNextLine()) {
			String[] parts = scanner.nextLine().split(",");
			if(parts.length>1) {
				dataSchema.put(parts[0], classMap(parts[1]));
			}
		}
		scanner.close();
		epService.getEPAdministrator().getConfiguration().addEventType(name, dataSchema);
	}
    
    private static Object classMap(String className) {
		Object object = null;
		switch(className.toLowerCase()) {
			case "string":
				object = String.class;
				break;
			case "float":
				object = Float.class;
				break;
			case "double":
				object = Double.class;
				break;
			case "integer":
				object = Integer.class;
				break;
			case "timestamp":
				object = Timestamp.class;
				break;
		}
		return object;
	}
}
