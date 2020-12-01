package com.safi.managedBeans;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.util.JavaScriptRunner;

import com.safi.entity.CierreEjercicio;
import com.safi.entity.CodigoExpediente;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.Funcion;
import com.safi.entity.Movimiento;
import com.safi.entity.RecursoPropio;
import com.safi.entity.Servicio;
import com.safi.entity.TipoFondo;
import com.safi.entity.TipoImputacion;
import com.safi.entity.TipoOrdenGasto;
import com.safi.entity.Usuario;
import com.safi.enums.AccionEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.CierreEjercicioFacadeLocal;
import com.safi.facade.CodigoExpedienteFacadeLocal;
import com.safi.facade.CuentaBancariaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.FuncionFacadeLocal;
import com.safi.facade.MovimientoFacadeLocal;
import com.safi.facade.RecursoPropioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.facade.UsuarioFacadeLocal;
import com.safi.utilidad.Recurso;
import com.safi.utilidad.ReporteJava;
import com.safi.utilidad.SAFIReporteJava;
import com.safi.utilidad.Utilidad;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;


/**
 *
 * @author Gonzalez Facundo, Zacowicz Matias, Alvarenga Angel,Doroñuk Gustavo
 */
@ManagedBean
@SessionScoped
public class MovimientoManagedBean extends UtilManagedBean implements Serializable {

    private static Logger log = Logger.getLogger(MovimientoManagedBean.class);
    private final static String txtReversion = "[REVERSIÓN]";

    @EJB
    private MovimientoFacadeLocal movimientoFacade;
    @EJB
    private FuncionFacadeLocal funcionFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private RecursoPropioFacadeLocal recursoPropioFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    @EJB
    private CierreEjercicioFacadeLocal cierreEjercicioFacade;
    @EJB
    private CodigoExpedienteFacadeLocal codigoExpedienteFacade;
    private Long idEjercicio;
    private Long idTipoOrdenGasto;
    private Long idTipoFondo;
    private Long idTipoImputacion;
    private Long numeroOrden;
    private Long numeroPedidoFondo;
    private Long numeroEntregaFondo;
    private Long codigo;// codigo organismo para expediente
    private Long numero;// nro correlativo para expediente
    private Long anio;// año para expediente
    private Long idCuentaBancaria;
    private Usuario usuario = new Usuario();
    private Long numeroComprobante;
    private Date fechaComprobante;
    private String extractoComprobante;
    private String descripcion;// extracto
    private BigDecimal importe;
    private Double saldoCuentaBancaria;
    private List<SelectItem> selectItemsTipoFondo = new ArrayList<>();
    private List<SelectItem> selectItemsTipoImputacion = new ArrayList<>();
    private Movimiento movimiento;
    private boolean documentoPresentado;
    private Servicio servicio;
    private Long idServicio;
    private boolean revertirMovimiento;// accion agregada
    private String impDesde;
    private String impHasta;
    public final Long ID_TIPO_ORDEN_GASTO = 9L;
    public final Long ID_TIPO_FONDO = 2L;
    public boolean servicioUsuario; // si el usuario tiene servicio
    public Long idCodigoOrganismo;
    /**
     * Atributos para búsqueda*
     */
    private String descripcionBsq;
    private Date fechaAltaDesdeBsq;
    private Date fechaAltaHastaBsq;
    private Date fechaComprobDesdeBsq;
    private Date fechaComprobHastaBsq;
    private Long codigoBsq;
    private Long numeroBsq;
    private Long anioBsq;
    private BigDecimal importeDesdeBsq;
    private BigDecimal importeHastaBsq;
    private Long idServicioBsq;
    private Long idEjercicioBsq;
    private Long idCuentaBancariaBsq;
    private List<SelectItem> selectItemsCuentaBsq = new ArrayList<>();
    private List<SelectItem> selectItemsServiciosAlta = new ArrayList<>();
    private BigDecimal imp1;
    private BigDecimal imp2;
    private List<Funcion> funcionAux = new ArrayList<>();
    private Long numeroOrdenBsq;
    private Long numeroPedidoFondoBsq;
    private Long numeroEntregaFondoBsq;
    private String query;
    private String importeReversion;
    private Long idTipoOrdenGastoBsq;
    private Long idTipoFondoBsq;
    private Long idTipoImputacionBsq;
    private List<SelectItem> selectItemsTipoFondoBsq = new ArrayList<>();
    private List<SelectItem> selectItemsTipoImputacionBsq = new ArrayList<>();
    /**
     * Atributos para Mov. de Recurso Propio*
     */
    private List<SelectItem> selectItemsRecursosPropios = new ArrayList<>();
    private Long idRecursoPropio = 0L;
    private Recurso recurso;// clase para ver detallado el nomenclador de recurso propio

    // Optimización de las consultas a la BD
    public List<Movimiento> movimientoAux = new ArrayList<>();
    public List<Movimiento> lista = new ArrayList<>();
    public int totalTuplas;
    private boolean mostrarReporte, lazy = true;
    public int first, pageSize, primero, ultimo;
    private int rows = 10;

