package by.epam.filmstore.dao.impl;

import by.epam.filmstore.dao.IDiscountDAO;
import by.epam.filmstore.dao.exception.DAOException;
import by.epam.filmstore.dao.poolconnection.ConnectionPool;
import by.epam.filmstore.dao.poolconnection.ConnectionPoolException;
import by.epam.filmstore.domain.Discount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olga Shahray on 27.06.2016.
 */
public class DiscountDAOImpl implements IDiscountDAO {

    private static final String INSERT_DISCOUNT = "INSERT INTO discount (discount_id, sum_from, value) VALUES (?,?,?)";
    private static final String SELECT_DISCOUNT = "SELECT discount_id, discount.sum_from, discount.value FROM discount " +
            "WHERE discount_id=?";
    private static final String SELECT_ALL_DISCOUNTS = "SELECT discount_id, discount.sum_from, discount.value" +
            "FROM discount ORDER BY value";
    private static final String DELETE_DISCOUNT = "DELETE FROM discount WHERE discount.id = ?";


    @Override
    public void save(Discount discount) throws DAOException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DISCOUNT, Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setInt(1, discount.getId());
            preparedStatement.setDouble(2, discount.getSumFrom());
            preparedStatement.setDouble(3, discount.getValue());

            int row = preparedStatement.executeUpdate();
            if(row == 0){
                throw new DAOException("Error saving discount");
            }
            try(ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if(rs.next()) {
                    discount.setId(rs.getInt(1));
                }
                else{
                    throw new DAOException("Error getting discount id");
                }
            }
        } catch (ConnectionPoolException | SQLException e) {
            throw new DAOException("Error saving discount", e);
        }
    }

    @Override
    public Discount get(int discountId) throws DAOException {
        Discount discount = null;
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DISCOUNT)) {

            preparedStatement.setInt(1, discountId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if(rs.next()) {
                    discount = new Discount();
                    discount.setId(rs.getInt(1));
                    discount.setSumFrom(rs.getDouble(2));
                    discount.setValue(rs.getDouble(3));
                }
                else{
                    throw new DAOException("Error getting discount");
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error getting discount", e);
        }
        return discount;
    }

    @Override
    public boolean delete(int discountId) throws DAOException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DISCOUNT)) {
            preparedStatement.setInt(1, discountId);
            int row = preparedStatement.executeUpdate();
            return row != 0;

        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error deleting order", e);
        }
    }

    @Override
    public List<Discount> getDiscountsList() throws DAOException {
        List<Discount> discountList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DISCOUNTS)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                Discount discount = null;
                while (rs.next()) {
                    discount = new Discount();
                    discount.setId(rs.getInt(1));
                    discount.setSumFrom(rs.getDouble(2));
                    discount.setValue(rs.getDouble(3));

                    discountList.add(discount);
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Error getting all discounts", e);
        }
        return discountList;
    }
}
