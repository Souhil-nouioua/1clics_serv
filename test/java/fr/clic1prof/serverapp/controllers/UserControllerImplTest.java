package fr.clic1prof.serverapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.clic1prof.serverapp.model.registration.RegistrationType;
import fr.clic1prof.serverapp.model.user.UserRole;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationRequest;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationResponse;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class UserControllerImplTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void test_successStudentLogin() throws Exception {

        MvcResult result = this.mvc.perform(this.getLoginBuilder("test1.student@test.com", "UnRenard60**"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        AuthenticationResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), AuthenticationResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals(UserRole.STUDENT, response.getRole());
        Assert.hasText(response.getToken(), "Empty token.");
    }

    @Test
    public void test_successTeacherLogin() throws Exception {

        MvcResult result = this.mvc.perform(this.getLoginBuilder("test1.teacher@test.com", "UnRenard60**"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        AuthenticationResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), AuthenticationResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals(UserRole.TEACHER, response.getRole());
        Assert.hasText(response.getToken(), "Empty token.");
    }

    @Test
    public void test_errorLoginPartialCredentials() throws Exception {

        this.mvc.perform(this.getLoginBuilder(null, "UnRenard60**"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        this.mvc.perform(this.getLoginBuilder("test1.student@test.com", null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        this.mvc.perform(this.getLoginBuilder(null, null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        this.mvc.perform(this.getLoginBuilder("", ""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_errorLoginInvalidCredentials() throws Exception {

        this.mvc.perform(this.getLoginBuilder("invalid_user", "UnRenard60**"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        this.mvc.perform(this.getLoginBuilder("test1.student@test.com", "invalid_password"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        this.mvc.perform(this.getLoginBuilder("invalid_user", "invalid_password"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void test_successRegistration() throws Exception {

        // If this test doesn't work, try to refresh database with the data generation script.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "JamesBond007**", RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Trying to register another user with the same email than the previous one.
        this.mvc.perform(this.getRegistrationBuilder("Helene", "Smith", "james.bond@mi6.uk", "HeleneSmith1234**", RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void test_errorRegistration() throws Exception {

        // Missing first name.
        this.mvc.perform(this.getRegistrationBuilder(null, "Bond", "james.bond@mi6.uk", "JamesBond007**", RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Missing last name.
        this.mvc.perform(this.getRegistrationBuilder("James", null, "james.bond@mi6.uk", "JamesBond007**", RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Missing email.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", null, "JamesBond007**", RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Missing password.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", null, RegistrationType.STUDENT.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Missing registration type.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "JamesBond007**", null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad first name.
        this.mvc.perform(this.getRegistrationBuilder("J", "Bond", "james.bond@mi6.uk", "JamesBond007**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad last name.
        this.mvc.perform(this.getRegistrationBuilder("James", "B", "james.bond@mi6.uk", "JamesBond007**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad email 1.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond", "JamesBond007**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad email 2.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@", "JamesBond007**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad email 3.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.uk", "JamesBond007**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad password 1.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "james", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad password 2.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "jamesBond", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad password 3.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "jamesBond007", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad password 4.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "jamesBond**", "STUDENT"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Bad registration type.
        this.mvc.perform(this.getRegistrationBuilder("James", "Bond", "james.bond@mi6.uk", "JamesBond007**", "TEACHER"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private MockHttpServletRequestBuilder getLoginBuilder(String email, String password) throws JsonProcessingException {

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        return MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));
    }

    private MockHttpServletRequestBuilder getRegistrationBuilder(String firstName, String lastName, String email, String password, String type) throws JsonProcessingException {

        ObjectNode node = this.mapper.createObjectNode();

        node.put("firstName", firstName);
        node.put("lastName", lastName);
        node.put("email", email);
        node.put("password", password);
        node.put("type", type);

        return MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(node));
    }
}
