package pl.san.jakub.model;

import pl.san.jakub.model.data.Authorities;
import pl.san.jakub.tools.exceptions.GeneralServerException;

/**
 * Created by Jakub on 12.12.2015.
 */
public interface AuthoritiesAccess {

    Authorities save(Authorities authorities) throws GeneralServerException;
    void remove(String username);
    Authorities find(String username);

}
