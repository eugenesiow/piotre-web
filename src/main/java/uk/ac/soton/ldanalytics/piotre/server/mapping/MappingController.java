package uk.ac.soton.ldanalytics.piotre.server.mapping;

import static uk.ac.soton.ldanalytics.piotre.server.Application.mappingDao;
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

public class MappingController {
	public static Route fetchMappings = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("mappings", mappingDao.getAllMappings());
            model.put("displayTool", new DisplayTool());
            return ViewUtil.render(request, model, Path.Template.MAPPINGS, Path.PageNames.MAPPINGS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(mappingDao.getAllMappings());
        }
		return ViewUtil.notAcceptable.handle(request, response);
	};
}
