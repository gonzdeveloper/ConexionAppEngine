package com.conexion.ventanas;

import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Usuario;
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

public class EliminarUsuario extends Composite {

	private static EliminarUsuarioUiBinder uiBinder = GWT
			.create(EliminarUsuarioUiBinder.class);

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

	interface EliminarUsuarioUiBinder extends UiBinder<Widget, EliminarUsuario> {
	}

	public EliminarUsuario(final List<Usuario> usuarios, final ListarUsuarios listarUsuarios) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		if (usuarios.size() == 1)
			lblConf.setText("/�Seguro que desea eliminar el usuario?");
		else
			lblConf.setText("/�Seguro que desea eliminar los usuarios?");
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Deshabilito el bot�n.
				btnAceptar.setEnabled(false);
				eliminarUsuario(usuarios);
			}

			private void eliminarUsuario(List<Usuario> usuarios) {
				greetingService.eliminarUsuariosSeleccionados(usuarios,
						new AsyncCallback<String>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error.");
							}

							@Override
							public void onSuccess(String result) {
								listarUsuarios.cerrarDialogBox();
							}
						});
			}
		});

	}

	

}
