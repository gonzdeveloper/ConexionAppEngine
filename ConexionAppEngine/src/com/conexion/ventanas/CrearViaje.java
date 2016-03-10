package com.conexion.ventanas;

import java.util.Date;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.conexion.entidades.Viaje;
import com.conexion.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CrearViaje extends Composite {

	private static CrearViajeUiBinder uiBinder = GWT
			.create(CrearViajeUiBinder.class);
	@UiField
	ListBox cbProveedor;
	@UiField
	ListBox cbCliente;
	@UiField
	TextBox tbKilos;
	@UiField
	TextBox tbPrecioXKilo;
	@UiField
	TextBox tbPrecioToneladaFlete;
	@UiField
	TextBox tbTipoCarga;
	@UiField
	TextBox tbMontoDescargador;
	@UiField
	ListBox cbFletero;
	@UiField
	TextBox tbFactura;
	@UiField
	Label errorLabel;
	@UiField
	Button btnAceptar;
	@UiField
	ListBox cbDescargador;
	@UiField
	ListBox cbEmpresaPropia;
	boolean cargado = false;
	boolean pagado = false;
	ListarViajes lv;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface CrearViajeUiBinder extends UiBinder<Widget, CrearViaje> {
	}

	public CrearViaje(final Viaje viajeSel, ListarViajes listarViajes) {
		initWidget(uiBinder.createAndBindUi(this));
		errorLabel.addStyleName("serverResponseLabelError");
		lv = listarViajes;
		cbProveedor.setFocus(true);
		// Para el caso de un viaje sobrante.
		if (viajeSel != null) {
			tbKilos.setText(viajeSel.getKilos());
			tbKilos.setEnabled(false);
			tbPrecioXKilo.setText(viajeSel.getPrecio_kilo());
			tbPrecioToneladaFlete.setText(viajeSel.getPrecioXtonelada_flete());
			tbTipoCarga.setText(viajeSel.getTipoCarga());
			tbMontoDescargador.setText(viajeSel.getPago_descargador());
			tbFactura.setText(viajeSel.getFacturado());
			cargado = !viajeSel.isPendienteCarga();
			pagado = !viajeSel.isPendienteDePago();
		}
		// Cargo las empresas.
		greetingService.getListaEmpresas(new AsyncCallback<List<Empresa>>() {

			@Override
			public void onSuccess(List<Empresa> result) {
				if (result != null) {
					int h;
					int prov = -1;
					int client = -1;
					int empProp = -1;
					for (h = 0; h < result.size(); h++) {
						Empresa emp = result.get(h);
						// Para los proveedores
						if (emp.getTipo().equals("Proveedor")) {
							prov += 1;
							cbProveedor.addItem(emp.getNombre());
							cbDescargador.addItem(emp.getNombre());
							cbFletero.addItem(emp.getNombre());
							if (viajeSel != null) {
								if (viajeSel.getProveedor().equals(
										emp.getNombre())) {
									cbProveedor.setSelectedIndex(prov);
									cbProveedor.setEnabled(false);
								}
								if (viajeSel.getFletero().equals(
										emp.getNombre())) {
									cbFletero.setSelectedIndex(prov);
									cbFletero.setEnabled(false);
								}
								if (viajeSel.getNombre_descargador().equals(
										emp.getNombre())) {
									cbDescargador.setSelectedIndex(prov);
								}
							}
							// Para los clientes
						} else if (emp.getTipo().equals("Cliente")) {
							client += 1;
							cbCliente.addItem(emp.getNombre());
							if (viajeSel != null) {
								if (viajeSel.getCliente().equals(
										emp.getNombre())) {
									cbCliente.setSelectedIndex(client);
								}
							}
						}
						// Para las empresas propias
						else if (emp.getTipo().equals("Empresa propia")) {
							empProp++;
							cbEmpresaPropia.addItem(emp.getNombre());
							if (viajeSel != null) {
								if (viajeSel.getEmpresaPropia().equals(
										emp.getNombre())) {
									cbEmpresaPropia.setSelectedIndex(empProp);
								}
							}
						}

					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error al cargar las empresas.");
			}
		});
	}

	@UiHandler("btnAceptar")
	void handleClickAgregar(ClickEvent e) {
		// Deshabilito el boton.
		btnAceptar.setEnabled(false);
		// La fecha de hoy.
		Date d = new Date();
		@SuppressWarnings("deprecation")
		String fecha = d.getDate() + "/" + (d.getMonth() + 1) + "/"
				+ (d.getYear() % 100);
		// Fin la fecha de hoy.
		String proveedor = cbProveedor.getItemText(cbProveedor
				.getSelectedIndex());
		String cliente = cbCliente.getItemText(cbCliente.getSelectedIndex());
		String kilos = tbKilos.getText();
		String precioXKilo = "0";
		if ((tbPrecioXKilo.getText() != null)
				&& (!tbPrecioXKilo.getText().equals("")))
			precioXKilo = tbPrecioXKilo.getText();
		String descargador = cbDescargador.getItemText(cbDescargador
				.getSelectedIndex());
		String montoDescargador = tbMontoDescargador.getText();
		String fletero = cbFletero.getItemText(cbFletero.getSelectedIndex());
		String precioXFlete = tbPrecioToneladaFlete.getText();
		String tipoCarga = tbTipoCarga.getText();
		String factura = tbFactura.getText();
		String empresaPropia = cbEmpresaPropia.getItemText(cbEmpresaPropia
				.getSelectedIndex());
		// Compruebo que sean numericos
		if (FieldVerifier.isNumeric(kilos)
				&& FieldVerifier.isNumeric(precioXKilo)
				&& FieldVerifier.isNumeric(montoDescargador)
				&& FieldVerifier.isNumeric(precioXFlete)) {
			greetingService.crearViaje(fecha, proveedor, cliente, kilos,
					precioXKilo, descargador, montoDescargador, fletero,
					precioXFlete, tipoCarga, factura, cargado, pagado,
					empresaPropia, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							errorLabel.setText("Error al crear el viaje.");
						}

						@Override
						public void onSuccess(String result) {
//							Window.alert("Se crea el viaje correctamente.");
							lv.cerrarDialogBox();

						}
					});
		} else {
			Window.alert("Error al ingresar un valor num\u00e9rico.");
		}
		// Vuelvo a habilitar el boton.
		 btnAceptar.setEnabled(true);
	}

	protected void limpiarCampos() {
		cbProveedor.setSelectedIndex(0);
		cbCliente.setSelectedIndex(0);
		tbKilos.setText("");
		tbPrecioXKilo.setText("");
		cbDescargador.setSelectedIndex(0);
		tbMontoDescargador.setText("");
		cbFletero.setSelectedIndex(0);
		tbPrecioToneladaFlete.setText("");
		tbTipoCarga.setText("");
		tbFactura.setText("");
		cbFletero.setEnabled(true);
		cbProveedor.setEnabled(true);
	}
}
