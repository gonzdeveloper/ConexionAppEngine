package com.conexion.entidades;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class DatosEmpresaPropia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	//
	private String EmpresaPropia; // Es el nombre de la empresa propia.
	@Persistent
	// Creacion del viaje
	private String fecha; // DIA/MES/ANO(2 digitos)
	@Persistent
	//
	private String factura; // Es la factura utilizada en el viaje.
	@Persistent
	//
	private String idCliente; // Es el id de la empresa que fue cliente.
	@Persistent
	//
	private String concepto; // Para la facturaci�n
	@Persistent
	//
	private float toneladas; // Las toneladas que compr� el cliente durante el
								// mes.
	@Persistent
	//
	private String pagosRecibidos; // El total de lo que pago el cliente durante
									// el mes.
	@Persistent
	//
	private String fechaPagosRecibidos; // La fecha en que pago el cliente por
										// �ltima vez.
	@Persistent
	//
	private String nroRecibo; // El recibo que se le da al cliente una vez que
								// paga la deuda.
	@Persistent
	//
	private String vendedor; // El vendedor que vende al cliente.
	@Persistent
	//
	private String comision; // La comisi�n que se lleva el vendedor.
	@Persistent
	//
	private String pagoVendedor; // El monto pagado al vendedor.
	@Persistent
	//
	private float gravado; // El monto gravado, es el que suma iva
	@Persistent
	//
	private float noGravado; // El monto NO gravado, es el que NO suma iva.

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdEmpresaPropia() {
		return EmpresaPropia;
	}

	public void setIdEmpresaPropia(String idEmpresaPropia) {
		this.EmpresaPropia = idEmpresaPropia;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public float getToneladas() {
		return toneladas;
	}

	public void setToneladas(float toneladas) {
		this.toneladas = toneladas;
	}

	public String getPagosRecibidos() {
		return pagosRecibidos;
	}

	public void setPagosRecibidos(String pagosRecibidos) {
		this.pagosRecibidos = pagosRecibidos;
	}

	public String getFechaPagosRecibidos() {
		return fechaPagosRecibidos;
	}

	public void setFechaPagosRecibidos(String fechaPagosRecibidos) {
		this.fechaPagosRecibidos = fechaPagosRecibidos;
	}

	public String getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(String nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getComision() {
		return comision;
	}

	public void setComision(String comision) {
		this.comision = comision;
	}

	public String getPagoVendedor() {
		return pagoVendedor;
	}

	public void setPagoVendedor(String pagoVendedor) {
		this.pagoVendedor = pagoVendedor;
	}

	public float getGravado() {
		return gravado;
	}

	public void setGravado(float gravado) {
		this.gravado = gravado;
	}

	public float getNoGravado() {
		return noGravado;
	}

	public void setNoGravado(float noGravado) {
		this.noGravado = noGravado;
	}
}
