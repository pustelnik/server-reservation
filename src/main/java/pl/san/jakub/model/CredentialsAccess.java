package pl.san.jakub.model;

import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.tools.exceptions.GeneralServerException;

import java.util.List;

/**
 * Created by Jakub on 08.11.2015.
 */
public interface CredentialsAccess {

    Credentials save(Credentials credentials) throws GeneralServerException;
    void delete(String ip);
    Credentials findByIp(String ip);
    List<Credentials> findAll();

}
