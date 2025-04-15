package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    private Contrat contrat;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setDateDebutContrat(new Date());
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() + 86400000L * 30)); // 30 jours
        contrat.setSpecialite(Specialite.IA);
        contrat.setArchive(false);
        contrat.setMontantContrat(1500);
    }

    @Test
    public void testAddContrat() {
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat saved = contratService.addContrat(contrat);

        assertNotNull(saved);
        assertEquals(1, saved.getIdContrat());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    public void testUpdateContrat() {
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        Contrat updated = contratService.updateContrat(contrat);

        assertNotNull(updated);
        assertEquals(1500, updated.getMontantContrat());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    public void testRetrieveContrat() {
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        Contrat retrieved = contratService.retrieveContrat(1);

        assertNotNull(retrieved);
        assertEquals(1, retrieved.getIdContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    public void testRetrieveContratNotFound() {
        when(contratRepository.findById(1)).thenReturn(Optional.empty());

        Contrat retrieved = contratService.retrieveContrat(1);

        assertNull(retrieved);
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    public void testRemoveContrat() {
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(1);

        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    public void testRetrieveAllContrats() {
        List<Contrat> contrats = Arrays.asList(contrat, contrat);
        when(contratRepository.findAll()).thenReturn(contrats);

        List<Contrat> result = contratService.retrieveAllContrats();

        assertEquals(2, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    public void testNbContratsValides() {
        Date start = new Date(System.currentTimeMillis() - 86400000L * 10); // il y a 10 jours
        Date end = new Date();
        when(contratRepository.getnbContratsValides(start, end)).thenReturn(3);

        Integer count = contratService.nbContratsValides(start, end);

        assertEquals(3, count);
        verify(contratRepository, times(1)).getnbContratsValides(start, end);
    }
}
