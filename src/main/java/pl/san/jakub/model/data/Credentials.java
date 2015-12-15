package pl.san.jakub.model.data;

import javax.persistence.*;

/**
 * Created by Jakub on 08.11.2015.
 */

@Entity
public class Credentials {

    private @Id String ip;
    private @Column(nullable = false, length = 12) String login;
    private @Column(nullable = false, length = 24) String password;

    public Credentials(String irmc_ip, String login, String password) {
        this.ip = irmc_ip;
        this.login = login;
        this.password = password;
    }

    public Credentials() {
    }

    @OneToOne
    @JoinColumn(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
