package pl.san.jakub.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.exceptions.GeneralServerException;
import pl.san.jakub.tools.exceptions.UserAlreadyExistException;
import pl.san.jakub.tools.exceptions.UserDontExistException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Jakub on 08.11.2015.
 */
@Repository
public class UsersImpl implements UsersAccess {

    Logger LOGGER = LoggerFactory.getLogger(UsersImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Users save(Users users, boolean change) throws GeneralServerException {
        try {
            if(change) {
                LOGGER.info("Checking if user {} already exists.", users.getUsername());
                try {
                    findByUsername(users.getUsername());
                    entityManager.merge(users);
                    return findByUsername(users.getUsername());
                } catch (NoResultException e) {
                    throw new UserDontExistException();
                }
            } else {
                try {
                    findByUsername(users.getUsername());
                    throw new UserAlreadyExistException();
                } catch (NoResultException e) {
                    entityManager.persist(users);
                    return users;
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new GeneralServerException("Error occured during executing DB query.");
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        entityManager.remove(findByUsername(username));
    }

    @Override
    @Transactional
    public Users findOne(long id) {
        return entityManager.find(Users.class, id);
    }

    @Override
    @Transactional()
    public Users findByUsername(String username) {
        return (Users) entityManager.createQuery("select u from Users u where u.username=?")
                .setParameter(1, username)
                .getSingleResult();
    }

    @Override
    @Transactional
    public List<Users> findAll() {
        return entityManager.createQuery("select u from Users u")
                .getResultList();
    }
}
