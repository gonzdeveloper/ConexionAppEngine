package com.conexion.client;

import java.util.List;

import com.conexion.entidades.Empresa;
import com.conexion.entidades.Usuario;
import com.conexion.entidades.Viaje;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String valNombreEmpresa, String tipo,
			String valDireEmpresa, String valCCEmpresa,
			AsyncCallback<String> callback) throws IllegalArgumentException;

	void greetServerCrearUsuario(String valNombreUsuario,
			String valClaveUsuario, String valRolUsuario,
			AsyncCallback<String> asyncCallback);

	void greetServerLogin(String valLblUsuario, String valLblClave,
			AsyncCallback<String> asyncCallback);

	public void getListaUsuarios(AsyncCallback<List<Usuario>> asyncCallback);

	void getUsuario(String usuSel, AsyncCallback<Usuario> asyncCallback);

	void ModificarUsuario(Usuario usuario, AsyncCallback<String> asyncCallback);

	void eliminarUsuario(Usuario usuario, AsyncCallback<String> asyncCallback);

	void getListaEmpresas(AsyncCallback<List<Empresa>> asyncCallback);

	void getEmpresa(String empSel, AsyncCallback<Empresa> asyncCallback);

	void ModificarEmpresa(Empresa empresaSel,
			AsyncCallback<String> asyncCallback);

	void eliminarEmpresa(Empresa empresa, AsyncCallback<String> asyncCallback);

	void getListaViajes(AsyncCallback<List<Viaje>> asyncCallback);

	void crearViaje(String fecha, String proveedor, String cliente,
			String kilos, String precioXKilo, String descargador,
			String montoDescargador, String fletero, String precioXFlete,
			String tipoCarga, String factura, Boolean carga, Boolean descarga,
			String empresaPropia, AsyncCallback<String> asyncCallback);

	void getUsuarioById(Long usuarioResponsable,
			AsyncCallback<Usuario> asyncCallback);

	void eliminarViaje(Viaje viaje, AsyncCallback<String> asyncCallback);

	void getViaje(String viajeSel, AsyncCallback<Viaje> asyncCallback);

	void ModificarViaje(Viaje vje, Boolean estabaCargado, Boolean estabaPago,
			Boolean estabaDescargado, Boolean estabaCobrado,
			Boolean estabaPagoFlete, Boolean estabaPagoDescargador,
			AsyncCallback<String> asyncCallback);

	void getListaEmpresasPorTipo(String string,
			AsyncCallback<List<Empresa>> asyncCallback);

	void eliminarUsuariosSeleccionados(List<Usuario> usuarios,
			AsyncCallback<String> asyncCallback);

	void getListaUsuariosSeleccionadosByNombres(List<String> lstUsuario,
			AsyncCallback<List<Usuario>> callback);

	void eliminarEmpresasSeleccionadas(List<Empresa> empresas,
			AsyncCallback<String> asyncCallback);

	void getListaEmpresasSeleccionadasByNombres(List<String> lstEmpresa,
			AsyncCallback<List<Empresa>> asyncCallback);

	void eliminarViajesSeleccionados(List<Viaje> viaje,
			AsyncCallback<String> asyncCallback);

	void getListaViajesSeleccionadosById(List<String> lstViajes,
			AsyncCallback<List<Viaje>> asyncCallback);

	void facturarViajesSeleccionados(List<Viaje> viajes, String string,
			AsyncCallback<String> asyncCallback);

	void pagarCobrarEmpresa(Empresa empresa, String monto, String accion,
			AsyncCallback<String> asyncCallback);

	void getDatosEmpresaPropia(String nombre,
			AsyncCallback<List<String>> asyncCallback);

	void limpiarDatos(AsyncCallback<String> asyncCallback);

	void obtenerCCdelCliente(String cliente, AsyncCallback<String> asyncCallback);

	void ModificarViajeSaldoAPagar(Viaje vje2, String pagar,
			String cobrarCiente, Boolean estabaCargado2, Boolean estabaPago2,
			Boolean estabaDescargado2, Boolean estabaCobrado2,
			Boolean estabaPagoFlete2, Boolean estabaPagoDescargador2,
			AsyncCallback<String> asyncCallback);
}
