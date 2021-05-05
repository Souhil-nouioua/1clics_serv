package fr.clic1prof.serverapp.dao;

import fr.clic1prof.serverapp.dao.contacts.TeacherContactDAO;
import fr.clic1prof.serverapp.model.contacts.ContactModel;
import fr.clic1prof.serverapp.model.contacts.StudentContact;
import fr.clic1prof.serverapp.model.profile.SchoolLevel;
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
public class TeacherContactDAOTest {

    @Autowired
    private TeacherContactDAO dao;

    @Test // 2 contacts.
    public void test_getContactsTeacher5() {

        List<ContactModel> expected = Arrays.asList(
                new StudentContact(1, "Jean", "Mickael", new SchoolLevel(12, "Terminale")),
                new StudentContact(2, "Bernard", "Flou", null)
        );

        Collection<ContactModel> contacts = this.dao.getContacts(5);

        Assertions.assertEquals(2, contacts.size());
        Assertions.assertEquals(expected, contacts);
    }

    @Test // 3 contacts.
    public void test_getContactsTeacher6() {

        List<ContactModel> expected = Arrays.asList(
                new StudentContact(1, "Jean", "Mickael", new SchoolLevel(12, "Terminale")),
                new StudentContact(2, "Bernard", "Flou", null),
                new StudentContact(3, "Winnie", "Ourson", null)
        );

        Collection<ContactModel> contacts = this.dao.getContacts(6);

        Assertions.assertEquals(3, contacts.size());
        Assertions.assertEquals(expected, contacts);
    }

    @Test // No contacts.
    public void test_getContactsTeacher8() {

        Collection<ContactModel> contacts = this.dao.getContacts(8);

        Assertions.assertEquals(0, contacts.size());
    }

    @Test
    public void test_getContactsTeacherNotExists() {

        Collection<ContactModel> contacts = this.dao.getContacts(3000);

        Assertions.assertEquals(0, contacts.size());
    }
}
