package pl.san.jakub.model.data;


/**
 * Created by Jakub on 07.11.2015.
 */
public enum USER_ROLES {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

    USER_ROLES(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
