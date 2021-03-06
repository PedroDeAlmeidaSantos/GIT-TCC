package br.net.daumhelp.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.net.daumhelp.model.Subcategoria;
import br.net.daumhelp.repository.SubcategoriaRepository;

@CrossOrigin
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://ec2-3-220-68-195.compute-1.amazonaws.com")
@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaResource {
	
	@Autowired
	private SubcategoriaRepository subcatRepository;

	@GetMapping
	public List<Subcategoria> getSubcategorias(){
		return subcatRepository.findAllOrderByName();
	}

	@GetMapping("/categoria/{idCategoria}")
	public List<Subcategoria> getSubcatsByCat(@PathVariable Long idCategoria){
		return subcatRepository.findByCategoria(idCategoria);
	}
	
	@GetMapping("/id/{id}")
	public Optional<Subcategoria> getSubcatsById(@PathVariable Long id){
		System.out.println(subcatRepository.findById(id));
		return subcatRepository.findById(id);
	}
		
	
	
}
