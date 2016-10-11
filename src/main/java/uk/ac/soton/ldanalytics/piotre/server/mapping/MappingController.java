package uk.ac.soton.ldanalytics.piotre.server.mapping;

import static uk.ac.soton.ldanalytics.piotre.server.Application.mappingDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.dataToJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsJson;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getParamId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryContent;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryId;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryName;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryUri;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getQueryAuthor;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.getSessionCurrentUser;

import java.util.HashMap;

import org.json.JSONObject;

import com.github.jsonldjava.utils.Obj;

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
		Mapping mapping = null;
		if(id.equals("add")) {
			mapping = mappingDao.createMapping(getSessionCurrentUser(request));
		} else {
			mapping = mappingDao.getMapping(id);
		}
        if (clientAcceptsHtml(request)) {
        	 HashMap<String, Object> model = new HashMap<>();
             model.put("mapping", mapping);
             model.put("mappingJson", mappingDao.convertMappingContent(mapping.getContent()));
             return ViewUtil.render(request, model, Path.Template.MAPPING, Path.PageNames.MAPPINGS);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(mapping);
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
	
	public static Route saveMapping = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Boolean success = mappingDao.updateMapping(getQueryId(request), getQueryName(request), getQueryAuthor(request), getQueryUri(request), getQueryContent(request));
		JSONObject o = new JSONObject();
		o.put("success",success);
        if (clientAcceptsJson(request)) {
            return o.toString();
        }
		return ViewUtil.notAcceptable.handle(request, response);		
	};
}
