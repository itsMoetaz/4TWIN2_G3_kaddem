package tn.esprit.spring.kaddem.services;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.kaddem.controllers.DepartementRestController;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartementRestController.class)
public class DepartementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDepartementService departementService;

    @Autowired
    private ObjectMapper objectMapper;

    private Departement departement;

    @BeforeEach
    void setUp() {
        departement = new Departement(1, "Informatique");
    }

    @Test
    void testGetDepartements() throws Exception {
        // Arrange
        List<Departement> departementList = Arrays.asList(
                new Departement(1, "Informatique"),
                new Departement(2, "Mathématiques")
        );
        when(departementService.retrieveAllDepartements()).thenReturn(departementList);

        // Act & Assert
        mockMvc.perform(get("/departement/retrieve-all-departements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nomDepart", is("Informatique")))
                .andExpect(jsonPath("$[1].nomDepart", is("Mathématiques")));

        verify(departementService, times(1)).retrieveAllDepartements();
    }

    @Test
    void testRetrieveDepartement() throws Exception {
        // Arrange
        when(departementService.retrieveDepartement(anyInt())).thenReturn(departement);

        // Act & Assert
        mockMvc.perform(get("/departement/retrieve-departement/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDepart", is("Informatique")));

        verify(departementService, times(1)).retrieveDepartement(1);
    }

    @Test
    void testAddDepartement() throws Exception {
        // Arrange
        when(departementService.addDepartement(any(Departement.class))).thenReturn(departement);

        // Act & Assert
        mockMvc.perform(post("/departement/add-departement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDepart", is("Informatique")));

        verify(departementService, times(1)).addDepartement(any(Departement.class));
    }

    @Test
    void testUpdateDepartement() throws Exception {
        // Arrange
        departement.setNomDepart("Informatique Modifié");
        when(departementService.updateDepartement(any(Departement.class))).thenReturn(departement);

        // Act & Assert
        mockMvc.perform(put("/departement/update-departement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDepart", is("Informatique Modifié")));

        verify(departementService, times(1)).updateDepartement(any(Departement.class));
    }

    @Test
    void testRemoveDepartement() throws Exception {
        // Arrange
        doNothing().when(departementService).deleteDepartement(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/departement/remove-departement/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(departementService, times(1)).deleteDepartement(1);
    }
}
