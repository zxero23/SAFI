package com.safi.managedBeans;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.icefaces.ace.model.table.LazyDataModel;
import com.safi.entity.Usuario;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.Expediente;
import com.safi.entity.Movimiento;
import com.safi.entity.RecursoPropio;
import com.safi.entity.Servicio;
import com.safi.enums.AccionEnum;
import com.safi.enums.TipoServicioEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.CuentaBancariaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.ExpedienteFacadeLocal;
import com.safi.facade.MovimientoFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.utilidad.ReporteJava;
import com.safi.utilidad.SAFIReporteJava;
import com.safi.utilidad.Recurso;
import com.safi.utilidad.Utilidad;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class ExpedienteManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private ExpedienteFacadeLocal expedienteFacade;
    @EJB
    private MovimientoFacadeLocal movimientoFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    private Long codigo;
    private Long numero;
    private Long idServicio;
    private Long idCuenta;
    private Long orgExpediente;
    private Long idEjercicio;
    private String anio;
    private Long idProveedor;
    private String proveedor;
    private String descripcionProveedor;
    private String cuilProveedor;
    private String numeroBuscar = "";
    private String cuenta;
    private boolean buscar;
    private BigDecimal totalIngresos = new BigDecimal("0.0");
    private BigDecimal totalDescargos = new BigDecimal("0.0");
    private BigDecimal totalCargos = new BigDecimal("0.0");
    private BigDecimal totalPendientes = new BigDecimal("0.0");
    private BigDecimal situacionFinanciera = new BigDecimal("0.0");
    private boolean verSituacionFinanciera = false;
    private List<SelectItem> itemsEjercicios;
    private List<SelectItem> cuentas;
    private Usuario usuario = new Usuario();
    private Servicio servicio;
    private Servicio servicio1 = new Servicio();
    private List<SelectItem> selectItemsServicio = new ArrayList<>();
    private List<SelectItem> selectItemsCuentaBsq = new ArrayList<>();
    private Long idEjercicioBsq;
    private Long idServicioBsq;
    private Long idCuentaBancariaBsq;
    private Long codigoOrganismoBsq;
    private Long numeroExpedienteBsq;
    private Movimiento movimiento;
    private Recurso recurso;
    private List<Movimiento> listaMovimientos = new ArrayList<>();
    private JasperPrint jasperResultado = new JasperPrint();
    private String codigoServicio;
    private String abreviaturaServicio;
    private String numeroCuenta;
    private String descripcionCuenta;

    public ExpedienteManagedBean() {

    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        this.setServicio(sessionBean.getServicio());
        this.setUsuario(sessionBean.getUsuario());
        if (sessionBean != null) {
            try {

            } catch (Exception e) {
            }
        }

    }

    public List<SelectItem> getItemsEjercicios() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> listRecurso = ejercicioFacade.findAll(true);
        if (!(listRecurso.isEmpty())) {
            for (Ejercicio ejAux : listRecurso) {
                selectItems.add(new SelectItem(ejAux.getId(), ejAux.getAnio().toString()));
            }
        }
        return selectItems;
    }

  
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getDescripcionCuenta() {
        return descripcionCuenta;
    }

    public void setDescripcionCuenta(String descripcionCuenta) {
        this.descripcionCuenta = descripcionCuenta;
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

    public BigDecimal getSituacionFinanciera() {
        return situacionFinanciera;
    }

    public void setSituacionFinanciera(BigDecimal situacionFinanciera) {
        this.situacionFinanciera = situacionFinanciera;
    }

    public boolean isVerSituacionFinanciera() {
        return verSituacionFinanciera;
    }

    public void setVerSituacionFinanciera(boolean verSituacionFinanciera) {
        this.verSituacionFinanciera = verSituacionFinanciera;
    }

    public List<Movimiento> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Long getNumeroExpedienteBsq() {
        return numeroExpedienteBsq;
    }

    public void setNumeroExpedienteBsq(Long numeroExpedienteBsq) {
        this.numeroExpedienteBsq = numeroExpedienteBsq;
    }

    public Servicio getServicio1() {
        return servicio1;
    }

    public void setServicio1(Servicio servicio1) {
        this.servicio1 = servicio1;
    }

    public JasperPrint getJasperResultado() {
        return jasperResultado;
    }

    public void setJasperResultado(JasperPrint jasperResultado) {
        this.jasperResultado = jasperResultado;
    }

    public Long getIdEjercicioBsq() {
        return idEjercicioBsq;
    }

    public void setIdEjercicioBsq(Long idEjercicioBsq) {
        this.idEjercicioBsq = idEjercicioBsq;
    }

    public Long getCodigoOrganismoBsq() {
        return codigoOrganismoBsq;
    }

    public void setCodigoOrganismoBsq(Long codigoOrganismoBsq) {
        this.codigoOrganismoBsq = codigoOrganismoBsq;
    }

    public Long getIdServicioBsq() {
        return idServicioBsq;
    }

    public void setIdServicioBsq(Long idServicioBsq) {
        try {
            this.idServicioBsq = idServicioBsq;
            this.setServicio1(this.servicioFacade.find(this.idServicioBsq));
        } catch (Exception ex) {
            System.out.println("error en setidSErvBsq" + ex.getMessage());

        }
    }

    public Long getIdCuentaBancariaBsq() {
        return idCuentaBancariaBsq;
    }

    public void setIdCuentaBancariaBsq(Long idCuentaBancariaBsq) {
        this.idCuentaBancariaBsq = idCuentaBancariaBsq;
    }

    public List<SelectItem> getSelectItemsServicio() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Servicio> lstServicioAux = this.ejercicioFacade.findAllServ(this.getIdEjercicioBsq(), true);
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                    selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
                });
            }
            
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public List<SelectItem> getSelectItemsCuentaBsq() {
        try {          
            List<SelectItem> selectItems = new ArrayList<>();
            List<CuentaBancaria> cuentasBancarias = new ArrayList<>();
            if (this.getServicio() == null) {
                cuentasBancarias = this.cuentaFacade.findAll(this.getIdEjercicioBsq(), this.getIdServicioBsq(), null);
            } else {
                cuentasBancarias = this.cuentaFacade.findAll(this.getIdEjercicioBsq(), this.getServicio().getId(), null);
            }
            if (!(cuentasBancarias.isEmpty())) {
                cuentasBancarias.stream().forEach((cuentaAux) -> {
                    selectItems.add(new SelectItem(cuentaAux.getId(), String.valueOf(cuentaAux.getNumero()) + " - " + cuentaAux.getDescripcion()));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void setSelectItemsCuentaBsq(List<SelectItem> selectItemsCuentaBsq) {
        this.selectItemsCuentaBsq = selectItemsCuentaBsq;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;

    }

    public void setItemsEjercicios(List<SelectItem> itemsEjercicios) {
        this.itemsEjercicios = itemsEjercicios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public ExpedienteFacadeLocal getExpedienteFacade() {
        return expedienteFacade;
    }

    public void setExpedienteFacade(ExpedienteFacadeLocal expedienteFacade) {
        this.expedienteFacade = expedienteFacade;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCuilProveedor() {
        return cuilProveedor;
    }

    public void setCuilProveedor(String cuilProveedor) {
        this.cuilProveedor = cuilProveedor;
    }

    public String getDescripcionProveedor() {
        return descripcionProveedor;
    }

    public void setDescripcionProveedor(String descripcionProveedor) {
        this.descripcionProveedor = descripcionProveedor;
    }

    public String getNumeroBuscar() {
        return numeroBuscar;
    }

    public void setNumeroBuscar(String numeroBuscar) {
        this.numeroBuscar = numeroBuscar;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;

    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Long getOrgExpediente() {
        return orgExpediente;
    }

    public void setOrgExpediente(Long orgExpediente) {
        this.orgExpediente = orgExpediente;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalCargos() {
        return totalCargos;
    }

    public void setTotalCargos(BigDecimal totalCargos) {
        this.totalCargos = totalCargos;
    }

    public BigDecimal getTotalDescargos() {
        return totalDescargos;
    }

    public void setTotalDescargos(BigDecimal totalDescargos) {
        this.totalDescargos = totalDescargos;
    }

    public BigDecimal getTotalPendientes() {
        return totalPendientes;
    }

    public void setTotalPendientes(BigDecimal totalPendientes) {
        this.totalPendientes = totalPendientes;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public List<?> getListElementsExpedientes() {
        try {
            long numero1 = System.currentTimeMillis();
            List<Movimiento> movAux = new ArrayList<>();
            if (this.getSessionBean().getUsuario().getServicio() != null) {
                this.setIdServicio(this.getSessionBean().getUsuario().getServicio().getId());
                this.setIdServicioBsq(this.getIdServicio());
            } else {
                this.setIdServicio(this.idServicioBsq);
            }
            if (this.flag) {
                if (!this.buscar) {
                    this.setList(new ArrayList<>());
                    this.setTotalCargos(new BigDecimal("0.0"));
                    this.setTotalDescargos(new BigDecimal("0.0"));
                    this.setTotalIngresos(new BigDecimal("0.0"));
                    this.setTotalPendientes(new BigDecimal("0.0"));
                    this.setFlag(false);
                    buscar = false;
                } else {
                    this.setList(expedienteFacade.findExpedientes(this.getIdEjercicioBsq(),
                            this.getNumeroBuscar(),
                            this.getIdServicioBsq(),
                            this.getIdCuentaBancariaBsq(),
                            this.getCodigoOrganismoBsq(),
                            this.getAnio()));
                    this.calcularTotales();

                    //INICIO AUDITORIA//
                    Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicioBsq());
                    Servicio servicioAux = this.servicioFacade.find(this.getIdServicioBsq());
                    if (servicioAux != null) {
                        this.setCodigoServicio(servicioAux.getCodigo());
                        this.setAbreviaturaServicio(servicioAux.getAbreviatura());
                    } else {
                        this.setCodigoServicio("");
                        this.setAbreviaturaServicio("");
                    }
                    CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(this.getIdCuentaBancariaBsq());
                    if (cuentaBancariaAux != null) {
                        this.setNumeroCuenta(cuentaBancariaAux.getNumero().toString());
                        this.setDescripcionCuenta(cuentaBancariaAux.getDescripcion());
                    } else {
                        this.setNumeroCuenta("");
                        this.setDescripcionCuenta("");
                    }
                    this.setAuditoriaActual(
                            this.devolverStringAuditoria(
                                    this.ejercicioFacade.find(
                                            this.getIdEjercicioBsq()).getAnio().toString(),
                                    this.getCodigoServicio(),
                                    this.getAbreviaturaServicio(),
                                    this.getNumeroCuenta(),
                                    this.getDescripcionCuenta(),
                                    this.getCodigoOrganismoBsq() == 0 ? "-" : this.getCodigoOrganismoBsq().toString(),
                                    this.getNumeroBuscar().equals("-1") ? "-" : this.getNumeroBuscar(),
                                    this.getAnio() == null ? "" : this.getAnio()
                            )
                    );
                    this.auditoriaFacade.create(AccionEnum.BUSQUEDA_EXPEDIENTE.getName(), new Date(), this.getUsuario().getId(), "-", this.getAuditoriaActual(), ejercicioAux.getId());
                    //FIN AUDITORIA//

                    buscar = false;
                    this.setFlag(false);
                    for (Movimiento mov : (List<Movimiento>) this.getList()) {
                        if (!mov.getFunciones().isEmpty()) {
                            movAux.add(mov);
                        }
                    }
                    this.setList(movAux);
                }
                final double c = ((double) System.currentTimeMillis() - numero1) / 1000;
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        List<Movimiento> lista = (List<Movimiento>) this.getList();

        return lista;

    }

    public void setBuscar(boolean buscar) {
        this.buscar = buscar;
    }

    public void buscar() {
        this.buscar = true;
        this.limpiarIngresos();
        this.setFlag(true);
        this.getListElementsExpedientes();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Expediente> listRecurso = expedienteFacade.findAll(true);
        if (!(listRecurso.isEmpty())) {
            for (Expediente recursoAux : listRecurso) {
                selectItems.add(new SelectItem(recursoAux.getId(), recursoAux.getNumero().toString()));
            }
        }
        return selectItems;
    }

    @Override
    public void limpiar() {
        buscar = true;       
        this.setIdProveedor(0L);
        this.setProveedor("");
        this.setDescripcionProveedor("");
        this.setCuilProveedor("");
        this.setAnio(null);
        this.setVerSituacionFinanciera(false);
        
    }

    @Override
    public void actualizar() {
        buscar = true;
        this.setCodigo(0L);
        this.setNumero(0L);
        this.setNumeroBuscar("");
        this.setAnio("");
        this.setIdProveedor(0L);
        this.setProveedor("");
        this.setDescripcionProveedor("");
        this.setCuilProveedor("");
        this.setIdServicioBsq(null);
        this.setOrgExpediente(null);
        this.setIdEjercicioBsq(null);
        this.setCodigoOrganismoBsq(null);
        this.setNumeroBuscar("");
        this.limpiarIngresos();
        this.setList(new ArrayList<>());
        this.setIdCuentaBancariaBsq(null);
        //this.getListElements();
    }

    @Override
    public String crear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @author Alvarenga Angel,Gonzalez Facundo
     */
    @Override
    public void verDetalle() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long codigo = Long.parseLong(myRequest.getParameter("codigo"));
            String numero = String.valueOf(myRequest.getParameter("numero"));
            String anio = String.valueOf(myRequest.getParameter("anio"));
            String cuenta = String.valueOf(myRequest.getParameter("cuenta"));
            this.setIdCuentaBancariaBsq(this.cuentaBancariaFacade.findAll(this.getIdEjercicioBsq(), this.getIdServicioBsq())
                    .stream()
                    .filter(x -> x.getNumero().equals(Long.valueOf(cuenta)))
                    .collect(Collectors.toList()).get(0).getId()
            );
            this.setNumero(Long.valueOf(numero));
            this.setCodigo(codigo);
            this.setAnio(anio);
            this.setCuenta(cuenta);
            this.limpiarIngresos();
            this.setListaMovimientos(this.expedienteFacade.findAll(codigo, numero, anio, this.getIdEjercicioBsq(), this.getIdCuentaBancariaBsq()));
            this.calcularTotales();
            this.setSituacionFinanciera(this.getTotalIngresos().subtract(this.getTotalCargos()));
            this.setVerSituacionFinanciera(this.getListaMovimientos().get(0).getCuentaBancaria().getServicio().getTipoServicio().getId().equals(TipoServicioEnum.SERVICIO.getId()));
        } catch (Exception ex) {
            System.out.println("Error: ".concat(ex.getMessage()));
        }
    }

    public void generarReporte() throws Exception {
        try {
            this.setJasperResultado(new ReporteJava().reporteExpedienteMovimientos(
                    this.getCodigo(),
                    this.getNumero(),
                    Long.parseLong(this.getAnio()),
                    this.getIdEjercicioBsq(), Long.parseLong(this.getCuenta()),
                    this.isVerSituacionFinanciera()
            ));
            this.setTitle("Proceso Completo");
            this.setImages("fa fa-check-circle-o");
            this.setMsgSuccessError("Reporte generado con éxito.");
            this.setEsCorrecto(true);
        } catch (JRException | IOException e) {
            this.setEsCorrecto(false);
            this.setTitle("Error");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al generar el reporte: " + e.getMessage());
        }
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
        JRXlsExporter exporterXLS = new JRXlsExporter();
        exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperGenerado);
        exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterXLS.exportReport();
        byte[] bites = xlsReport.toByteArray();
        miRecurso = new SAFIReporteJava(bites);
        return miRecurso;
    }

    @Override
    public void guardarBorrado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void calcularTotales() {
        try {
            Movimiento mov;
            for (Object movItems : this.getListaMovimientos()) {
                mov = (Movimiento) movItems;
                switch (mov.getFunciones().get(0).getTipoImputacion().getId().intValue()) {
                    case 3:
                        this.setTotalIngresos(this.getTotalIngresos().add(mov.getImporte()));
                        break;
                    case 9:
                        this.setTotalIngresos(this.getTotalIngresos().add(mov.getImporte()));
                        break;
                    case 1:
                        this.setTotalCargos(this.getTotalCargos().add(mov.getImporte()));
                        break;
                    case 2:
                        this.setTotalDescargos(this.getTotalDescargos().add(mov.getImporte()));
                        break;
                    case 4:
                        this.setTotalDescargos(this.getTotalDescargos().add(mov.getImporte()));
                        break;
                }

                this.setTotalPendientes(this.getTotalCargos().subtract(this.getTotalDescargos()));
            }
        } catch (Exception ex) {
            System.out.println("error totales:" + ex.getMessage());
        }
    }

    public void limpiarIngresos() {
        this.setTotalCargos(new BigDecimal("0.0"));
        this.setTotalDescargos(new BigDecimal("0.0"));
        this.setTotalIngresos(new BigDecimal("0.0"));
        this.setTotalPendientes(new BigDecimal("0.0"));

    }

    public void setCuentas(List<SelectItem> cuentas) {
        this.cuentas = cuentas;
    }

    /**
     * @author Gonzalez Facundo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsCuentaBancaria() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<CuentaBancaria> lstCuentaBancariaAux = new ArrayList<>();
        try {
            if (this.getIdServicio() == null) {
                lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicio(), this.getServicio().getId(), 1L);//1L= EstadoCuenta Activa
            } else {
                lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicio(), this.getIdServicio(), 1L);//1L= EstadoCuenta Activa
                this.setServicio(servicioFacade.find(this.getIdServicio()));
            }
            if (!(lstCuentaBancariaAux.isEmpty())) {
                lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                    selectItems.add(new SelectItem(cuentaBancariaAux.getId(), cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
                });
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return selectItems;
    }

    /**
     * author Gonzalez Facundo
     */
    public void actualizarServicio() {
        try {
            this.selectItemsServicio.clear();
            Ejercicio ejercicioAux = new Ejercicio();
            if (this.getIdEjercicioBsq() != null) {
                ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicioBsq());
            }
            List<Servicio> lstServicioAux = ejercicioAux.getLstServicios();
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().forEach((servicioAux) -> {
                    this.selectItemsServicio.add(new SelectItem(servicioAux.getId(), servicioAux.getId() + "-" + servicioAux.getAbreviatura()));
                });
            }
            if (this.getSessionBean().getUsuario().getServicio().getId() != null) {
                this.setIdServicioBsq(this.getSessionBean().getUsuario().getServicio().getId());
            }
            this.actualizarCuentaBancaria();
        } catch (Exception ex) {

        }
    }

    /**
     * @author Facundo Gonzalez Para actualizar el select de cuenta bancaria en
     * el filtro de búsqueda al momento de seleccionar un servicio
     * administrativo
     */
    public void actualizarCuentaBancaria() {
        this.selectItemsCuentaBsq.clear();
        List<CuentaBancaria> lstCuentaBancariaAux = this.cuentaBancariaFacade.findAll(this.getIdEjercicioBsq(), this.getIdServicioBsq());// 1L Estado Cuenta Activa
        if (!(lstCuentaBancariaAux.isEmpty())) {
            lstCuentaBancariaAux.stream().forEach((cuentaBancariaAux) -> {
                this.selectItemsCuentaBsq.add(new SelectItem(cuentaBancariaAux.getId(), cuentaBancariaAux.getNumero() + "-" + cuentaBancariaAux.getDescripcion()));
            });
        }
    }

    /**
     * @author Gonzalez Facundo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicio() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true);
            if (!(lstEjercicioAux.isEmpty())) {
                lstEjercicioAux.stream().forEach((ejercicioAux) -> {
                    selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los codigos de expedientes de los movimentos.
     *
     * @author Doroñuk Gustavo
     */
    public List<SelectItem> getSelectItemsCodigoOrganismo() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Long> codigosOrganismos = this.movimientoFacade.findAllCodigoOrganismo(this.getIdEjercicioBsq(), this.getIdCuentaBancariaBsq());
            if (!(codigosOrganismos.isEmpty())) {
                codigosOrganismos.stream().forEach((ejercicioAux) -> {
                    selectItems.add(new SelectItem(ejercicioAux, String.valueOf(ejercicioAux)));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los numeros de expedientes de los movimentos.
     *
     * @author Gonzalez Facundo
     */
    public List<SelectItem> getSelectItemsNumerosExpedientes() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Long> codigosNumeros = this.movimientoFacade.findAllExpedientes(this.getIdEjercicioBsq(), this.getIdCuentaBancariaBsq(), this.getCodigoOrganismoBsq());
            if (!(codigosNumeros.isEmpty())) {
                codigosNumeros.stream().forEach((ejercicioAux) -> {
                    selectItems.add(new SelectItem(ejercicioAux, String.valueOf(ejercicioAux)));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public LazyDataModel<?> getListElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * author Gonzalez Facundo
     *
     * @param ejercicio
     * @param codigoServicio
     * @param descripcionServicio
     * @param nrocuenta
     * @param descripcionCuenta
     * @param codOrganismo
     * @param correlativo
     * @param anio
     * @see Devuelve cadena de texto para Auditoria(valorActual,valorAnterior)
     * @return String
     */
    public String devolverStringAuditoria(
            String ejercicio,
            String codigoServicio,
            String descripcionServicio,
            String nrocuenta,
            String descripcionCuenta,
            String codOrganismo,
            String correlativo,
            String anio) {

        return "Ejercicio: " + ejercicio + "\n"
                + "Servicio: " + codigoServicio + " - " + descripcionServicio + "\n"
                + "Cuenta: " + nrocuenta + " - " + descripcionCuenta + "\n"
                + "Cod. Organismo: " + codOrganismo + "\n"
                + "Correlativo: " + correlativo + "\n"
                + "Año: " + anio + "\n";

    }
    
    
    /**
     * @author Gonzalez Facundo
     * @see Obtiene el saldo de una cuenta X para mostrarlo en nuevo movimiento
     */
    public void actualizarServicioBsq() {
        this.setCodigoOrganismoBsq(0L);
    }

}
