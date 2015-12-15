package pl.san.jakub.model;

import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;

import java.util.List;

/**
 * Created by Jakub on 07.11.2015.
 */
public interface ServersAccess {

    Servers save(Servers server, Credentials os, Credentials irmc);
    Servers save(Servers server);
    void delete(String host_name);
    Servers findOne(String host_name);
    Servers findByOsIP(String osIP);
    Servers findByIrmcIP(String irmcIP);
    List<Servers> findAll();


}
