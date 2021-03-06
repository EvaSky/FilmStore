package by.epam.filmstore.command.impl;

import by.epam.filmstore.command.Command;
import by.epam.filmstore.command.ParameterAndAttributeName;
import by.epam.filmstore.domain.User;
import by.epam.filmstore.service.IUserService;
import by.epam.filmstore.service.ServiceFactory;
import by.epam.filmstore.service.exception.ServiceAuthException;
import by.epam.filmstore.service.exception.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>Command implements a request to save new user.
 * Saved user is sent to home page.</p>
 *
 * @author Olga Shahray
 */
public class SaveNewUserCommand implements Command {

    private static final String INDEX_PAGE = "Controller?command=load-main-page";
    private static final int ERROR_STATUS = 404;
    private static final String SAVE_NEW_USER_ERROR = "saveNewUserError";
    private static final Logger LOG = LogManager.getLogger(SaveNewUserCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String name = request.getParameter(ParameterAndAttributeName.NAME);
        String email = request.getParameter(ParameterAndAttributeName.EMAIL);
        String pass = request.getParameter(ParameterAndAttributeName.PASSWORD);
        String confPass = request.getParameter(ParameterAndAttributeName.CONFIRM_PASSWORD);
        String phone = request.getParameter(ParameterAndAttributeName.PHONE);

        IUserService service = ServiceFactory.getInstance().getUserService();

        HttpSession session = request.getSession(true);

        try{
            User user = service.saveUser(name, email, pass, confPass, phone);
            session.setAttribute(ParameterAndAttributeName.USER, user);
            response.sendRedirect(INDEX_PAGE);

        }catch(ServiceAuthException e){
            LOG.warn("Saving new user failed", e);
            request.setAttribute(SAVE_NEW_USER_ERROR, e.getMessage());
            request.getRequestDispatcher(INDEX_PAGE).forward(request, response);
        }
        catch (ServiceException e) {
            LOG.error("Exception is caught", e);
            response.sendError(ERROR_STATUS);
        }

    }
}
