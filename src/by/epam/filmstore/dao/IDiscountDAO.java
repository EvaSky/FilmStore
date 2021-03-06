package by.epam.filmstore.dao;

import by.epam.filmstore.dao.exception.DAOException;
import by.epam.filmstore.domain.Discount;

import java.util.List;

/**
 * @author Olga Shahray
 */
public interface IDiscountDAO {

    void save(Discount discount) throws DAOException;

    void update(Discount discount) throws DAOException;

    Discount getUserDiscount(int userId)  throws DAOException;

    // false if not found
    boolean delete(int discountId) throws DAOException;

    List<Discount> getAll() throws DAOException;


}
