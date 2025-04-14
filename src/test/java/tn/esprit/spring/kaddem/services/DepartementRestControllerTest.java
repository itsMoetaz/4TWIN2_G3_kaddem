package tn.esprit.spring.kaddem.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.spring.kaddem.controllers.DepartementRestController;
import tn.esprit.spring.kaddem.dto.DepartementDTO;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DepartementRestControllerTest {

    @Mock
    private IDepartementService departementService;

    @InjectMocks
    private DepartementRestController departementController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllDepartements() {
        Departement departement1 = new Departement(1, "Informatique");
        Departement departement2 = new Departement(2, "Mathématiques");
        List<Departement> departementList = Arrays.asList(departement1, departement2);

        when(departementService.retrieveAllDepartements()).thenReturn(departementList);

        ResponseEntity<List<Departement>> responseEntity = departementController.getDepartements();

        verify(departementService, times(1)).retrieveAllDepartements();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testGetDepartementById() {
        Departement departement = new Departement(1, "Informatique");

        when(departementService.retrieveDepartement(1)).thenReturn(departement);

        ResponseEntity<Departement> responseEntity = departementController.retrieveDepartement(1);

        verify(departementService, times(1)).retrieveDepartement(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departement, responseEntity.getBody());
    }

    @Test
    public void testAddDepartement() {
        DepartementDTO departementDTO = new DepartementDTO();
        departementDTO.setNomDepart("Informatique");

        when(departementService.addDepartement(any(DepartementDTO.class))).thenReturn(departementDTO);

        ResponseEntity<DepartementDTO> responseEntity = departementController.addDepartement(departementDTO);

        verify(departementService, times(1)).addDepartement(any(DepartementDTO.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testUpdateDepartement() {
        DepartementDTO departementDTO = new DepartementDTO();
        departementDTO.setIdDepart(1);
        departementDTO.setNomDepart("Informatique Modifié");

        when(departementService.updateDepartement(any(DepartementDTO.class))).thenReturn(departementDTO);

        ResponseEntity<DepartementDTO> responseEntity = departementController.updateDepartement(departementDTO);

        verify(departementService, times(1)).updateDepartement(any(DepartementDTO.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testDeleteDepartement() {
        doNothing().when(departementService).deleteDepartement(anyInt());

        ResponseEntity<Void> responseEntity = departementController.removeDepartement(1);

        verify(departementService, times(1)).deleteDepartement(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}