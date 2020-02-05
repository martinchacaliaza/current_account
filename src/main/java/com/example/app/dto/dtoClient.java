package com.example.app.dto;


import com.example.app.models.dtoTypeClient;

import lombok.Data;


@Data
public class dtoClient {

	private String dni;
	private String nombres;
	private String apellidos;
	private String sexo;
	private String telefono;
	private String edad;
	private String correo;
	private dtoTypeClient tipoCliente;
	private String codigoBancario;
}










