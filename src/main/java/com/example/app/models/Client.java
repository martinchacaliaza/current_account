package com.example.app.models;


import lombok.Data;


@Data
public class Client {

	private String dni;
	private String nombres;
	private String apellidos;
	private String sexo;
	private String telefono;
	private String edad;
	private String correo;
	private TypeClient tipoCliente;
	private String codigo_bancario;
}










