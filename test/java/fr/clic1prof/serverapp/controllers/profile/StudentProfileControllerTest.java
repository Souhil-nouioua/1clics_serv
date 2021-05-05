package fr.clic1prof.serverapp.controllers.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.clic1prof.serverapp.model.profile.SchoolLevel;
import fr.clic1prof.serverapp.model.profile.model.StudentProfile;
import fr.clic1prof.serverapp.model.profile.model.TeacherProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class StudentProfileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("UserProfileControllerTest")
    private UserProfileControllerTest controller;

    @Autowired
    public void login() throws Exception {
        this.controller.login("test9.student@test.com", "UnRenard60**");
    }

    @Test
    public void test_updateFirstName() throws Exception {
        this.controller.test_updateFirstName("test9.student@test.com", "UnRenard60**", "/student/profile", "John");
    }

    @Test
    public void test_updateLastName() throws Exception {
        this.controller.test_updateLastName("test9.student@test.com", "UnRenard60**", "/student/profile", "Smith");
    }

    @Test
    public void test_updatePassword() throws Exception {
        this.controller.test_updatePassword("test9.student@test.com", "UnRenard60**", "/student/profile");
    }

    @Test
    public void test_getPicture() throws Exception {
        this.controller.test_getPicture("test9.student@test.com", "UnRenard60**", "/student/profile");
    }

    @Test
    public void test_deletePicture() throws Exception {
        this.controller.test_deletePicture("test9.student@test.com", "UnRenard60**", "/student/profile");
    }

    @Test
    public void test_updatePicture() throws Exception {
        this.controller.test_updatePicture("test9.student@test.com", "UnRenard60**", "/student/profile");
    }

    @Test
    public void test_updateSchoolLevel() throws Exception {

        String uri = "/student/profile/school-level";
        String token = this.controller.login("test9.student@test.com", "UnRenard60**");

        // With a valid id.
        ObjectNode node = this.mapper.createObjectNode();
        node.put("id", 1);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Checking that the description has been updated.
        String profileAsString = this.mvc.perform(MockMvcRequestBuilders.get("/student/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        StudentProfile profile = this.mapper.readValue(profileAsString, StudentProfile.class);

        Assertions.assertEquals(new SchoolLevel(1, "CP"), profile.getLevel());

        // With an invalid positive id.
        node = this.mapper.createObjectNode();
        node.put("id", 3000);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // With an invalid negative id.
        node = this.mapper.createObjectNode();
        node.put("id", -1);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_getProfileComplete() throws Exception {

        String uri = "/student/profile";
        String token = this.controller.login("test11.student@test.com", "UnRenard60**");

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        StudentProfile profile = this.mapper.readValue(content, StudentProfile.class);

        Assertions.assertNotNull(profile);
        Assertions.assertEquals("Leonardo", profile.getFirstName());
        Assertions.assertEquals("Davinci", profile.getLastName());
        Assertions.assertEquals(new SchoolLevel(12, "Terminale"), profile.getLevel());
    }

    @Test
    public void test_getProfilePartial() throws Exception {

        String uri = "/student/profile";
        String token = this.controller.login("test13.student@test.com", "UnRenard60**");

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        StudentProfile profile = this.mapper.readValue(content, StudentProfile.class);

        Assertions.assertNotNull(profile);
        Assertions.assertEquals("Asuna", profile.getFirstName());
        Assertions.assertEquals("Yuki", profile.getLastName());
        Assertions.assertNull(profile.getLevel());
    }
}
