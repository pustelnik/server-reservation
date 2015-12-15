package pl.san.jakub.model.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.data.Credentials;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Jakub on 08.11.2015.
 */
@Repository
public class CredentialsImpl implements CredentialsAccess {

    Logger LOGGER = LoggerFactory.getLogger(CredentialsImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public Credentials save(Credentials credentials) {
        try {
            if(entityManager.find(Credentials.class, credentials.getIp())!= null) {
                entityManager.merge(credentials);
                return credentials;
            }
        } catch (NoResultException e) {
            LOGGER.debug(e.getMessage());
        }
        entityManager.persist(credentials);
        return credentials;
    }

    @Override
    @Transactional
    public void delete(String ip) {
        entityManager.remove(findByIp(ip));
    }

    @Override
    @Transactional
    public Credentials findByIp(String ip) {
        return (Credentials) entityManager.createQuery("select c from Credentials c where c.ip=?")
                .setParameter(1, ip)
                .getSingleResult();
    }

    @Override
    @Transactional
    public List<Credentials> findAll() {
        return entityManager.createQuery("select c from Credentials c")
                .getResultList();
    }
}
