package pl.san.jakub.controller.forms;

import pl.san.jakub.model.data.Users;
import javax.validation.constraints.Size;

/**
 * Created by Jakub on 08.11.2015.
 */
public class RegisterForm {

    private boolean enabled = true;
    @Size(min = 3, max = 15, message = "{firstName.size}")
    private String firstName;
    @Size(min = 4, max = 15, message = "{lastName.size")
    private String lastName;
    @Size(min = 4, max = 15, message = "{username.size}")
    private String username;
    @Size(min = 4, max = 15, message = "{password.size}")
    private String password;
    @Size(min = 4, max = 15,message = "{passwordConfirm.size")
    private String passwordConfirm;
    @Size(min = 5, max=20, message = "{email.size}")
    private String email;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users toProfile() {
        return new Users(firstName, lastName, username, password, email, enabled);
    }
}
