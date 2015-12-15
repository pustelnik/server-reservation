package pl.san.jakub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.san.jakub.config.HibernateConfig;
import pl.san.jakub.controller.UserController;
import pl.san.jakub.model.AuthoritiesAccess;
import pl.san.jakub.model.UsersAccess;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by Jakub on 08.11.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateConfig.class)
public class UsersControllerTest {


    @Test
    public void shouldShowRegistration() throws Exception {

        MockMvc mockMvc = getMockMvc();
        mockMvc.perform(get("/profile/register"))
                .andExpect(view().name("registerForm"));
    }

    @Test
    public void shouldDisplayProfileDetails() throws Exception {
        MockMvc mockMvc = getMockMvc();
        mockMvc.perform(get("/profile/jakub"))
                .andExpect(view().name("profile"));
    }

    private MockMvc getMockMvc() {
        UsersAccess mockAccess = mock(UsersAccess.class);
        AuthoritiesAccess mockAuth = mock(AuthoritiesAccess.class);
        UserController controller = new UserController(mockAccess, mockAuth);
        return standaloneSetup(controller).build();
    }

}
