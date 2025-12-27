package com.api.algortimos.model;

import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Personas")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "El nombre no puede estar vacio")
	private String nombre;
	
	@NotBlank(message = "El correo no puede estar vacio")
	@Column(unique = true)
	private String correo;
	
	@NotNull(message = "La fecha no puede estar vacia")
	private LocalTime fechaNacimiento;
	
	@NotNull(message = "El genero no puede estar vacio")
	@Enumerated(EnumType.STRING)
	private Genero genero;

	public Persona() {
		super();
	}

	public Persona(String nombre, String correo, LocalTime fechaNacimiento, Genero genero) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.fechaNacimiento = fechaNacimiento;
		this.genero = genero;
	}

	//Getters y setters EXCEPTO setID
	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public LocalTime getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalTime fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}
}
