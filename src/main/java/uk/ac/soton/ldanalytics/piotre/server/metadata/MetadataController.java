package uk.ac.soton.ldanalytics.piotre.server.metadata;

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

public class MetadataController {
	public static Route fetchMetadata = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
//            model.put("apps", appDao.getAllApps());
            model.put("displayTool", new DisplayTool());
            return ViewUtil.render(request, model, Path.Template.METADATA, Path.PageNames.METADATA);
        }
        if (clientAcceptsJson(request)) {
//            return dataToJson(appDao.getAllApps());
        }
		return ViewUtil.notAcceptable.handle(request, response);
	};
}
