package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
//pour pouvoir ordonner le lancement des tests selon
//order travailler
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//une classe de test
@SpringBootTest
public class UniversiteServiceImplTest {
    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository;

    private Universite universite;
    private Departement departement;

    @Captor
    private ArgumentCaptor<Universite> universiteCaptor;

    @BeforeEach
    public void setUp() {
        universite = new Universite();

        universite.setIdUniv(1);
        universite.setNomUniv("Université Test");

        departement = new Departement();

        departement.setIdDepart(1);
        departement.setNomDepart("Département Test");
    }

    // Test avancé : Récupérer toutes les universités avec une liste vide
    @Test
    public void testRetrieveAllUniversites_EmptyList() {
        // Arrange
        when(universiteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Universite> result = universiteService.retrieveAllUniversites();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(universiteRepository, times(1)).findAll();
    }

    // Test avancé : Ajouter une université avec validation d'exception
    @Test
    public void testAddUniversite_ThrowsExceptionOnNull() {
        // Arrange
        Universite universiteNull = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> universiteService.addUniversite(universiteNull));
        verify(universiteRepository, never()).save(any(Universite.class));
    }

    // Test avancé : Mise à jour d'une université avec ArgumentCaptor
    @Test
    public void testUpdateUniversite_UsingArgumentCaptor() {
        // Arrange
        universite.setNomUniv("Université Modifiée");
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // Act
        Universite result = universiteService.updateUniversite(universite);

        // Assert
        assertEquals("Université Modifiée", result.getNomUniv());
        verify(universiteRepository).save(universiteCaptor.capture());
        Universite capturedUniversite = universiteCaptor.getValue();
        assertEquals("Université Modifiée", capturedUniversite.getNomUniv());
    }

    // Test avancé : Récupérer une université inexistante
    @Test
    public void testRetrieveUniversite_NotFound() {
        // Arrange
        when(universiteRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> universiteService.retrieveUniversite(999));
        verify(universiteRepository, times(1)).findById(999);
    }

    // Test avancé : Suppression d'une université inexistante
    @Test
    public void testDeleteUniversite_NotFound() {
        // Arrange
        doThrow(new IllegalArgumentException("Université non trouvée")).when(universiteRepository).deleteById(999);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> universiteService.deleteUniversite(999));
        verify(universiteRepository, times(1)).deleteById(999);
    }

    // Test avancé : Assignation d'une université à un département avec mocks complexes
    @Test
    public void testAssignUniversiteToDepartement_ComplexScenario() {
        // Arrange
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Simuler un comportement personnalisé avec Answer
        when(universiteRepository.save(any(Universite.class))).thenAnswer(invocation -> {
            Universite savedUniversite = invocation.getArgument(0);
            savedUniversite.getDepartements().add(departement);
            return savedUniversite;
        });

        // Act
        universiteService.assignUniversiteToDepartement(1, 1);

        // Assert
        verify(universiteRepository).save(universiteCaptor.capture());
        Universite capturedUniversite = universiteCaptor.getValue();
        assertTrue(capturedUniversite.getDepartements().contains(departement));
        verify(universiteRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).findById(1);
    }

    // Test avancé : Récupérer les départements avec un cas d'erreur
    @Test
    public void testRetrieveDepartementsByUniversite_UniversiteNotFound() {
        // Arrange
        when(universiteRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> universiteService.retrieveDepartementsByUniversite(999));
        verify(universiteRepository, times(1)).findById(999);
    }

    // Test avancé : Vérifier l'ordre des appels avec InOrder
    @Test
    public void testAssignUniversiteToDepartement_VerifyOrder() {
        // Arrange
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        // Act
        universiteService.assignUniversiteToDepartement(1, 1);

        // Assert : Vérifier l'ordre des appels
        InOrder inOrder = inOrder(universiteRepository, departementRepository);
        inOrder.verify(universiteRepository).findById(1);
        inOrder.verify(departementRepository).findById(1);
        inOrder.verify(universiteRepository).save(any(Universite.class));
    }

    // Test avancé : Simuler une exception dans le repository
    @Test
    public void testRetrieveAllUniversites_RepositoryThrowsException() {
        // Arrange
        when(universiteRepository.findAll()).thenThrow(new RuntimeException("Erreur base de données"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> universiteService.retrieveAllUniversites());
        verify(universiteRepository, times(1)).findAll();
    }
}
