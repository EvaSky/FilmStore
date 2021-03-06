package by.epam.filmstore.command.impl;

import by.epam.filmstore.command.Command;
import by.epam.filmstore.command.ParameterAndAttributeName;
import by.epam.filmstore.domain.User;
import by.epam.filmstore.service.IOrderService;
import by.epam.filmstore.service.ServiceFactory;
import by.epam.filmstore.service.exception.ServiceException;
import by.epam.filmstore.service.exception.ServiceValidationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>Command implements a request of user with role USER to add film in cart,
 * i.e. to save new order. Unpaid orders are shown in cart.
 * Access right is checked in class UserFilter.</p>
 *
 * @see by.epam.filmstore.controller.filter.UserFilter
 * @author Olga Shahray
 */
public class UserAddToCartCommand implements Command {

    private static final String CART_PAGE = "Controller?command=user-cart";
    private static final int ERROR_STATUS = 404;

    private static final Logger LOG = LogManager.getLogger(UserAddToCartCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedUser = (User) session.getAttribute(ParameterAndAttributeName.USER);
        try {
            int filmId = Integer.parseInt(request.getParameter(ParameterAndAttributeName.FILM_ID));
            double price = Double.parseDouble(request.getParameter(ParameterAndAttributeName.PRICE));

            IOrderService service = ServiceFactory.getInstance().getOrderService();
            service.save(filmId, price, loggedUser);
            response.sendRedirect(CART_PAGE);

        } catch (ServiceValidationException | NumberFormatException e) {
            LOG.warn("Data is not valid", e);
            response.sendError(ERROR_STATUS);
        } catch (ServiceException e) {
            LOG.error("Exception is caught", e);
            response.sendError(ERROR_STATUS);
        }
    }
}
