package com.conexion.ventanas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.conexion.entidades.Viaje;
import com.conexion.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModificarViaje extends Composite {

	private static ModificarViajeUiBinder uiBinder = GWT
			.create(ModificarViajeUiBinder.class);
	@UiField
	TextBox tbFecha;
	@UiField
	TextBox tbToneladas;
	@UiField
	TextBox tbFactura;
	@UiField
	ListBox cbOrigen;
	@UiField
	ListBox cbDestino;
	@UiField
	TextBox tbMontoMonto;
	@UiField
	TextBox tbMontoDescarga;
	@UiField
	ListBox cbFleteFlete;
	@UiField
	TextBox tbMontoFlete;
	@UiField
	TextBox tbDescargaRealToneladas;
	@UiField
	TextBox tbPrecioMaderaDescarga;
	@UiField
	TextBox tbPrecioFleteroDescarga;
	@UiField
	Button btnModificar;
	@UiField
	Label errorLabel;
	@UiField
	CheckBox chkCargado;
	@UiField
	CheckBox chkCargadoPago;
	@UiField
	CheckBox chkDescargar;
	@UiField
	CheckBox chkDescargarPago;
	@UiField
	Label lblPrecioCarga;
	@UiField
	Label lblPrecioRealDescarga;
	@UiField
	ListBox cbNombreDescargador;
	@UiField
	ListBox cbEmpresaPropia;
	@UiField
	CheckBox chkPagoFletero;
	@UiField
	CheckBox chkPagoDescargador;
	@UiField
	DialogBox dialogBox;
	Viaje vje;
	Boolean pendienteCarga = true;
	Boolean pendienteDeDescarga = true;
	Boolean estabaCargado = false;
	Boolean estabaPago = false;
	Boolean estabaDescargado = false;
	Boolean estabaCobrado = false;
	Boolean estabaPagoFlete = false;
	Boolean estabaPagoDescargador = false;
	String IVA = "1.22";
	BigDecimal bdPago;
	ListarViajes lv;

	/**
	 * 22f Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface ModificarViajeUiBinder extends UiBinder<Widget, ModificarViaje> {
	}

	public ModificarViaje(final Viaje viaje, ListarViajes listarViajes) {
		initWidget(uiBinder.createAndBindUi(this));
		lv = listarViajes;
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		errorLabel.addStyleName("serverResponseLabelError");
		lblPrecioCarga.addStyleName("resaltarLabel");
		lblPrecioRealDescarga.addStyleName("resaltarLabel");
		dialogBox.setVisible(false);

		// Multiplico las toneladas por el precio por tonelada
		BigDecimal bdTon = new BigDecimal("0.00");
		BigDecimal bdPrecioXTon = new BigDecimal("0.00");
		bdPago = new BigDecimal("0.00");
		bdTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		bdPrecioXTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecio_kilo()));
		bdPago = bdTon.multiply(bdPrecioXTon);
		bdPago = bdPago.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		lblPrecioCarga.setText(bdPago.toString());
		if (viaje != null) {
			vje = viaje;
			// Controlo el estado inicial del viaje
			estabaCargado = !viaje.isPendienteCarga();
			estabaPago = !viaje.isPendienteDePago();
			estabaDescargado = !viaje.isPendienteDeDescarga();
			estabaCobrado = !viaje.isPendienteDeCobro();
			estabaPagoFlete = viaje.isSePagoFletero();
			estabaPagoDescargador = viaje.isSePagoDescargador();
			// FIN control estado inicial del viaje.
			pendienteCarga = viaje.isPendienteCarga();
			pendienteDeDescarga = viaje.isPendienteDeDescarga();
			tbFecha.setText(viaje.getFecha());
			tbToneladas.setText(String.valueOf(viaje.getKilos()));
			chkPagoFletero.setValue(viaje.isSePagoFletero());
			chkPagoDescargador.setValue(viaje.isSePagoDescargador());
			tbDescargaRealToneladas.setText(String.valueOf(viaje
					.getKilosOrigenReales()));
			// Seteo los checkbox
			if (!viaje.isPendienteCarga() && viaje.isPendienteDePago()) {
				chkCargado.setValue(true);
			} else if (!viaje.isPendienteCarga() && !viaje.isPendienteDePago()) {
				chkCargadoPago.setValue(true);
			}
			if (!viaje.isPendienteDeDescarga() && viaje.isPendienteDeCobro()) {
				chkDescargar.setValue(true);
			} else if (!viaje.isPendienteDeDescarga()
					&& !viaje.isPendienteDeCobro()) {
				chkDescargarPago.setValue(true);
			}
			// Fin seteo los checkbox
			String montoS = String.valueOf(viaje.getPrecio_kilo());
			tbMontoMonto.setText(montoS);
			greetingService
					.getListaEmpresas(new AsyncCallback<List<Empresa>>() {
						@Override
						public void onSuccess(List<Empresa> result) {
							if (result != null) {
								int prov = -1;
								int client = -1;
								int empPropia = -1;
								int h;
								for (h = 0; h < result.size(); h++) {
									Empresa emp = result.get(h);
									// Para el proveedor
									if (emp.getTipo().equals("Proveedor")) {
										prov += 1;
										cbOrigen.addItem(emp.getNombre());
										cbFleteFlete.addItem(emp.getNombre());
										cbNombreDescargador.addItem(emp
												.getNombre());
										if (vje.getProveedor().equals(
												emp.getNombre())) {
											cbOrigen.setSelectedIndex(prov);
										}
										if (vje.getFletero().equals(
												emp.getNombre())) {
											cbFleteFlete.setSelectedIndex(prov);
										}
										if (vje.getNombre_descargador().equals(
												emp.getNombre())) {
											cbNombreDescargador
													.setSelectedIndex(prov);
										}
									}
									// Para el cliente
									else if (emp.getTipo().equals("Cliente")) {
										client += 1;
										cbDestino.addItem(emp.getNombre());
										if (vje.getCliente().equals(
												emp.getNombre())) {
											cbDestino.setSelectedIndex(client);
										}
									}
									// Para la empresa propia
									else if (emp.getTipo().equals(
											"Empresa propia")) {
										empPropia++;
										cbEmpresaPropia.addItem(emp.getNombre());
										if (vje.getEmpresaPropia() != null) {
											if (vje.getEmpresaPropia().equals(
													emp.getNombre())) {
												cbEmpresaPropia
														.setSelectedIndex(empPropia);
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
			tbFactura.setText(viaje.getFacturado());
			tbMontoDescarga.setText(viaje.getMonto_descargador());
			if (viaje.getPrecioMaderaDescarga() == null) {
				tbPrecioMaderaDescarga.setText(viaje.getPrecio_kilo());
			} else {
				tbPrecioMaderaDescarga.setText(viaje.getPrecioMaderaDescarga());
			}
			if (viaje.getPrecioFleteroDescarga() == null) {
				tbPrecioFleteroDescarga.setText(viaje
						.getPrecioXtonelada_flete());
			} else {
				tbPrecioFleteroDescarga.setText(viaje
						.getPrecioFleteroDescarga());
			}
			cambiarPrecioReal();
			tbMontoFlete.setText(viaje.getPrecioXtonelada_flete());
			// Bloqueo los combos para que no se puedan modificar las empresas
			// involucradas en el viaje.
			cbDestino.setEnabled(false);
			cbEmpresaPropia.setEnabled(false);
			cbFleteFlete.setEnabled(false);
			cbNombreDescargador.setEnabled(false);
			cbOrigen.setEnabled(false);
			// FIN BLOQUEO.
		}
	}

	@UiHandler("btnModificar")
	void handleClickAceptar(ClickEvent e) {
		// Deshabilito el boton
		btnModificar.setEnabled(false);
		errorLabel.setText("");
		if ((chkDescargar.getValue() || chkDescargarPago.getValue())
				&& (!chkCargado.getValue() && !chkCargadoPago.getValue())) {
			errorLabel.setText("Error: El viaje primero debe ser cargado.");
		} else if ((chkPagoDescargador.getValue() || chkPagoFletero.getValue())
				&& !(chkDescargar.getValue() || chkDescargarPago.getValue())) {
			errorLabel
					.setText("Error: No se puede pagar al fletero o descargador si el viaje no fue descargado.");
		} else {
			if (vje != null) {
				vje.setFecha(tbFecha.getText());
				vje.setKilos(tbToneladas.getText());
				vje.setProveedor(cbOrigen.getItemText(cbOrigen
						.getSelectedIndex()));
				vje.setCliente(cbDestino.getItemText(cbDestino
						.getSelectedIndex()));
				vje.setEmpresaPropia(cbEmpresaPropia
						.getItemText(cbEmpresaPropia.getSelectedIndex()));
				vje.setNombreEmpresaPropia(cbEmpresaPropia
						.getItemText(cbEmpresaPropia.getSelectedIndex()));
				vje.setFletero(cbFleteFlete.getItemText(cbFleteFlete
						.getSelectedIndex()));
				vje.setNombre_descargador(cbNombreDescargador
						.getItemText(cbNombreDescargador.getSelectedIndex()));
				vje.setFacturado(tbFactura.getText());
				vje.setPrecioMaderaDescarga(tbPrecioMaderaDescarga.getText());
				vje.setPrecioFleteroDescarga(tbPrecioFleteroDescarga.getText());
				vje.setPrecio_kilo(tbMontoMonto.getText());
				vje.setMonto_descargador(tbMontoDescarga.getText());
				vje.setPrecioXtonelada_flete(tbMontoFlete.getText());
				// Seteo los chechbox
				if (chkCargado.getValue()) {
					vje.setPendienteCarga(false);
					vje.setPendienteDePago(true);
					vje.setPago("0");
					vje.setFechaPagar_origen("-");
				} else if (chkCargadoPago.getValue()) {
					vje.setPendienteCarga(false);
					vje.setPendienteDePago(false);
					if (pendienteCarga) {
						// La fecha de hoy.
						Date d = new Date();
						@SuppressWarnings("deprecation")
						String fecha = d.getDate() + "/" + (d.getMonth() + 1)
								+ "/" + (d.getYear() % 100);
						// Fin la fecha de hoy.
						vje.setPago(bdPago.toString());
						vje.setFechaPagar_origen(fecha);
						vje.setFechaDeCarga(fecha);
					}
				} else {
					vje.setPendienteCarga(true);
					vje.setPendienteDePago(true);
				}
				if (chkDescargar.getValue()) {
					// La fecha de hoy.
					Date d = new Date();
					@SuppressWarnings("deprecation")
					String fecha = d.getDate() + "/" + (d.getMonth() + 1) + "/"
							+ (d.getYear() % 100);
					// Fin la fecha de hoy.
					vje.setPendienteDeDescarga(false);
					vje.setPendienteDeCobro(true);
					vje.setFechaPago_flete(fecha);
					vje.setPago_flete("0");
					vje.setKilosOrigenReales(tbDescargaRealToneladas.getText());
					vje.setKilos(tbDescargaRealToneladas.getText());
					vje.setFecha_pago_descargador(fecha);
					vje.setPago_descargados(tbMontoDescarga.getText());
					Double tonOrigen = Double
							.parseDouble(tbToneladas.getText());
					Double tonRealDescargadas = Double
							.parseDouble(tbDescargaRealToneladas.getText());
					if (tonOrigen > tonRealDescargadas) {
						// Multiplico las toneladas por el precio por tonelada
						BigDecimal bdTonVSob = BigDecimal.valueOf(Double
								.parseDouble(vje.getKilosOrigenReales()));
						BigDecimal bdPrecioXTon = BigDecimal.valueOf(Double
								.parseDouble(vje.getPrecio_kilo()));
						BigDecimal bdPago = bdTonVSob.multiply(bdPrecioXTon);
						vje.setPago(bdPago.toString());
					}
				} else if (chkDescargarPago.getValue()) {
					vje.setPendienteDeDescarga(false);
					vje.setPendienteDeCobro(false);
					if (pendienteDeDescarga) {
						// La fecha de hoy.
						Date d = new Date();
						@SuppressWarnings("deprecation")
						String fecha = d.getDate() + "/" + (d.getMonth() + 1)
								+ "/" + (d.getYear() % 100);
						// Fin la fecha de hoy.
						vje.setPago_flete(lblPrecioRealDescarga.getText());
						vje.setFechaPago_flete(fecha);
						vje.setKilosOrigenReales(tbDescargaRealToneladas
								.getText());
						vje.setKilos(tbDescargaRealToneladas.getText());
						vje.setFecha_pago_descargador(fecha);
						vje.setPago_descargados(tbMontoDescarga.getText());
						Double tonOrigen = Double.parseDouble(tbToneladas
								.getText());
						Double tonRealDescargadas = Double
								.parseDouble(tbDescargaRealToneladas.getText());
						if (tonOrigen > tonRealDescargadas) {
							// Multiplico las toneladas por el precio por
							// tonelada
							BigDecimal bdTonVSob = BigDecimal.valueOf(Double
									.parseDouble(vje.getKilosOrigenReales()));
							BigDecimal bdPrecioXTon = BigDecimal.valueOf(Double
									.parseDouble(vje.getPrecio_kilo()));
							BigDecimal bdPago = bdTonVSob
									.multiply(bdPrecioXTon);
							vje.setPago(bdPago.toString());
						}
					}
				} else {
					vje.setPendienteDeDescarga(true);
					vje.setPendienteDeCobro(true);
				}
				// En el caso de que se desactive la carga, se deben volver para
				// atras los pasos
				if (!(chkDescargar.getValue())
						&& !(chkDescargarPago.getValue())) {
					vje.setPendienteDeDescarga(true);
					vje.setPendienteDeCobro(true);
					vje.setFecha_pago_descargador(null);
					vje.setFechaPago_flete(null);
					vje.setPago_flete(null);
					vje.setPago_descargados(null);
					vje.setPago_descargador(null);
				}
				if (!(chkCargado.getValue()) && !(chkCargadoPago.getValue())) {
					vje.setPendienteCarga(true);
					vje.setPendienteDePago(true);
					vje.setPago(null);
					vje.setFechaPagar_origen(null);
				}
				// Les doy los valores de si se paga el flete o al descargador.
				vje.setSePagoFletero(chkPagoFletero.getValue());
				vje.setSePagoDescargador(chkPagoDescargador.getValue());
				// Compruebo que sean numericos
				if (FieldVerifier.isNumeric(tbToneladas.getText())
						&& FieldVerifier.isNumeric(tbMontoMonto.getText())
						&& FieldVerifier.isNumeric(tbMontoDescarga.getText())
						&& FieldVerifier.isNumeric(tbMontoFlete.getText())
						&& FieldVerifier.isNumeric(tbDescargaRealToneladas
								.getText())
						&& FieldVerifier.isNumeric(tbPrecioMaderaDescarga
								.getText())
						&& FieldVerifier.isNumeric(tbPrecioFleteroDescarga
								.getText())) {
					if (!estabaDescargado && chkDescargarPago.getValue()) {
						greetingService.obtenerCCdelCliente(vje.getCliente(),
								new AsyncCallback<String>() {
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Error al obtener la CC del cliente.");
									}

									@Override
									public void onSuccess(String result) {
										Double CC = Double.parseDouble(result);
										if (CC < 0) {
											Double pago = Double
													.parseDouble(lblPrecioRealDescarga
															.getText());
											if ((pago + CC) > 0) {
												CobrarPagoParcial pagoParcial = new CobrarPagoParcial(
														vje, pago, CC,
														estabaCargado,
														estabaPago,
														estabaDescargado,
														estabaCobrado,
														estabaPagoFlete,
														estabaPagoDescargador,lv, ModificarViaje.this);
												dialogBox = createDialogBox(
														"Cobrar viaje.",
														pagoParcial);
												dialogBox.center();
												dialogBox.show();
											} else {
												// Cuando el viaje es pagado del
												// saldo.
												modificarViaje(vje,
														estabaCargado,
														estabaPago,
														estabaDescargado,
														estabaCobrado,
														estabaPagoFlete,
														estabaPagoDescargador);
											}
										} else {
											modificarViaje(vje, estabaCargado,
													estabaPago,
													estabaDescargado,
													estabaCobrado,
													estabaPagoFlete,
													estabaPagoDescargador);
										}
									}
								});
					} else {
						modificarViaje(vje, estabaCargado, estabaPago,
								estabaDescargado, estabaCobrado,
								estabaPagoFlete, estabaPagoDescargador);
					}
				} else {
					Window.alert("Error al ingresar un valor num\u00e9rico.");
				}
			}
		}
		 //Vuelvo a habilitar el boton.
		 btnModificar.setEnabled(true);
	}
	
	public void cerrarDialogBox() {
		this.dialogBox.hide();
	}

	// Llamo a la la funcion para modificar el viaje.
	private void modificarViaje(Viaje vje2, Boolean estabaCargado2,
			Boolean estabaPago2, Boolean estabaDescargado2,
			Boolean estabaCobrado2, Boolean estabaPagoFlete2,
			Boolean estabaPagoDescargador2) {
		greetingService.ModificarViaje(vje2, estabaCargado2, estabaPago2,
				estabaDescargado2, estabaCobrado2, estabaPagoFlete2,
				estabaPagoDescargador2, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText("Error al modificar el viaje.");
					}

					@Override
					public void onSuccess(String result) {
						if (chkDescargarPago.getValue()
								|| chkDescargar.getValue()) {
							Double tonOrigen = Double.parseDouble(tbToneladas
									.getText());
							Double tonRealDescargadas = Double
									.parseDouble(tbDescargaRealToneladas
											.getText());
							if (tonOrigen > tonRealDescargadas) {
								vje.setKilos(String.valueOf(tonOrigen
										- tonRealDescargadas));
								//TODO Al cerrar el viaje sobrante 
								//se debe cerrar la ventana.
								CrearViaje viajeSobrante = new CrearViaje(vje,
										lv);
								dialogBox = createDialogBox("Crear viaje.",
										viajeSobrante);
								dialogBox.center();
								dialogBox.show();
							}
						}
						lv.cerrarDialogBox();
						// Window.alert("Se modifico el viaje correctamente.");
					}
				});
	}

	@UiHandler("chkCargado")
	void onChkCargadoClick(ClickEvent event) {
		if (chkCargado.getValue()) {
			chkCargadoPago.setValue(false);
		}
	}

	@UiHandler("chkCargadoPago")
	void onChkCargadoPagoClick(ClickEvent event) {
		if (chkCargadoPago.getValue()) {
			chkCargado.setValue(false);
		}
	}

	@UiHandler("chkDescargar")
	void onChkDescargarClick(ClickEvent event) {
		if (chkDescargar.getValue()) {
			chkDescargarPago.setValue(false);
		}
	}

	@UiHandler("chkDescargarPago")
	void onChkDescargarPagoClick(ClickEvent event) {
		if (chkDescargarPago.getValue()) {
			chkDescargar.setValue(false);
		}
	}

	@UiHandler("tbDescargaRealToneladas")
	void onTbDescargaRealToneladasChange(ChangeEvent event) {
		// Controlo que ingrese toneladas menor o igual.
		if (Double.parseDouble(tbDescargaRealToneladas.getText()) <= Double
				.parseDouble(vje.getKilos()))
			cambiarPrecioReal();
		else if (vje.getKilosOrigenReales() == null)
			tbDescargaRealToneladas.setText(vje.getKilos());
		else {
			tbDescargaRealToneladas.setText(vje.getKilosOrigenReales());
			cambiarPrecioReal();
		}
	}

	@UiHandler("tbPrecioFleteroDescarga")
	void onTbPrecioFleteroDescargaChange(ChangeEvent event) {
		cambiarPrecioReal();
	}

	@UiHandler("tbPrecioMaderaDescarga")
	void onTbPrecioMaderaDescargaChange(ChangeEvent event) {
		cambiarPrecioReal();
	}

	private DialogBox createDialogBox(String titulo, Widget widget) {
		final DialogBox dialogBox = new DialogBox();
		// Personalizo el dialogBox
		dialogBox.setText(titulo);
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		// Pone el Dialog box con el fondo en gris y centrado
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		// Boton para cerrar.
		Button closeButton = new Button("Cerrar");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		closeButton.setWidth("250px");
		dialogContents
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dialogContents.add(widget);
		dialogContents.add(closeButton);
		return dialogBox;
	}

	private void cambiarPrecioReal() {
		BigDecimal bd = new BigDecimal("0.00");
		BigDecimal bdIva = new BigDecimal("0.00");
		BigDecimal bdPrecioFletero = new BigDecimal("0.00");
		BigDecimal bdToneladasDescarga = new BigDecimal("0.00");
		BigDecimal bdPrecioMaderaDescarga = new BigDecimal("0.00");
		BigDecimal rSuma = new BigDecimal("0.00");

		// Realizo los calculos
		bdPrecioFletero = BigDecimal.valueOf(Double
				.parseDouble(tbPrecioFleteroDescarga.getText()));
		bdToneladasDescarga = BigDecimal.valueOf(Double
				.parseDouble(tbDescargaRealToneladas.getText()));
		bdPrecioMaderaDescarga = BigDecimal.valueOf(Double
				.parseDouble(tbPrecioMaderaDescarga.getText()));
		bdIva = BigDecimal.valueOf(Double.parseDouble(IVA));
		BigDecimal rPrecioFleteroIva = new BigDecimal("0.00");
		rPrecioFleteroIva = bdPrecioFletero.multiply(bdIva);
		// Sumo el precio del flete con iva incluido y el precio final de la
		// madera
		rSuma = rPrecioFleteroIva.add(bdPrecioMaderaDescarga);
		// Multiplico las toneladas por la suma del flete con iva incluido y el
		// precio final de la madera

		rSuma = rSuma.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		bd = bdToneladasDescarga.multiply(rSuma);
		// Por error que salta
//		bd = bd.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		lblPrecioRealDescarga.setText(bd.toString());
	}
}
