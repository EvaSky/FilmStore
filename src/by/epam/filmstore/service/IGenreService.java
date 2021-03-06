package by.epam.filmstore.service;

import by.epam.filmstore.domain.Genre;
import by.epam.filmstore.service.exception.ServiceException;

import java.util.List;

/**
 * @author Olga Shahray
 */
public interface IGenreService {

    void save(String genre) throws ServiceException;

    void update(int id, String genre) throws ServiceException;

    boolean delete(int id) throws ServiceException;

    List<Genre> getAll() throws ServiceException;
}
