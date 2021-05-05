package fr.clic1prof.serverapp.controllers.invoices;

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

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class StudentInvoiceControllerTest {

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
    public void test_getNoInvoices() throws Exception {

        String uri = "/student/invoices";
        String token = this.login("test13.student@test.com", "UnRenard60**");

        // No invoice.
        String invoicesAsString = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Document> invoices = this.mapper.readValue(invoicesAsString, new TypeReference<List<Document>>(){});

        Assertions.assertEquals(0, invoices.size());
    }

    @Test
    public void test_getInvoices() throws Exception {

        String uri = "/student/invoices";
        String token = this.login("test11.student@test.com", "UnRenard60**");

        String invoicesAsString = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Document> invoices = this.mapper.readValue(invoicesAsString, new TypeReference<List<Document>>(){});

        Assertions.assertEquals(1, invoices.size());

        Document invoice = invoices.get(0);

        Assertions.assertEquals(3, invoice.getId());
        Assertions.assertEquals(11, invoice.getOwnerId());
        Assertions.assertEquals("ce946036-03de-4146-9184-c7876f934ea8", invoice.getFileId());
        Assertions.assertEquals("invoice", invoice.getName());
        Assertions.assertEquals("application/pdf", invoice.getMediaType());
        Assertions.assertEquals(13264, invoice.getSize());
        Assertions.assertEquals(DocumentType.INVOICE, invoice.getType());
        Assertions.assertNotNull(invoice.getCreationDate());
        Assertions.assertNotNull(invoice.getModificationDate());
    }

    @Test
    public void test_getInvoice() throws Exception {

        String uri = "/student/invoice/3";
        String token = this.login("test11.student@test.com", "UnRenard60**");

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
    public void test_getNoInvoice() throws Exception {

        String uri = "/student/invoice/100";
        String token = this.login("test11.student@test.com", "UnRenard60**");

        this.mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
