package com.example.app.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class dtoCurrentAccount {

	private String dni;
	private String numero_cuenta;
	private TypeCurrentAccount tipoProducto;
	private double saldo;
}
