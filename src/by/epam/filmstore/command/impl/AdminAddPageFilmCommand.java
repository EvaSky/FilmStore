package by.epam.filmstore.command.impl;

import by.epam.filmstore.command.Command;
import by.epam.filmstore.domain.Country;
import by.epam.filmstore.domain.Film;
import by.epam.filmstore.domain.FilmMaker;
import by.epam.filmstore.domain.Genre;
import by.epam.filmstore.service.*;
import by.epam.filmstore.service.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Olga Shahray on 01.09.2016.
 */
public class AdminAddPageFilmCommand implements Command {
    private static final String ID = "id";
    private static final String FILM = "film";
    private static final String GENRES = "genres";
    private static final String FILM_MAKERS = "filmMakers";
    private static final String COUNTRIES = "countries";
    private static final String EXCEPTION = "exception";
    private static final String ERROR_PAGE = "/errorPage.jsp";
    private static final String FILMS_PAGE = "/WEB-INF/jsp/admin/addFilm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ICountryService countryService = ServiceFactory.getInstance().getCountryService();
        IGenreService genreService = ServiceFactory.getInstance().getGenreService();
        IFilmMakerService filmMakerService = ServiceFactory.getInstance().getFilmMakerService();
        IFilmService filmService = ServiceFactory.getInstance().getFilmService();

        try {
            Film film = null;
            String id = request.getParameter(ID);
            if(id != null && !id.isEmpty()) {
                film = filmService.get(Integer.parseInt(id));
            }
            List<Country> countries = countryService.getAll();
            List<Genre> genres = genreService.getAll();
            List<FilmMaker> filmMakers = filmMakerService.getAll();

            request.setAttribute(FILM, film);
            request.setAttribute(COUNTRIES, countries);
            request.setAttribute(FILM_MAKERS, filmMakers);
            request.setAttribute(GENRES, genres);

            request.getRequestDispatcher(FILMS_PAGE).forward(request, response);


        } catch (ServiceException e) {
            request.setAttribute(EXCEPTION, e);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
}
