package com.conexion.client;

import com.conexion.ventanas.InformeClientes;
import com.conexion.ventanas.InformeProveedor;
import com.conexion.ventanas.ListarEmpresa;
import com.conexion.ventanas.ListarEmpresasPropias;
import com.conexion.ventanas.ListarUsuarios;
import com.conexion.ventanas.ListarViajes;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite {
	HTMLPanel panelDerecho;

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);
	@UiField
	Label lblUsuarios;
	@UiField
	Label lblViajes;
	@UiField
	Label lblEmpresas;
	@UiField
	Label lblCCEmpresas;
	@UiField
	Label lblInfPro;
	@UiField
	Label lblInfClientes;

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}

	public Menu(HTMLPanel panelDerecho2) {
		panelDerecho = panelDerecho2;
		initWidget(uiBinder.createAndBindUi(this));
		onLblViajesClick(null);
	}

	@UiHandler("lblUsuarios")
	void onLblUsuariosClick(ClickEvent event) {
		ListarUsuarios crearUW = new ListarUsuarios();
		reseteoColores();
		lblUsuarios.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(crearUW);
	}

	@UiHandler("lblViajes")
	void onLblViajesClick(ClickEvent event) {
		ListarViajes lisarViajes = new ListarViajes();
		reseteoColores();
		lblViajes.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(lisarViajes);
	}

	@UiHandler("lblCCEmpresas")
	void onLblCCEmpresasClick(ClickEvent event) {
		ListarEmpresasPropias lstEmpPro = new ListarEmpresasPropias();
		reseteoColores();
		lblCCEmpresas.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(lstEmpPro);
	}

	@UiHandler("lblInfPro")
	void onLblInfProClick(ClickEvent event) {
		InformeProveedor infProv = new InformeProveedor();
		reseteoColores();
		lblInfPro.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(infProv);
	}

	@UiHandler("lblInfClientes")
	void onLblInfClientesClick(ClickEvent event) {
		InformeClientes infCli = new InformeClientes();
		reseteoColores();
		lblInfClientes.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(infCli);
	}

	@UiHandler("lblEmpresas")
	void onLblEmpresasClick(ClickEvent event) {
		ListarEmpresa listEmp = new ListarEmpresa();
		reseteoColores();
		lblEmpresas.getElement().getStyle()
				.setProperty("color", "rgb(61, 70, 77)");
		panelDerecho.clear();
		panelDerecho.add(listEmp);
	}

	private void reseteoColores() {
		lblUsuarios.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		lblViajes.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		lblInfPro.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		lblInfClientes.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		lblEmpresas.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
		lblCCEmpresas.getElement().getStyle()
				.setProperty("color", "rgb(40, 149, 241)");
	}
}
