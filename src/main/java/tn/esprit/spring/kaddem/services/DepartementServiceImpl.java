package tn.esprit.spring.kaddem.services;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.dto.DepartementDTO;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartementServiceImpl implements IDepartementService {

	DepartementRepository departementRepository;

	@Override
	public List<Departement> retrieveAllDepartements() {
		return (List<Departement>) departementRepository.findAll();
	}

	@Override
	public Departement retrieveDepartement(Integer id) {
		return departementRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Departement not found with id: " + id));
	}

	@Override
	public DepartementDTO addDepartement(DepartementDTO d) {
		Departement departement = new Departement();
		departement.setNomDepart(d.getNomDepart());
		Departement saved = departementRepository.save(departement);
		return new DepartementDTO(saved.getIdDepart(), saved.getNomDepart());
	}

	@Override
	public void deleteDepartement(Integer id) {
		Departement departement = departementRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Departement not found with id: " + id));
		departementRepository.delete(departement);
	}

	@Override
	public DepartementDTO updateDepartement(DepartementDTO d) {
		Departement departement = departementRepository.findById(d.getIdDepart())
				.orElseThrow(() -> new RuntimeException("Departement not found with id: " + d.getIdDepart()));
		departement.setNomDepart(d.getNomDepart());
		Departement updated = departementRepository.save(departement);
		return new DepartementDTO(updated.getIdDepart(), updated.getNomDepart());
	}
}
