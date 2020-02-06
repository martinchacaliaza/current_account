package com.example.app.models;

import java.util.Date;


import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import com.example.app.models.TypeCurrentAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Document(collection ="cuentas_bancarias")
public class CurrentAccount {
	
	@Id
	@NotEmpty
	private String id;
	@NotEmpty
	private String dni;
	@NotEmpty
	private String numeroCuenta;
	@NotEmpty
	private TypeCurrentAccount tipoProducto;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha_afiliacion;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha_caducidad;
	@NotEmpty
	private double saldo;
	@NotEmpty
	private String usuario;
	@NotEmpty
	private String clave;
	@NotEmpty
	private String codigoBancario;
	
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_afiliacion() {
		return fecha_afiliacion;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_caducidad() {
		return fecha_caducidad;
	}
}










