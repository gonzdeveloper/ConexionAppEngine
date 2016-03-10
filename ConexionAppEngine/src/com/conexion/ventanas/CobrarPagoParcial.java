package com.conexion.ventanas;

import java.math.BigDecimal;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Viaje;
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
import com.google.gwt.user.client.ui.Widget;

public class CobrarPagoParcial extends Composite {

	private static CobrarPagoParcialUiBinder uiBinder = GWT
			.create(CobrarPagoParcialUiBinder.class);

	@UiField
	Label lblMonto;
	@UiField
	Button btnAceptar;
	Boolean esCargado = false;
	Boolean esPago = false;
	Boolean esDescargado = false;
	Boolean esCobrado = false;
	Boolean esPagoFlete = false;
	Boolean esPagoDescargador = false;
	Viaje vje = null;
	String pagar = "0";
	String cobrarCliente = "0";
	ListarViajes lv;
	ModificarViaje mv;

	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface CobrarPagoParcialUiBinder extends
			UiBinder<Widget, CobrarPagoParcial> {
	}

	public CobrarPagoParcial(final Viaje viaje, final Double pago,
			final Double CC, Boolean estabaCargado, Boolean estabaPago,
			Boolean estabaDescargado, Boolean estabaCobrado,
			Boolean estabaPagoFlete, Boolean estabaPagoDescargador,
			ListarViajes listarViajes, ModificarViaje modificarViaje) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		lv = listarViajes;
		mv = modificarViaje;
		BigDecimal total = new BigDecimal("0.00");
		total = BigDecimal.valueOf(pago).add(BigDecimal.valueOf(CC));
		lblMonto.setText("¿Desea cobrar " + total.toString() + "?");
		pagar = String.valueOf(-CC);
		total = total.multiply(BigDecimal.valueOf(-1));
		cobrarCliente = total.toString();
		esCargado = estabaCargado;
		esPago = estabaPago;
		esDescargado = estabaDescargado;
		esCobrado = estabaCobrado;
		esPagoFlete = estabaPagoFlete;
		esPagoDescargador = estabaPagoDescargador;
		vje = viaje;
	}

	@UiHandler("btnAceptar")
	void handleClickAceptar(ClickEvent e) {
		btnAceptar.setEnabled(false);
		modificarViaje(vje, pagar, cobrarCliente, esCargado, esPago,
				esDescargado, esCobrado, esPagoFlete, esPagoDescargador);

	}

	// Llamo a la la funcion para modificar el viaje.
	private void modificarViaje(Viaje vje2, String pagar, String cobrarCiente,
			Boolean estabaCargado2, Boolean estabaPago2,
			Boolean estabaDescargado2, Boolean estabaCobrado2,
			Boolean estabaPagoFlete2, Boolean estabaPagoDescargador2) {
		greetingService.ModificarViajeSaldoAPagar(vje2, pagar, cobrarCiente,
				estabaCargado2, estabaPago2, estabaDescargado2, estabaCobrado2,
				estabaPagoFlete2, estabaPagoDescargador2,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error al modificar el viaje.");
					}
					public void onSuccess(String result) {
						lv.cerrarDialogBox();
						mv.cerrarDialogBox();
					}
				});
	}
}
