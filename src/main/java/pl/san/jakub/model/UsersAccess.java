package pl.san.jakub.model;

import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.exceptions.GeneralServerException;

import java.util.List;

/**
 * Created by Jakub on 07.11.2015.
 */
public interface UsersAccess {

    /**
     *
     * @param users User persistent object
     * @param change If true existing user is edited
     * @return
     * @throws GeneralServerException
     */
    Users save(Users users, boolean change) throws GeneralServerException;
    void delete(String username);
    Users findOne(long id);
    Users findByUsername(String username);
    List<Users> findAll();

}
