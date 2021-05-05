package fr.clic1prof.serverapp.dao;

import fr.clic1prof.serverapp.dao.contacts.StudentContactDAO;
import fr.clic1prof.serverapp.model.contacts.ContactModel;
import fr.clic1prof.serverapp.model.contacts.TeacherContact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class StudentContactDAOTest {

    @Autowired
    private StudentContactDAO dao;

    @Test // 2 contacts.
    public void test_getContactsStudent1() {

        List<ContactModel> expected = Arrays.asList(
                new TeacherContact(5, "David", "Willis", ""),
                new TeacherContact(6, "Bruce", "Davis", "")
        );

        Collection<ContactModel> contacts = this.dao.getContacts(1);

        Assertions.assertEquals(2, contacts.size());
        Assertions.assertEquals(expected, contacts);
    }

    @Test // 2 contacts.
    public void test_getContactsStudent2() {

        List<ContactModel> expected = Arrays.asList(
                new TeacherContact(5, "David", "Willis", ""),
                new TeacherContact(6, "Bruce", "Davis", "")
        );

        Collection<ContactModel> contacts = this.dao.getContacts(2);

        Assertions.assertEquals(2, contacts.size());
        Assertions.assertEquals(expected, contacts);
    }

    @Test // No contacts.
    public void test_getContactsStudent4() {

        Collection<ContactModel> contacts = this.dao.getContacts(4);

        Assertions.assertEquals(0, contacts.size());
    }

    @Test
    public void test_getContactsStudentNotExists() {

        Collection<ContactModel> contacts = this.dao.getContacts(3000);

        Assertions.assertEquals(0, contacts.size());
    }
}
