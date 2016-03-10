/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-03-26 20:30:19 UTC)
 * on 2015-06-26 at 00:12:39 UTC 
 * Modify at your own risk.
 */

package com.conexion.entidades.empresaendpoint;

/**
 * Service definition for Empresaendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link EmpresaendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Empresaendpoint extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.18.0-rc of the empresaendpoint library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://fletemadera.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "empresaendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Empresaendpoint(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Empresaendpoint(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getEmpresa".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link GetEmpresa#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetEmpresa getEmpresa(java.lang.Long id) throws java.io.IOException {
    GetEmpresa result = new GetEmpresa(id);
    initialize(result);
    return result;
  }

  public class GetEmpresa extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.Empresa> {

    private static final String REST_PATH = "empresa/{id}";

    /**
     * Create a request for the method "getEmpresa".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link GetEmpresa#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetEmpresa#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetEmpresa(java.lang.Long id) {
      super(Empresaendpoint.this, "GET", REST_PATH, null, com.conexion.entidades.empresaendpoint.model.Empresa.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetEmpresa setAlt(java.lang.String alt) {
      return (GetEmpresa) super.setAlt(alt);
    }

    @Override
    public GetEmpresa setFields(java.lang.String fields) {
      return (GetEmpresa) super.setFields(fields);
    }

    @Override
    public GetEmpresa setKey(java.lang.String key) {
      return (GetEmpresa) super.setKey(key);
    }

    @Override
    public GetEmpresa setOauthToken(java.lang.String oauthToken) {
      return (GetEmpresa) super.setOauthToken(oauthToken);
    }

    @Override
    public GetEmpresa setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetEmpresa) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetEmpresa setQuotaUser(java.lang.String quotaUser) {
      return (GetEmpresa) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetEmpresa setUserIp(java.lang.String userIp) {
      return (GetEmpresa) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetEmpresa setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetEmpresa set(String parameterName, Object value) {
      return (GetEmpresa) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertEmpresa".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link InsertEmpresa#execute()} method to invoke the remote
   * operation.
   *
   * @param content the {@link com.conexion.entidades.empresaendpoint.model.Empresa}
   * @return the request
   */
  public InsertEmpresa insertEmpresa(com.conexion.entidades.empresaendpoint.model.Empresa content) throws java.io.IOException {
    InsertEmpresa result = new InsertEmpresa(content);
    initialize(result);
    return result;
  }

  public class InsertEmpresa extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.Empresa> {

    private static final String REST_PATH = "empresa";

    /**
     * Create a request for the method "insertEmpresa".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link InsertEmpresa#execute()} method to invoke the remote
     * operation. <p> {@link InsertEmpresa#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link com.conexion.entidades.empresaendpoint.model.Empresa}
     * @since 1.13
     */
    protected InsertEmpresa(com.conexion.entidades.empresaendpoint.model.Empresa content) {
      super(Empresaendpoint.this, "POST", REST_PATH, content, com.conexion.entidades.empresaendpoint.model.Empresa.class);
    }

    @Override
    public InsertEmpresa setAlt(java.lang.String alt) {
      return (InsertEmpresa) super.setAlt(alt);
    }

    @Override
    public InsertEmpresa setFields(java.lang.String fields) {
      return (InsertEmpresa) super.setFields(fields);
    }

    @Override
    public InsertEmpresa setKey(java.lang.String key) {
      return (InsertEmpresa) super.setKey(key);
    }

    @Override
    public InsertEmpresa setOauthToken(java.lang.String oauthToken) {
      return (InsertEmpresa) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertEmpresa setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertEmpresa) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertEmpresa setQuotaUser(java.lang.String quotaUser) {
      return (InsertEmpresa) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertEmpresa setUserIp(java.lang.String userIp) {
      return (InsertEmpresa) super.setUserIp(userIp);
    }

    @Override
    public InsertEmpresa set(String parameterName, Object value) {
      return (InsertEmpresa) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "listEmpresa".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link ListEmpresa#execute()} method to invoke the remote
   * operation.
   *
   * @return the request
   */
  public ListEmpresa listEmpresa() throws java.io.IOException {
    ListEmpresa result = new ListEmpresa();
    initialize(result);
    return result;
  }

  public class ListEmpresa extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.CollectionResponseEmpresa> {

    private static final String REST_PATH = "empresa";

    /**
     * Create a request for the method "listEmpresa".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link ListEmpresa#execute()} method to invoke the remote
     * operation. <p> {@link
     * ListEmpresa#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected ListEmpresa() {
      super(Empresaendpoint.this, "GET", REST_PATH, null, com.conexion.entidades.empresaendpoint.model.CollectionResponseEmpresa.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public ListEmpresa setAlt(java.lang.String alt) {
      return (ListEmpresa) super.setAlt(alt);
    }

    @Override
    public ListEmpresa setFields(java.lang.String fields) {
      return (ListEmpresa) super.setFields(fields);
    }

    @Override
    public ListEmpresa setKey(java.lang.String key) {
      return (ListEmpresa) super.setKey(key);
    }

    @Override
    public ListEmpresa setOauthToken(java.lang.String oauthToken) {
      return (ListEmpresa) super.setOauthToken(oauthToken);
    }

    @Override
    public ListEmpresa setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListEmpresa) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListEmpresa setQuotaUser(java.lang.String quotaUser) {
      return (ListEmpresa) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListEmpresa setUserIp(java.lang.String userIp) {
      return (ListEmpresa) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String cursor;

    /**

     */
    public java.lang.String getCursor() {
      return cursor;
    }

    public ListEmpresa setCursor(java.lang.String cursor) {
      this.cursor = cursor;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public ListEmpresa setLimit(java.lang.Integer limit) {
      this.limit = limit;
      return this;
    }

    @Override
    public ListEmpresa set(String parameterName, Object value) {
      return (ListEmpresa) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "modificarCuentaCorriente".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link ModificarCuentaCorriente#execute()} method to invoke the
   * remote operation.
   *
   * @param id
   * @param cuentaCorriente
   * @return the request
   */
  public ModificarCuentaCorriente modificarCuentaCorriente(java.lang.Long id, java.lang.String cuentaCorriente) throws java.io.IOException {
    ModificarCuentaCorriente result = new ModificarCuentaCorriente(id, cuentaCorriente);
    initialize(result);
    return result;
  }

  public class ModificarCuentaCorriente extends EmpresaendpointRequest<Void> {

    private static final String REST_PATH = "modificarCuentaCorriente/{id}/{cuenta_corriente}";

    /**
     * Create a request for the method "modificarCuentaCorriente".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link ModificarCuentaCorriente#execute()} method to invoke the
     * remote operation. <p> {@link ModificarCuentaCorriente#initialize(com.google.api.client.googleap
     * is.services.AbstractGoogleClientRequest)} must be called to initialize this instance
     * immediately after invoking the constructor. </p>
     *
     * @param id
     * @param cuentaCorriente
     * @since 1.13
     */
    protected ModificarCuentaCorriente(java.lang.Long id, java.lang.String cuentaCorriente) {
      super(Empresaendpoint.this, "POST", REST_PATH, null, Void.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      this.cuentaCorriente = com.google.api.client.util.Preconditions.checkNotNull(cuentaCorriente, "Required parameter cuentaCorriente must be specified.");
    }

    @Override
    public ModificarCuentaCorriente setAlt(java.lang.String alt) {
      return (ModificarCuentaCorriente) super.setAlt(alt);
    }

    @Override
    public ModificarCuentaCorriente setFields(java.lang.String fields) {
      return (ModificarCuentaCorriente) super.setFields(fields);
    }

    @Override
    public ModificarCuentaCorriente setKey(java.lang.String key) {
      return (ModificarCuentaCorriente) super.setKey(key);
    }

    @Override
    public ModificarCuentaCorriente setOauthToken(java.lang.String oauthToken) {
      return (ModificarCuentaCorriente) super.setOauthToken(oauthToken);
    }

    @Override
    public ModificarCuentaCorriente setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ModificarCuentaCorriente) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ModificarCuentaCorriente setQuotaUser(java.lang.String quotaUser) {
      return (ModificarCuentaCorriente) super.setQuotaUser(quotaUser);
    }

    @Override
    public ModificarCuentaCorriente setUserIp(java.lang.String userIp) {
      return (ModificarCuentaCorriente) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public ModificarCuentaCorriente setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @com.google.api.client.util.Key("cuenta_corriente")
    private java.lang.String cuentaCorriente;

    /**

     */
    public java.lang.String getCuentaCorriente() {
      return cuentaCorriente;
    }

    public ModificarCuentaCorriente setCuentaCorriente(java.lang.String cuentaCorriente) {
      this.cuentaCorriente = cuentaCorriente;
      return this;
    }

    @Override
    public ModificarCuentaCorriente set(String parameterName, Object value) {
      return (ModificarCuentaCorriente) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "modificarCuentaCorrienteNombre".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link ModificarCuentaCorrienteNombre#execute()} method to invoke
   * the remote operation.
   *
   * @param nombre
   * @param cuentaCorriente
   * @return the request
   */
  public ModificarCuentaCorrienteNombre modificarCuentaCorrienteNombre(java.lang.String nombre, java.lang.String cuentaCorriente) throws java.io.IOException {
    ModificarCuentaCorrienteNombre result = new ModificarCuentaCorrienteNombre(nombre, cuentaCorriente);
    initialize(result);
    return result;
  }

  public class ModificarCuentaCorrienteNombre extends EmpresaendpointRequest<Void> {

    private static final String REST_PATH = "modificarCuentaCorrienteNombre/{nombre}/{cuenta_corriente}";

    /**
     * Create a request for the method "modificarCuentaCorrienteNombre".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link ModificarCuentaCorrienteNombre#execute()} method to invoke
     * the remote operation. <p> {@link ModificarCuentaCorrienteNombre#initialize(com.google.api.clien
     * t.googleapis.services.AbstractGoogleClientRequest)} must be called to initialize this instance
     * immediately after invoking the constructor. </p>
     *
     * @param nombre
     * @param cuentaCorriente
     * @since 1.13
     */
    protected ModificarCuentaCorrienteNombre(java.lang.String nombre, java.lang.String cuentaCorriente) {
      super(Empresaendpoint.this, "POST", REST_PATH, null, Void.class);
      this.nombre = com.google.api.client.util.Preconditions.checkNotNull(nombre, "Required parameter nombre must be specified.");
      this.cuentaCorriente = com.google.api.client.util.Preconditions.checkNotNull(cuentaCorriente, "Required parameter cuentaCorriente must be specified.");
    }

    @Override
    public ModificarCuentaCorrienteNombre setAlt(java.lang.String alt) {
      return (ModificarCuentaCorrienteNombre) super.setAlt(alt);
    }

    @Override
    public ModificarCuentaCorrienteNombre setFields(java.lang.String fields) {
      return (ModificarCuentaCorrienteNombre) super.setFields(fields);
    }

    @Override
    public ModificarCuentaCorrienteNombre setKey(java.lang.String key) {
      return (ModificarCuentaCorrienteNombre) super.setKey(key);
    }

    @Override
    public ModificarCuentaCorrienteNombre setOauthToken(java.lang.String oauthToken) {
      return (ModificarCuentaCorrienteNombre) super.setOauthToken(oauthToken);
    }

    @Override
    public ModificarCuentaCorrienteNombre setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ModificarCuentaCorrienteNombre) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ModificarCuentaCorrienteNombre setQuotaUser(java.lang.String quotaUser) {
      return (ModificarCuentaCorrienteNombre) super.setQuotaUser(quotaUser);
    }

    @Override
    public ModificarCuentaCorrienteNombre setUserIp(java.lang.String userIp) {
      return (ModificarCuentaCorrienteNombre) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String nombre;

    /**

     */
    public java.lang.String getNombre() {
      return nombre;
    }

    public ModificarCuentaCorrienteNombre setNombre(java.lang.String nombre) {
      this.nombre = nombre;
      return this;
    }

    @com.google.api.client.util.Key("cuenta_corriente")
    private java.lang.String cuentaCorriente;

    /**

     */
    public java.lang.String getCuentaCorriente() {
      return cuentaCorriente;
    }

    public ModificarCuentaCorrienteNombre setCuentaCorriente(java.lang.String cuentaCorriente) {
      this.cuentaCorriente = cuentaCorriente;
      return this;
    }

    @Override
    public ModificarCuentaCorrienteNombre set(String parameterName, Object value) {
      return (ModificarCuentaCorrienteNombre) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "obtenerEmpresaByNombre".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link ObtenerEmpresaByNombre#execute()} method to invoke the
   * remote operation.
   *
   * @param nombre
   * @return the request
   */
  public ObtenerEmpresaByNombre obtenerEmpresaByNombre(java.lang.String nombre) throws java.io.IOException {
    ObtenerEmpresaByNombre result = new ObtenerEmpresaByNombre(nombre);
    initialize(result);
    return result;
  }

  public class ObtenerEmpresaByNombre extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.Empresa> {

    private static final String REST_PATH = "obtenerEmpresaByNombre/{nombre}";

    /**
     * Create a request for the method "obtenerEmpresaByNombre".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link ObtenerEmpresaByNombre#execute()} method to invoke the
     * remote operation. <p> {@link ObtenerEmpresaByNombre#initialize(com.google.api.client.googleapis
     * .services.AbstractGoogleClientRequest)} must be called to initialize this instance immediately
     * after invoking the constructor. </p>
     *
     * @param nombre
     * @since 1.13
     */
    protected ObtenerEmpresaByNombre(java.lang.String nombre) {
      super(Empresaendpoint.this, "POST", REST_PATH, null, com.conexion.entidades.empresaendpoint.model.Empresa.class);
      this.nombre = com.google.api.client.util.Preconditions.checkNotNull(nombre, "Required parameter nombre must be specified.");
    }

    @Override
    public ObtenerEmpresaByNombre setAlt(java.lang.String alt) {
      return (ObtenerEmpresaByNombre) super.setAlt(alt);
    }

    @Override
    public ObtenerEmpresaByNombre setFields(java.lang.String fields) {
      return (ObtenerEmpresaByNombre) super.setFields(fields);
    }

    @Override
    public ObtenerEmpresaByNombre setKey(java.lang.String key) {
      return (ObtenerEmpresaByNombre) super.setKey(key);
    }

    @Override
    public ObtenerEmpresaByNombre setOauthToken(java.lang.String oauthToken) {
      return (ObtenerEmpresaByNombre) super.setOauthToken(oauthToken);
    }

    @Override
    public ObtenerEmpresaByNombre setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ObtenerEmpresaByNombre) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ObtenerEmpresaByNombre setQuotaUser(java.lang.String quotaUser) {
      return (ObtenerEmpresaByNombre) super.setQuotaUser(quotaUser);
    }

    @Override
    public ObtenerEmpresaByNombre setUserIp(java.lang.String userIp) {
      return (ObtenerEmpresaByNombre) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String nombre;

    /**

     */
    public java.lang.String getNombre() {
      return nombre;
    }

    public ObtenerEmpresaByNombre setNombre(java.lang.String nombre) {
      this.nombre = nombre;
      return this;
    }

    @Override
    public ObtenerEmpresaByNombre set(String parameterName, Object value) {
      return (ObtenerEmpresaByNombre) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "obtenerMaxId".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link ObtenerMaxId#execute()} method to invoke the remote
   * operation.
   *
   * @return the request
   */
  public ObtenerMaxId obtenerMaxId() throws java.io.IOException {
    ObtenerMaxId result = new ObtenerMaxId();
    initialize(result);
    return result;
  }

  public class ObtenerMaxId extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.Empresa> {

    private static final String REST_PATH = "obtenerMaxId";

    /**
     * Create a request for the method "obtenerMaxId".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link ObtenerMaxId#execute()} method to invoke the remote
     * operation. <p> {@link
     * ObtenerMaxId#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected ObtenerMaxId() {
      super(Empresaendpoint.this, "POST", REST_PATH, null, com.conexion.entidades.empresaendpoint.model.Empresa.class);
    }

    @Override
    public ObtenerMaxId setAlt(java.lang.String alt) {
      return (ObtenerMaxId) super.setAlt(alt);
    }

    @Override
    public ObtenerMaxId setFields(java.lang.String fields) {
      return (ObtenerMaxId) super.setFields(fields);
    }

    @Override
    public ObtenerMaxId setKey(java.lang.String key) {
      return (ObtenerMaxId) super.setKey(key);
    }

    @Override
    public ObtenerMaxId setOauthToken(java.lang.String oauthToken) {
      return (ObtenerMaxId) super.setOauthToken(oauthToken);
    }

    @Override
    public ObtenerMaxId setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ObtenerMaxId) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ObtenerMaxId setQuotaUser(java.lang.String quotaUser) {
      return (ObtenerMaxId) super.setQuotaUser(quotaUser);
    }

    @Override
    public ObtenerMaxId setUserIp(java.lang.String userIp) {
      return (ObtenerMaxId) super.setUserIp(userIp);
    }

    @Override
    public ObtenerMaxId set(String parameterName, Object value) {
      return (ObtenerMaxId) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeEmpresa".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link RemoveEmpresa#execute()} method to invoke the remote
   * operation.
   *
   * @param id
   * @return the request
   */
  public RemoveEmpresa removeEmpresa(java.lang.Long id) throws java.io.IOException {
    RemoveEmpresa result = new RemoveEmpresa(id);
    initialize(result);
    return result;
  }

  public class RemoveEmpresa extends EmpresaendpointRequest<Void> {

    private static final String REST_PATH = "empresa/{id}";

    /**
     * Create a request for the method "removeEmpresa".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link RemoveEmpresa#execute()} method to invoke the remote
     * operation. <p> {@link RemoveEmpresa#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveEmpresa(java.lang.Long id) {
      super(Empresaendpoint.this, "DELETE", REST_PATH, null, Void.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveEmpresa setAlt(java.lang.String alt) {
      return (RemoveEmpresa) super.setAlt(alt);
    }

    @Override
    public RemoveEmpresa setFields(java.lang.String fields) {
      return (RemoveEmpresa) super.setFields(fields);
    }

    @Override
    public RemoveEmpresa setKey(java.lang.String key) {
      return (RemoveEmpresa) super.setKey(key);
    }

    @Override
    public RemoveEmpresa setOauthToken(java.lang.String oauthToken) {
      return (RemoveEmpresa) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveEmpresa setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveEmpresa) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveEmpresa setQuotaUser(java.lang.String quotaUser) {
      return (RemoveEmpresa) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveEmpresa setUserIp(java.lang.String userIp) {
      return (RemoveEmpresa) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public RemoveEmpresa setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public RemoveEmpresa set(String parameterName, Object value) {
      return (RemoveEmpresa) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateEmpresa".
   *
   * This request holds the parameters needed by the empresaendpoint server.  After setting any
   * optional parameters, call the {@link UpdateEmpresa#execute()} method to invoke the remote
   * operation.
   *
   * @param content the {@link com.conexion.entidades.empresaendpoint.model.Empresa}
   * @return the request
   */
  public UpdateEmpresa updateEmpresa(com.conexion.entidades.empresaendpoint.model.Empresa content) throws java.io.IOException {
    UpdateEmpresa result = new UpdateEmpresa(content);
    initialize(result);
    return result;
  }

  public class UpdateEmpresa extends EmpresaendpointRequest<com.conexion.entidades.empresaendpoint.model.Empresa> {

    private static final String REST_PATH = "empresa";

    /**
     * Create a request for the method "updateEmpresa".
     *
     * This request holds the parameters needed by the the empresaendpoint server.  After setting any
     * optional parameters, call the {@link UpdateEmpresa#execute()} method to invoke the remote
     * operation. <p> {@link UpdateEmpresa#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link com.conexion.entidades.empresaendpoint.model.Empresa}
     * @since 1.13
     */
    protected UpdateEmpresa(com.conexion.entidades.empresaendpoint.model.Empresa content) {
      super(Empresaendpoint.this, "PUT", REST_PATH, content, com.conexion.entidades.empresaendpoint.model.Empresa.class);
    }

    @Override
    public UpdateEmpresa setAlt(java.lang.String alt) {
      return (UpdateEmpresa) super.setAlt(alt);
    }

    @Override
    public UpdateEmpresa setFields(java.lang.String fields) {
      return (UpdateEmpresa) super.setFields(fields);
    }

    @Override
    public UpdateEmpresa setKey(java.lang.String key) {
      return (UpdateEmpresa) super.setKey(key);
    }

    @Override
    public UpdateEmpresa setOauthToken(java.lang.String oauthToken) {
      return (UpdateEmpresa) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateEmpresa setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateEmpresa) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateEmpresa setQuotaUser(java.lang.String quotaUser) {
      return (UpdateEmpresa) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateEmpresa setUserIp(java.lang.String userIp) {
      return (UpdateEmpresa) super.setUserIp(userIp);
    }

    @Override
    public UpdateEmpresa set(String parameterName, Object value) {
      return (UpdateEmpresa) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Empresaendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Empresaendpoint}. */
    @Override
    public Empresaendpoint build() {
      return new Empresaendpoint(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link EmpresaendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setEmpresaendpointRequestInitializer(
        EmpresaendpointRequestInitializer empresaendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(empresaendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}