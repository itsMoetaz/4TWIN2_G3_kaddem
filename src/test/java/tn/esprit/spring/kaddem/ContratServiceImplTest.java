package tn.esprit.spring.kaddem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    private Contrat contrat;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création des objets de test
        etudiant = new Etudiant();
        etudiant.setNomE("Dupont");
        etudiant.setPrenomE("Jean");

        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setSpecialite(tn.esprit.spring.kaddem.entities.Specialite.IA);
        contrat.setEtudiant(etudiant);
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30)); // 30 jours plus tard
    }

    @Test
    void testAddContrat() {
        // Arrange
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat addedContrat = contratService.addContrat(contrat);

        // Assert
        assertNotNull(addedContrat);
        assertEquals(1, addedContrat.getIdContrat());
        verify(contratRepository, times(1)).save(any(Contrat.class));
    }

    @Test
    void testUpdateContrat() {
        // Arrange
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat updatedContrat = contratService.updateContrat(contrat);

        // Assert
        assertNotNull(updatedContrat);
        assertEquals(1, updatedContrat.getIdContrat());
        verify(contratRepository, times(1)).save(any(Contrat.class));
    }

    @Test
    void testRetrieveContrat() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        // Act
        Contrat foundContrat = contratService.retrieveContrat(1);

        // Assert
        assertNotNull(foundContrat);
        assertEquals(1, foundContrat.getIdContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    void testRemoveContrat() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        // Act
        contratService.removeContrat(1);

        // Assert
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void testAffectContratToEtudiant() {
        // Arrange
        when(etudiantRepository.findByNomEAndPrenomE("Dupont", "Jean")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1)).thenReturn(contrat);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat updatedContrat = contratService.affectContratToEtudiant(1, "Dupont", "Jean");

        // Assert
        assertNotNull(updatedContrat);
        assertEquals(etudiant, updatedContrat.getEtudiant());
        verify(contratRepository, times(1)).save(any(Contrat.class));
    }

    @Test
    void testNbContratsValides() {
        // Arrange
        Date startDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 10); // 10 jours avant
        Date endDate = new Date(System.currentTimeMillis());
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        // Act
        Integer nbContratsValides = contratService.nbContratsValides(startDate, endDate);

        // Assert
        assertNotNull(nbContratsValides);
        assertEquals(5, nbContratsValides);
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }


}
