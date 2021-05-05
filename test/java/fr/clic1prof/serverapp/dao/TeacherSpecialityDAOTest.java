package fr.clic1prof.serverapp.dao;

import fr.clic1prof.serverapp.dao.other.TeacherSpecialityDAO;
import fr.clic1prof.serverapp.model.profile.Speciality;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class TeacherSpecialityDAOTest {

    @Autowired
    private TeacherSpecialityDAO dao;

    @Test
    public void test_getSpecialities() {

        List<Speciality> specialities = this.dao.getSpecialities();

        Assertions.assertEquals(7, specialities.size());
        Assertions.assertEquals(specialities.get(0), new Speciality(1, "Mathématiques"));
        Assertions.assertEquals(specialities.get(3), new Speciality(4, "SVT"));
        Assertions.assertEquals(specialities.get(6), new Speciality(7, "Anglais"));
    }

    @Test
    public void test_getTeacherSpecialities() {

        Assertions.assertEquals(2, this.dao.getSpecialities(12).size());
        Assertions.assertEquals(0, this.dao.getSpecialities(14).size());

        Assertions.assertEquals(0, this.dao.getSpecialities(-1).size());
        Assertions.assertEquals(0, this.dao.getSpecialities(3000).size());
    }

    @Test
    public void test_specialityExists() {

        Assertions.assertTrue(this.dao.exists(1));
        Assertions.assertTrue(this.dao.exists(7));

        Assertions.assertFalse(this.dao.exists(-1));
        Assertions.assertFalse(this.dao.exists(3000));

        Assertions.assertTrue(this.dao.exists(3, 4, 5));
        Assertions.assertTrue(this.dao.exists(1, 2, 3, 4, 5, 6, 7));

        Assertions.assertFalse(this.dao.exists(-1, 1, 2, 3));
        Assertions.assertFalse(this.dao.exists(1, 2, 3, 3000));
    }
}
