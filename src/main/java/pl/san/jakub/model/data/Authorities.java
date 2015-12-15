package pl.san.jakub.model.data;

import javax.persistence.*;

/**
 * Created by Jakub on 14.11.2015.
 */
@Entity
public class Authorities {

    private @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthIdGen") @SequenceGenerator(name = "AuthIdGen", sequenceName = "Auth_id_seq") long id;
    private @Column(nullable = false) String username;
    private @Column(nullable = false) String authority;

    public Authorities() {
    }

    public Authorities(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    @OneToOne
    @JoinColumn(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
