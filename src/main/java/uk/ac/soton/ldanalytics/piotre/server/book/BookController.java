package uk.ac.soton.ldanalytics.piotre.server.book;

import spark.*;
import uk.ac.soton.ldanalytics.piotre.server.login.*;
import uk.ac.soton.ldanalytics.piotre.server.util.*;

import java.util.*;

import static uk.ac.soton.ldanalytics.piotre.server.Application.bookDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.JsonUtil.*;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.*;

public class BookController {

    public static Route fetchAllBooks = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("books", bookDao.getAllBooks());
            return ViewUtil.render(request, model, Path.Template.BOOKS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getAllBooks());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneBook = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Book book = bookDao.getBookByIsbn(getParamIsbn(request));
            model.put("book", book);
            return ViewUtil.render(request, model, Path.Template.BOOKS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getBookByIsbn(getParamIsbn(request)));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
