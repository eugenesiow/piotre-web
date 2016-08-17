package uk.ac.soton.ldanalytics.piotre.server.data;

import static uk.ac.soton.ldanalytics.piotre.server.Application.dataDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.tools.generic.DisplayTool;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.user.UserController;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class DataController {
	public static Route fetchData = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("data", dataDao.getAllData());
            model.put("displayTool", new DisplayTool());
            return ViewUtil.render(request, model, Path.Template.DATA, Path.PageNames.DATA);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dataDao.getAllData());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
    
    public static Route fetchDatum = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String id = getParamId(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Data datum = dataDao.getDatum(id);
            model.put("datum", datum);
            model.put("schema", dataDao.getAllSchema("data", datum.getType().toString().toLowerCase()));
            model.put("metadata", dataDao.getMetadata(id));
            return ViewUtil.render(request, model, Path.Template.DATUM, Path.PageNames.DATA);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dataDao.getDatum(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
    
    public static Route addData = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            String type = getParamDataType(request);
            model.put("type", type);
//            System.out.println(getParamDataType(request));
            model.put("schema", dataDao.getAllSchema("data", type));
            return ViewUtil.render(request, model, Path.Template.DATA_ADD, Path.PageNames.DATA);
        }
    	return ViewUtil.notAcceptable.handle(request, response);
    };
    
    public static Route handleAddDataPost = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	Boolean success = dataDao.addData(getParamDataType(request),getQueryName(request),getQueryAuthor(request),getQueryDescription(request),getQueryMetadata(request));
    	if(success) {
    		response.redirect(Path.Web.DATA);
    	} //TODO: handle error
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
