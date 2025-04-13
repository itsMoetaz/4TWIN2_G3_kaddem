package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
@ExtendWith(MockitoExtension.class)
class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    private Universite universite;
    private Departement departement;

    @BeforeEach
    void setUp() {
        universite = new Universite(1, "Test University");
        departement = new Departement();
        departement.setIdDepart(1);
    }

    @Test
    void testRetrieveAllUniversites() {
        // Arrange
        List<Universite> universiteList = Arrays.asList(universite);
        when(universiteRepository.findAll()).thenReturn(universiteList);

        // Act
        List<Universite> result = universiteService.retrieveAllUniversites();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test University", result.get(0).getNomUniv());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testAddUniversite() {
        // Arrange
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // Act
        Universite result = universiteService.addUniversite(universite);

        // Assert
        assertNotNull(result);
        assertEquals("Test University", result.getNomUniv());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testRetrieveUniversite() {
        // Arrange
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // Act
        Universite result = universiteService.retrieveUniversite(1);

        // Assert
        assertNotNull(result);
        assertEquals("Test University", result.getNomUniv());
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUniversite() {
        // Arrange
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        doNothing().when(universiteRepository).delete(universite);

        // Act
        universiteService.deleteUniversite(1);

        // Assert
        verify(universiteRepository, times(1)).findById(1);
        verify(universiteRepository, times(1)).delete(universite);
    }

    @Test
    void testAssignUniversiteToDepartement() {
        // Arrange
        universite.setDepartements(new HashSet<>()); // Initialisation ici departement pour eviter null
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // Act
        universiteService.assignUniversiteToDepartement(1, 1);

        // Assert
        verify(universiteRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).findById(1);
        verify(universiteRepository, times(1)).save(universite);
    }

   @Test
    void testRetrieveDepartementsByUniversite() {
        // Arrange
        Set<Departement> departements = new HashSet<>();
        departements.add(departement);
        universite.setDepartements(departements);
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        // Act
        Set<Departement> result = universiteService.retrieveDepartementsByUniversite(1);

        // Assert
        assertEquals(1, result.size());
        verify(universiteRepository, times(1)).findById(1);
    }
}
