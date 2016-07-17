package by.epam.filmstore.dao.impl;

import by.epam.filmstore.dao.IGenreDAO;
import by.epam.filmstore.dao.exception.DAOException;
import by.epam.filmstore.dao.poolconnection.ConnectionPool;
import by.epam.filmstore.dao.poolconnection.ConnectionPoolException;
import by.epam.filmstore.domain.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olga Shahray on 19.06.2016.
 *
 * Класс GenreDAOImpl позволяет совершить CRUD операции с сущностью Жанр.
 * В каждом методе используется Connection, полученный из DAOHelper (см. AbstractDAO и DAOHelper).
 * Возврат Connection в пул происходит в DAOHelper
 */
public class GenreDAOImpl extends AbstractDAO implements IGenreDAO {
    private static final String INSERT_GENRE = "INSERT INTO allgenres (genre) VALUES(?)";
    private static final String SELECT_GENRE = "SELECT id, genre FROM allgenres WHERE allgenres.id =?";
    private static final String DELETE_GENRE = "DELETE FROM allgenres WHERE allgenres.id=?";
    private static final String SELECT_ALL_GENRES = "SELECT id, genre FROM allgenres";

    @Override
    public void save(Genre genre) throws DAOException {

        PreparedStatement preparedStatement = null;

        try {
            Connection connection = getConnection();
            preparedStatement = connection.prepareStatement(INSERT_GENRE, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,genre.getGenreName());
            int row = preparedStatement.executeUpdate();
            if(row == 0){
                throw new DAOException("Error saving genre");
            }
            try(ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if(rs.next()) {
                    genre.setId(rs.getInt(1));
                }
                else{
                    throw new DAOException("Error getting genre id");
                }
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DAOException("Error saving genre", e);
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DAOException("Error closing prepared statement in genre dao", e);
                }
            }
        }
    }

    @Override
    public boolean delete(int id) throws DAOException {

        PreparedStatement preparedStatement = null;

        try {
            Connection connection = getConnection();
            preparedStatement = connection.prepareStatement(DELETE_GENRE);

            preparedStatement.setInt(1, id);
            int row = preparedStatement.executeUpdate();
            return row != 0;

        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error deleting genre", e);
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DAOException("Error closing prepared statement in genre dao", e);
                }
            }
        }
    }

    @Override
    public Genre get(int id) throws DAOException {
        Genre genre = null;
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = getConnection();
            preparedStatement = connection.prepareStatement(SELECT_GENRE);

            preparedStatement.setInt(1, id);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                if(rs.next()) {
                    genre = new Genre();
                    genre.setId(rs.getInt(1));
                    genre.setGenreName(rs.getString(2));
                }
                else{
                    throw new DAOException("Error getting genre");
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error getting genre", e);
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DAOException("Error closing prepared statement in genre dao", e);
                }
            }
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() throws DAOException {
        List<Genre> allGenres = new ArrayList<>();
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_GENRES);

            try(ResultSet rs = preparedStatement.executeQuery()) {
                Genre genre = null;
                while(rs.next()) {
                    genre = new Genre();
                    genre.setId(rs.getInt(1));
                    genre.setGenreName(rs.getString(2));

                    allGenres.add(genre);
                }
            }
        }catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error getting all genres", e);
        }
        finally {
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DAOException("Error closing prepared statement in genre dao", e);
                }
            }
        }
        return allGenres;
    }

}
