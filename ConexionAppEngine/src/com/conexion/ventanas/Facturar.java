package com.conexion.ventanas;

import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Viaje;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Facturar extends Composite {

	private static FacturarUiBinder uiBinder = GWT
			.create(FacturarUiBinder.class);

	@UiField
	TextBox tbFactura;
	@UiField
	Button btnAceptar;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface FacturarUiBinder extends UiBinder<Widget, Facturar> {
	}

	public Facturar(final List<Viaje> viajes, final ListarViajes listarViajes) {
		initWidget(uiBinder.createAndBindUi(this));
		tbFactura.setFocus(true);
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (tbFactura.getText().equals(""))
					Window.alert("Debe ingresar el numero de factura.");
				else {
					btnAceptar.setEnabled(false);
					greetingService.facturarViajesSeleccionados(viajes,
							tbFactura.getText(), new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Error.");
								}

								@Override
								public void onSuccess(String result) {
									listarViajes.cerrarDialogBox();
								}
							});
				}
			}
		});
	}
}
