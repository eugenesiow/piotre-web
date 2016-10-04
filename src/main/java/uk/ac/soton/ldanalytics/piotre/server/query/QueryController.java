package uk.ac.soton.ldanalytics.piotre.server.query;

import static uk.ac.soton.ldanalytics.piotre.server.Application.dataDao;
import static uk.ac.soton.ldanalytics.piotre.server.Application.queryDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.resultSetToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryQuery;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.mapping.Mapping;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class QueryController {
	public static Route serveQueryStorePage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		HashMap<String, Object> model = new HashMap<>();
        model.put("data", dataDao.getAllData());
        return ViewUtil.render(request, model, Path.Template.QUERY_STORE, Path.PageNames.QUERY_STORE);
	};
	
	public static Route serveQueryStreamPage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		HashMap<String, Object> model = new HashMap<>();
        model.put("data", dataDao.getAllData());
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
}
