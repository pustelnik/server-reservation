package pl.san.jakub.model.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.tools.exceptions.GeneralServerException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Jakub on 07.11.2015.
 */

@Repository
public class ServersImpl implements ServersAccess {

    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    @Transactional
    public Servers save(Servers server, Credentials os, Credentials irmc) throws GeneralServerException {
        try {
            if(null == findOne(server.getHost_name())) {
                entityManager.persist(os);
                entityManager.persist(irmc);
                entityManager.persist(server);

                return server;
            }
            return null;
        } catch (Exception e) {
            throw new GeneralServerException("Error occured during executing DB query. "+e.getMessage());
        }
    }

    @Override
    @Transactional
    public Servers save(Servers server) {


        entityManager.merge(server);

        return server;
    }

    @Override
    @Transactional
    public void delete(String host_name) {
        entityManager.remove(findOne(host_name));
    }


    @Override
    @Transactional
    public Servers findOne(String host_name) {
        return entityManager.find(Servers.class, host_name);
    }

    @Override
    @Transactional
    public Servers findByOsIP(String osIP) {
        return (Servers) entityManager.createQuery("select s from Servers s where s.os_ip=?").
                setParameter(1, osIP).
                getSingleResult();
    }

    @Override
    @Transactional
    public Servers findByIrmcIP(String irmcIP) {
        return (Servers) entityManager.createQuery("select s from Servers s where s.irmc_ip=?").
                setParameter(1, irmcIP).
                getSingleResult();
    }

    @Override
    @Transactional
    public List<Servers> findAll() {
        return entityManager.createQuery("select s from Servers s").getResultList();
    }
}
