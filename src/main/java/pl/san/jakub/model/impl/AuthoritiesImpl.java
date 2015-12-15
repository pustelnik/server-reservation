package pl.san.jakub.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.model.AuthoritiesAccess;
import pl.san.jakub.model.data.Authorities;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Created by Jakub on 12.12.2015.
 */
@Repository
public class AuthoritiesImpl implements AuthoritiesAccess{

    Logger LOGGER = LoggerFactory.getLogger(AuthoritiesImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Authorities save(Authorities authorities) {
        try {
            Authorities auth = find(authorities.getUsername());
            entityManager.merge(authorities);
            return authorities;
        } catch (NoResultException e) {
            LOGGER.debug(e.getMessage());
        }
        entityManager.persist(authorities);
        return authorities;

    }

    @Override
    @Transactional
    public void remove(String username) {
        entityManager.remove(find(username));
    }

    @Override
    @Transactional
    public Authorities find(String username) {
        return (Authorities) entityManager.createQuery("select a from Authorities a where a.username=?")
                .setParameter(1, username)
                .getSingleResult();
    }
}
