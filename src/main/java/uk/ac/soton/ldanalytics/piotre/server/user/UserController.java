package uk.ac.soton.ldanalytics.piotre.server.user;

import static uk.ac.soton.ldanalytics.piotre.server.Application.userDao;
import static uk.ac.soton.ldanalytics.piotre.server.util.RequestUtil.clientAcceptsHtml;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.soton.ldanalytics.piotre.server.login.LoginController;
import uk.ac.soton.ldanalytics.piotre.server.util.Path;
import uk.ac.soton.ldanalytics.piotre.server.util.ViewUtil;

public class UserController {

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
        return hashedPassword.equals(user.getHashedPassword());
    }

//    // This method doesn't do anything, it's just included as an example
//    public static void setPassword(String username, String oldPassword, String newPassword) {
//        if (authenticate(username, oldPassword)) {
//            String newSalt = BCrypt.gensalt();
//            String newHashedPassword = BCrypt.hashpw(newSalt, newPassword);
//            // Update the user salt and password
//        }
//    }
    
    public static Route serveAccountPage = (Request request, Response response) -> {
    	LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
	        Map<String, Object> model = new HashMap<>();
	        model.put("user", userDao.getUserByUsername(request.session().attribute("currentUser")));
	        return ViewUtil.render(request, model, Path.Template.ACCOUNT, Path.PageNames.LOGIN);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
