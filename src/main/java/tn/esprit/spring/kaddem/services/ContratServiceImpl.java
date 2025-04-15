package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ContratServiceImpl implements IContratService{
@Autowired
ContratRepository contratRepository;
@Autowired
	EtudiantRepository etudiantRepository;
	public List<Contrat> retrieveAllContrats(){
		return (List<Contrat>) contratRepository.findAll();
	}

	public Contrat updateContrat(Contrat ce) {
		log.debug("Mise à jour du contrat ID {}", ce.getIdContrat());
		return contratRepository.save(ce);
	}

	public Contrat addContrat(Contrat ce) {
		log.info("Ajout du contrat avec la spécialité {}", ce.getSpecialite());
		return contratRepository.save(ce);
	}


	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public void removeContrat(Integer idContrat) {
		log.warn("Suppression du contrat ID {}", idContrat);
		Contrat c = retrieveContrat(idContrat);
		if (c != null) {
			contratRepository.delete(c);
		} else {
			log.error("Contrat ID {} introuvable", idContrat);
		}
	}



	public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		log.info("Affectation du contrat ID {} à l'étudiant {} {}", idContrat, nomE, prenomE);
		Etudiant e = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		if (e == null) {
			log.error("Étudiant {} {} introuvable", nomE, prenomE);
			return null;
		}
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		Set<Contrat> contrats = e.getContrats();
		long nbContratsActifs = contrats.stream().filter(c -> Boolean.FALSE.equals(c.getArchive())).count();
		if (nbContratsActifs <= 4) {
			ce.setEtudiant(e);
			contratRepository.save(ce);
			log.info("Contrat affecté avec succès.");
		} else {
			log.warn("Nombre maximal de contrats actifs dépassé pour l'étudiant {} {}", nomE, prenomE);
		}
		return ce;
	}

	public 	Integer nbContratsValides(Date startDate, Date endDate){
		return contratRepository.getnbContratsValides(startDate, endDate);
	}

	public void retrieveAndUpdateStatusContrat() {
		List<Contrat> contrats = contratRepository.findAll();
		if (contrats.isEmpty()) {
			log.warn("Aucun contrat à traiter.");
			return;
		}
		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();
			if (Boolean.FALSE.equals(contrat.getArchive())) {
				long diffMillis = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
				long diffDays = (diffMillis / (1000 * 60 * 60 * 24));
				if (diffDays == 15) {
					log.info("Contrat approchant la date de fin (15j) : {}", contrat);
				}
				if (diffDays == 0) {
					contrat.setArchive(true);
					contratRepository.save(contrat);
					log.info("Contrat archivé automatiquement : {}", contrat);
				}
			}
		}
	}

	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate){
		float difference_In_Time = endDate.getTime() - startDate.getTime();
		float difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
		float difference_In_months =difference_In_Days/30;
        List<Contrat> contrats=contratRepository.findAll();
		float chiffreAffaireEntreDeuxDates=0;
		for (Contrat contrat : contrats) {
			if (contrat.getSpecialite()== Specialite.IA){
				chiffreAffaireEntreDeuxDates+=(difference_In_months*300);
			} else if (contrat.getSpecialite()== Specialite.CLOUD) {
				chiffreAffaireEntreDeuxDates+=(difference_In_months*400);
			}
			else if (contrat.getSpecialite()== Specialite.RESEAUX) {
				chiffreAffaireEntreDeuxDates+=(difference_In_months*350);
			}
			else
			 {
				 chiffreAffaireEntreDeuxDates+=(difference_In_months*450);
			}
		}
		return chiffreAffaireEntreDeuxDates;


	}


}
