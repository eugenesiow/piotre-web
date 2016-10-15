package uk.ac.soton.ldanalytics.piotre.server.overview;

import static uk.ac.soton.ldanalytics.piotre.server.Application.userDao;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class OverviewController {
	public static Route serveOverviewPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        return ViewUtil.render(request, model, Path.Template.OVERVIEW, Path.PageNames.OVERVIEW);
    };
}
