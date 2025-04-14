package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.dto.DepartementDTO;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartementServiceImplTest {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    private Departement departement;
    private Etudiant etudiant;
    private DepartementDTO departementDTO;

    @BeforeEach
    void setUp() {
        departement = new Departement(1, "Informatique");
        etudiant = new Etudiant(1, "John", "Doe", null);
        etudiant.setDepartement(departement);
        departement.setEtudiants(new HashSet<>(Arrays.asList(etudiant)));
        departementDTO = new DepartementDTO(1, "Informatique");
    }

    @Test
    @Order(1)
    void testRetrieveAllDepartements() {
        // Arrange
        List<Departement> departementList = Arrays.asList(
                new Departement(1, "Informatique"),
                new Departement(2, "Mathématiques")
        );
        when(departementRepository.findAll()).thenReturn(departementList);

        // Act
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Informatique", result.get(0).getNomDepart());
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    @Order(2)
    void testAddDepartement() {
        // Arrange
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Act
        DepartementDTO result = departementService.addDepartement(departementDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Informatique", result.getNomDepart());
        verify(departementRepository, times(1)).save(any(Departement.class));
    }

    @Test
    @Order(3)
    void testAddDepartementWithNullShouldFail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> departementService.addDepartement(null));
        verify(departementRepository, never()).save(any(Departement.class));
    }

    @Test
    @Order(4)
    void testRetrieveDepartement() {
        // Arrange
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Act
        Departement result = departementService.retrieveDepartement(1);

        // Assert
        assertNotNull(result);
        assertEquals("Informatique", result.getNomDepart());
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    @Order(5)
    void testRetrieveDepartementWithInvalidId() {
        // Arrange
        when(departementRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departementService.retrieveDepartement(999));
        verify(departementRepository, times(1)).findById(999);
    }

    @Test
    @Order(6)
    void testUpdateDepartement() {
        // Arrange
        DepartementDTO updatedDTO = new DepartementDTO(1, "Informatique Modifié");
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(departementRepository.save(any(Departement.class))).thenReturn(new Departement(1, "Informatique Modifié"));

        // Act
        DepartementDTO result = departementService.updateDepartement(updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Informatique Modifié", result.getNomDepart());
        verify(departementRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).save(any(Departement.class));
    }

    @Test
    @Order(7)
    void testDeleteDepartement() {
        // Arrange
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        doNothing().when(departementRepository).delete(departement);

        // Act
        departementService.deleteDepartement(1);

        // Assert
        verify(departementRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).delete(departement);
    }

    @Test
    @Order(8)
    void testDeleteDepartementWithInvalidId() {
        // Arrange
        when(departementRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departementService.deleteDepartement(999));
        verify(departementRepository, times(1)).findById(999);
        verify(departementRepository, never()).delete(any(Departement.class));
    }

    @Test
    @Order(9)
    void testRetrieveEtudiantsByDepartement() {
        // Arrange
        Set<Etudiant> etudiants = new HashSet<>(Arrays.asList(etudiant));
        departement.setEtudiants(etudiants);
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Act
        Departement result = departementService.retrieveDepartement(1);
        Set<Etudiant> retrievedEtudiants = result.getEtudiants();

        // Assert
        assertNotNull(retrievedEtudiants);
        assertEquals(1, retrievedEtudiants.size());
        assertTrue(retrievedEtudiants.contains(etudiant));
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    @Order(10)
    void testUpdateDepartementWithInvalidId() {
        // Arrange
        DepartementDTO invalidDTO = new DepartementDTO(999, "Informatique Modifié");
        when(departementRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departementService.updateDepartement(invalidDTO));
        verify(departementRepository, times(1)).findById(999);
        verify(departementRepository, never()).save(any(Departement.class));
    }

    @Test
    @Order(11)
    void testUpdateDepartementWithNullShouldFail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> departementService.updateDepartement(null));
        verify(departementRepository, never()).findById(anyInt());
        verify(departementRepository, never()).save(any(Departement.class));
    }
}