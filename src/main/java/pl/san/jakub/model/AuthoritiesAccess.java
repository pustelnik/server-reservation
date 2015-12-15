package pl.san.jakub.model;

import pl.san.jakub.model.data.Authorities;

/**
 * Created by Jakub on 12.12.2015.
 */
public interface AuthoritiesAccess {

    Authorities save(Authorities authorities);
    void remove(String username);
    Authorities find(String username);

}
