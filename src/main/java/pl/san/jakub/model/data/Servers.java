package pl.san.jakub.model.data;

import javax.persistence.*;

/**
 * @author  by Jakub on 07.11.2015.
 */

@Entity
public class Servers {

    private @Id String host_name;
    private @Column(nullable = false) String os_ip;
    private @Column(nullable = false) String irmc_ip;
    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;
    private String model, rackPosition, lan, operatingSystem, comment;

    public Servers() {

    }

    public Servers(String host_name, String os_ip, String irmc_ip) {
        this.host_name = host_name;
        this.os_ip = os_ip;
        this.irmc_ip = irmc_ip;
    }

    public Servers(String host_name,
                   String os_ip,
                   String irmc_ip,
                   String model,
                   String rackPosition,
                   String lan,
                   String operatingSystem,
                   String comment) {
        this.host_name = host_name;
        this.os_ip = os_ip;
        this.irmc_ip = irmc_ip;
        this.model = model;
        this.rackPosition = rackPosition;
        this.lan = lan;
        this.operatingSystem = operatingSystem;
        this.comment = comment;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public void setOs_ip(String os_ip) {
        this.os_ip = os_ip;
    }

    public void setIrmc_ip(String irmc_ip) {
        this.irmc_ip = irmc_ip;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRackPosition() {
        return rackPosition;
    }

    public void setRackPosition(String rackPosition) {
        this.rackPosition = rackPosition;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getHost_name() {
        return host_name;
    }

    @OneToOne
    @JoinColumn(name = "ip")
    public String getOs_ip() {
        return os_ip;
    }

    @OneToOne
    @JoinColumn(name = "ip")
    public String getIrmc_ip() {
        return irmc_ip;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Servers servers = (Servers) o;

        if (host_name != null ? !host_name.equals(servers.host_name) : servers.host_name != null) return false;
        if (os_ip != null ? !os_ip.equals(servers.os_ip) : servers.os_ip != null) return false;
        return !(irmc_ip != null ? !irmc_ip.equals(servers.irmc_ip) : servers.irmc_ip != null);

    }

    @Override
    public int hashCode() {
        int result = host_name != null ? host_name.hashCode() : 0;
        result = 31 * result + (os_ip != null ? os_ip.hashCode() : 0);
        result = 31 * result + (irmc_ip != null ? irmc_ip.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return host_name;
    }


}
