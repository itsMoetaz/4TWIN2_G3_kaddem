package tn.esprit.spring.kaddem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

class EtudiantServiceImplTest {

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private DepartementRepository departementRepository;

    private Etudiant etudiant;
    private List<Etudiant> etudiantList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");

        etudiantList = new ArrayList<>();
        etudiantList.add(etudiant);
    }

    @Test
    void testRetrieveAllEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(etudiantList);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getNomE());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testAddEtudiant() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);

        assertNotNull(result);
        assertEquals("John", result.getNomE());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testUpdateEtudiant() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.updateEtudiant(etudiant);

        assertNotNull(result);
        assertEquals("John", result.getNomE());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testRetrieveEtudiant() {
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1);

        assertNotNull(result);
        assertEquals("John", result.getNomE());
        verify(etudiantRepository, times(1)).findById(1);
    }

    @Test
    void testRemoveEtudiant() {
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).delete(etudiant);

        etudiantService.removeEtudiant(1);

        verify(etudiantRepository, times(1)).findById(1);
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void testAssignEtudiantToDepartement() {
        Departement departement = new Departement();
        departement.setIdDepart(1);

        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        etudiantService.assignEtudiantToDepartement(1, 1);

        assertEquals(departement, etudiant.getDepartement());
        verify(etudiantRepository, times(1)).findById(1);
        verify(departementRepository, times(1)).findById(1);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testAddAndAssignEtudiantToEquipeAndContract() {
        Contrat contrat = new Contrat();
        Equipe equipe = new Equipe();
        equipe.setEtudiants(new HashSet<>()); // Initialize the set

        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(etudiant, 1, 1);

        assertNotNull(result);
        assertEquals(etudiant, contrat.getEtudiant());
        assertTrue(equipe.getEtudiants().contains(etudiant));
        verify(contratRepository, times(1)).findById(1);
        verify(equipeRepository, times(1)).findById(1);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testGetEtudiantsByDepartement() {
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(1)).thenReturn(etudiantList);

        List<Etudiant> result = etudiantService.getEtudiantsByDepartement(1);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getNomE());
        verify(etudiantRepository, times(1)).findEtudiantsByDepartement_IdDepart(1);
    }
}