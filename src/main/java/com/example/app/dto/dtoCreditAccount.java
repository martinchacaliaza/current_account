package com.example.app.dto;


import javax.validation.constraints.NotEmpty;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class dtoCreditAccount {

	private String id;
	private String numeroCuenta;
	private String dni;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String fecha_caducidad;
	private Double credito;
	private Double saldo;
	private Double consumo;
	private String usuario;
	private String clave;
	private String codigoBancario;

	
	
}










