package fr.clic1prof.serverapp.dao;

import fr.clic1prof.serverapp.dao.other.StudentSchoolLevelDAO;
import fr.clic1prof.serverapp.model.profile.SchoolLevel;
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
public class StudentSchoolLevelDAOTest {

    @Autowired
    private StudentSchoolLevelDAO schoolLevelDAO;

    @Test
    public void test_getSchoolLevels() {

        List<SchoolLevel> levels = this.schoolLevelDAO.getSchoolLevels();

        Assertions.assertEquals(13, levels.size());
        Assertions.assertEquals(levels.get(0), new SchoolLevel(1, "CP"));
        Assertions.assertEquals(levels.get(6), new SchoolLevel(7, "5ème"));
        Assertions.assertEquals(levels.get(11), new SchoolLevel(12, "Terminale"));
        Assertions.assertEquals(levels.get(12), new SchoolLevel(13, "Supérieur"));
    }

    @Test
    public void test_schoolLevelExists() {

        Assertions.assertTrue(this.schoolLevelDAO.exists(1));
        Assertions.assertTrue(this.schoolLevelDAO.exists(6));
        Assertions.assertTrue(this.schoolLevelDAO.exists(13));

        Assertions.assertFalse(this.schoolLevelDAO.exists(-1));
        Assertions.assertFalse(this.schoolLevelDAO.exists(3000));
    }
}
