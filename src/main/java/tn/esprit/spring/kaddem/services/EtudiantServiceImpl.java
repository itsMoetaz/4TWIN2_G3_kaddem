package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class EtudiantServiceImpl implements IEtudiantService{
	@Autowired
	EtudiantRepository etudiantRepository ;
	@Autowired
	ContratRepository contratRepository;
	@Autowired
	EquipeRepository equipeRepository;
    @Autowired
    DepartementRepository departementRepository;
	@Override
	public List<Etudiant> retrieveAllEtudiants() {
		log.info("Retrieving all students...");
		List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findAll();
		log.debug("Number of students retrieved: {}", etudiants.size());
		return etudiants;
	}

	@Override
	public Etudiant addEtudiant(Etudiant e) {
		log.info("Adding a new student: {}", e);
		Etudiant savedEtudiant = etudiantRepository.save(e);
		log.debug("Student added successfully with ID: {}", savedEtudiant.getIdEtudiant());
		return savedEtudiant;
	}

	@Override
	public Etudiant updateEtudiant(Etudiant e) {
		log.info("Updating student with ID: {}", e.getIdEtudiant());
		Etudiant updatedEtudiant = etudiantRepository.save(e);
		log.debug("Student updated successfully: {}", updatedEtudiant);
		return updatedEtudiant;
	}

	@Override
	public Etudiant retrieveEtudiant(Integer idEtudiant) {
		log.info("Retrieving student with ID: {}", idEtudiant);
		Etudiant etudiant = etudiantRepository.findById(idEtudiant).orElse(null);
		if (etudiant == null) {
			log.error("Student with ID {} not found!", idEtudiant);
		} else {
			log.debug("Student retrieved: {}", etudiant);
		}
		return etudiant;
	}

	@Override
	public void removeEtudiant(Integer idEtudiant) {
		log.info("Removing student with ID: {}", idEtudiant);
		Etudiant e = retrieveEtudiant(idEtudiant);
		if (e != null) {
			etudiantRepository.delete(e);
			log.debug("Student with ID {} removed successfully.", idEtudiant);
		} else {
			log.error("Failed to remove student with ID {}. Student not found.", idEtudiant);
		}
	}

	@Override
	public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
		log.info("Assigning student with ID {} to department with ID {}", etudiantId, departementId);
		Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
		Departement departement = departementRepository.findById(departementId).orElse(null);
		if (etudiant != null && departement != null) {
			etudiant.setDepartement(departement);
			etudiantRepository.save(etudiant);
			log.debug("Student with ID {} assigned to department with ID {}", etudiantId, departementId);
		} else {
			log.error("Failed to assign student with ID {} to department with ID {}. Either student or department not found.", etudiantId, departementId);
		}
	}

	@Override
	@Transactional
	public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, Integer idContrat, Integer idEquipe) {
		log.info("Adding and assigning student to contract ID {} and team ID {}", idContrat, idEquipe);
		Contrat contrat = contratRepository.findById(idContrat).orElse(null);
		Equipe equipe = equipeRepository.findById(idEquipe).orElse(null);
		if (contrat != null && equipe != null) {
			contrat.setEtudiant(e);
			equipe.getEtudiants().add(e);
			etudiantRepository.save(e);
			log.debug("Student added and assigned to contract ID {} and team ID {}", idContrat, idEquipe);
		} else {
			log.error("Failed to assign student to contract ID {} or team ID {}. Either contract or team not found.", idContrat, idEquipe);
		}
		return e;
	}

	@Override
	public List<Etudiant> getEtudiantsByDepartement(Integer idDepartement) {
		log.info("Retrieving students for department ID {}", idDepartement);
		List<Etudiant> etudiants = etudiantRepository.findEtudiantsByDepartement_IdDepart(idDepartement);
		log.debug("Number of students retrieved for department ID {}: {}", idDepartement, etudiants.size());
		return etudiants;
	}
}
