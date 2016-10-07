package uk.ac.soton.ldanalytics.piotre.server.mapping;

import static uk.ac.soton.ldanalytics.piotre.server.Application.mappingDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getParamId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryContent;

import java.util.HashMap;

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
            return ViewUtil.render(request, model, Path.Template.MAPPINGS, Path.PageNames.MAPPINGS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(mappingDao.getAllMappings());
        }
		return ViewUtil.notAcceptable.handle(request, response);
	};
	
	public static Route editMapping = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		String id = getParamId(request);
        if (clientAcceptsHtml(request)) {
        	 HashMap<String, Object> model = new HashMap<>();
        	 Mapping mapping = mappingDao.getMapping(id);
             model.put("mapping", mapping);
             model.put("mappingJson", mappingDao.convertMappingContent(mapping.getContent()));
             return ViewUtil.render(request, model, Path.Template.MAPPING, Path.PageNames.MAPPINGS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(mappingDao.getMapping(id));
        }
		return ViewUtil.notAcceptable.handle(request, response);
	};
	
	public static Route translateMapping = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		String content = getQueryContent(request);
        if (clientAcceptsJson(request)) {
            return mappingDao.convertMappingContent(content);
        }
		return ViewUtil.notAcceptable.handle(request, response);		
	};
}
