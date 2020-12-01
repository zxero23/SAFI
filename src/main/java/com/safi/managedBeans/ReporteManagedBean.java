package com.safi.managedBeans;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.log4j.Logger;
import org.icefaces.ace.model.table.LazyDataModel;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.Movimiento;
import com.safi.entity.Organismo;
import com.safi.entity.Servicio;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.CuentaBancariaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.OrganismoFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.utilidad.ReporteJava;
import com.safi.utilidad.SAFIReporteJava;
import com.safi.utilidad.Utilidad;
import com.safi.enums.AccionEnum;
import static com.safi.enums.GrupoUsuarioEnum.HTC;
import static com.safi.enums.GrupoUsuarioEnum.REPORTES;
import static com.safi.enums.GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO;
import static com.safi.enums.TipoServicioEnum.SERVICIO;
import com.safi.facade.MovimientoFacadeLocal;

/**
 *
 * @author Monchy
 * @author Diana Olivera
 */
@ManagedBean
@SessionScoped

public class ReporteManagedBean extends UtilManagedBean implements Serializable {

    private static Logger log = Logger.getLogger(ReporteManagedBean.class);

    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private OrganismoFacadeLocal organismoFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private MovimientoFacadeLocal movimientoFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;

    private JasperPrint jasperResultado = new JasperPrint();
    private Long idServicio = 0L;
    private Long idOrganismo = 0L;
    private Date fechaDesde = Calendar.getInstance().getTime();
    private Date fechaHasta = Calendar.getInstance().getTime();
    private Date fechaDesdeInicioAnio = new Date(new Date().getYear(), 0, 1, 0, 0, 0);
    private Long numeroCuenta;
    private Long idEjercicio = 0L;
    private Long idCuentaBancaria = 0L;
    private Long nroPedidoFondo;
    private int tiempo;
    private Date fecha = Calendar.getInstance().getTime();
    private Date fechaMaxima = Calendar.getInstance().getTime();
    private Integer anioMes = Calendar.getInstance().get(Calendar.YEAR) - 1;
    private Integer mes;
    private boolean diario = true;
    private boolean mostrarSoloUnServicio = false;
    private boolean perteneceHTC = false;
    private boolean mensual;
    private String reporte;
    private String ventanaVolver = "administracion";
    private List<SelectItem> selectItemsServicio;
    private List<SelectItem> selectItemsEjercicio;
    private List<SelectItem> SelectItemsCuentaBancaria;
    private String codigoServicio;
    private String abreviaturaServicio;
    private Boolean VerSoloLibroBancoExpedientes;
    private Boolean perteneceTipoServicioServicio;
    private Long idReporteLibroBancoOpcion = 1L;

