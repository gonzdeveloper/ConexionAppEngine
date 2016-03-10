package com.conexion.entidades;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Viaje implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	// Creacion del viaje
	private String fecha; // DIA/MES/ANO(2 digitos)

	@Persistent
	//
	private String kilos;

	@Persistent
	//
	private String kilosOrigenReales; // Esta variable se utiliza para las
										// toneladas descargadas.
										// Estos dos capos son generadores de la
										// columna
	@Persistent
	// MONTO en la planilla de excel
	private String precio_kilo; //

	@Persistent
	// Con los campos kilos X precio_kilos
	private String pago; // generamos el campo saldo que se encuntra en el excel

	@Persistent
	private String nombre_descargador; // Estos campos son los generadores de la
										// columna
										// DESCARGA en la planilla del excel.
										// (el che pibe)
	@Persistent
	private String monto_descargador; // Puede ser un precio fijo o precio por
										// toneladas. (Es lo que cobra por
										// tonelada)

	@Persistent
	private String fecha_pago_descargador;

	@Persistent
	private String pago_descargador; // Es lo que efectivamente cobro el che
										// pibe.

	@Persistent
	private String saldo_descargador; // Es la resta del monto_descargador -
										// pago_descargador
										// BORRAR---->NO SE USA.

	@Persistent
	private String fechaPagar_origen;

	@Persistent
	// Es el ORIGEN en la
	private String proveedor; // planilla de excel

	@Persistent
	// Es el DESTINO en la
	private String cliente; // planilla de excel

	@Persistent
	// Es el usuario
	private Long usuarioResponsable; // responsable.

	@Persistent
	private String fletero; // Es la empresa que act�� c�mo fletero.

	@Persistent
	// Con este campo y los kilos generamos el monto
	private String precioXtonelada_flete; // que figura en el excel

	@Persistent
	// Con el precioXtonelada_flete y el pago_flete
	private String pago_flete; // generamos el campo SALDO del excel

	@Persistent
	private String fechaPago_flete;

	@Persistent
	private String facturado;

	@Persistent
	private String precioMaderaDescarga;

	@Persistent
	private String precioFleteroDescarga;

	@Persistent
	private String empresaPropia;

	// CAMPOS EXTRAS QUE NO ESTAN EN EL EXCEL
	@Persistent
	private String tipoCarga;

	@Persistent
	private String observaciones;

	@Persistent
	private boolean pendienteCarga = true;

	@Persistent
	private boolean pendienteDeDescarga = true;

	@Persistent
	private boolean pendienteDePago = true;

	@Persistent
	private boolean pendienteDeCobro = true;

	@Persistent
	private String fechaDeCarga;

	@Persistent
	private String fechaDeDescarga;

	@Persistent
	private String nombreEmpresaPropia;

	@Persistent
	private boolean sePagoFletero = false;

	@Persistent
	private boolean sePagoDescargador = false;

	@Persistent
	private String pagoParcial; // Lo que se le paga al proveedor. 06/11/15

	@Persistent
	private String cobroParcial; // Lo que se le cobŕo al cliente. 06/11/15

	// CAMPOS EXTRAS QUE NO ESTAN EN EL EXCEL

	public boolean isPendienteDePago() {
		return pendienteDePago;
	}

	public void setPendienteDePago(boolean pendienteDePago) {
		this.pendienteDePago = pendienteDePago;
	}

	public boolean isPendienteDeCobro() {
		return pendienteDeCobro;
	}

	public void setPendienteDeCobro(boolean pendienteDeCobro) {
		this.pendienteDeCobro = pendienteDeCobro;
	}

	public String getFechaDeCarga() {
		return fechaDeCarga;
	}

	public void setFechaDeCarga(String fechaDeCarga) {
		this.fechaDeCarga = fechaDeCarga;
	}

	public String getFechaDeDescarga() {
		return fechaDeDescarga;
	}

	public void setFechaDeDescarga(String fechaDeDescarga) {
		this.fechaDeDescarga = fechaDeDescarga;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKilos() {
		return kilos;
	}

	public void setKilos(String kilos) {
		this.kilos = kilos;
	}

	public String getPrecio_kilo() {
		return precio_kilo;
	}

	public void setPrecio_kilo(String precio_kilo) {
		this.precio_kilo = precio_kilo;
	}

	public String getNombre_descargador() {
		return nombre_descargador;
	}

	public void setNombre_descargador(String nombre_descargador) {
		this.nombre_descargador = nombre_descargador;
	}

	public String getMonto_descargador() {
		return monto_descargador;
	}

	public void setMonto_descargador(String monto_descargador) {
		this.monto_descargador = monto_descargador;
	}

	public String getPrecioFleteroDescarga() {
		return precioFleteroDescarga;
	}

	public void setPrecioFleteroDescarga(String precioFleteroDescarga) {
		this.precioFleteroDescarga = precioFleteroDescarga;
	}

	public String getFecha_pago_descargador() {
		return fecha_pago_descargador;
	}

	public void setFecha_pago_descargador(String fecha_pago_descargador) {
		this.fecha_pago_descargador = fecha_pago_descargador;
	}

	public String getPago_descargados() {
		return pago_descargador;
	}

	public void setPago_descargados(String pago_descargados) {
		this.pago_descargador = pago_descargados;
	}

	public String getSaldo_descargador() {
		return saldo_descargador;
	}

	public void setSaldo_descargador(String saldo_descargador) {
		this.saldo_descargador = saldo_descargador;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Long getUsuarioResponsable() {
		return usuarioResponsable;
	}

	public void setUsuarioResponsable(Long usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getPago_descargador() {
		return pago_descargador;
	}

	public void setPago_descargador(String pago_descargador) {
		this.pago_descargador = pago_descargador;
	}

	public String getFletero() {
		return fletero;
	}

	public void setFletero(String fletero) {
		this.fletero = fletero;
	}

	public boolean isPendienteCarga() {
		return pendienteCarga;
	}

	public void setPendienteCarga(boolean pendienteCarga) {
		this.pendienteCarga = pendienteCarga;
	}

	public boolean isPendienteDeDescarga() {
		return pendienteDeDescarga;
	}

	public void setPendienteDeDescarga(boolean pendienteDeDescarga) {
		this.pendienteDeDescarga = pendienteDeDescarga;
	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getPago() {
		return pago;
	}

	public void setPago(String pago) {
		this.pago = pago;
	}

	public String getFechaPagar_origen() {
		return fechaPagar_origen;
	}

	public void setFechaPagar_origen(String fechaPagar_origen) {
		this.fechaPagar_origen = fechaPagar_origen;
	}

	public String getPrecioXtonelada_flete() {
		return precioXtonelada_flete;
	}

	public void setPrecioXtonelada_flete(String precioXtonelada_flete) {
		this.precioXtonelada_flete = precioXtonelada_flete;
	}

	public String getPago_flete() {
		return pago_flete;
	}

	public void setPago_flete(String pago_flete) {
		this.pago_flete = pago_flete;
	}

	public String getFechaPago_flete() {
		return fechaPago_flete;
	}

	public void setFechaPago_flete(String fechaPago_flete) {
		this.fechaPago_flete = fechaPago_flete;
	}

	public String getFacturado() {
		return facturado;
	}

	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	public String getKilosOrigenReales() {
		return kilosOrigenReales;
	}

	public void setKilosOrigenReales(String kilosOrigenReales) {
		this.kilosOrigenReales = kilosOrigenReales;
	}

	public String getPrecioMaderaDescarga() {
		return precioMaderaDescarga;
	}

	public void setPrecioMaderaDescarga(String precioMaderaDescarga) {
		this.precioMaderaDescarga = precioMaderaDescarga;
	}

	public String getEmpresaPropia() {
		return empresaPropia;
	}

	public void setEmpresaPropia(String empresaPropia) {
		this.empresaPropia = empresaPropia;
	}

	public boolean isSePagoFletero() {
		return sePagoFletero;
	}

	public void setSePagoFletero(boolean sePagoFletero) {
		this.sePagoFletero = sePagoFletero;
	}

	public boolean isSePagoDescargador() {
		return sePagoDescargador;
	}

	public void setSePagoDescargador(boolean sePagoDescargador) {
		this.sePagoDescargador = sePagoDescargador;
	}

	public String getNombreEmpresaPropia() {
		return nombreEmpresaPropia;
	}

	public void setNombreEmpresaPropia(String nombreEmpresaPropia) {
		this.nombreEmpresaPropia = nombreEmpresaPropia;
	}

	public String getPagoParcial() {
		return pagoParcial;
	}

	public void setPagoParcial(String pagoParcial) {
		this.pagoParcial = pagoParcial;
	}

	public String getCobroParcial() {
		return cobroParcial;
	}

	public void setCobroParcial(String cobroParcial) {
		this.cobroParcial = cobroParcial;
	}
}