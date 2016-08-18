package by.epam.filmstore.service.impl;

import by.epam.filmstore.dao.DAOFactory;
import by.epam.filmstore.dao.ICountryDAO;
import by.epam.filmstore.dao.exception.DAOException;
import by.epam.filmstore.domain.Country;
import by.epam.filmstore.service.ICountryService;
import by.epam.filmstore.service.exception.ServiceException;
import by.epam.filmstore.util.DAOHelper;

import java.util.List;

/**
 * Created by Olga Shahray on 17.08.2016.
 */
public class CountryServiceImpl implements ICountryService {
    @Override
    public List<Country> getAll() throws ServiceException {
        ICountryDAO dao = DAOFactory.getMySqlDAOFactory().getICountryDAO();
        try {
            return DAOHelper.execute(() -> dao.getAll());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}