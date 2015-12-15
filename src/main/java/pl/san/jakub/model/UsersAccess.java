package pl.san.jakub.model;

import pl.san.jakub.model.data.Users;

import java.util.List;

/**
 * Created by Jakub on 07.11.2015.
 */
public interface UsersAccess {

    Users save(Users users);
    void delete(String username);
    Users findOne(long id);
    Users findByUsername(String username);
    List<Users> findAll();

}
