package tn.esprit.spring.kaddem.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DepartementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartementRepository departementRepository;

    private Departement departement;

    @BeforeEach
    void setUp() {
        departement = new Departement(null, "Informatique");
        entityManager.persist(departement);
        entityManager.flush();
    }

    @Test
    void testFindAll() {
        // Arrange
        Departement departement2 = new Departement(null, "Mathématiques");
        entityManager.persist(departement2);
        entityManager.flush();

        // Act
        List<Departement> departements = (List<Departement>) departementRepository.findAll();

        // Assert
        assertEquals(2, departements.size());
        assertEquals("Informatique", departements.get(0).getNomDepart());
        assertEquals("Mathématiques", departements.get(1).getNomDepart());
    }

    @Test
    void testFindById() {
        // Act
        Optional<Departement> found = departementRepository.findById(departement.getIdDepart());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Informatique", found.get().getNomDepart());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Departement> found = departementRepository.findById(999);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testSave() {
        // Arrange
        Departement newDepartement = new Departement(null, "Physique");

        // Act
        Departement saved = departementRepository.save(newDepartement);

        // Assert
        assertNotNull(saved.getIdDepart());
        assertEquals("Physique", saved.getNomDepart());
    }

    @Test
    void testDelete() {
        // Act
        departementRepository.delete(departement);
        Optional<Departement> found = departementRepository.findById(departement.getIdDepart());

        // Assert
        assertFalse(found.isPresent());
    }
}
