package com.conexion.ventanas;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PagarCobrarEmpresas extends Composite {

	private static PagarCobrarEmpresasUiBinder uiBinder = GWT
			.create(PagarCobrarEmpresasUiBinder.class);
	@UiField
	Label lblMje;
	@UiField
	TextBox tbMonto;
	@UiField
	Button btnAceptar;

	Empresa empresa;
	String accion;
	InformeClientes ic;
	InformeProveedor ip;

	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface PagarCobrarEmpresasUiBinder extends
			UiBinder<Widget, PagarCobrarEmpresas> {
	}

	public PagarCobrarEmpresas(Empresa empSel, String tipoAccion, InformeClientes informeClientes, InformeProveedor informeProveedor) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		ic = informeClientes;
		ip = informeProveedor;
		tbMonto.setFocus(true);
		empresa = empSel;
		accion = tipoAccion;
		if (tipoAccion.equals("cobrar")) {
			lblMje.setText("Ingrese el monto a cobrar.");
		} else {
			lblMje.setText("Ingrese el monto a pagar.");
		}
	}

	@UiHandler("btnAceptar")
	void handleClickAceptar(ClickEvent e) {
		if (!FieldVerifier.isNumeric(tbMonto.getText())) {
			Window.alert("Debe ingresar un monto valido.");
		} else {
			btnAceptar.setEnabled(false);
			pagarCobrar(empresa, tbMonto.getText(), accion);
		}
	}

	// Esta funcion se utiliza para pagar/cobrar a la empresa.
	private void pagarCobrar(Empresa empresa, String monto, final String accion) {
		greetingService.pagarCobrarEmpresa(empresa, monto, accion,
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error.");
					}

					@Override
					public void onSuccess(String result) {
						if (accion.equals("pagar")){
							ip.cerrarDialogBox();
						}else{
							ic.cerrarDialogBox();
						}						
					}
				});
	}
}
