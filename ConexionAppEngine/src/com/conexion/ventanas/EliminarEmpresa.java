package com.conexion.ventanas;

import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EliminarEmpresa extends Composite {

	private static EliminarEmpresaUiBinder uiBinder = GWT
			.create(EliminarEmpresaUiBinder.class);
	@UiField
	Button btnAceptar;
	@UiField
	Label lblConf;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface EliminarEmpresaUiBinder extends UiBinder<Widget, EliminarEmpresa> {
	}

	public EliminarEmpresa(final List<Empresa> empresas, final ListarEmpresa listarEmpresa) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		if (empresas.size() == 1)
			lblConf.setText("/�Seguro que desea eliminar la empresa?");
		else
			lblConf.setText("/�Seguro que desea eliminar las empresas?");
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Deshabilito el boton.
				btnAceptar.setEnabled(false);
				eliminarEmpresa(empresas);
			}

			private void eliminarEmpresa(List<Empresa> empresas) {
				greetingService.eliminarEmpresasSeleccionadas(empresas,
						new AsyncCallback<String>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error.");
							}

							@Override
							public void onSuccess(String result) {
//								Window.alert(result);
								listarEmpresa.cerrarDialogBox();
							}
						});
			}
		});
	}

}
