package pl.san.jakub.model.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 07.11.2015.
*/
@Entity
public class Users {

    private @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProfileIdGen") @SequenceGenerator(name = "ProfileIdGen", sequenceName = "Profile_id_seq") long id;
    private @Column(nullable = false) String firstName;
    private @Column(nullable = false) String lastName;
    private @Column(nullable = false, unique = true) String username;
    private @Column(nullable = false) String password;
    private @Column(nullable = false) String email;
    private @Column(nullable = false) Boolean enabled;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Servers> host_names;

    public Users() {
        if(null == host_names) {
            host_names = new LinkedList<>();
            enabled = true;
        }

    }

    public Users(String firstName, String lastName, String username, String password, String email, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.host_names = new LinkedList<>();
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addHostname(Servers server) {
        host_names.add(server);
    }
    public void removeHostname(Servers server) {
        host_names.remove(server);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHost_names(List<Servers> host_names) {
        this.host_names = host_names;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List getHost_names() {
        if(null == host_names) {
            return new LinkedList<>();
        }
        return host_names;
    }


    @Override
    public String toString() {
        return String.format("Login: %s Zarezerwowane maszyny: %s", getUsername(), Arrays.toString(getHost_names().toArray()));
    }
}
