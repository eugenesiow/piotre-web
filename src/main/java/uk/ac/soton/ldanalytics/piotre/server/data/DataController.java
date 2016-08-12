package uk.ac.soton.ldanalytics.piotre.server.data;

import static uk.ac.soton.ldanalytics.piotre.server.Application.dataDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;

import java.util.HashMap;

import org.apache.velocity.tools.generic.DisplayTool;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
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
    
    public static Route addData = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
    	if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
//            System.out.println(getParamDataType(request));
            
            //model.put("data", dataDao.getAllData());
            return ViewUtil.render(request, model, Path.Template.DATA_ADD, Path.PageNames.DATA);
        }
    	return ViewUtil.notAcceptable.handle(request, response);
    };
}
