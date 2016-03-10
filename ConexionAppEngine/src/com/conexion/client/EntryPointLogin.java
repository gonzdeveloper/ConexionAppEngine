package com.conexion.client;

import com.conexion.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EntryPointLogin implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private static final FocusWidget btnIniciarSesion = null;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	// Variables
	TextBox tbUsuario = new TextBox();
	PasswordTextBox tbPass = new PasswordTextBox();
	Label errorLabel = new Label("");

	@Override
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("cerrarSesion");
		rootPanel.clear();
		RootPanel.get("contenedorLink").clear();
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setBorderWidth(0);
		errorLabel.addStyleName("serverResponseLabelError");
		RootPanel.get("contenedor").add(verticalPanel);
		// RootPanel.get("link").getElement().setPropertyBoolean("hidden",
		// true);
		verticalPanel.setSize("250px", "100px");

		Label lblNewLabel = new Label("Usuario");
		verticalPanel.add(lblNewLabel);
		lblNewLabel.setWidth("100%");
		verticalPanel.add(tbUsuario);
		verticalPanel.setCellVerticalAlignment(tbUsuario,
				HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setCellHorizontalAlignment(tbUsuario,
				HasHorizontalAlignment.ALIGN_CENTER);
		tbUsuario.setWidth("250px");
		tbUsuario.setFocus(true);

		Label lblNewLabel_1 = new Label("Clave");
		verticalPanel.add(lblNewLabel_1);
		lblNewLabel_1.setWidth("100%");

		// final PasswordTextBox tbPass = new PasswordTextBox();
		verticalPanel.add(tbPass);
		verticalPanel.setCellVerticalAlignment(tbPass,
				HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setCellHorizontalAlignment(tbPass,
				HasHorizontalAlignment.ALIGN_CENTER);
		tbPass.setWidth("250px");
		LayoutPanel layoutPanel = new LayoutPanel();
		verticalPanel.add(layoutPanel);
		layoutPanel.setHeight("12px");

		Button btnIniciarSesion = new Button("Iniciar sesion");

		verticalPanel.add(btnIniciarSesion);
		verticalPanel.setCellVerticalAlignment(btnIniciarSesion,
				HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setCellHorizontalAlignment(btnIniciarSesion,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnIniciarSesion.setWidth("100%");
		verticalPanel.add(errorLabel);

		// DANIEL
		Button btnEntrar = new Button("Forzar entrada");
		verticalPanel.add(btnEntrar);

		btnEntrar.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ConexionAppEngine cApp = new ConexionAppEngine();
				RootPanel.get("contenedor").clear();
				cApp.onModuleLoad();
			}
		});

		// DANIEL: eL FORZAR INICIO.
		btnEntrar.setVisible(false);
		// Listen for keyboard events in the input box.

		// Agrego el handler.
		MyHandler handler = new MyHandler();
		btnIniciarSesion.addClickHandler(handler);
		tbUsuario.addKeyUpHandler(handler);
		tbPass.addKeyUpHandler(handler);
	}

	public void Login() {
		String valLblUsuario = tbUsuario.getText();
		String valLblClave = tbPass.getText();
		// Realizo los controles de los campos.
		if ((!FieldVerifier.isValidName(valLblUsuario))
				|| (!FieldVerifier.isValidName(valLblClave))) {
			errorLabel.setText("El usuario o clave no pueden ser vacios.");
			return;
		}
		// Llamo al greetingService
		// btnIniciarSesion.setEnabled(false);
		greetingService.greetServerLogin(valLblUsuario, valLblClave,
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText("Error al iniciar sesion");
						// btnIniciarSesion.setEnabled(true);
					}

					@Override
					public void onSuccess(String result) {
						if (result == "noEsta") {
							errorLabel.setText("Usuario o clave incorrectos.");
							// btnIniciarSesion.setEnabled(true);
						} else {
							ConexionAppEngine cApp = new ConexionAppEngine();
							RootPanel.get("contenedor").clear();
							cApp.onModuleLoad();
						}
					}
				});
	}

	// Create a handler for the sendButton and nameField
	class MyHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		@Override
		public void onClick(ClickEvent event) {
			Login();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				Login();
			}
		}
	}
}
