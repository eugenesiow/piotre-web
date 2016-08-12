package uk.ac.soton.ldanalytics.piotre.server.data;

import java.util.HashMap;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.*;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.*;
import static uk.ac.soton.ldanalytics.piotre.server.Application.dataDao;

public class DataController {
	public static Route fetchData = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("data", dataDao.getAllData());
            return ViewUtil.render(request, model, Path.Template.DATA, Path.PageNames.DATA);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(dataDao.getAllData());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
    
    public static Route addData = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("data", dataDao.getAllData());
            return ViewUtil.render(request, model, Path.Template.DATA_ADD, Path.PageNames.DATA);
        }
    	return ViewUtil.notAcceptable.handle(request, response);
    };
}
