package com.example.app.dto;

import com.example.app.models.TypeCurrentAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class dtoSaldos {

	private String dni;
	private String numeroCuenta;
	private Double Saldo;
	private TypeCurrentAccount tipoProducto;
}
