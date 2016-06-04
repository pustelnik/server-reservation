package pl.san.jakub.tools;

/**
 * Created by Jakub on 05.12.2015.
 */
public enum OperatingSystem {

    WINDOWS("Windows"),
    LINUX("Linux"),
    ESXI("ESXi"),
    HYPERV("HyperV"),
    OTHER("Other"),
    NON_AVAILABLE_PATH("Can't find OS. Maybe machine is OFFLINE.");

    public String value;

    OperatingSystem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
