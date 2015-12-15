package pl.san.jakub.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Authorities;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.model.data.USER_ROLES;

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
    public Users save(Users users) {

        try {
            LOGGER.info("Checking if user exists...");
            LOGGER.info("User login: {} name: {} last name: {}", users.getUsername(), users.getFirstName(), users.getLastName());
            entityManager.merge(users);
            return findByUsername(users.getUsername());
        } catch (NoResultException e) {
            LOGGER.debug("Can't find user. Creating new one.");
            entityManager.persist(new Authorities(users.getUsername(), USER_ROLES.USER.getValue()));
            entityManager.persist(users);
            return users;
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
