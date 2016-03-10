package com.conexion.client;

import java.util.List;

import com.conexion.entidades.Empresa;
import com.conexion.entidades.Usuario;
import com.conexion.entidades.Viaje;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String valNombreEmpresa, String tipo,
			String valDireEmpresa, String valCCEmpresa);

	String greetServerCrearUsuario(String valNombreUsuario,
			String valClaveUsuario, String valRolUsuario);

	String greetServerLogin(String valLblUsuario, String valLblClave);

	List<Usuario> getListaUsuarios();

	Usuario getUsuario(String usuSel);

	String ModificarUsuario(Usuario usuario);

	String eliminarUsuario(Usuario usuario);

	List<Empresa> getListaEmpresas();

	Empresa getEmpresa(String empSel);

	String ModificarEmpresa(Empresa empresaSel);

	String eliminarEmpresa(Empresa empresa);

	List<Viaje> getListaViajes();

	String crearViaje(String fecha, String proveedor, String cliente,
			String kilos, String precioXKilo, String descargador,
			String montoDescargador, String fletero, String precioXFlete,
			String tipoCarga, String factura, Boolean carga, Boolean descarga,
			String empresaPropia);

	Usuario getUsuarioById(Long usuarioResponsable);

	String eliminarViaje(Viaje viaje);

	Viaje getViaje(String viajeSel);

	String ModificarViaje(Viaje vje, Boolean estabaCargado, Boolean estabaPago,
			Boolean estabaDescargado, Boolean estabaCobrado,
			Boolean estabaPagoFlete, Boolean estabaPagoDescargador);

	List<Empresa> getListaEmpresasPorTipo(String string);

	String eliminarUsuariosSeleccionados(List<Usuario> usuarios);

	List<Usuario> getListaUsuariosSeleccionadosByNombres(List<String> lstUsuario);

	String eliminarEmpresasSeleccionadas(List<Empresa> empresas);

	List<Empresa> getListaEmpresasSeleccionadasByNombres(List<String> lstEmpresa);

	String eliminarViajesSeleccionados(List<Viaje> viaje);

	List<Viaje> getListaViajesSeleccionadosById(List<String> lstViajes);

	String facturarViajesSeleccionados(List<Viaje> viajes, String string);

	String pagarCobrarEmpresa(Empresa empresa, String monto, String accion);

	List<String> getDatosEmpresaPropia(String nombre);

	String limpiarDatos();

	String obtenerCCdelCliente(String cliente);

	String ModificarViajeSaldoAPagar(Viaje vje2, String pagar,
			String cobrarCiente, Boolean estabaCargado2, Boolean estabaPago2,
			Boolean estabaDescargado2, Boolean estabaCobrado2,
			Boolean estabaPagoFlete2, Boolean estabaPagoDescargador2);
}