    public MovimientoManagedBean() {
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @see Limpia parametros de la clase.
     */
    @Override
    public void actualizar() {
        this.setImpDesde(null);
        this.setImpHasta(null);
        this.setIdEjercicioBsq(this.ejercicioFacade.ejercicioActual());
        this.setIdServicioBsq(null);
        this.setIdCuentaBancariaBsq(null);
        this.setDescripcionBsq(null);
        this.setFechaAltaDesdeBsq(null);
        this.setFechaAltaHastaBsq(null);
        this.setFechaComprobDesdeBsq(null);
        this.setFechaComprobHastaBsq(null);
        this.setImporteDesdeBsq(null);
        this.setImporteHastaBsq(null);
        this.setCodigoBsq(null);
        this.setNumeroBsq(null);
        this.setAnioBsq(null);
        this.setList(new ArrayList<>());
        this.selectItemsCuentaBsq.clear();
        this.setIdTipoOrdenGasto(0L);
        this.setIdTipoFondo(0L);
        this.setIdTipoImputacion(0L);
        this.setIdTipoOrdenGastoBsq(0L);
        this.setIdTipoFondoBsq(0L);
        this.setIdTipoImputacionBsq(0L);
        this.setFlagMovimiento(true);
        this.funcionAux.clear();
        this.setNumeroOrdenBsq(null);
        this.setNumeroEntregaFondoBsq(null);
        this.setNumeroPedidoFondoBsq(null);
        this.setBusqueda(false);

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Setea el focus en fecha de modificacion en nuevoRecursoModificado.
     *
     */
    public void actualizarCombos() {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "actualizarCombos();");
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @see Para actualizar el select de cuenta banacaria en el filtro de
     * búsqueda al momento de seleccionar un servicio administrativo.
     */
    public void actualizarCuentaBancaria() {
        this.selectItemsCuentaBsq.clear();
        List<CuentaBancaria> lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicioBsq(),
                this.getIdServicioBsq(), 1L);// 1L Estado Cuenta Activa
        if (!(lstCuentaBancariaAux.isEmpty())) {
            lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                this.selectItemsCuentaBsq.add(new SelectItem(cuentaBancariaAux.getId(),
                        cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
            });
        }

    }

    /**
     * @author Gonzalez Facundo
     * @see Obtiene el saldo de una cuenta X para mostrarlo en nuevo movimiento
     */
    public void actualizarSaldo() {
        if (this.getIdCuentaBancaria() != null) {
            this.setSaldoCuentaBancaria(
                    this.cuentaBancariaFacade.find(this.getIdCuentaBancaria()).getSaldo().doubleValue());

        } else {
            this.setSaldoCuentaBancaria(0.00);
        }
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @see actualiza cuenta bancaria según servicio
     */
    public void actualizarServicio() {
        try {
            Ejercicio ejercicioAux = new Ejercicio();
            if (this.getIdEjercicioBsq() != null) {
                ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicioBsq());
            }
            List<Servicio> lstServicioAux = servicioFacade.findAll(ejercicioAux.getId());
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().forEach((servicioAux) -> {
                });
            }
            if (this.getSessionBean().getUsuario().getServicio().getId() != null) {
                this.setIdServicioBsq(this.getSessionBean().getUsuario().getServicio().getId());
            }
            this.actualizarCuentaBancaria();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @see Actualiza combo tipo fondo segun orden de gasto. alta movimiento
     */
    public void actualizarTipoFondo() {

        this.selectItemsTipoFondo.clear();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(this.getIdTipoOrdenGasto(), 0L, 0L, 0L);
        if (this.getIdTipoOrdenGasto().equals(ID_TIPO_ORDEN_GASTO)) {
            this.setIdTipoFondo(ID_TIPO_FONDO);
            this.setIdTipoImputacion(ID_TIPO_ORDEN_GASTO);
            lstFuncionAux = this.funcionFacade.findAll(this.getIdTipoOrdenGasto(), ID_TIPO_FONDO, ID_TIPO_ORDEN_GASTO,
                    0L);
            this.actualizarCombos();
            this.actualizarTipoImputacion();
        } else {
            this.selectItemsTipoFondo.clear();
            this.selectItemsTipoImputacion.clear();

        }
        List<TipoFondo> lstTipoFondoAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoFondoAux.add(funcionAux.getTipoFondo());
        });
        HashSet<TipoFondo> hashSet = new HashSet<>(lstTipoFondoAux);
        lstTipoFondoAux.clear();
        this.setIdTipoFondo(null);
        this.setIdTipoImputacion(null);
        lstTipoFondoAux.addAll(hashSet);
        if (!(lstTipoFondoAux.isEmpty())) {
            lstTipoFondoAux.stream().forEach((tipofondoAux) -> {
                this.selectItemsTipoFondo.add(
                        new SelectItem(tipofondoAux.getId(), tipofondoAux.getId() + " - " + tipofondoAux.getNombre()));
            });
        }
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Actualiza combo tipo fondo en busqueda. Búsqueda
     */
    public void actualizarTipoFondoBsq() {

        this.selectItemsTipoFondoBsq.clear();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(true);
        List<TipoFondo> lstTipoFondoAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoFondoAux.add(funcionAux.getTipoFondo());
        });
        HashSet<TipoFondo> hashSet = new HashSet<>(lstTipoFondoAux);
        lstTipoFondoAux.clear();
        this.setIdTipoFondoBsq(null);
        this.setIdTipoImputacionBsq(null);
        lstTipoFondoAux.addAll(hashSet);
        if (!(lstTipoFondoAux.isEmpty())) {
            lstTipoFondoAux.stream().forEach((tipofondoAux) -> {
                this.selectItemsTipoFondoBsq.add(
                        new SelectItem(tipofondoAux.getId(), tipofondoAux.getId() + " - " + tipofondoAux.getNombre()));
            });
        }
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo
     *
     * @see Actualiza combo tipo imputación segun la combinacón orden-fondo alta
     * movimiento
     */
    public void actualizarTipoImputacion() {
        this.selectItemsTipoImputacion.clear();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(this.getIdTipoOrdenGasto(), this.getIdTipoFondo(), 0L,
                0L);
        if (this.getIdTipoOrdenGasto().equals(ID_TIPO_ORDEN_GASTO)) {
            this.setIdTipoFondo(ID_TIPO_FONDO);
            this.setIdTipoImputacion(ID_TIPO_ORDEN_GASTO);
            lstFuncionAux = this.funcionFacade.findAll(this.getIdTipoOrdenGasto(), ID_TIPO_FONDO, ID_TIPO_ORDEN_GASTO,
                    0L);

        }
        List<TipoImputacion> lstTipoImputacionAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoImputacionAux.add(funcionAux.getTipoImputacion());
        });
        HashSet<TipoImputacion> hashSet = new HashSet<>(lstTipoImputacionAux);
        lstTipoImputacionAux.clear();
        this.setIdTipoImputacion(null);
        lstTipoImputacionAux.addAll(hashSet);
        if (!(lstTipoImputacionAux.isEmpty())) {
            lstTipoImputacionAux.stream().forEach((tipoImputacionAux) -> {
                this.selectItemsTipoImputacion.add(new SelectItem(tipoImputacionAux.getId(),
                        tipoImputacionAux.getId() + " - " + tipoImputacionAux.getNombre()));
            });
        }

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Actualiza combo tipo imputación segun la combinación orden-fondo
     * busqueda
     */
    public void actualizarTipoImputacionBsq() {
        this.selectItemsTipoImputacionBsq.clear();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(true);
        List<TipoImputacion> lstTipoImputacionAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoImputacionAux.add(funcionAux.getTipoImputacion());
        });
        HashSet<TipoImputacion> hashSet = new HashSet<>(lstTipoImputacionAux);
        lstTipoImputacionAux.clear();
        lstTipoImputacionAux.addAll(hashSet);
        if (!(lstTipoImputacionAux.isEmpty())) {
            lstTipoImputacionAux.stream().forEach((tipoImputacionAux) -> {
                this.selectItemsTipoImputacionBsq.add(new SelectItem(tipoImputacionAux.getId(),
                        tipoImputacionAux.getId() + " - " + tipoImputacionAux.getNombre()));
            });
        }

    }

    /**
     * author Doroñuk Gustavo
     *
     * @see Controla que un movimiento revertido no se pueda volver a revertir
     * @param movimiento
     */
    public boolean canBeReversed(Movimiento movimiento) {
        try {
            if ((movimiento.getDescripcion().contains(txtReversion)) || movimiento.isRevertido()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @author Gonzalez Facundo
     * @see Crea el movimiento.
     * @return String
     */
    @Override
    public String crear() {
        BigDecimal bdImporte = this.getImporte();
        try {
            if (this.getIdTipoOrdenGasto() == null || this.getIdTipoFondo() == null
                    || this.getIdTipoImputacion() == null) {
                throw new Exception(
                        "No se pudo efectuar el movimiento la combinación Orden-Fondo-imputación es incorrecta");
            }
            this.setCodigo(this.idCodigoOrganismo);
            this.setServicio(this.servicioFacade.find(
                    this.getIdServicio() == null ? this.getUsuario().getServicio().getId() : this.getIdServicio()));
            if (!cierreEjercicioFacade.findAll(this.getIdEjercicio(), this.getServicio().getId()).isEmpty()) {
                throw new Exception(
                        "No se pudo efectuar el movimiento debido a que el ejercicio se encuentra cerrado para el servicio.");
            }
            if (this.isControlAlta()) {// controla que no se creen 2 movimientos al mismo tiempo
                this.getUsuario().setServicio(
                        this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio());
                this.movimientoFacade.create(this.getIdEjercicio(), this.getIdCuentaBancaria(),
                        this.getIdTipoOrdenGasto(), this.getIdTipoFondo(), this.getIdTipoImputacion(),
                        this.getNumeroOrden(), this.getNumeroPedidoFondo(), this.getNumeroEntregaFondo(),
                        this.getCodigo(), this.getNumero(), this.getAnio(), this.getNumeroComprobante(),
                        this.getFechaComprobante(), this.getDescripcion(), bdImporte, this.getUsuario(),
                        this.getIdRecursoPropio(), false);
                this.setControlAlta(false); // control duplicación de movimiento
                // INICIO AUDITORIA//
                Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
                Servicio servicioAux = this.getUsuario().getServicio() == null ? this.getServicio()
                        : this.getUsuario().getServicio();
                CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(this.getIdCuentaBancaria());
                RecursoPropio recursoAux = this.recursoPropioFacade.find(this.getIdRecursoPropio());
                this.setAuditoriaActual(this.devolverStringAuditoria(
                        this.ejercicioFacade.find(this.getIdEjercicio()).getAnio().toString(), servicioAux.getCodigo(),
                        servicioAux.getAbreviatura(), cuentaBancariaAux.getNumero().toString(),
                        cuentaBancariaAux.getDescripcion(), this.getIdTipoOrdenGasto().toString(),
                        this.getIdTipoFondo().toString(), this.getIdTipoImputacion().toString(),
                        this.getNumeroOrden() != null ? this.getNumeroOrden().toString() : "0",
                        this.getNumeroPedidoFondo() != null ? this.getNumeroPedidoFondo().toString() : "0",
                        this.getNumeroEntregaFondo() != null ? this.getNumeroEntregaFondo().toString() : "0",
                        this.getCodigo().toString(), this.getNumero().toString(), this.getAnio().toString(),
                        this.getNumeroComprobante().toString(), this.getFechaComprobante(), this.getDescripcion(),
                        bdImporte, recursoAux));

                this.auditoriaFacade.create(AccionEnum.ALTA_MOVIMIENTO.getName(), new Date(),
                        this.getIdUsuarioResponsable(), "-", this.getAuditoriaActual(), ejercicioAux.getId());
                // FIN AUDITORIA//
                this.setTitle("Proceso completo...");
                this.setImages("fa fa-check-circle-o");
                this.setResultado("successErrorMovimiento");
                this.setMsgSuccessError("El movimiento ha sido generado con éxito.");
                this.setBusqueda(true);
            }
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMovimiento");
            log.error(ex.getMessage());
        }
        return this.getResultado();
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see Guardar y crear otro
     * @return String
     */
    @Override
    public String crearOtro() {
        try {
            if (this.getIdTipoOrdenGasto() == null || this.getIdTipoFondo() == null
                    || this.getIdTipoImputacion() == null) {
                throw new Exception(
                        "No se pudo efectuar el movimiento la combinación Orden-Fondo-imputación es incorrecta");
            }
            this.setCodigo(this.idCodigoOrganismo);
            this.setServicio(this.servicioFacade.find(
                    this.getIdServicio() == null ? this.getUsuario().getServicio().getId() : this.getIdServicio()));
            this.getUsuario().setServicio(
                    this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio());
            BigDecimal bdImporte = this.getImporte();
            this.movimientoFacade.create(this.getIdEjercicio(), this.getIdCuentaBancaria(), this.getIdTipoOrdenGasto(),
                    this.getIdTipoFondo(), this.getIdTipoImputacion(), this.getNumeroOrden(),
                    this.getNumeroPedidoFondo(), this.getNumeroEntregaFondo(), this.getCodigo(), this.getNumero(),
                    this.getAnio(), this.getNumeroComprobante(), this.getFechaComprobante(), this.getDescripcion(),
                    bdImporte, this.getUsuario(), this.getIdRecursoPropio(), false);
            // INICIO AUDITORIA//
            Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
            Servicio servicioAux = this.getUsuario().getServicio() == null ? this.getServicio()
                    : this.getUsuario().getServicio();
            CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(this.getIdCuentaBancaria());
            RecursoPropio recursoAux = this.recursoPropioFacade.find(this.getIdRecursoPropio());
            this.setAuditoriaActual(this.devolverStringAuditoria(
                    this.ejercicioFacade.find(this.getIdEjercicio()).getAnio().toString(), servicioAux.getCodigo(),
                    servicioAux.getAbreviatura(), cuentaBancariaAux.getNumero().toString(),
                    cuentaBancariaAux.getDescripcion(), this.getIdTipoOrdenGasto().toString(),
                    this.getIdTipoFondo().toString(), this.getIdTipoImputacion().toString(),
                    this.getNumeroOrden() != null ? this.getNumeroOrden().toString() : "0",
                    this.getNumeroPedidoFondo() != null ? this.getNumeroPedidoFondo().toString() : "0",
                    this.getNumeroEntregaFondo() != null ? this.getNumeroEntregaFondo().toString() : "0",
                    this.getCodigo().toString(), this.getNumero().toString(), this.getAnio().toString(),
                    this.getNumeroComprobante().toString(), this.getFechaComprobante(), this.getDescripcion(),
                    bdImporte, recursoAux));

            this.auditoriaFacade.create(AccionEnum.ALTA_MOVIMIENTO.getName(), new Date(),
                    this.getIdUsuarioResponsable(), "-", this.getAuditoriaActual(), ejercicioAux.getId());
            this.limpiarAuditorias();
            // FIN AUDITORIA//
            this.setResultado("nuevoMovimiento");
            this.limpiarParaGuardarYCrearOtro();
            this.actualizarSaldo();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMovimiento");
            log.error(ex.getMessage());
        }
        return this.getResultado();
    }

    /**
     * author Gonzalez Facundo
     *
     * @param ejercicio
     * @param CodigoServicio
     * @param DescripcionServicio
     * @param Nrocuenta
     * @param DescripcionCuenta
     * @param ordenGasto
     * @param tipoFondo
     * @param tipoImputacion
     * @param nroOrden
     * @param nroPedidoFondo
     * @param nroEntregaFondo
     * @param CodigoExpediente
     * @param NumeroExpediente
     * @param AnioExpediente
     * @param nroComprobante
     * @param fechaComprobante
     * @param Descripcion
     * @param bdImporte
     * @param recursoAux
     * @see Devuelve cadena de texto para Auditoria(valorActual,valorAnterior)
     * @return String
     */
    public String devolverStringAuditoria(String ejercicio, String CodigoServicio, String DescripcionServicio,
            String Nrocuenta, String DescripcionCuenta, String ordenGasto, String tipoFondo, String tipoImputacion,
            String nroOrden, String nroPedidoFondo, String nroEntregaFondo, String CodigoExpediente,
            String NumeroExpediente, String AnioExpediente, String nroComprobante, Date fechaComprobante,
            String Descripcion, BigDecimal bdImporte, RecursoPropio recursoAux) {

        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String recursoElegido = recursoAux == null ? "NINGUNO"
                : (recursoAux.getOrganismo().getClasificadorOrganismo().getId() + "."
                + Utilidad.ceroIzquierda(recursoAux.getOrganismo().getCodigoOrganismo()) + "."
                + recursoAux.getCaracter().getId() + "."
                + recursoAux.getClasificadorRecurso().obtenerCodigoCompleto(recursoAux.getClasificadorRecurso())
                + "." + recursoAux.getConcepto() + " - " + recursoAux.getDescripcion());

        return "Ejercicio: " + ejercicio + "\n" + "Servicio: " + CodigoServicio + " - " + DescripcionServicio + "\n"
                + "Cuenta: " + Nrocuenta + " - " + DescripcionCuenta + "\n" + "Función: " + ordenGasto + " - "
                + tipoFondo + " - " + tipoImputacion + "\n" + "N° Orden: " + (nroOrden == null ? "NINGUNO" : nroOrden)
                + "\n" + "N° Pedido Fondo: " + (nroPedidoFondo == null ? "NINGUNO" : nroPedidoFondo) + "\n"
                + "N° Entrega Fondo: " + (nroEntregaFondo == null ? "NINGUNO" : nroEntregaFondo) + "\n" + "Expediente: "
                + CodigoExpediente + "-" + NumeroExpediente + "/" + AnioExpediente + "\n" + "N° Comprobante: "
                + nroComprobante + "\n" + "Fecha Comprobante: " + formato.format(fechaComprobante) + "\n"
                + "Descripción: " + Descripcion.toUpperCase() + "\n" + "Importe: " + bdImporte + "\n"
                + "Recurso Propio: " + recursoElegido;

    }

    public Long getAnio() {
        return anio;
    }

    public Long getAnioBsq() {
        return anioBsq;
    }

    public Long getCodigo() {
        return codigo;
    }

    public Long getCodigoBsq() {
        return codigoBsq;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDescripcionBsq() {
        return descripcionBsq;
    }

    public String getExtractoComprobante() {
        return extractoComprobante;
    }

    public Date getFechaAltaDesdeBsq() {
        return fechaAltaDesdeBsq;
    }

    public Date getFechaAltaHastaBsq() {
        return fechaAltaHastaBsq;
    }

    public Date getFechaComprobante() {
        return fechaComprobante;
    }

    public Date getFechaComprobDesdeBsq() {
        return fechaComprobDesdeBsq;
    }

    public Date getFechaComprobHastaBsq() {
        return fechaComprobHastaBsq;
    }

    public int getFirst() {
        return first;
    }

    public List<Funcion> getFuncionAux() {
        return funcionAux;
    }

    public Resource getGenerarReportePDF() throws JRException, FileNotFoundException, IOException, Exception {
        SAFIReporteJava miRecurso;

        JasperPrint jasperResultado = new ReporteJava().exportMovimientos(this.getQuery());
        byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
        miRecurso = new SAFIReporteJava(bites);
        return miRecurso;
    }

    public Resource getGenerarReporteXLS() throws Exception {
        JasperPrint jasperResultado = new ReporteJava().exportMovimientos(this.getQuery());
        SAFIReporteJava miRecurso = null;
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        jasperResultado.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
        jasperResultado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.1", "pageHeader");
        jasperResultado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.band.2", "pageFooter");
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperResultado));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setMaxRowsPerSheet(60000);
        configuration.setIgnorePageMargins(true);
        configuration.setWhitePageBackground(true);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        byte[] bites = xlsReport.toByteArray();
        miRecurso = new SAFIReporteJava(bites);
        return miRecurso;
    }

    public Long getIdCodigoOrganismo() {
        return idCodigoOrganismo;
    }

    public Long getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public Long getIdCuentaBancariaBsq() {
        return idCuentaBancariaBsq;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public Long getIdEjercicioBsq() {
        if (idEjercicioBsq != null) {
            return idEjercicioBsq;
        } else {
            return this.ejercicioFacade.ejercicioActual();
        }

    }

    public Long getIdRecursoPropio() {
        return idRecursoPropio;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public Long getIdServicioBsq() {
        return idServicioBsq;
    }

    public Long getIdTipoFondo() {
        return idTipoFondo;
    }

    public Long getIdTipoFondoBsq() {
        return idTipoFondoBsq;
    }

    public Long getIdTipoImputacion() {
        return idTipoImputacion;
    }

    public Long getIdTipoImputacionBsq() {
        return idTipoImputacionBsq;
    }

    public Long getIdTipoOrdenGasto() {
        return idTipoOrdenGasto;
    }

    public Long getIdTipoOrdenGastoBsq() {
        return idTipoOrdenGastoBsq;
    }

    public String getImpDesde() {
        return impDesde;
    }

    public String getImpHasta() {
        return impHasta;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public BigDecimal getImporteDesdeBsq() {
        return importeDesdeBsq;
    }

    public BigDecimal getImporteHastaBsq() {
        return importeHastaBsq;
    }

    public String getImporteReversion() {
        return importeReversion;
    }

    public List<Movimiento> getLista() {
        return lista;
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo,Matias Zakowicz
     * @see Obtiene lista de movimientos para el DataTable.
     * @return List Movimiento
     */
    @Override
    public LazyDataModel<Movimiento> getListElements() {

        // busqueda por función(cod de orden, fondo e imputación
        if (this.idTipoOrdenGastoBsq != null && idTipoFondoBsq != null && idTipoImputacionBsq != null) {
            if (idTipoOrdenGastoBsq == 0L && idTipoFondoBsq == 0L && idTipoImputacionBsq == 0L) {
                this.setFuncionAux(new ArrayList<>());
            } else {
                this.setFuncionAux(
                        this.funcionFacade.findAll(idTipoOrdenGastoBsq, idTipoFondoBsq, idTipoImputacionBsq, 0L));
            }
        }
        if (flag) {
            // solución busqueda importe con coma y decimal
            String impD = "";
            String impH = "";
            if (this.getImpDesde() != null) {
                impD = this.getImpDesde();
                impD = impD.replace(".", "");
                impD = impD.replace(",", ".");
                if (!impD.equals("")) {
                    this.setImporteDesdeBsq(new BigDecimal(impD));
                } else {
                    this.setImporteDesdeBsq(null);
                }
            } else {
                this.setImporteDesdeBsq(null);
            }
            if (this.getImpHasta() != null) {
                impH = this.getImpHasta();
                impH = impH.replace(".", "");
                impH = impH.replace(",", ".");
                if (!impH.equals("")) {
                    this.setImporteHastaBsq(new BigDecimal(impH));
                } else {
                    this.setImporteHastaBsq(null);
                }
            } else {
                this.setImporteHastaBsq(null);
            }
            this.setFlag(false);
        }
        LazyDataModel<Movimiento> lstMovimientos = new LazyDataModel<Movimiento>() {

            @Override
            public List<Movimiento> load(int first, int pageSize, final SortCriteria[] criteria,
                    final Map<String, String> filters) {
                primero = first;
                ultimo = pageSize;
                return getMovimientoAux(first, pageSize);
            }
        };
        lstMovimientos.setRowCount(this.getTotalTuplas());

        return lstMovimientos;

    }

    /**
     * @author Matias Zakowicz
     * @see Devuelve lista movimientos sin lazy
     *
     */
    public List<Movimiento> getListElementsWithoutLazy() {
        List<Movimiento> returnList = new ArrayList();
        returnList = movimientoFacade.findAll(
                idEjercicioBsq != null ? idEjercicioBsq : ejercicioFacade.ejercicioActual(),
                servicio != null ? servicio.getId() : idServicioBsq, idCuentaBancariaBsq, descripcionBsq,
                fechaAltaDesdeBsq, fechaAltaHastaBsq, fechaComprobDesdeBsq, fechaComprobHastaBsq, importeDesdeBsq,
                importeHastaBsq, codigoBsq, numeroBsq, anioBsq, numeroOrdenBsq, numeroPedidoFondoBsq,
                numeroEntregaFondoBsq, this.getIdTipoOrdenGastoBsq(), this.getIdTipoFondoBsq(),
                this.getIdTipoImputacionBsq(), primero, ultimo);
        return returnList;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    /**
     * @author Gonzalez Facundo,
     * @see Controlo que la lista de movimientos no sea solicitada más de una
     * vez, solo cuando se actualiza o se busca.
     * @return Lista
     */
    public List<Movimiento> getMovimientoAux(int first, int pageSize) {
        if (busqueda) {
            if (flagMovimiento || this.getFirst() != first || this.getPageSize() != pageSize) {
                List<Movimiento> lstMovimientosAux = movimientoFacade.findAll(
                        idEjercicioBsq != null ? idEjercicioBsq : ejercicioFacade.ejercicioActual(),
                        servicio != null ? servicio.getId() : idServicioBsq, idCuentaBancariaBsq, descripcionBsq,
                        fechaAltaDesdeBsq, fechaAltaHastaBsq, fechaComprobDesdeBsq, fechaComprobHastaBsq,
                        importeDesdeBsq, importeHastaBsq, codigoBsq, numeroBsq, anioBsq, numeroOrdenBsq,
                        numeroPedidoFondoBsq, numeroEntregaFondoBsq, this.getIdTipoOrdenGastoBsq(),
                        this.getIdTipoFondoBsq(), this.getIdTipoImputacionBsq(), first, pageSize);

                this.setTotalTuplas(movimientoFacade
                        .countAll(idEjercicioBsq != null ? idEjercicioBsq : ejercicioFacade.ejercicioActual(),
                                servicio != null ? servicio.getId() : idServicioBsq, idCuentaBancariaBsq,
                                descripcionBsq, fechaAltaDesdeBsq, fechaAltaHastaBsq, fechaComprobDesdeBsq,
                                fechaComprobHastaBsq, importeDesdeBsq, importeHastaBsq, codigoBsq, numeroBsq, anioBsq,
                                numeroOrdenBsq, numeroPedidoFondoBsq, numeroEntregaFondoBsq,
                                this.getIdTipoOrdenGastoBsq(), this.getIdTipoFondoBsq(), this.getIdTipoImputacionBsq())
                        .intValue());
                this.setFirst(first);
                this.setPageSize(pageSize);
                this.setFlagMovimiento(false);
                this.setLista(lstMovimientosAux);
            }
            return this.getLista();

        } else {
            return new ArrayList<>();
        }
    }

    public Long getNumero() {
        return numero;
    }

    public Long getNumeroBsq() {
        return numeroBsq;
    }

    public Long getNumeroComprobante() {
        return numeroComprobante;
    }

    public Long getNumeroEntregaFondo() {
        return numeroEntregaFondo;
    }

    public Long getNumeroEntregaFondoBsq() {
        return numeroEntregaFondoBsq;
    }

    public Long getNumeroOrden() {
        return numeroOrden;
    }

    public Long getNumeroOrdenBsq() {
        return numeroOrdenBsq;
    }

    public Long getNumeroPedidoFondo() {
        return numeroPedidoFondo;
    }

    public Long getNumeroPedidoFondoBsq() {
        return numeroPedidoFondoBsq;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getQuery() {
        return query;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public int getRows() {
        return rows;
    }

    public Double getSaldoCuentaBancaria() {
        return saldoCuentaBancaria;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Obtiene codigos de expediente para el combo de codigos expediente
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsCodigoExpediente() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<CodigoExpediente> lstCodigoExpedienteAux = new ArrayList<>();
            lstCodigoExpedienteAux = this.servicioFacade
                    .findAllServicio(this.getServicio() != null ? this.getServicio().getId() : this.getIdServicio(),
                            this.getIdEjercicio())
                    .get(0).getLstCodigoExpediente();
            if (!(lstCodigoExpedienteAux.isEmpty())) {
                lstCodigoExpedienteAux.stream().forEach((codigoExpedienteAux) -> {
                    selectItems.add(new SelectItem(codigoExpedienteAux.getCodigo(), codigoExpedienteAux.getCodigo()));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see actualiza combo de cuenta bancaria en nuevo movimiento
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsCuentaBancaria() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<CuentaBancaria> lstCuentaBancariaAux = new ArrayList<>();
        lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicio(),
                this.getServicio() != null ? this.getServicio().getId() : this.getIdServicio(), 1L);// 1L= EstadoCuenta
        // Activa

        if (!(lstCuentaBancariaAux.isEmpty())) {
            lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                selectItems.add(new SelectItem(cuentaBancariaAux.getId(),
                        cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
            });
        }

        return selectItems;
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see Actualiza combo de cuenta bancaria en búsqueda
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsCuentaBancariaBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<CuentaBancaria> lstCuentaBancariaAux = new ArrayList<>();
        try {
            lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicioBsq(),
                    this.getServicio() == null ? this.getIdServicioBsq() : this.getServicio().getId(), 1L);// 1L=
            // EstadoCuenta
            // Activa
            if (!(lstCuentaBancariaAux.isEmpty())) {
                lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                    selectItems.add(new SelectItem(cuentaBancariaAux.getId(),
                            cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
                });
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return selectItems;
    }

    public List<SelectItem> getSelectItemsCuentaBsq() {
        return selectItemsCuentaBsq;
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see Actualiza combo de ejercicio en nuevo movimiento
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicio() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(0, 1L);// 1L = Estado Activo
            if (!(lstEjercicioAux.isEmpty())) {
                lstEjercicioAux.stream().forEach((ejercicioAux) -> {
                    selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
                });
            }

            return selectItems;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see Actualiza combo de ejercicio en nuevo movimiento
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicioBsq() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true);// 1L = Estado Activo
            if (!(lstEjercicioAux.isEmpty())) {
                lstEjercicioAux.stream().forEach((ejercicioAux) -> {
                    selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
                });
            }

            return selectItems;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    /**
     * @author Gonzalez Facundo,
     * @see Obtiene lista de Recursos Propios segun servicio y cuenta bancaria
     * @return List Movimiento
     */
    public List<SelectItem> getSelectItemsRecursosPropios() {
        try {
            Comparator<RecursoPropio> compararClaseRec = Comparator
                    .comparing(x -> x.getClasificadorRecurso().getCodigo());
            Comparator<RecursoPropio> compararTipoRec = Comparator
                    .comparing(x -> x.getClasificadorRecurso().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararJur = Comparator.comparing(x -> x.getClasificadorRecurso()
                    .getClasificadorRecursoPropio().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararGenero = Comparator
                    .comparing(x -> x.getClasificadorRecurso().getClasificadorRecursoPropio()
                    .getClasificadorRecursoPropio().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararConcepto = Comparator.comparing(x -> Integer.valueOf(x.getConcepto()));
            List<SelectItem> selectItems = new ArrayList<>();
            List<RecursoPropio> lstRecursoAux = this.recursoPropioFacade
                    .findAll(this.getIdEjercicio(),
                            this.getServicio() != null ? this.getServicio().getId() : this.getIdServicio())
                    .stream()
                    .sorted(compararGenero.thenComparing(compararJur).thenComparing(compararTipoRec)
                            .thenComparing(compararClaseRec).thenComparing(compararConcepto))
                    .collect(Collectors.toList());
            if (!(lstRecursoAux.isEmpty())) {
                lstRecursoAux.stream().forEach((recursoAux) -> {
                    selectItems.add(new SelectItem(recursoAux.getId(),
                            String.valueOf(recursoAux.getOrganismo().getClasificadorOrganismo().getId().toString()
                                    + recursoAux.getOrganismo().getCodigoOrganismo() + recursoAux.getCaracter().getId()
                                    + "."
                                    + recursoAux.getClasificadorRecurso()
                                            .obtenerCodigoCompleto(recursoAux.getClasificadorRecurso())
                                    + "       " + recursoAux.getConcepto() + "       " + recursoAux.getDescripcion())));
                });
            }

            return selectItems;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Para actualizar el combo de servicio
     * @return selectItem
     */
    public List<SelectItem> getSelectItemsServicio() {
        try {
            List<SelectItem> selectItem = new ArrayList<>();
            List<Servicio> lstServicioAux = servicioFacade.findAll(this.getIdEjercicio());
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().forEach((servicioAux) -> {
                    List<CierreEjercicio> lstServicioCerrados = cierreEjercicioFacade.findAll(this.getIdEjercicio(),
                            servicioAux.getId());
                    if (lstServicioCerrados.isEmpty()) {
                        selectItem.add(new SelectItem(servicioAux.getId(),
                                servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
                    }
                });
            }
            return selectItem;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Para actualizar el combo de servicio en la búsqueda
     *
     * @return selectItem
     */
    public List<SelectItem> getSelectItemsServicioBsq() {
        try {
            List<SelectItem> selectItem = new ArrayList<>();
            List<Servicio> lstServicioAux = servicioFacade.findAll(this.getIdEjercicioBsq());
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().forEach((servicioAux) -> {
                    selectItem.add(new SelectItem(servicioAux.getId(),
                            servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
                });
            }
            return selectItem;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }

    }

    public List<SelectItem> getSelectItemsServiciosAlta() {
        return selectItemsServiciosAlta;
    }

    // alta
    public List<SelectItem> getSelectItemsTipoFondo() {
        return selectItemsTipoFondo;
    }

    public List<SelectItem> getSelectItemsTipoFondoBsq() {
        return selectItemsTipoFondoBsq;
    }

    // alta
    public List<SelectItem> getSelectItemsTipoImputacion() {
        return selectItemsTipoImputacion;
    }

    public List<SelectItem> getSelectItemsTipoImputacionBsq() {
        return selectItemsTipoImputacionBsq;
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Actualiza combo tipo imputación segun la combinación orden-fondo
     * busqueda
     */
    public List<SelectItem> getSelectItemsTipoOrdenGasto() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(true);
        List<TipoOrdenGasto> lstTipoOrdenGastoAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoOrdenGastoAux.add(funcionAux.getTipoOrdenGasto());
        });
        HashSet<TipoOrdenGasto> hashSet = new HashSet<>(lstTipoOrdenGastoAux);
        lstTipoOrdenGastoAux.clear();
        lstTipoOrdenGastoAux.addAll(hashSet);
        if (!(lstTipoOrdenGastoAux.isEmpty())) {
            lstTipoOrdenGastoAux.stream().forEach((tipoOrdenGastoAux) -> {
                selectItems.add(new SelectItem(tipoOrdenGastoAux.getId(),
                        tipoOrdenGastoAux.getId() + " - " + tipoOrdenGastoAux.getNombre()));
            });
        }

        return selectItems;
    }

    /**
     * author Alvarenga Angel,Gonzalez Facundo Actualiza combo tipo orden gasto
     * Búsqueda
     *
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsTipoOrdenGastoBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Funcion> lstFuncionAux = this.funcionFacade.findAll(true);
        List<TipoOrdenGasto> lstTipoOrdenGastoAux = new ArrayList<>();
        lstFuncionAux.stream().forEach((funcionAux) -> {
            lstTipoOrdenGastoAux.add(funcionAux.getTipoOrdenGasto());
        });
        HashSet<TipoOrdenGasto> hashSet = new HashSet<>(lstTipoOrdenGastoAux);
        lstTipoOrdenGastoAux.clear();
        lstTipoOrdenGastoAux.addAll(hashSet);
        if (!(lstTipoOrdenGastoAux.isEmpty())) {
            lstTipoOrdenGastoAux.stream().forEach((tipoOrdenGastoAux) -> {
                selectItems.add(new SelectItem(tipoOrdenGastoAux.getId(),
                        tipoOrdenGastoAux.getId() + " - " + tipoOrdenGastoAux.getNombre()));
            });
        }

        return selectItems;
    }

    public Servicio getServicio() {
        return servicio;
    }

    /**
     *
     * @author González Facundo
     * @see función JS donde solo se permiten números,backspace,coma,TAB y punto
     * decimal, en el evento de p:onkeydown donde tiene un código muy limitado y
     * no se pueden usar mayor ni menor , ni && ni ||.
     * @return String
     */
    public String getSoloNumeros() {
        return "if (event.keyCode !== 49) \n" + "               if (event.keyCode !== 50) \n"
                + "               if (event.keyCode !== 51)\n" + "               if (event.keyCode !== 52)\n"
                + "               if (event.keyCode !== 53)\n" + "               if (event.keyCode !== 54) \n"
                + "               if (event.keyCode !== 55) \n" + "               if (event.keyCode !== 56)\n"
                + "               if (event.keyCode !== 57)\n" + "               if (event.keyCode !== 58)\n"
                + "               if (event.keyCode !== 59) \n" + "               if (event.keyCode !== 48)\n"
                + "               if (event.keyCode !== 96) \n" + "               if (event.keyCode !== 97) \n"
                + "               if (event.keyCode !== 98)\n" + "               if (event.keyCode !== 99)\n"
                + "               if (event.keyCode !== 100)\n" + "               if (event.keyCode !== 101) \n"
                + "               if (event.keyCode !== 102) \n" + "               if (event.keyCode !== 103)\n"
                + "               if (event.keyCode !== 104)\n" + "               if (event.keyCode !== 105)\n"
                + "               if (event.keyCode !==  188)\n" + "               if (event.keyCode !==  190)\n"
                + "               if (event.keyCode !== 8) \n" + "               if (event.keyCode !== 110) \n"
                + "               if (event.keyCode !== 9) \n" + "               return false;";
    }

    public int getTotalTuplas() {
        return totalTuplas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public void guardarBorrado() {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @author Gonzalez Facundo
     * @see Guarda movimiento revertido.
     *
     */
    public String guardarReversion() {
        try {
            this.setServicio(this.servicioFacade.find(
                    this.getIdServicio() == null ? this.getUsuario().getServicio().getId() : this.getIdServicio()));
            this.getUsuario().setServicio(
                    this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio());
            Movimiento movimientoAnterior = movimientoFacade.find(this.getId());
            BigDecimal bdImporte = new BigDecimal(this.getImporte().toString());
            this.movimientoFacade.create(this.getMovimiento().getEjercicio().getId(),
                    this.getMovimiento().getCuentaBancaria().getId(), this.getIdTipoOrdenGasto(), this.getIdTipoFondo(),
                    this.getIdTipoImputacion(), this.getNumeroOrden(), this.getNumeroPedidoFondo(),
                    this.getNumeroEntregaFondo(), this.getCodigo(), this.getNumero(), this.getAnio(),
                    this.getNumeroComprobante(), this.getFechaComprobante(), this.getDescripcion(), bdImporte,
                    this.getUsuario(), this.getIdRecursoPropio(), true);
            // INICIO AUDITORIA//
            RecursoPropio recursoAux = this.getMovimiento().getRecursoPropio();
            this.setAuditoriaActual(this.devolverStringAuditoria(
                    this.getMovimiento().getEjercicio().getAnio().toString(),
                    this.getMovimiento().getCuentaBancaria().getServicio().getCodigo(),
                    this.getMovimiento().getCuentaBancaria().getServicio().getAbreviatura(),
                    this.getMovimiento().getCuentaBancaria().getNumero().toString(),
                    this.getMovimiento().getCuentaBancaria().getDescripcion(), this.getIdTipoOrdenGasto().toString(),
                    this.getIdTipoFondo().toString(), this.getIdTipoImputacion().toString(),
                    this.getNumeroOrden().toString(), this.getNumeroPedidoFondo().toString(),
                    this.getNumeroEntregaFondo().toString(), this.getCodigo().toString(), this.getNumero().toString(),
                    this.getAnio().toString(), this.getNumeroComprobante().toString(), this.getFechaComprobante(),
                    this.getDescripcion(), bdImporte, recursoAux));

            this.auditoriaFacade.create(AccionEnum.REVERSION_MOVIMIENTO.getName(), new Date(),
                    this.getIdUsuarioResponsable(), this.getAuditoriaAnterior(), this.getAuditoriaActual(),
                    this.getMovimiento().getEjercicio().getId());
            this.limpiarAuditorias();
            // FIN AUDITORIA//
            movimientoAnterior.setRevertido(true);
            this.movimientoFacade.edit(movimientoAnterior);
            this.setFlag(true);
            this.setBusqueda(true);
            this.limpiar();
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorMovimiento");
            this.setMsgSuccessError("El movimiento de valor opuesto ha sido generado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMovimiento");
            log.error(ex.getMessage());
        }
        return this.getResultado();
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setIdUsuarioResponsable(sessionBean.getUsuario().getId());
                this.setUsuario(sessionBean.getUsuario());
                this.setServicio(sessionBean.getServicio());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoMovimiento")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarMovimiento")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("revertirMovimiento")) {
                            this.setRevertirMovimiento(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleMovimiento")))
                            .forEach((_item) -> {
                                this.setDetalle(true);
                            });
                }
            } catch (Exception e) {

            }
        }
        this.actualizarTipoFondoBsq();
        this.actualizarTipoImputacionBsq();
    }

    public boolean isDocumentoPresentado() {
        return documentoPresentado;
    }

    public boolean isLazy() {
        return lazy;
    }

    public boolean isMostrarReporte() {
        return mostrarReporte;
    }

    public boolean isRevertirMovimiento() {
        return revertirMovimiento;
    }

    public boolean isServicioUsuario() {
        Usuario user = this.usuarioFacade.find(this.getSessionBean().getUsuario().getId());
        return user.getServicio() == null;
    }

    /**
     * @author Gonzalez Facundo
     * @see Limpiar parametros de la clase
     *
     */
    @Override
    public void limpiar() {
        this.setImpDesde(null);
        this.setImpHasta(null);
        this.setIdEjercicio(null);
        this.setIdCuentaBancaria(null);
        this.setIdTipoOrdenGasto(null);
        this.setIdTipoFondo(null);
        this.setIdTipoImputacion(null);
        this.setIdTipoOrdenGastoBsq(null);
        this.setIdTipoFondoBsq(null);
        this.setIdTipoImputacionBsq(null);
        this.setNumeroOrden(null);
        this.setNumeroPedidoFondo(null);
        this.setNumeroEntregaFondo(null);
        this.setCodigo(null);
        this.setNumero(null);
        this.setAnio(null);
        this.setSaldoCuentaBancaria(null);
        this.setNumeroComprobante(null);
        this.setFechaComprobante(null);
        this.setDescripcion(null);
        this.setImporte(null);
        this.setDocumentoPresentado(false);
        this.setIdRecursoPropio(0L);
        this.setIdServicio(null);
        this.setNumeroOrdenBsq(null);
        this.setNumeroEntregaFondoBsq(null);
        this.setNumeroPedidoFondoBsq(null);
        this.setFlagMovimiento(true);
        if (this.usuarioFacade.find(this.getUsuario().getId()).getServicio() == null) { // Vuelvo a recuperar el usuario
            // original para saber si tiene
            // o no servicio
            this.getUsuario().setServicio(null);
            this.setServicio(null);
        }
        this.setIdEjercicio(this.ejercicioFacade.ejercicioActual());
        this.limpiarAuditorias();
        this.setBusqueda(true);
        this.setControlAlta(true);
        this.actualizarTipoFondoBsq();
        this.actualizarTipoImputacionBsq();
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Cuando se elige la combinación 9-2-9 se renderiza la opcion de
     * recursos propios al movimiento Sector esta formado por la concatenación
     * de : clasif_inst || cod_org || caracter
     *
     * @param e
     * @return List<SelecItems>
     */
    public void limpiarComboClasificadorRecursoPropio(ValueChangeEvent e) {
        Object value = e.getNewValue();
        this.setIdRecursoPropio((Long) value);

    }

    public void limpiarParaGuardarYCrearOtro() {
        this.setImporte(null);
        this.setIdRecursoPropio(0L);
        if (this.usuarioFacade.find(this.getUsuario().getId()).getServicio() == null) { // Vuelvo a recuperar el usuario
            // original para saber si tiene
            // o no servicio
            this.getUsuario().setServicio(null);
            this.setServicio(null);
        }
    }

    /**
     *
     * @author González Facundo
     * @see Cuando orden de gasto es 9, es recurso propio, y se renderiza el
     * componente para cargarlo.
     * @param event
     */
    public void ordenChange(ValueChangeEvent event) {
        Object value = event.getNewValue();
        Long valor = (Long) value;
        if (value != null) {
            if ((valor.equals(ID_TIPO_ORDEN_GASTO))) {
                this.setIdTipoOrdenGasto(ID_TIPO_ORDEN_GASTO);
                this.setIdTipoFondo(ID_TIPO_FONDO);
                this.setIdTipoImputacion(ID_TIPO_ORDEN_GASTO);
            }
        }
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @author Gonzalez Facundo
     * @see Obtiene los datos para revertir movimiento.
     *
     */
    public void prepararParaRevertir() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idMovimientoAux = Long.parseLong(myRequest.getParameter("id"));
        this.setMovimiento(this.movimientoFacade.find(idMovimientoAux));
        this.setId(idMovimientoAux);
        this.setIdServicio(this.getMovimiento().getCuentaBancaria().getServicio().getId());
        this.setIdTipoOrdenGasto(this.getMovimiento().getFunciones().get(0).getTipoOrdenGasto().getId());
        this.setIdTipoFondo(this.getMovimiento().getFunciones().get(0).getTipoFondo().getId());
        this.actualizarTipoImputacion();
        this.setIdTipoImputacion(this.getMovimiento().getFunciones().get(0).getTipoImputacion().getId());
        this.setNumeroOrden(this.getMovimiento().getNumeroOrden());
        this.setNumeroPedidoFondo(this.getMovimiento().getNumeroPedidoFondo());
        this.setNumeroEntregaFondo(this.getMovimiento().getNumeroEntregaFondo());
        this.setCodigo(this.getMovimiento().getExpediente().getCodigo());
        this.setNumero(this.getMovimiento().getExpediente().getNumero());
        this.setAnio(this.getMovimiento().getExpediente().getAnio());
        this.setNumeroComprobante(this.getMovimiento().getNumeroComprobante());
        this.setFechaComprobante(this.getMovimiento().getFechaComprobante());
        this.setDescripcion(txtReversion + this.getMovimiento().getDescripcion());

        if (this.getMovimiento().getRecursoPropio() != null) {
            this.setIdRecursoPropio(this.getMovimiento().getRecursoPropio().getId());
        }
        if (this.getDescripcion().length() > 255) {
            this.setDescripcion(this.getDescripcion().substring(0, 255));
        }

        this.setImporte(this.getMovimiento().getImporte().multiply(BigDecimal.ONE.negate()));
        this.setImporteReversion(String.format("%.2f", this.getImporte()));// lo uso solo para mostrar el importe en la
        // reversión
        // INICIO AUDITORIA
        RecursoPropio recursoAux = this.getMovimiento().getRecursoPropio();
        this.setAuditoriaAnterior(this.devolverStringAuditoria(this.getMovimiento().getEjercicio().getAnio().toString(),
                this.getMovimiento().getCuentaBancaria().getServicio().getCodigo(),
                this.getMovimiento().getCuentaBancaria().getServicio().getAbreviatura(),
                this.getMovimiento().getCuentaBancaria().getNumero().toString(),
                this.getMovimiento().getCuentaBancaria().getDescripcion(), this.getIdTipoOrdenGasto().toString(),
                this.getIdTipoFondo().toString(), this.getIdTipoImputacion().toString(),
                this.getNumeroOrden().toString(), this.getNumeroPedidoFondo().toString(),
                this.getNumeroEntregaFondo().toString(), this.getCodigo().toString(), this.getNumero().toString(),
                this.getAnio().toString(), this.getNumeroComprobante().toString(), this.getFechaComprobante(),
                this.getMovimiento().getDescripcion(), this.getMovimiento().getImporte(), recursoAux));
        // FIN AUDITORIA

    }

    public void prepararReportes() throws Exception {
        try {

            if (movimientoFacade.countAll(idEjercicioBsq != null ? idEjercicioBsq : ejercicioFacade.ejercicioActual(),
                    servicio != null ? servicio.getId() : idServicioBsq, idCuentaBancariaBsq, descripcionBsq,
                    fechaAltaDesdeBsq, fechaAltaHastaBsq, fechaComprobDesdeBsq, fechaComprobHastaBsq, importeDesdeBsq,
                    importeHastaBsq, codigoBsq, numeroBsq, anioBsq, numeroOrdenBsq, numeroPedidoFondoBsq,
                    numeroEntregaFondoBsq, this.getIdTipoOrdenGastoBsq(), this.getIdTipoFondoBsq(),
                    this.getIdTipoImputacionBsq()).intValue() > 50000) {
                throw new Exception("El número de resultados excede el máximo permitido.");
            }
            StringBuilder query = new StringBuilder();
            query.append(" ");
            if (idEjercicioBsq != null && idEjercicioBsq != 0L) {
                query.append(" AND m.ejercicio_id =").append(idEjercicioBsq);
            }
            if (idServicioBsq != null && idServicioBsq != 0L) {
                query.append(" AND cb.servicio_id =").append(idServicioBsq);
            } else if (this.getSessionBean().getUsuario().getServicio() != null) {
                query.append(" AND cb.servicio_id =").append(this.getSessionBean().getUsuario().getServicio().getId());
            }
            if (idCuentaBancariaBsq != null && idCuentaBancariaBsq != 0L) {
                query.append(" AND cb.id =").append(idCuentaBancariaBsq);
            }
            if (descripcionBsq != null && !(descripcionBsq.equals(""))) {
                query.append(" AND unaccent(m.descripcion) LIKE '%")
                        .append(Utilidad.desacentuar(descripcionBsq.toUpperCase())).append("%'");
            }
            if (codigoBsq != null && codigoBsq != 0L) {
                query.append(" AND exp.codigo =").append(codigoBsq);
            }
            if (numeroBsq != null && numeroBsq != -1L) {
                query.append(" AND exp.numero =").append(numeroBsq);
            }
            if (anioBsq != null && anioBsq != 0L) {
                query.append(" AND exp.anio =").append(anioBsq);
            }
            if (importeDesdeBsq != null) {
                query.append("   AND m.importe >= ").append(importeDesdeBsq);
            }

            if (importeHastaBsq != null) {
                query.append("   AND m.importe <= ").append(importeHastaBsq);
            }

            if (fechaAltaDesdeBsq != null) {
                fechaAltaDesdeBsq.setHours(0);
                fechaAltaDesdeBsq.setMinutes(0);
                fechaAltaDesdeBsq.setSeconds(0);
                query.append(" AND m.fecha_alta  >= '").append(new java.sql.Timestamp(fechaAltaDesdeBsq.getTime()))
                        .append("' ");
            }
            if (fechaAltaHastaBsq != null) {
                fechaAltaHastaBsq.setHours(23);
                fechaAltaHastaBsq.setMinutes(59);
                fechaAltaHastaBsq.setSeconds(59);
                query.append(" AND m.fecha_alta  <= '").append(new java.sql.Timestamp(fechaAltaHastaBsq.getTime()))
                        .append("' ");
            }
            if (fechaComprobDesdeBsq != null) {
                fechaComprobDesdeBsq.setHours(0);
                fechaComprobDesdeBsq.setMinutes(0);
                fechaComprobDesdeBsq.setSeconds(0);
                query.append(" AND m.fecha_comprobante >= '")
                        .append(new java.sql.Timestamp(fechaComprobDesdeBsq.getTime())).append("' ");
            }
            if (fechaComprobHastaBsq != null) {
                fechaComprobHastaBsq.setHours(23);
                fechaComprobHastaBsq.setMinutes(59);
                fechaComprobHastaBsq.setSeconds(59);
                query.append(" AND m.fecha_comprobante <= '")
                        .append(new java.sql.Timestamp(fechaComprobHastaBsq.getTime())).append("' ");
            }

            if (this.getIdTipoOrdenGastoBsq() != null && getIdTipoOrdenGastoBsq() != 0L) {
                query.append(" and f.tipoordenGasto_id =").append(getIdTipoOrdenGastoBsq());
            }
            if (this.getIdTipoFondoBsq() != null && this.getIdTipoFondoBsq() != 0L) {
                query.append(" and f.tipofondo_id =").append(this.getIdTipoFondoBsq());
            }
            if (this.getIdTipoImputacionBsq() != null && this.getIdTipoImputacionBsq() != 0L) {
                query.append(" and f.tipoimputacion_id =").append(this.getIdTipoImputacionBsq());
            }

            if (numeroOrdenBsq != null) {
                query.append(" AND m.numero_orden = ").append(numeroOrdenBsq);
            }

            if (numeroPedidoFondoBsq != null) {
                query.append(" AND m.numero_pedido_fondo = ").append(numeroPedidoFondoBsq);
            }

            if (numeroEntregaFondoBsq != null) {
                query.append(" AND m.numero_entrega_fondo = ").append(numeroEntregaFondoBsq);
            }

            query.append(" ORDER BY m.ejercicio_id DESC,cb.servicio_id ASC,m.numero_asiento DESC");

            this.setMostrarReporte(true);
            this.setQuery(query.toString());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setMsgSuccessError("El listado de movimientos ha sido generado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setMostrarReporte(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMovimiento");
            log.error(ex.getMessage());
        }
    }

    /**
     * author Gonzalez Facundo
     *
     * @return boolean
     * @see Controla los usuarios sin servicio.
     *
     */
    public boolean servicioUsuario() {
        Usuario user = this.usuarioFacade.find(this.getSessionBean().getUsuario().getId());
        return user.getServicio() == null;

    }

    public void setAnio(Long anio) {
        this.anio = anio;
    }

    public void setAnioBsq(Long anioBsq) {
        this.anioBsq = anioBsq;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setCodigoBsq(Long codigoBsq) {
        this.codigoBsq = codigoBsq;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDescripcionBsq(String descripcionBsq) {
        this.descripcionBsq = descripcionBsq;
    }

    public void setDocumentoPresentado(boolean documentoPresentado) {
        this.documentoPresentado = documentoPresentado;
    }

    public void setExtractoComprobante(String extractoComprobante) {
        this.extractoComprobante = extractoComprobante;
    }

    public void setFechaAltaDesdeBsq(Date fechaAltaDesdeBsq) {
        this.fechaAltaDesdeBsq = fechaAltaDesdeBsq;
    }

    public void setFechaAltaHastaBsq(Date fechaAltaHastaBsq) {
        this.fechaAltaHastaBsq = fechaAltaHastaBsq;
    }

    public void setFechaComprobante(Date fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public void setFechaComprobDesdeBsq(Date fechaComprobDesdeBsq) {
        this.fechaComprobDesdeBsq = fechaComprobDesdeBsq;
    }

    public void setFechaComprobHastaBsq(Date fechaComprobHastaBsq) {
        this.fechaComprobHastaBsq = fechaComprobHastaBsq;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setFuncionAux(List<Funcion> funcionAux) {
        this.funcionAux = funcionAux;
    }

    public void setIdCodigoOrganismo(Long idCodigoOrganismo) {
        this.idCodigoOrganismo = idCodigoOrganismo;
    }

    public void setIdCuentaBancaria(Long idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public void setIdCuentaBancariaBsq(Long idCuentaBancariaBsq) {
        this.idCuentaBancariaBsq = idCuentaBancariaBsq;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public void setIdEjercicioBsq(Long idEjercicioBsq) {
        this.idEjercicioBsq = idEjercicioBsq;
    }

    public void setIdRecursoPropio(Long idRecursoPropio) {
        this.idRecursoPropio = idRecursoPropio;
    }

    public void setIdServicio(Long idServicio) {
        try {
            this.idServicio = idServicio;
        } catch (Exception ex) {
            log.error("setIdServicio(" + idServicio + ")" + ex.getMessage());
        }

    }

    public void setIdServicioBsq(Long idServicioBsq) {
        this.idServicioBsq = idServicioBsq;
    }

    public void setIdTipoFondo(Long idTipoFondo) {
        this.idTipoFondo = idTipoFondo;
    }

    public void setIdTipoFondoBsq(Long idTipoFondoBsq) {
        this.idTipoFondoBsq = idTipoFondoBsq;
    }

    public void setIdTipoImputacion(Long idTipoImputacion) {
        this.idTipoImputacion = idTipoImputacion;
    }

    public void setIdTipoImputacionBsq(Long idTipoImputacionBsq) {
        this.idTipoImputacionBsq = idTipoImputacionBsq;
    }

    public void setIdTipoOrdenGasto(Long idTipoOrdenGasto) {
        this.idTipoOrdenGasto = idTipoOrdenGasto;
    }

    public void setIdTipoOrdenGastoBsq(Long idTipoOrdenGastoBsq) {
        this.idTipoOrdenGastoBsq = idTipoOrdenGastoBsq;
    }

    public void setImpDesde(String impDesde) {
        this.impDesde = impDesde;

    }

    public void setImpHasta(String impHasta) {
        this.impHasta = impHasta;

    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setImporteDesdeBsq(BigDecimal importeDesdeBsq) {
        this.importeDesdeBsq = importeDesdeBsq;
    }

    public void setImporteHastaBsq(BigDecimal importeHastaBsq) {
        this.importeHastaBsq = importeHastaBsq;
    }

    public void setImporteReversion(String importeReversion) {
        this.importeReversion = importeReversion;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public void setLista(List<Movimiento> lista) {
        this.lista = lista;
    }

    public void setMostrarReporte(boolean mostrarReporte) {
        this.mostrarReporte = mostrarReporte;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public void setMovimientoAux(List<Movimiento> movimientoAux) {
        this.movimientoAux = movimientoAux;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public void setNumeroBsq(Long numeroBsq) {
        this.numeroBsq = numeroBsq;
    }

    public void setNumeroComprobante(Long numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public void setNumeroEntregaFondo(Long numeroEntregaFondo) {
        this.numeroEntregaFondo = numeroEntregaFondo;
    }

    public void setNumeroEntregaFondoBsq(Long numeroEntregaFondoBsq) {
        this.numeroEntregaFondoBsq = numeroEntregaFondoBsq;
    }

    public void setNumeroOrden(Long numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public void setNumeroOrdenBsq(Long numeroOrdenBsq) {
        this.numeroOrdenBsq = numeroOrdenBsq;
    }

    public void setNumeroPedidoFondo(Long numeroPedidoFondo) {
        this.numeroPedidoFondo = numeroPedidoFondo;
    }

    public void setNumeroPedidoFondoBsq(Long numeroPedidoFondoBsq) {
        this.numeroPedidoFondoBsq = numeroPedidoFondoBsq;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public void setRevertirMovimiento(boolean revertirMovimiento) {
        this.revertirMovimiento = revertirMovimiento;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setSaldoCuentaBancaria(Double saldoCuentaBancaria) {
        this.saldoCuentaBancaria = saldoCuentaBancaria;
    }

    public void setSelectItemsCuentaBsq(List<SelectItem> selectItemsCuentaBsq) {
        this.selectItemsCuentaBsq = selectItemsCuentaBsq;
    }

    public void setSelectItemsRecursosPropios(List<SelectItem> selectItemsRecursosPropios) {
        this.selectItemsRecursosPropios = selectItemsRecursosPropios;
    }

    public void setSelectItemsServiciosAlta(List<SelectItem> selectItemsServiciosAlta) {
        this.selectItemsServiciosAlta = selectItemsServiciosAlta;
    }

    public void setSelectItemsTipoFondo(List<SelectItem> selectItemsTipoFondo) {
        this.selectItemsTipoFondo = selectItemsTipoFondo;
    }

    public void setSelectItemsTipoFondoBsq(List<SelectItem> selectItemsTipoFondoBsq) {
        this.selectItemsTipoFondoBsq = selectItemsTipoFondoBsq;
    }

    public void setSelectItemsTipoImputacion(List<SelectItem> selectItemsTipoImputacion) {
        this.selectItemsTipoImputacion = selectItemsTipoImputacion;
    }

    public void setSelectItemsTipoImputacionBsq(List<SelectItem> selectItemsTipoImputacionBsq) {
        this.selectItemsTipoImputacionBsq = selectItemsTipoImputacionBsq;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public void setServicioUsuario(boolean servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    public void setTotalTuplas(int totalTuplas) {
        this.totalTuplas = totalTuplas;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * author Doroñuk Gustavo, Gonzalez Facundo
     *
     * @param context
     * @param validate
     * @param value
     * @throws java.lang.Exception
     * @see Valida que el año a ingresar en el nuevo movimiento no supere al año
     * actual
     *
     */
    public void validarAnioExp(final FacesContext context, final UIComponent validate, final Object value)
            throws Exception {
        if ((Long) value != null) {
            if ((Long) value > (new Date().getYear() + 1900)) {
                final FacesMessage msg = new FacesMessage("El año no debe superar al actual.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
            if ((Long) value < (1950)) {
                final FacesMessage msg = new FacesMessage("El año debe ser mayor a 1950.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    /**
     * author Gonzalez Facundo
     *
     * @param context
     * @param validate
     * @param value
     * @throws java.lang.Exception
     * @see Valida codigo de expediente exista. actual
     *
     */
    public void validarCodigoExp(final FacesContext context, final UIComponent validate, final Object value)
            throws Exception {
        List<CodigoExpediente> codigosAux = new ArrayList<>();
        codigosAux = this.servicioFacade
                .findAllServicio(this.getServicio() != null ? this.getServicio().getId() : this.getIdServicio(),
                        this.getIdEjercicio())
                .get(0).getLstCodigoExpediente();
        if ((Long) value != null) {
            if (!codigosAux.stream().anyMatch(x -> Long.valueOf(x.getCodigo()).equals(value))) {
                final FacesMessage msg = new FacesMessage(
                        "El código de expediente ingresado no está asociado al servicio.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
            this.setIdCodigoOrganismo((Long) value);
        }

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Valida que la fecha de comprobante no supere el dia actual
     *
     * @param context
     * @param validate
     * @param value
     */
    public void validarFechaComprobante(final FacesContext context, final UIComponent validate, final Object value)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha_desde = sdf.parse("2015-01-01");
        if ((Date) value != null) {
            Date fechaComprobante = (Date) value;
            if (fechaComprobante.after(new Date())) {
                final FacesMessage msg = new FacesMessage(
                        "La fecha del comprobante no debe superar a la fecha actual.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
            if (fechaComprobante.before(fecha_desde)) {
                final FacesMessage msg = new FacesMessage("La fecha del comprobante debe ser mayor a 01/01/2015.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }

        }
    }

    /**
     *
     * @author Matias Zakowicz
     * @param context
     * @param validate
     * @param value
     * @see Comprueba que el recurso propio seleccionado sea el correcto
     */
    public void validatorRecursoPropio(FacesContext context, UIComponent validate, Object value) {
        Long idRecursoPropioAux = (Long) value;
        if (idRecursoPropioAux.equals(0L)) {
            FacesMessage facesMessage = new FacesMessage((FacesMessage.Severity) FacesMessage.VALUES.get(2),
                    "Debe seleccionar un recurso propio.", "Debe seleccionar un recurso propio.");
            context.addMessage(validate.getClientId(context), facesMessage);
            throw new ValidatorException(facesMessage);
        }
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     * @see Se utiliza para ver detalle movimiento
     *
     */
    @Override
    public void verDetalle() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idMovimientoAux = Long.parseLong(myRequest.getParameter("id"));
            this.setMovimiento(this.movimientoFacade.find(idMovimientoAux));
            String clasificador = this.getMovimiento().getRecursoPropio().getClasificadorRecurso()
                    .obtenerCodigoCompleto(this.getMovimiento().getRecursoPropio().getClasificadorRecurso());
            Recurso recursoAux = new Recurso(
                    this.getMovimiento().getRecursoPropio().getServicio().getCodigo().concat(" - ")
                            .concat(this.getMovimiento().getRecursoPropio().getServicio().getDescripcion()), // servicio
                    this.getMovimiento().getRecursoPropio().getOrganismo().getClasificadorOrganismo().getId().toString()
                            .concat(" - ")
                            .concat(this.getMovimiento().getRecursoPropio().getOrganismo().getClasificadorOrganismo()
                                    .getNombre()), // cla_institucional
                    this.getMovimiento().getRecursoPropio().getOrganismo().getCodigoOrganismo().toString().concat(" - ")
                            .concat(this.getMovimiento().getRecursoPropio().getOrganismo().getNombre()), // codigo
                    // organismo
                    this.getMovimiento().getRecursoPropio().getCaracter().getId().toString().concat(" - ")
                            .concat(this.getMovimiento().getRecursoPropio().getCaracter().getNombre()), // caracter
                    String.valueOf(clasificador.replace(".", "").charAt(0)), // genero
                    String.valueOf(clasificador.replace(".", "").charAt(1)), // jurisdicción
                    String.valueOf(clasificador.replace(".", "").charAt(2)), // tipo_rec
                    String.valueOf(clasificador.replace(".", "").charAt(3)), // cla_recurso
                    String.valueOf(this.getMovimiento().getRecursoPropio().getConcepto()), // concepto
                    String.valueOf(this.getMovimiento().getRecursoPropio().getDescripcion()), // descripcion concepto
                    String.valueOf(this.getMovimiento().getRecursoPropio().getEjercicio().getAnio()), // ejercicio
                    this.getMovimiento().getRecursoPropio().getClasificadorRecurso()
                            .obtenerCodigoCompleto(this.getMovimiento().getRecursoPropio().getClasificadorRecurso())
                            .concat(" - ")
                            .concat(this.getMovimiento().getRecursoPropio().getClasificadorRecurso().getNombre()));

            this.setRecurso(recursoAux);
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            log.error(ex.getMessage());
        }
    }

}
