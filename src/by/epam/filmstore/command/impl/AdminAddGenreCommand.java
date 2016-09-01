package by.epam.filmstore.command.impl;

import by.epam.filmstore.command.Command;
import by.epam.filmstore.service.IGenreService;
import by.epam.filmstore.service.ServiceFactory;
import by.epam.filmstore.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Olga Shahray on 29.08.2016.
 */
public class AdminAddGenreCommand implements Command {
    private static final String GENRE_NAME = "genreName";
    private static final String GENRE_PAGE = "/FilmStore/UserServlet?command=admin-get-genres";
    private static final String ERROR_PAGE = "/error.jsp";
    private static final String EXCEPTION = "exception";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String genreName = request.getParameter(GENRE_NAME);

        IGenreService service = ServiceFactory.getInstance().getGenreService();

        try{
            service.save(genreName);
            response.sendRedirect(GENRE_PAGE);

        }catch(ServiceException e){
            request.setAttribute(EXCEPTION, e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
}
