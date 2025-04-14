package tn.esprit.spring.kaddem.controllers;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.dto.DepartementDTO;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departement")
public class DepartementRestController {

	IDepartementService departementService;

	@GetMapping("/retrieve-all-departements")
	public ResponseEntity<List<Departement>> getDepartements() {
		List<Departement> listDepartements = departementService.retrieveAllDepartements();
		return new ResponseEntity<>(listDepartements, HttpStatus.OK);
	}

	@GetMapping("/retrieve-departement/{departement-id}")
	public ResponseEntity<Departement> retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
		Departement departement = departementService.retrieveDepartement(departementId);
		return new ResponseEntity<>(departement, HttpStatus.OK);
	}

	@PostMapping("/add-departement")
	public ResponseEntity<DepartementDTO> addDepartement(@RequestBody DepartementDTO d) {
		DepartementDTO departement = departementService.addDepartement(d);
		return new ResponseEntity<>(departement, HttpStatus.CREATED);
	}

	@DeleteMapping("/remove-departement/{departement-id}")
	public ResponseEntity<Void> removeDepartement(@PathVariable("departement-id") Integer departementId) {
		departementService.deleteDepartement(departementId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/update-departement")
	public ResponseEntity<DepartementDTO> updateDepartement(@RequestBody DepartementDTO e) {
		DepartementDTO departement = departementService.updateDepartement(e);
		return new ResponseEntity<>(departement, HttpStatus.OK);
	}
}