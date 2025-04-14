package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departement")
public class DepartementRestController {

	IDepartementService departementService;

	// http://localhost:8089/Kaddem/departement/retrieve-all-departements
	@GetMapping("/retrieve-all-departements")
	public ResponseEntity<List<Departement>> getDepartements() {
		List<Departement> listDepartements = departementService.retrieveAllDepartements();
		return new ResponseEntity<>(listDepartements, HttpStatus.OK);
	}

	// http://localhost:8089/Kaddem/departement/retrieve-departement/8
	@GetMapping("/retrieve-departement/{departement-id}")
	public ResponseEntity<Departement> retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
		Departement departement = departementService.retrieveDepartement(departementId);
		return new ResponseEntity<>(departement, HttpStatus.OK);
	}

	// http://localhost:8089/Kaddem/departement/add-departement
	@PostMapping("/add-departement")
	public ResponseEntity<Departement> addDepartement(@RequestBody Departement d) {
		Departement departement = departementService.addDepartement(d);
		return new ResponseEntity<>(departement, HttpStatus.CREATED);
	}

	// http://localhost:8089/Kaddem/departement/remove-departement/1
	@DeleteMapping("/remove-departement/{departement-id}")
	public ResponseEntity<Void> removeDepartement(@PathVariable("departement-id") Integer departementId) {
		departementService.deleteDepartement(departementId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// http://localhost:8089/Kaddem/departement/update-departement
	@PutMapping("/update-departement")
	public ResponseEntity<Departement> updateDepartement(@RequestBody Departement e) {
		Departement departement = departementService.updateDepartement(e);
		return new ResponseEntity<>(departement, HttpStatus.OK);
	}
}