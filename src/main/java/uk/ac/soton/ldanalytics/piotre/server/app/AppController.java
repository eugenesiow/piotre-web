package uk.ac.soton.ldanalytics.piotre.server.app;

import static uk.ac.soton.ldanalytics.piotre.server.Application.appDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getParamId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryAuthor;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryDescription;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryName;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryUri;

import java.util.HashMap;

import org.apache.velocity.tools.generic.DisplayTool;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class AppController {
	public static Route fetchApps = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("apps", appDao.getAllData());
            model.put("displayTool", new DisplayTool());
            return ViewUtil.render(request, model, Path.Template.APPS, Path.PageNames.APPS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(appDao.getAllData());
        }
		return ViewUtil.notAcceptable.handle(request, response);
	};
	
	public static Route fetchApp = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
        String id = getParamId(request);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            App app = appDao.getApp(id);
            model.put("app", app);
            return ViewUtil.render(request, model, Path.Template.APP, Path.PageNames.APPS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(appDao.getApp(id));
        }
        return ViewUtil.notAcceptable.handle(request, response);
	};
	
	public static Route addApps = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
    	if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            return ViewUtil.render(request, model, Path.Template.APPS_ADD, Path.PageNames.APPS);
        }
    	return ViewUtil.notAcceptable.handle(request, response);
	};
	
	public static Route handleAddAppPost = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	Boolean success = appDao.addApp(getQueryName(request),getQueryAuthor(request),getQueryDescription(request),getQueryUri(request));
    	if(success) {
    		response.redirect(Path.Web.APPS);
    	} //TODO: handle error
        return ViewUtil.notAcceptable.handle(request, response);
    };
	
	public static Route handleUpdateAppPost = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	Boolean success = appDao.updateApp(getParamId(request),getQueryName(request),getQueryAuthor(request),getQueryDescription(request),getQueryUri(request));
    	if(success) {
    		response.redirect(Path.Web.APPS);
    	} //TODO: handle error
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
