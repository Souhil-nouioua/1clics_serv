package fr.clic1prof.serverapp.controllers.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.clic1prof.serverapp.model.profile.Speciality;
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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class TeacherProfileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("UserProfileControllerTest")
    private UserProfileControllerTest controller;

    @Autowired
    public void login() throws Exception {
        this.controller.login("test10.teacher@test.com", "UnRenard60**");
    }

    @Test
    public void test_updateFirstName() throws Exception {
        this.controller.test_updateFirstName("test10.teacher@test.com", "UnRenard60**", "/teacher/profile", "John");
    }

    @Test
    public void test_updateLastName() throws Exception {
        this.controller.test_updateLastName("test10.teacher@test.com", "UnRenard60**", "/teacher/profile", "Smith");
    }

    @Test
    public void test_updatePassword() throws Exception {
        this.controller.test_updatePassword("test10.teacher@test.com", "UnRenard60**", "/teacher/profile");
    }

    @Test
    public void test_getPicture() throws Exception {
        this.controller.test_getPicture("test10.teacher@test.com", "UnRenard60**", "/teacher/profile");
    }

    @Test
    public void test_deletePicture() throws Exception {
        this.controller.test_deletePicture("test10.teacher@test.com", "UnRenard60**", "/teacher/profile");
    }

    @Test
    public void test_updatePicture() throws Exception {
        this.controller.test_updatePicture("test10.teacher@test.com", "UnRenard60**", "/teacher/profile");
    }

    @Test
    public void test_updateDescription() throws Exception {

        String uri = "/teacher/profile/description";
        String token = this.controller.login("test10.teacher@test.com", "UnRenard60**");

        // With a valid description.
        ObjectNode node = this.mapper.createObjectNode();
        node.put("description", "This is my new description.");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Checking that the description has been updated.
        String profileAsString = this.mvc.perform(MockMvcRequestBuilders.get("/teacher/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        TeacherProfile profile = this.mapper.readValue(profileAsString, TeacherProfile.class);

        Assertions.assertEquals("This is my new description.", profile.getDescription());

        // With an empty description.
        node = this.mapper.createObjectNode();
        node.put("description", "");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Checking that the description has been updated.
        profileAsString = this.mvc.perform(MockMvcRequestBuilders.get("/teacher/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        profile = this.mapper.readValue(profileAsString, TeacherProfile.class);

        Assertions.assertEquals("", profile.getDescription());

        // Without description.
        this.mvc.perform(this.controller.getBuilder(uri, token, null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // With a too long description.
        node = this.mapper.createObjectNode();
        node.put("description", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_updateStudies() throws Exception {

        String uri = "/teacher/profile/studies";
        String token = this.controller.login("test10.teacher@test.com", "UnRenard60**");

        // With a valid description.
        ObjectNode node = this.mapper.createObjectNode();
        node.put("studies", "Engineer in computer science");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Checking that the studies has been updated.
        String profileAsString = this.mvc.perform(MockMvcRequestBuilders.get("/teacher/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        TeacherProfile profile = this.mapper.readValue(profileAsString, TeacherProfile.class);

        Assertions.assertEquals("Engineer in computer science", profile.getStudies());

        // With an empty description.
        node = this.mapper.createObjectNode();
        node.put("studies", "");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Checking that the studies has been updated.
        profileAsString = this.mvc.perform(MockMvcRequestBuilders.get("/teacher/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        profile = this.mapper.readValue(profileAsString, TeacherProfile.class);

        Assertions.assertEquals("", profile.getStudies());

        // Without description.
        this.mvc.perform(this.controller.getBuilder(uri, token, null))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // With a too long description.
        node = this.mapper.createObjectNode();
        node.put("studies", "poijzeaoirjezaioirjezao^rezajiriezîrzejiairezjizea^rjze^zeajrizeireziirzejze");

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_updateSpeciality() throws Exception {

        String uri = "/teacher/profile/speciality";
        String token = this.controller.login("test10.teacher@test.com", "UnRenard60**");

        // With a valid request.
        ObjectNode node = this.mapper.createObjectNode();
        node.put("toReplace", 1);
        node.put("replaceWith", 7);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Want to replace a speciality that he doesn't own.
        node = this.mapper.createObjectNode();
        node.put("toReplace", 2);
        node.put("replaceWith", 1);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Want to replace an invalid speciality.
        node = this.mapper.createObjectNode();
        node.put("toReplace", 3000);
        node.put("replaceWith", 1);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Want to replace with an invalid speciality.
        node = this.mapper.createObjectNode();
        node.put("toReplace", 4);
        node.put("replaceWith", 3000);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Want to replace with a speciality already owned.
        node = this.mapper.createObjectNode();
        node.put("toReplace", 4);
        node.put("replaceWith", 4);

        this.mvc.perform(this.controller.getBuilder(uri, token, node))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test_getProfileComplete() throws Exception {

        String uri = "/teacher/profile";
        String token = this.controller.login("test12.teacher@test.com", "UnRenard60**");

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        TeacherProfile profile = this.mapper.readValue(content, TeacherProfile.class);

        Assertions.assertNotNull(profile);
        Assertions.assertEquals("Harry", profile.getFirstName());
        Assertions.assertEquals("Potter", profile.getLastName());
        Assertions.assertEquals("Un sorcier puissant.", profile.getDescription());
        Assertions.assertEquals("Poudlard", profile.getStudies());

        List<Speciality> specialities = Arrays.asList(
                new Speciality(2, "Physique"),
                new Speciality(4, "SVT")
        );

        Assertions.assertEquals(2, profile.getSpecialities().size());
        Assertions.assertEquals(specialities, profile.getSpecialities());
    }

    @Test
    public void test_getProfilePartial() throws Exception {

        String uri = "/teacher/profile";
        String token = this.controller.login("test14.teacher@test.com", "UnRenard60**");

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        TeacherProfile profile = this.mapper.readValue(content, TeacherProfile.class);

        Assertions.assertEquals("Will", profile.getFirstName());
        Assertions.assertEquals("Smith", profile.getLastName());
        Assertions.assertEquals("", profile.getDescription());
        Assertions.assertEquals("", profile.getStudies());
        Assertions.assertEquals(0, profile.getSpecialities().size());
    }
}