    /**
     * Creates a new instance of RendicionMensualManagedBean
     */
    public ReporteManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setIdEjercicio(this.ejercicioFacade.ejercicioActual());
                if (!(this.getLstActionItems().isEmpty())) {
                    this.getLstActionItems().stream().filter((actionItemAux) -> (actionItemAux.getNombre().equalsIgnoreCase("mostrarSoloUnServicio"))).forEach((_item) -> {
                        this.setMostrarSoloUnServicio(true);
                    });
                }
                if (!(sessionBean.getUsuario().getGrupos().isEmpty())) {
                    sessionBean.getUsuario().getGrupos().stream().filter((grupoAux) -> (grupoAux.getId().equals(SERVICIO_ADMINISTRATIVO.getId())) || grupoAux.getId().equals(REPORTES.getId())).forEach((_item) -> {
                        if (sessionBean.getUsuario().getServicio().getTipoServicio().getId().equals(SERVICIO.getId())) {
                            this.setPerteneceTipoServicioServicio(true);
                        }
                        this.setVerSoloLibroBancoExpedientes(true);
                    });
                }
                if (!(sessionBean.getUsuario().getGrupos().isEmpty())) {
                    sessionBean.getUsuario().getGrupos().stream().filter((grupoAux) -> (grupoAux.getId().equals(HTC.getId()))).forEach((_item) -> {
                        this.parametrizarHTC();
                    });
                }
                this.setIdUsuarioResponsable(sessionBean.getUsuario().getId());
                if (this.isMostrarSoloUnServicio()) {
                    this.setCodigoServicio(this.getSessionBean().getUsuario().getServicio().getCodigo());
                    this.setAbreviaturaServicio(this.getSessionBean().getUsuario().getServicio().getAbreviatura());
                    this.setIdServicio(this.getSessionBean().getUsuario().getServicio().getId());
                    this.setCodigoServicio(this.servicioFacade.find(this.getIdServicio()).getCodigo());
                    this.setAbreviaturaServicio(this.servicioFacade.find(this.getIdServicio()).getAbreviatura());
                }
            } catch (Exception e) {
                log.fatal("Error en init(): " + e.getMessage());
            }
        }
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public String getAbreviaturaServicio() {
        return abreviaturaServicio;
    }

    public void setAbreviaturaServicio(String abreviaturaServicio) {
        this.abreviaturaServicio = abreviaturaServicio;
    }

    public JasperPrint getJasperResultado() {
        return jasperResultado;
    }

    public void setJasperResultado(JasperPrint jasperResultado) {
        this.jasperResultado = jasperResultado;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaDesdeInicioAnio() {
        return fechaDesdeInicioAnio;
    }

    public void setFechaDesdeInicioAnio(Date fechaDesdeInicioAnio) {
        this.fechaDesdeInicioAnio = fechaDesdeInicioAnio;
    }

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public Long getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public void setIdCuentaBancaria(Long idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public Long getNroPedidoFondo() {
        return nroPedidoFondo;
    }

    public void setNroPedidoFondo(Long nroPedidoFondo) {
        this.nroPedidoFondo = nroPedidoFondo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaMaxima() {
        return fechaMaxima;
    }

    public void setFechaMaxima(Date fechaMaxima) {
        this.fechaMaxima = fechaMaxima;
    }

    public Integer getAnioMes() {
        return anioMes;
    }

    public void setAnioMes(Integer anioMes) {
        this.anioMes = anioMes;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public boolean isDiario() {
        return diario;
    }

    public void setDiario(boolean diario) {
        this.diario = diario;
    }

    public boolean isMensual() {
        return mensual;
    }

    public void setMensual(boolean mensual) {
        this.mensual = mensual;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getVentanaVolver() {
        return ventanaVolver;
    }

    public void setVentanaVolver(String ventanaVolver) {
        this.ventanaVolver = ventanaVolver;
    }

    public void prepararReporte(ValueChangeEvent e) {
        Long idActividadAux = (Long) e.getNewValue();
        this.setIdServicio(idActividadAux);
    }

    public boolean isMostrarSoloUnServicio() {
        return mostrarSoloUnServicio;
    }

    public void setMostrarSoloUnServicio(boolean mostrarSoloUnServicio) {
        this.mostrarSoloUnServicio = mostrarSoloUnServicio;
    }

    public boolean isPerteneceHTC() {
        return perteneceHTC;
    }

    public void setPerteneceHTC(boolean perteneceHTC) {
        this.perteneceHTC = perteneceHTC;
    }

    public void parametrizarHTC() {
        this.setPerteneceHTC(true);
        //Para los reportes de Rango de Fechas
        this.getSelectItemsMeses();
    }

    public void definirFechaMaxima() {

    }

    public Boolean getVerSoloLibroBancoExpedientes() {
        return VerSoloLibroBancoExpedientes;
    }

    public void setVerSoloLibroBancoExpedientes(Boolean VerSoloLibroBancoExpedientes) {
        this.VerSoloLibroBancoExpedientes = VerSoloLibroBancoExpedientes;
    }

    public Boolean getPerteneceTipoServicioServicio() {
        return perteneceTipoServicioServicio;
    }

    public void setPerteneceTipoServicioServicio(Boolean perteneceTipoServicioServicio) {
        this.perteneceTipoServicioServicio = perteneceTipoServicioServicio;
    }

    public Long getIdReporteLibroBancoOpcion() {
        return idReporteLibroBancoOpcion;
    }

    public void setIdReporteLibroBancoOpcion(Long idReporteLibroBancoOpcion) {
        this.idReporteLibroBancoOpcion = idReporteLibroBancoOpcion;
    }

    public void getGenerarReporte() throws JRException, FileNotFoundException, IOException {
        try {
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());

            String servicioElegido;
            if (this.getIdServicio() != 0) {
                Servicio servicioAux = this.servicioFacade.find(this.getIdServicio());
                servicioElegido = servicioAux.getCodigoAbreviatura();
            } else {
                servicioElegido = "TODOS";
            }

            String organismoElegido;
            if (this.getIdOrganismo() != 0) {
                Organismo organismoAux = this.organismoFacade.find(this.getIdOrganismo());
                organismoElegido = organismoAux.getCodigoOrganismo() + " - " + organismoAux.getNombre();
            } else {
                organismoElegido = "TODOS";
            }

            String cuentaElegida;
            if (this.getIdCuentaBancaria() != 0) {
                CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(this.getIdCuentaBancaria());
                cuentaElegida = cuentaBancariaAux.getNumero() + " - " + cuentaBancariaAux.getDescripcion();
            } else {
                cuentaElegida = "TODAS";
            }

            this.setAuditoriaActual(
                    "Reporte: " + this.getReporte() + "\n"
                    + "Ejercicio: " + ejercicioAux.getAnio() + "\n"
                    + "Servicio: " + servicioElegido + "\n");

            switch (this.getReporte()) {
                case "ejecucionRecursosDiario":
                    this.setJasperResultado(new ReporteJava().reporteEjecucionRecursosDiario(
                            this.getReporte(),
                            this.getIdEjercicio(),
                            this.getIdServicio(),
                            this.getIdOrganismo(),
                            this.getFechaDesde()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Organismo: " + organismoElegido + "\n"
                            + "Fecha: " + formato.format(this.getFechaDesde()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "ejecucionRecursosMensual":
                case "ejecucionRecursosPeriodo":
                    this.setJasperResultado(new ReporteJava().reporteEjecucionRecursosPeriodo(
                            this.getReporte(),
                            this.getIdEjercicio(),
                            this.getIdServicio(),
                            this.getIdOrganismo(),
                            this.getFechaDesdeInicioAnio(),
                            this.getFechaDesde(),
                            this.getFechaHasta()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Organismo: " + organismoElegido + "\n"
                            + "Fecha Desde: " + formato.format(this.getFechaDesde()) + "\n"
                            + "Fecha Hasta: " + formato.format(this.getFechaHasta()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "modificacionRecursos":
                    this.setJasperResultado(new ReporteJava().reporteModificacionRecursos(
                            this.getReporte(),
                            this.getIdServicio(),
                            this.getIdEjercicio(),
                            this.getFechaDesde(),
                            this.getFechaHasta(),
                            this.getIdOrganismo()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Organismo: " + organismoElegido + "\n"
                            + "Fecha Desde: " + formato.format(this.getFechaDesde()) + "\n"
                            + "Fecha Hasta: " + formato.format(this.getFechaHasta()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "movimientosRecursos":
                    this.setJasperResultado(new ReporteJava().reporteMovimientosRecursos(
                            this.getReporte(),
                            this.getIdEjercicio(),
                            this.getIdServicio(),
                            this.getFechaDesdeInicioAnio(),
                            this.getFechaHasta()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Fecha Desde: " + formato.format(this.getFechaDesdeInicioAnio()) + "\n"
                            + "Fecha Hasta: " + formato.format(this.getFechaHasta()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "cargosTesoreria":
                case "reintegrosTesoreria":
                case "rendiciones":
                case "movimiento_bancario":
                    this.setJasperResultado(new ReporteJava().reporteBase(
                            this.getReporte(),
                            this.getIdServicio(),
                            this.getIdEjercicio(),
                            this.getIdCuentaBancaria(),
                            this.getFechaDesde(),
                            this.getFechaHasta()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Cuenta Bancaria: " + cuentaElegida + "\n"
                            + "Fecha Desde: " + formato.format(this.getFechaDesde()) + "\n"
                            + "Fecha Hasta: " + formato.format(this.getFechaHasta()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "documentosPendientesRendicion":
                case "parteDiario":
                    this.setJasperResultado(new ReporteJava().reporteFecha(
                            this.getReporte(),
                            this.getIdServicio(),
                            this.getIdEjercicio(),
                            this.getIdCuentaBancaria(),
                            this.getFecha()));
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Cuenta Bancaria: " + cuentaElegida + "\n"
                            + "Fecha: " + formato.format(this.getFecha()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
                case "libroBanco":
                case "libroBancoExpediente":
                    this.setJasperResultado(new ReporteJava().reporteLibroBanco(
                            this.getReporte(),
                            this.getIdServicio(),
                            this.getIdEjercicio(),
                            this.getIdCuentaBancaria(),
                            this.getFechaDesdeInicioAnio(),
                            this.getFechaHasta(),
                            this.getNroPedidoFondo()));
                    String pedidoFondo = this.getNroPedidoFondo() != null ? this.getNroPedidoFondo().toString() : "TODOS";
                    this.setAuditoriaActual(this.getAuditoriaActual()
                            + "Cuenta Bancaria: " + cuentaElegida + "\n"
                            + "Pedido de Fondo: " + pedidoFondo + "\n"
                            + "Fecha Hasta: " + formato.format(this.getFechaHasta()) + "\n"
                            + "ID Usuario: " + this.getSessionBean().getUsuario().getId());
                    break;
            }
            //INICIO AUDITORÍA
            this.auditoriaFacade.create(AccionEnum.GENERACION_REPORTE.getName(), new Date(), this.getSessionBean().getUsuario().getId(), "-", this.getAuditoriaActual(), this.getIdEjercicio());
            this.limpiarAuditorias();
            //FIN AUDITORÍA       
        } catch (Exception e) {
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            System.out.println(e.getMessage());
            log.error("Error en getGenerarReporte(): " + e.getMessage());
        }
    }

    public String definirRegreso(boolean limpiarFiltro) {
        switch (this.getReporte()) {
            case "cargosTesoreria":
                this.setVentanaVolver("reporteCargosTesoreriaConf");
                break;
            case "documentosPendientesRendicion":
                this.setVentanaVolver("reporteDocumentosPendientesRendicionConf");
                break;
            case "ejecucionRecursosDiario":
            case "ejecucionRecursosMensual":
            case "ejecucionRecursosPeriodo":
                this.setVentanaVolver("reporteEjecucionRecursosConf");
                break;
            case "historialModificacionesRecursos":
                this.setVentanaVolver("reporteHistorialModificacionesRecursosConf");
                break;
            case "libroBanco":
                this.setVentanaVolver("reporteLibroBancoConf");
                break;
            case "modificacionRecursos":
                this.setVentanaVolver("reporteModificacionRecursosConf");
                break;
            case "movimiento_bancario":
                this.setVentanaVolver("reporteMovimientoBancarioConf");
                break;
            case "parteDiario":
                this.setVentanaVolver("reporteParteDiarioConf");
                break;
            case "reintegrosTesoreria":
                this.setVentanaVolver("reporteReintegrosTesoreriaConf");
                break;
            case "rendiciones":
                this.setVentanaVolver("reporteRendicionesConf");
                break;
        }
        if (limpiarFiltro) {
            this.limpiar();
        }

        return this.getVentanaVolver();
    }

    public Resource getGenerarReportePDF() throws JRException, FileNotFoundException, IOException {
        SAFIReporteJava miRecurso;
        byte[] bites = JasperExportManager.exportReportToPdf(this.getJasperResultado());
        miRecurso = new SAFIReporteJava(bites);
        return miRecurso;
    }

    public Resource getGenerarReporteXLS() throws Exception {
        SAFIReporteJava miRecurso = null;
        ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
        JasperPrint jasperGenerado = this.getJasperResultado();
        jasperGenerado.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
        jasperGenerado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.1", "pageHeader");
        jasperGenerado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.band.2", "pageFooter");
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperGenerado));
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

    @Override
    public void limpiar() {
        WebManagedBean sessionBean = this.getSessionBean();
        this.setIdServicio(sessionBean.getUsuario().getServicio() != null ? sessionBean.getUsuario().getServicio().getId() : 0L);
        this.setIdEjercicio(this.ejercicioFacade.ejercicioActual());
        this.setIdCuentaBancaria(0L);
        this.setNroPedidoFondo(null);
        this.setFechaDesde(Calendar.getInstance().getTime());
        this.setFechaHasta(Calendar.getInstance().getTime());
        this.setFechaDesdeInicioAnio(new Date(new Date().getYear(), 0, 1, 0, 0, 0));
        this.setNumeroCuenta(null);
        this.setTiempo(1);
        this.setFecha(Calendar.getInstance().getTime());
        this.setAnioMes(Calendar.getInstance().get(Calendar.YEAR) - 1);
        this.setMes(1);
        this.setDiario(true);
        this.setMensual(false);
        this.setFecha(Calendar.getInstance().getTime());
        this.setReporte("");
    }

    public void renderizarTiempo(ValueChangeEvent event) {
        this.setTiempo(Integer.parseInt(event.getNewValue().toString()));
        switch (this.getTiempo()) {

            case 1:
                this.setDiario(true);
                this.setMensual(false);
                break;
            case 2:
                this.setDiario(false);
                this.setMensual(true);
                break;
            default:
                this.setDiario(true);
                this.setMensual(false);
                break;
        }
    }

    public Integer getAnioActual() {
        Integer anioActual = Calendar.getInstance().get(Calendar.YEAR);
        return anioActual;
    }

    public void definirMeses() {
        Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
        this.setAnioMes(ejercicioAux != null ? ejercicioAux.getAnio() : new GregorianCalendar().get(Calendar.YEAR));
        this.getSelectItemsMeses();

    }

    public void actualizarCuentasBancarias(ValueChangeEvent event) {
        this.getSelectItemsCuentaBancaria();
    }

    public List<SelectItem> getSelectItemsMeses() {
        List<SelectItem> mesesAux = new ArrayList();
        Calendar c = new GregorianCalendar();

        Integer anioActual = c.get(Calendar.YEAR);
        Integer mesBase = c.get(Calendar.MONTH);

        if (c.get(Calendar.DAY_OF_MONTH) < 17) {
            mesBase--;
        }

        Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
        if (ejercicioAux.getAnio().equals(anioActual)) {
            for (Integer i = 0; i <= (mesBase - 1); i++) {
                mesesAux.add(new SelectItem(i.longValue() + 1, this.getNombreMes(i)));
            }

            this.setMes(mesBase);
        } else {
            for (Integer i = 0; i < 12; i++) {
                mesesAux.add(new SelectItem(i.longValue() + 1, this.getNombreMes(i)));
            }
            this.setMes(1);
        }

        return mesesAux;
    }

    public String getNombreMes(int m) {
        Calendar auxFecha = Calendar.getInstance();
        auxFecha.set(Calendar.DAY_OF_MONTH, 1);
        auxFecha.set(Calendar.MONTH, m);
        String nombre = new SimpleDateFormat("MMMM").format(auxFecha.getTime());
        String primeraLetra = nombre.substring(0, 1);
        String mayuscula = primeraLetra.toUpperCase();
        String demasLetras = nombre.substring(1, nombre.length());
        nombre = mayuscula + demasLetras;
        return nombre;
    }

    public void validarAnioRegPub(final FacesContext context, final UIComponent validate, final Object value) {
        String a = (String) value;
        int anioAux = Integer.parseInt(a);
        Date hoy = new Date();
        int anio = hoy.getYear() + 1900;
        if (anioAux > anio) {
            final FacesMessage msg = new FacesMessage("El año no es válido.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public void validarRangoAnioMes(FacesContext context, UIComponent validate, Object value) {
        if ((Integer) value > this.getAnioActual()) {
            FacesMessage msg = new FacesMessage("El año tiene que ser menor o igual al actual: " + this.getAnioActual() + ".");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public String verificarReporteEjecucionRecursos() {
        try {
            if (this.getFechaDesde().equals(this.getFechaHasta())) {
                this.setReporte("ejecucionRecursosDiario");
            } else {
                this.setFechaDesdeInicioAnio(this.actualizarInicioAnio(this.getIdEjercicio()));
                this.setReporte("ejecucionRecursosPeriodo");
            }
            if (this.isPerteneceHTC()) {
                this.definirTiempo();
            } else {
                this.setFechaHasta(this.definirHoraHasta(this.getFechaHasta()));
                this.setFecha(this.getFechaHasta());
            }
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteEjecucionRecursos(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public String verificarReporteModificacionRecursos() {
        try {
            if (this.isPerteneceHTC()) {
                this.definirTiempo();
            } else {
                this.setFechaHasta(this.definirHoraHasta(this.getFechaHasta()));
                this.setFecha(this.getFechaHasta());
            }
            this.setReporte("modificacionRecursos");
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteModificacionRecursos(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public String verificarReporteRango(String nombreReporte) {
        try {
            if (this.isPerteneceHTC()) {
                this.definirTiempo();
            } else {
                this.setFechaHasta(this.definirHoraHasta(this.getFechaHasta()));
                this.setFecha(this.getFechaHasta());
            }
            this.setReporte(nombreReporte);
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteRango(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public Date definirHoraHasta(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public String verificarReporteLibroBanco() {
        try {
            if (this.isPerteneceHTC()) {
                this.definirTiempo();
            } else {
                this.setFechaHasta(this.definirHoraHasta(this.getFechaHasta()));
                this.setFecha(this.getFechaHasta());
            }

            if (this.getIdReporteLibroBancoOpcion() == 0) {
                this.setReporte("libroBancoExpediente");
            } else {
                this.setReporte("libroBanco");
            }
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteRango(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public String verificarReporteMovimientoBancario() {
        try {
            if (this.isPerteneceHTC()) {
                this.definirTiempo();
            } else {
                this.setFechaHasta(this.definirHoraHasta(this.getFechaHasta()));
                this.setFecha(this.getFechaHasta());
            }
            this.setReporte("movimiento_bancario");
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteMovimientoBancario(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public String verificarReporteParteDiario() {
        try {
            this.setReporte("parteDiario");
            this.getGenerarReporte();
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorReporte");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
            this.setReporte(null);
            this.setResultado("successErrorReporte");
            log.error("Error en verificarReporteParteDiario(): " + e.getMessage());
        }
        return this.getResultado();
    }

    public void actualizarFechaHasta(ValueChangeEvent event) {
        Object value = event.getNewValue();
        Calendar hoy = Calendar.getInstance();
        Ejercicio ejercicioAux = this.ejercicioFacade.find(Long.parseLong(value.toString()));
        hoy.set(Calendar.YEAR, ejercicioAux.getAnio());
        this.setFechaHasta(hoy.getTime());
    }

    public void actualizarInicioAnio(ValueChangeEvent event) {
        Object value = event.getNewValue();
        this.setFechaDesde(this.actualizarInicioAnio(Long.parseLong(value.toString())));
        this.setFechaDesdeInicioAnio(this.actualizarInicioAnio(Long.parseLong(value.toString())));
    }

    public Date actualizarInicioAnio(Long idEjercicio) {
        Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.YEAR, ejercicioAux.getAnio());
        actual.set(Calendar.DAY_OF_YEAR, 1);
        actual.set(Calendar.HOUR, 0);
        actual.set(Calendar.MINUTE, 0);
        actual.set(Calendar.SECOND, 0);
        actual.set(Calendar.MILLISECOND, 0);
        actual.set(Calendar.YEAR, ejercicioAux.getAnio());
        return (actual.getTime());
    }

    public void definirTiempo() {
        switch (this.getMes()) {
            case 12: { //Para Diciembre Desde 01/12 al 10/01
                Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());

                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.MONTH, this.getMes() - 1);
                cale.set(Calendar.YEAR, ejercicioAux.getAnio());
                cale.set(Calendar.DAY_OF_MONTH, 1);
                cale.set(Calendar.HOUR, 0);
                cale.set(Calendar.MINUTE, 0);
                cale.set(Calendar.SECOND, 0);
                cale.set(Calendar.MILLISECOND, 0);
                cale.getTime();
                Timestamp desde = new Timestamp(cale.getTime().getTime());
                this.setFechaDesde(desde);

                Calendar cal = Calendar.getInstance();
                cal.setTime(desde);
                cal.set(Calendar.DAY_OF_MONTH, 10);
                cale.set(Calendar.MONTH, 0);
                cale.set(Calendar.YEAR, ejercicioAux.getAnio() + 1);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                Timestamp hasta = new Timestamp(cal.getTime().getTime());
                this.setFechaHasta(hasta);
                this.setFecha(this.getFechaHasta());
                break;
            }
            default: {//Para cualquier mes que NO SEA diciembre
                Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());

                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.MONTH, this.getMes() - 1);
                cale.set(Calendar.YEAR, ejercicioAux.getAnio());
                cale.set(Calendar.DAY_OF_MONTH, 1);
                cale.set(Calendar.HOUR, 0);
                cale.set(Calendar.MINUTE, 0);
                cale.set(Calendar.SECOND, 0);
                cale.set(Calendar.MILLISECOND, 0);
                cale.getTime();
                Timestamp desde = new Timestamp(cale.getTime().getTime());
                this.setFechaDesde(desde);

                Calendar cal = Calendar.getInstance();
                cal.setTime(desde);
                cal.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                Timestamp hasta = new Timestamp(cal.getTime().getTime());
                this.setFechaHasta(hasta);
                this.setFecha(this.getFechaHasta());
                break;
            }
        }
    }

    public List<SelectItem> getSelectItemsEjercicio() {
        this.selectItemsEjercicio = new ArrayList<>();
        List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(0, 0L);
        if (!(lstEjercicioAux.isEmpty())) {
            lstEjercicioAux.stream().forEach((ejercicioAux) -> {
                this.selectItemsEjercicio.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        return this.selectItemsEjercicio;
    }

    public List<SelectItem> getSelectItemsServicio() {
        try {
            this.selectItemsServicio = new ArrayList<>();
            List<Servicio> lstServicioAux = this.servicioFacade.findAll(this.idEjercicio);
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.forEach((servicioAux) -> {
                    this.selectItemsServicio.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigoAbreviatura()));
                });
            }
            return this.selectItemsServicio;
        } catch (Exception e) {
            log.error("Error en getSelectItemsServicio(): " + e.getMessage());
            return null;
        }

    }

    public List<SelectItem> getSelectItemsOrganismo() {
        List<SelectItem> selectItemsOrganismo = new ArrayList<>();
        if (this.getIdServicio() != null && this.getIdServicio() != 0L) {
            Servicio servicioAux = this.servicioFacade.find(this.getIdServicio());
            List<Organismo> lstOrganismoAux = servicioAux.getOrganismos();
            if (!(lstOrganismoAux.isEmpty())) {
                lstOrganismoAux.stream().forEach((organismoAux) -> {
                    selectItemsOrganismo.add(new SelectItem(organismoAux.getId(), Utilidad.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre()));
                });
            }
        }
        return selectItemsOrganismo;
    }

    public List<SelectItem> getSelectItemsCuentaBancaria() {
        this.SelectItemsCuentaBancaria = new ArrayList<>();
        List<CuentaBancaria> lstCuentaBancariaAux = new ArrayList<>();
        this.setIdCuentaBancaria(0L);
        if (this.getIdServicio() != 0L) {
            lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicio(), this.getIdServicio(), 0L);
        } else {
            lstCuentaBancariaAux.clear();
        }
        if (!(lstCuentaBancariaAux.isEmpty())) {
            lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                this.SelectItemsCuentaBancaria.add(new SelectItem(cuentaBancariaAux.getId(), cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
            });
        }
        return this.SelectItemsCuentaBancaria;
    }

    public void validarFechaMaxima(final FacesContext context, final UIComponent validate, final Object value) {
        Date fechaAux = (Date) value;
        if (this.isPerteneceHTC()) {
            if (fechaAux.after(this.getFechaMaxima())) {
                DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                final FacesMessage msg = new FacesMessage("Debe ser anterior o igual a " + formato.format(this.getFechaMaxima()) + ".");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    public void actualizarUltimaFecha(ValueChangeEvent e) {
        Long idCuentaAux = (Long) e.getNewValue();
        this.setFecha(new Date());
        if (!idCuentaAux.equals(0L)) {
            List<Movimiento> movimientos = this.movimientoFacade.findByCuentaBancaria(idCuentaAux);
            if (!movimientos.isEmpty()) {
                Movimiento movimientoAux = this.movimientoFacade.findByCuentaBancaria(idCuentaAux).get(0);
                Date fechaInicial = this.actualizarInicioAnio(this.getIdEjercicio());
                this.setFecha(fechaInicial);
                if (movimientoAux != null) {
                    this.setFecha(movimientoAux.getFechaAlta());
                }
            }
        }
    }

    public void opcionesServicioAdministrativo(ValueChangeEvent e) {
        this.setIdReporteLibroBancoOpcion((Long) e.getNewValue());
    }

    @Override
    public String crear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LazyDataModel<?> getListElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void verDetalle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardarBorrado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
