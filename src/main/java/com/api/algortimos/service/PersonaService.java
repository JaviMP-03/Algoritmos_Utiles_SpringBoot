package com.api.algortimos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.algortimos.DTO.PersonaDTO;
import com.api.algortimos.exceptions.RecursoNoEncontrado;
import com.api.algortimos.exceptions.RecursoRepetido;
import com.api.algortimos.model.Genero;
import com.api.algortimos.model.Persona;
import com.api.algortimos.repository.PersonaRepository;

@Service
public class PersonaService {

	private final PersonaRepository personaRepository;

	public PersonaService(PersonaRepository personaRepository) {
		super();
		this.personaRepository = personaRepository;
	}

	public List<Persona> obtenerPersonas() {
		return metodoObtenerTodos();
	}

	public Persona obtenerPorId(Long id) {
		return metodoObtenerPersona(id);
	}

	public Persona obtenerPorNombre(String nombre) {
		return metodoObtenerPersona(nombre);
	}

	public Persona obtenerPorCorreo(String correo) {
		return metodoObtenerPersonaCorreo(correo);
	}

	public List<Persona> obtenerPorFecha(LocalDate fecha) {
		return metodoObtenerPersonas(fecha);
	}

	public List<Persona> obtenerPorGenero(Genero genero) {
		return metodoObtenerPersonas(genero);
	}

	public Persona crearPersona(Persona p) {
		return metodoCrearPersona(p);
	}

	public Persona actualizarPersona(Long id, Persona nuevaPersona) {
		return metodoActualizarPersona(id, nuevaPersona);
	}

	public void borrarPersona(Long id) {
		metodoBorrarPersona(id);
	}

	// ----------Metodos GET------------

	public List<Persona> metodoObtenerTodos() {
		return personaRepository.findAll();
	}

	// Sobrecargamos el metodo 'metodoObtenerPersonas'
	// (Sobrecargar un metodo es tener varios metodos con el mismo nombre (firma)
	// pero que piden un tipo de dato diferente)
	public Persona metodoObtenerPersona(Long id) {
		Optional<Persona> personaExiste = personaRepository.findById(id);

		if (!personaExiste.isPresent()) {
			throw new RecursoNoEncontrado("No se ha encontrado a ninguna persona con ese id");
		}

		return personaExiste.get();
	}

	public Persona metodoObtenerPersona(String nombre) {
		Optional<Persona> personaExiste = personaRepository.findByNombre(nombre);

		if (!personaExiste.isPresent()) {
			throw new RecursoNoEncontrado("No se ha encontrado a ninguna persona con ese nombre");
		}

		return personaExiste.get();
	}

	public Persona metodoObtenerPersonaCorreo(String correo) {
		Optional<Persona> personaExiste = personaRepository.findByCorreo(correo);

		if (!personaExiste.isPresent()) {
			throw new RecursoNoEncontrado("No se ha encontrado a ninguna persona con ese correo");
		}

		return personaExiste.get();
	}

	// Estos 2 ultimos metodos dicen: "Devuelvo una lista pero antes voy a comprobar
	// si esta vacia
	// Si esta vacia devuelvo un error ya que no hay elementos por lo tanto no hay
	// personas
	public List<Persona> metodoObtenerPersonas(LocalDate fecha) {
		List<Persona> personas = personaRepository.findAllByFechaNacimiento(fecha);

		if (personas.isEmpty()) {
			throw new RecursoNoEncontrado("No hay personas nacidas en esa fecha");
		}

		return personas;
	}

	public List<Persona> metodoObtenerPersonas(Genero genero) {
		List<Persona> personas = personaRepository.findAllByGenero(genero);

		if (personas.isEmpty()) {
			throw new RecursoNoEncontrado("No hay personas de ese genero");
		}

		return personas;
	}

	// ----------Metodos GET------------

	// ----------Metodo POST------------

	// Se utiliza un boolean en vez de un Optional<Persona> ya que ocupa menos
	// espacio
	// solo queremos comprobar si ese correo existe o no
	public Persona metodoCrearPersona(Persona p) {

		boolean personaExiste = personaRepository.existsByCorreo(p.getCorreo());

		if (personaExiste) {
			throw new RecursoRepetido("Ya existe una persona con ese correo");
		}

		return personaRepository.save(p);
	}

	// ----------Metodo POST------------

	// ----------Metodo PUT-------------

	public Persona metodoActualizarPersona(Long id, Persona nuevaPersona) {
		// Primero comprobamos que la persona exista con el metodo del get, si existe ya
		// la guardamos en una variable
		Persona persona = metodoObtenerPersona(id);

		// Si el correo NO es el mismo buscamos si ese correo ya pertenece a otra
		// persona y lo guardamos en la variable
		if (!persona.getCorreo().equalsIgnoreCase(nuevaPersona.getCorreo())) {

			Optional<Persona> PersonaConCorreo = personaRepository.findByCorreo(nuevaPersona.getCorreo());

			// Si la persona existe (siempre va a existir ya que en el controller nos
			// aseguramos de que solo se puedan enviar
			// personas con todos los campos rellenados) nos la guardamos en otra variable
			if (PersonaConCorreo.isPresent()) {

				Persona personaExiste = PersonaConCorreo.get();

				// Si los ID's no coinciden se lanza la excepcion
				if (personaExiste.getId() != nuevaPersona.getId()) {
					throw new RecursoRepetido("No se puede actualizar porque el correo ya esta en uso");
				}
			}

			// Si el correo no coincide PERO no existe se cambia con normalidad, por eso va
			// dentro del if exterior
			persona.setCorreo(nuevaPersona.getCorreo());
		}

		persona.setNombre(nuevaPersona.getNombre());
		persona.setFechaNacimiento(nuevaPersona.getFechaNacimiento());
		persona.setGenero(nuevaPersona.getGenero());

		return personaRepository.save(persona);
	}

	/*
	 * EJEMPLO MAS PRACTICO PARA ENTENDERLO MEJOR
	 * 
	 * Tenemos 2 personas con correos: a@a.com b@b.com
	 * 
	 * CASO 1: ID 1 y nuevaPersona.getCorreo = d@d.com
	 * 
	 * La persona existe pero como el correo no existe solo cambia ese campo
	 * 
	 * CASO 2: ID 1 y nuevaPersona.getCorreo = b@b.com
	 * 
	 * La persona existe pero el correo ya esta en uso, no se cambia nada y devuelve
	 * la excepcion
	 * 
	 * CASO 3: ID 200 y nuevaPersona.getCorreo = p@p.com
	 * 
	 * La persona no existe por lo que ni se comprueba el correo, lanza excepcion en
	 * la linea 110 de codigo
	 */

	// ----------Metodo PUT-------------

	// ----------Metodo DELETE----------

	public void metodoBorrarPersona(Long id) {
		metodoObtenerPersona(id);

		personaRepository.deleteById(id);
	}

	// ----------Metodo DELETE----------

	public PersonaDTO convertirADTO(Persona e) {
		PersonaDTO pDTO = new PersonaDTO(e.getNombre(), e.getCorreo(), e.getFechaNacimiento(), e.getGenero());
		return pDTO;
	}
	
	public Persona convertirADominio(PersonaDTO eDTO) {
		Persona p = new Persona(eDTO.getNombre(), eDTO.getCorreo(), eDTO.getFechaNacimiento(), eDTO.getGenero());
		return p;
	}
}
