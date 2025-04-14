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
        // Mock data
        Departement departement1 = new Departement(1, "Informatique");
        Departement departement2 = new Departement(2, "Mathématiques");
        List<Departement> departementList = Arrays.asList(departement1, departement2);

        // Mocking behavior
        when(departementService.retrieveAllDepartements()).thenReturn(departementList);

        // Perform the test
        ResponseEntity<List<Departement>> responseEntity = departementController.getDepartements();

        // Verify the interactions
        verify(departementService, times(1)).retrieveAllDepartements();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testGetDepartementById() {
        // Mock data
        Departement departement = new Departement(1, "Informatique");

        // Mocking behavior
        when(departementService.retrieveDepartement(1)).thenReturn(departement);

        // Perform the test
        ResponseEntity<Departement> responseEntity = departementController.retrieveDepartement(1);

        // Verify the interactions
        verify(departementService, times(1)).retrieveDepartement(1);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departement, responseEntity.getBody());
    }

    @Test
    public void testAddDepartement() {
        // Mock data
        Departement departement = new Departement();
        departement.setNomDepart("Informatique");

        // Mocking behavior
        when(departementService.addDepartement(any(Departement.class))).thenReturn(departement);

        // Perform the test
        ResponseEntity<Departement> responseEntity = departementController.addDepartement(departement);

        // Verify the interactions
        verify(departementService, times(1)).addDepartement(any(Departement.class));

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testUpdateDepartement() {
        // Mock data
        Departement departement = new Departement();
        departement.setNomDepart("Informatique Modifié");

        // Mocking behavior
        when(departementService.updateDepartement(any(Departement.class))).thenReturn(departement);

        // Perform the test
        ResponseEntity<Departement> responseEntity = departementController.updateDepartement(departement);

        // Verify the interactions
        verify(departementService, times(1)).updateDepartement(any(Departement.class));

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testDeleteDepartement() {
        // Mocking behavior
        doNothing().when(departementService).deleteDepartement(anyInt());

        // Perform the test
        ResponseEntity<Void> responseEntity = departementController.removeDepartement(1);

        // Verify the interactions
        verify(departementService, times(1)).deleteDepartement(1);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}