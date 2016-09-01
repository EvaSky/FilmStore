package by.epam.filmstore.service.impl;

import by.epam.filmstore.dao.DAOFactory;
import by.epam.filmstore.dao.IFilmMakerDAO;
import by.epam.filmstore.dao.exception.DAOException;
import by.epam.filmstore.domain.FilmMaker;
import by.epam.filmstore.domain.Profession;
import by.epam.filmstore.service.IFilmMakerService;
import by.epam.filmstore.service.exception.ServiceException;
import by.epam.filmstore.service.util.ServiceHelper;
import by.epam.filmstore.util.DAOHelper;

import java.util.List;

/**
 * Created by Olga Shahray on 17.07.2016.
 */
public class FilmMakerServiceImpl implements IFilmMakerService {


    @Override
    public void save(String... params) throws ServiceException {
        if (params.length != 2){
            throw new ServiceException("Invalid arguments: FilmMakerServiceImpl");
        }
        String name = params[0];
        String prof = params[1];
        if(ServiceHelper.isNullOrEmpty(name, prof)){
            throw new ServiceException("Invalid arguments: FilmMakerServiceImpl. Fields must not be null");
        }
        Profession profession;
        try{
            profession = Profession.valueOf(prof.toUpperCase());
        }
        catch(IllegalArgumentException e){
            throw new ServiceException("Such profession does not exist");
        }

        FilmMaker fm = new FilmMaker(name, profession);
        IFilmMakerDAO dao = DAOFactory.getMySqlDAOFactory().getIFilmMakerDAO();
        try {
            DAOHelper.transactionExecute(() -> {
                dao.save(fm);
                return null;
            });
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public boolean delete(int id) throws ServiceException {
        IFilmMakerDAO dao = DAOFactory.getMySqlDAOFactory().getIFilmMakerDAO();
        try {
            return DAOHelper.execute(() -> dao.delete(id));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public FilmMaker get(int id) throws ServiceException {
        IFilmMakerDAO dao = DAOFactory.getMySqlDAOFactory().getIFilmMakerDAO();
        try {
            return DAOHelper.execute(() -> dao.get(id));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<FilmMaker> getAll(String order, int limit) throws ServiceException {
        IFilmMakerDAO dao = DAOFactory.getMySqlDAOFactory().getIFilmMakerDAO();
        try {
            return DAOHelper.execute(() -> dao.getAll(order, limit));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<FilmMaker> getAll() throws ServiceException {
        IFilmMakerDAO dao = DAOFactory.getMySqlDAOFactory().getIFilmMakerDAO();
        try {
            return DAOHelper.execute(() -> dao.getAll());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
