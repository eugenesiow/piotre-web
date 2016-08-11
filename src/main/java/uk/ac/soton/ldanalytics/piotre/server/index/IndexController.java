package uk.ac.soton.ldanalytics.piotre.server.index;

import spark.*;
import uk.ac.soton.ldanalytics.piotre.server.util.*;

import java.util.*;

import static uk.ac.soton.ldanalytics.piotre.server.Application.*;

public class IndexController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userDao.getAllUserNames());
        model.put("book", bookDao.getRandomBook());
        return ViewUtil.render(request, model, Path.Template.INDEX, Path.PageNames.INDEX);
    };
}
