package fr.clic1prof.serverapp.controllers.contacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.clic1prof.serverapp.model.contacts.ContactModel;
import fr.clic1prof.serverapp.model.contacts.StudentContact;
import fr.clic1prof.serverapp.model.profile.SchoolLevel;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationRequest;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class TeacherContactControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private String token;

    @Autowired
    public void login() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest("test1.teacher@test.com", "UnRenard60**");

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        this.token = this.mapper.readValue(content, AuthenticationResponse.class).getToken();
    }

    @Test
    public void test_getTeacherContacts() throws Exception {

        List<ContactModel> expected = Arrays.asList(
                new StudentContact(1, "Jean", "Mickael", new SchoolLevel(12, "Terminale")),
                new StudentContact(2, "Bernard", "Flou", null)
        );

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get("/teacher/contacts")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        List<StudentContact> contacts = this.mapper.readValue(response, new TypeReference<List<StudentContact>>(){});

        Assertions.assertEquals(2, contacts.size());
        Assertions.assertEquals(expected, contacts);
    }

    @Test
    public void test_teacherContactsAccessibility() throws Exception {

        this.mvc.perform(MockMvcRequestBuilders.get("/student/contacts")
                .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isForbidden());
    }
}
