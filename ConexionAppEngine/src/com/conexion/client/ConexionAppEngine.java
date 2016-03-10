package com.conexion.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConexionAppEngine implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		HorizontalPanel principal = new HorizontalPanel();
		RootPanel.get("contenedor").add(principal);
		Label cerrarSesion = new Label("Cerrar sesion");
		cerrarSesion.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		cerrarSesion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EntryPointLogin cApp = new EntryPointLogin();
				RootPanel.get("contenedor").clear();
				cApp.onModuleLoad();
			}
		});
		RootPanel.get("cerrarSesion").clear();
		RootPanel.get("cerrarSesion").add(cerrarSesion);

		HTMLPanel panelIzquierdo = new HTMLPanel("");
		principal.add(panelIzquierdo);
		panelIzquierdo.setWidth("180px");

		HTMLPanel panelDerecho = new HTMLPanel("");
		principal.add(panelDerecho);
		panelDerecho.setWidth("800px");

		Menu menu = new Menu(panelDerecho);
		panelIzquierdo.add(menu);
	}
}
