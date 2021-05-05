package fr.clic1prof.serverapp.controllers.payslips;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.clic1prof.serverapp.file.model.Document;
import fr.clic1prof.serverapp.file.model.DocumentType;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationRequest;
import fr.clic1prof.serverapp.security.jwt.authentication.AuthenticationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class TeacherPayslipControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    public String login(String email, String password) throws Exception {

        AuthenticationRequest request = new AuthenticationRequest(email, password);

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        return this.mapper.readValue(content, AuthenticationResponse.class).getToken();
    }

    @Test
    public void test_getNoPayslips() throws Exception {

        String uri = "/teacher/payslips";
        String token = this.login("test14.teacher@test.com", "UnRenard60**");

        // No invoice.
        String invoicesAsString = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Document> invoices = this.mapper.readValue(invoicesAsString, new TypeReference<List<Document>>(){});

        Assertions.assertEquals(0, invoices.size());
    }

    @Test
    public void test_getPayslips() throws Exception {

        String uri = "/teacher/payslips";
        String token = this.login("test12.teacher@test.com", "UnRenard60**");

        String invoicesAsString = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Document> invoices = this.mapper.readValue(invoicesAsString, new TypeReference<List<Document>>(){});

        Assertions.assertEquals(1, invoices.size());

        Document invoice = invoices.get(0);

        Assertions.assertEquals(4, invoice.getId());
        Assertions.assertEquals(12, invoice.getOwnerId());
        Assertions.assertEquals("43a203da-670f-441b-af42-ffca01744b9b", invoice.getFileId());
        Assertions.assertEquals("payslip", invoice.getName());
        Assertions.assertEquals("application/pdf", invoice.getMediaType());
        Assertions.assertEquals(13264, invoice.getSize());
        Assertions.assertEquals(DocumentType.PAYSLIP, invoice.getType());
        Assertions.assertNotNull(invoice.getCreationDate());
        Assertions.assertNotNull(invoice.getModificationDate());
    }

    @Test
    public void test_getPayslip() throws Exception {

        String uri = "/teacher/payslip/4";
        String token = this.login("test12.teacher@test.com", "UnRenard60**");

        MockHttpServletResponse response = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        // The software Postman (or another) must be used to check that the document has
        // been successfully downloaded. Don't forget to use it while testing !

        Assertions.assertEquals("application/pdf", response.getContentType());
        Assertions.assertEquals(13264, response.getContentLength());
    }

    @Test
    public void test_getNoPayslip() throws Exception {

        String uri = "/teacher/payslip/100";
        String token = this.login("test12.teacher@test.com", "UnRenard60**");

       this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
