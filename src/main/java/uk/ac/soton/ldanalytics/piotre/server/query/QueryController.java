package uk.ac.soton.ldanalytics.piotre.server.query;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class QueryController {
	public static Route serveQueryStorePage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.QUERY_STORE, Path.PageNames.QUERY_STORE);
	};
	
	public static Route serveQueryStreamPage = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.QUERY_STREAM, Path.PageNames.QUERY_STREAM);
    };
}
