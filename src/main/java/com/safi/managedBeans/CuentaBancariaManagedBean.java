package com.safi.managedBeans;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import com.safi.entity.Banco;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.EstadoCuentaBancaria;
import com.safi.entity.Servicio;
import com.safi.facade.BancoFacadeLocal;
import com.safi.facade.CuentaBancariaFacadeLocal;
import com.safi.facade.EstadoCuentaBancariaFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatus;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.util.JavaScriptRunner;
import com.safi.entity.Agente;
import com.safi.entity.Ejercicio;
import com.safi.entity.Funcion;
import com.safi.entity.Movimiento;
import com.safi.entity.Rol;
import com.safi.entity.SaldoAcumulado;
import com.safi.entity.TipoAcumulador;
import com.safi.entity.TipoCuenta;
import com.safi.entity.TipoMoneda;
import com.safi.facade.AgenteFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.MovimientoFacadeLocal;
import com.safi.facade.RolFacadeLocal;
import com.safi.facade.SaldoAcumuladoFacadeLocal;
import com.safi.facade.TipoAcumuladorFacadeLocal;
import com.safi.facade.TipoCuentaFacadeLocal;
import com.safi.facade.TipoMonedaFacadeLocal;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@ManagedBean
@SessionScoped
public class CuentaBancariaManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    private BancoFacadeLocal bancoFacade;
    @EJB
    private TipoMonedaFacadeLocal tipoMonedaFacade;
    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private EstadoCuentaBancariaFacadeLocal estadoCuentaFacace;
    @EJB
    private RolFacadeLocal rolFacace;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private AgenteFacadeLocal agenteFacace;
    @EJB
    private MovimientoFacadeLocal movimientoFacace;
    @EJB
    private SaldoAcumuladoFacadeLocal saldoAcumuladoFacade;
    @EJB
    private TipoAcumuladorFacadeLocal tipoAcumuladorFacade;
    /* Inicio Atributos */
    private Long numero;
    private String descripcion;
    private String cbu;
    private String path;
    private String alias;
    private Date fechaAlta;
    private Date fechaBaja;
    private Long idServicio;
    private Long idBanco;
    private Long idEstadoCuenta;
    private Long idEjercicio;
    private Long idTipoMoneda;
    private Long idTipoCuenta;
    private Long idAgente;
    private Long idRol;
    private Date fechaInicio;
    private Date fechaFin;
    private CuentaBancaria cuentaBancaria;
    private String nombreCuentaStr;
    private String numeroCuentaStr;
    private List<SelectItem> lstServiciosPorEjercicio = new ArrayList<>();
    private List<Movimiento> lstMovimientos = new ArrayList();
    private List<SaldoAcumulado> lstSaldoAcumulado = new ArrayList();
    /*Fin atributos*/

    /* Inicio Atributos de búsqueda */
    private Long idBancoBsq;
    private Long idServicioBsq;
    private Long idEjercicioBsq;
    private Long idEstadoCuentaBsq;
    private Long numeroBsq;
    private String descripcionBsq;
    private int saldoCero = 0;
    /*fin atributos de búsqueda */
    /*Otras acciones*/
    private boolean asignarRepresentante;
    private List<Long> lstIdRepresentantesAQuitar = new ArrayList<>();
    /*fin otras acciones */

    /*SelectItems */
    private List<SelectItem> itemsIzquierda = new ArrayList<>();

    private List<SelectItem> itemsDerecha = new ArrayList<>();

    ;
    /*SelectItems*/

    /**
     * Creates a new instance of CuentaBancariaManagedBean
     */
    public CuentaBancariaManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                if (sessionBean.getServicio() != null) {
                    this.setIdServicio(sessionBean.getServicio().getId());
                    this.setIdServicioBsq(sessionBean.getServicio().getId());
                }
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevaCuentaBancaria")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarCuentaBancaria")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarCuentaBancaria")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleCuentaBancaria"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }

    }

    public List<SaldoAcumulado> getLstSaldoAcumulado() {
        return lstSaldoAcumulado;
    }

    public void setLstSaldoAcumulado(List<SaldoAcumulado> lstSaldoAcumulado) {
        this.lstSaldoAcumulado = lstSaldoAcumulado;
    }

    public List<SelectItem> getLstServiciosPorEjercicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = this.ejercicioFacade.findAllServ(idEjercicio, true);
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().filter((servicio) -> (servicio.isEstado())).sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
            });
        }
        return selectItems;
    }

    public void setLstServiciosPorEjercicio(List<SelectItem> lstServiciosPorEjercicio) {
        this.lstServiciosPorEjercicio = lstServiciosPorEjercicio;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public List<Long> getLstIdRepresentantesAQuitar() {
        return lstIdRepresentantesAQuitar;
    }

    public void setLstIdRepresentantesAQuitar(List<Long> lstIdRepresentantesAQuitar) {
        this.lstIdRepresentantesAQuitar = lstIdRepresentantesAQuitar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Long getIdEstadoCuenta() {
        return idEstadoCuenta;
    }

    public void setIdEstadoCuenta(Long idEstadoCuenta) {
        this.idEstadoCuenta = idEstadoCuenta;
    }

    public Long getIdBancoBsq() {
        return idBancoBsq;
    }

    public void setIdBancoBsq(Long idBancoBsq) {
        this.idBancoBsq = idBancoBsq;
    }

    public Long getIdServicioBsq() {
        return idServicioBsq;
    }

    public void setIdServicioBsq(Long idServicioBsq) {
        this.idServicioBsq = idServicioBsq;
    }

    public Long getIdEstadoCuentaBsq() {
        return idEstadoCuentaBsq;
    }

    public void setIdEstadoCuentaBsq(Long idEstadoCuentaBsq) {
        this.idEstadoCuentaBsq = idEstadoCuentaBsq;
    }

    public Long getNumeroBsq() {
        return numeroBsq;
    }

    public void setNumeroBsq(Long numeroBsq) {
        this.numeroBsq = numeroBsq;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Long idAgente) {
        this.idAgente = idAgente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getIdTipoMoneda() {
        return idTipoMoneda;
    }

    public void setIdTipoMoneda(Long idTipoMoneda) {
        this.idTipoMoneda = idTipoMoneda;
    }

    public Long getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public boolean isAsignarRepresentante() {
        return asignarRepresentante;
    }

    public void setAsignarRepresentante(boolean asignarRepresentante) {
        this.asignarRepresentante = asignarRepresentante;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public Long getIdEjercicioBsq() {
        return idEjercicioBsq;
    }

    public void setIdEjercicioBsq(Long idEjercicioBsq) {
        this.idEjercicioBsq = idEjercicioBsq;
    }

    public List<SelectItem> getItemsIzquierda() {
        return itemsIzquierda;
    }

    public void setItemsIzquierda(List<SelectItem> itemsIzquierda) {
        this.itemsIzquierda = itemsIzquierda;
    }

    public List<SelectItem> getItemsDerecha() {
        return itemsDerecha;
    }

    public void setItemsDerecha(List<SelectItem> itemsDerecha) {
        this.itemsDerecha = itemsDerecha;
    }

    public List<Movimiento> getLstMovimientos() {
        return lstMovimientos;
    }

    public void setLstMovimientos(List<Movimiento> lstMovimientos) {
        this.lstMovimientos = lstMovimientos;
    }

    public String getNombreCuentaStr() {
        return nombreCuentaStr;
    }

    public void setNombreCuentaStr(String nombreCuentaStr) {
        this.nombreCuentaStr = nombreCuentaStr;
    }

    public String getNumeroCuentaStr() {
        return numeroCuentaStr;
    }

    public void setNumeroCuentaStr(String numeroCuentaStr) {
        this.numeroCuentaStr = numeroCuentaStr;
    }

    public String getDescripcionBsq() {
        return descripcionBsq;
    }

    public void setDescripcionBsq(String descripcionBsq) {
        this.descripcionBsq = descripcionBsq;
    }

    public int getSaldoCero() {
        return saldoCero;
    }

    public void setSaldoCero(int saldoCero) {
        this.saldoCero = saldoCero;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<CuentaBancaria> lstCuentaBancariasAux = new LazyDataModel<CuentaBancaria>() {

            @Override
            public List<CuentaBancaria> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<CuentaBancaria> lstCuentaBancariasAux = cuentaBancariaFacade.findAll(idServicioBsq, idBancoBsq, idEstadoCuentaBsq, numeroBsq, descripcionBsq, saldoCero, idEjercicioBsq, first, pageSize);

                return lstCuentaBancariasAux;
            }

        };
        lstCuentaBancariasAux.setRowCount(cuentaBancariaFacade.countAll(idServicioBsq, idBancoBsq, idEstadoCuentaBsq, numeroBsq, descripcionBsq, saldoCero, idEjercicioBsq).intValue());
        return lstCuentaBancariasAux;
    }

    @Override
    public void limpiar() {
        this.setNumero(null);
        this.setDescripcion(null);
        this.setCbu(null);
        this.setAlias(null);
        this.setFechaAlta(null);
        this.setIdBanco(null);
        this.setIdAgente(null);
        this.setIdRol(null);
        this.setIdServicio(null);
        this.setFechaBaja(null);
        this.setIdEstadoCuenta(null);
        this.setFechaInicio(null);
        this.setNombreCuentaStr(null);
        this.setNumeroCuentaStr(null);
        this.setIdTipoCuenta(null);
        this.setIdTipoMoneda(null);
        this.setIdEjercicio(null);
        this.lstServiciosPorEjercicio.clear();
        this.setPath(null);
        this.getLstIdRepresentantesAQuitar().clear();
    }

    /**
     * @autor Eugenio
     * @descripcion limpia casi todos los campos, exepto el periodo para que
     * continue la carga.
     */
    public void limpiarCrearOtro() {
        this.setNumero(null);
        this.setDescripcion(null);
        this.setCbu(null);
        this.setAlias(null);
        this.setFechaAlta(null);
        this.setIdBanco(null);
        this.setIdRol(null);
        this.setIdServicio(null);
        this.setFechaBaja(null);
        this.setIdEstadoCuenta(null);
        this.setIdTipoCuenta(null);
        this.setIdTipoMoneda(null);
        this.lstServiciosPorEjercicio.clear();
        this.setIdEjercicio(null);
        this.setFechaAlta(null);
        this.setPath(null);
    }

    @Override
    public String crear() {
        try {
            this.cuentaBancariaFacade.create(this.getIdEjercicio(), this.getIdServicio(),
                    this.getIdBanco(), this.getFechaAlta(), this.getNumero(),
                    this.getDescripcion(), this.getCbu(), this.getAlias(),
                    this.getIdTipoMoneda(), this.getIdTipoCuenta(), this.getPath());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCuentaBancaria");
            this.setMsgSuccessError("La cuenta bancaria ha sido generada con éxito");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.cuentaBancariaFacade.create(this.getIdEjercicio(), this.getIdServicio(),
                    this.getIdBanco(), this.getFechaAlta(), this.getNumero(),
                    this.getDescripcion(), this.getCbu(), this.getAlias(),
                    this.getIdTipoMoneda(), this.getIdTipoCuenta(), this.getPath());
            this.setResultado("nuevaCuentaBancaria");
            this.limpiarCrearOtro();
            JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "limpiarFileEntryNuevaCuentaBancaria()");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
        this.setCuentaBancaria(this.cuentaBancariaFacade.find(idCuentaBancariaAux));
        this.setLstMovimientos(this.movimientoFacace.findByCuentaBancaria(idCuentaBancariaAux));
        SaldoAcumulado saldo = new SaldoAcumulado();
        //this.setLstSaldoAcumulado(this.saldoAcumuladoFacade.find(this.getCuentaBancaria()));
        this.lstAcumuladores(idCuentaBancariaAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
            this.cuentaBancariaFacade.remove(idCuentaBancariaAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("cuentaBancariaConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
        CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(idCuentaBancariaAux);
        this.setId(cuentaBancariaAux.getId());
        this.setNumero(cuentaBancariaAux.getNumero());
        this.setDescripcion(cuentaBancariaAux.getDescripcion());
        this.setCbu(cuentaBancariaAux.getCbu());
        this.setFechaAlta(cuentaBancariaAux.getFechaAlta());
        this.setAlias(cuentaBancariaAux.getAlias());
        this.setFechaBaja(cuentaBancariaAux.getFechaBaja());
        this.setPath(cuentaBancariaAux.getUrl());
        if (cuentaBancariaAux.getBanco() != null) {
            this.setIdBanco(cuentaBancariaAux.getBanco().getId());
        }
        if (cuentaBancariaAux.getEstadoCuentaBancaria() != null) {
            this.setIdEstadoCuenta(cuentaBancariaAux.getEstadoCuentaBancaria().getId());
        }
        if (cuentaBancariaAux.getTipoCuenta() != null) {
            this.setIdTipoCuenta(cuentaBancariaAux.getTipoCuenta().getId());
        }
        if (cuentaBancariaAux.getTipoMoneda() != null) {
            this.setIdTipoMoneda(cuentaBancariaAux.getTipoMoneda().getId());
        }
        if (cuentaBancariaAux.getBanco() != null) {
            this.setIdBanco(cuentaBancariaAux.getBanco().getId());
        }
        if (cuentaBancariaAux.getServicio() != null) {
            this.setIdServicio(cuentaBancariaAux.getServicio().getId());
        }
        if (cuentaBancariaAux.getEjercicio() != null) {
            this.setIdEjercicio(cuentaBancariaAux.getEjercicio().getId());
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            this.cuentaBancariaFacade.edit(this.getId(), this.getIdEjercicio(), this.getNumero(),
                    this.getDescripcion(), this.getCbu(), this.getFechaAlta(), this.getFechaBaja(),
                    this.getIdEstadoCuenta(), this.getAlias(),
                    this.getIdTipoMoneda(), this.getIdTipoCuenta(), this.getPath(), this.getIdBanco());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCuentaBancaria");
            this.setMsgSuccessError("La cuenta bancaria ha sido editada con éxito");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
        return this.getResultado();
    }

    public List<SelectItem> getSelectItemsServicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = this.servicioFacade.findAll(this.getIdEjercicioBsq());
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + "-" + servicioAux.getAbreviatura()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoCuenta() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoCuenta> lstTipoCuenta = this.tipoCuentaFacade.findAll(true);
        if (!(lstTipoCuenta.isEmpty())) {
            lstTipoCuenta.stream().forEach((tipoCuentaAux) -> {
                selectItems.add(new SelectItem(tipoCuentaAux.getId(), tipoCuentaAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoMoneda() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoMoneda> lstTipoMoneda = this.tipoMonedaFacade.findAll(true);
        if (!(lstTipoMoneda.isEmpty())) {
            lstTipoMoneda.stream().forEach((tipoMonedaAux) -> {
                selectItems.add(new SelectItem(tipoMonedaAux.getId(), tipoMonedaAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsAgente() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Agente> lstAgente = this.agenteFacace.findAll(true);
        if (!(lstAgente.isEmpty())) {
            lstAgente.stream().forEach((agenteAux) -> {
                selectItems.add(new SelectItem(agenteAux.getId(), agenteAux.getDni().toString().concat(" - ").concat(agenteAux.getApellidoNombre())));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsRol() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Rol> lstRol = this.rolFacace.findAll(true);
        if (!(lstRol.isEmpty())) {
            lstRol.stream().forEach((rolAux) -> {
                selectItems.add(new SelectItem(rolAux.getId(), rolAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsBanco() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Banco> lstBanco = this.bancoFacade.findAll(true);
        if (!(lstBanco.isEmpty())) {
            lstBanco.stream().forEach((bancoAux) -> {
                selectItems.add(new SelectItem(bancoAux.getId(), bancoAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsEjercicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicio = this.ejercicioFacade.findAllNuevoBancario(0);
        if (!(lstEjercicio.isEmpty())) {
            lstEjercicio.stream().sorted((p1, p2) -> (p1.getAnio().compareTo(p2.getAnio()))).forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        return selectItems;
    }

    //Combo Ejercicio busqueda
    public List<SelectItem> getSelectItemsEjercicioBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicio = this.ejercicioFacade.findAll(true);
        if (!(lstEjercicio.isEmpty())) {
            lstEjercicio.stream().sorted((p1, p2) -> (p1.getAnio().compareTo(p2.getAnio()))).forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsEstadoCuenta() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<EstadoCuentaBancaria> lstEstadoCuenta = this.estadoCuentaFacace.findAll(true);
        if (!(lstEstadoCuenta.isEmpty())) {
            lstEstadoCuenta.stream().forEach((estadoCuentaAux) -> {
                selectItems.add(new SelectItem(estadoCuentaAux.getId(), estadoCuentaAux.getNombre()));
            });
        }
        return selectItems;
    }

    @Override
    public void actualizar() {
        this.setIdServicioBsq(null);
        this.setDescripcionBsq(null);
        this.setIdBancoBsq(null);
        this.setIdEstadoCuentaBsq(null);
        this.setNumeroBsq(null);
        this.setIdEjercicioBsq(0L);
        this.setSaldoCero(0);
        this.setList(new ArrayList<>());
        super.actualizar();
    }
    /* Método para subir los archivos */

    public void cargarArchivo(FileEntryEvent entryEvent) {
        FileEntryResults results = ((FileEntry) entryEvent.getComponent()).getResults();
        for (FileEntryResults.FileInfo file : results.getFiles()) {
            if (file.isSaved()) {
                boolean valido = false;
                String tipo = file.getContentType();
                String[] tipos = new String[]{"application/pdf", "image/bmp", "image/gif", "image/ief",
                    "image/jpeg", "image/jpeg", "image/jpeg", "image/png", "image/tiff", "image/tiff",
                    "image/vnd.djvu", "image/vnd.djvu", "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.ms-excel"};

                for (String item : tipos) {
                    if (item.equals(tipo)) {
                        valido = true;

                    }
                }

                if (!valido) {
                    file.updateStatus(new FileEntryStatus() {
                        @Override
                        public boolean isSuccess() {
                            return false;
                        }

                        @Override
                        public FacesMessage getFacesMessage(
                                FacesContext facesContext, UIComponent fileEntry, FileEntryResults.FileInfo fi) {
                            return new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Solo se pueden adjuntar imágenes o archivos pdf y excel.",
                                    "Solo se pueden adjuntar imágenes o archivos pdf y excel.");
                        }
                    }, true, true);
                } else {
                    try {
                        File miArchivo = new File(file.getFile().getAbsolutePath());

                        String extension = "";
                        int i = file.getFileName().lastIndexOf('.');
                        int p = Math.max(file.getFileName().lastIndexOf('/'), file.getFileName().lastIndexOf('\\'));
                        if (i > p) {
                            extension = file.getFileName().substring(i + 1);
                        }
                        File miArchivoAux = new File(miArchivo.getParent() + "/" + Utilidad.dateTimeString(new Date()) + "." + extension);
                        miArchivo.renameTo(miArchivoAux);
                        File fil = new File(this.getPath() + miArchivo.getName());
                        this.setPath(miArchivoAux.getName());
                        fil.delete();
                        file.updateStatus(new FileEntryStatus() {
                            @Override
                            public boolean isSuccess() {
                                return true;
                            }

                            @Override
                            public FacesMessage getFacesMessage(
                                    FacesContext facesContext, UIComponent fileEntry, FileEntryResults.FileInfo fi) {
                                return new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "Se adjuntó el archivo.",
                                        "Se adjuntó el archivo.");
                            }
                        }, true, true);

                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Métodos para asignar representante a una cuenta bancaria
     */
    public void prepararParaAsignarRepresentante() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
        CuentaBancaria cuentaBancariaAux = this.cuentaBancariaFacade.find(idCuentaBancariaAux);
        this.setId(cuentaBancariaAux.getId());
        this.setNumeroCuentaStr(cuentaBancariaAux.getNumero().toString());
        this.setNombreCuentaStr(cuentaBancariaAux.getDescripcion());
        this.setIdAgente(null);
        this.setIdRol(null);
        this.setFechaInicio(null);
        this.setFechaFin(null);

    }

    public String crearRepresentante() {
        try {
            this.cuentaBancariaFacade.edit(this.getId(), this.getIdAgente(), this.getIdRol(), this.getFechaInicio(), this.getFechaFin());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCuentaBancaria");
            this.setMsgSuccessError("El agente ha sido asignado como representante de la cuenta correctamente.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
        return this.getResultado();
    }

    public String crearOtroRepresentante() {
        try {
            this.cuentaBancariaFacade.edit(this.getId(), this.getIdAgente(), this.getIdRol(), this.getFechaInicio(), this.getFechaFin());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCuentaBancaria");
            this.setMsgSuccessError("El agente ha sido asignado como representante de la cuenta correctamente.");
            this.setIdAgente(null);
            this.setIdRol(null);
            this.setFechaInicio(null);
            this.setFechaFin(null);
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCuentaBancaria");
        }
        return this.getResultado();
    }

    /* Método que valida que la cuenta bancaria no exista ya en la base de datos */
    public void validarCuentaBancaria(FacesContext context, UIComponent validate, Object value) {
        Long numeroCuenta = (Long) value;
        List<CuentaBancaria> cuentasBanacarias = this.cuentaBancariaFacade.findAll(null, this.getIdBanco(), null, numeroCuenta, null);
        if (!cuentasBanacarias.isEmpty()) {
            FacesMessage msg = new FacesMessage("La cuenta bancaria ya existe para el banco seleccionado.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    /**
     * @autor Eugenio
     * @param event
     * @descripcion Obtiene todos los servicios para el ejercicio seleccionado
     */
    public void updateServicios(ValueChangeEvent event) {
        Ejercicio ejercicio = this.ejercicioFacade.find(Long.parseLong(event.getNewValue().toString()));

        this.lstServiciosPorEjercicio.clear();
        for (Servicio servicio : ejercicio.getLstServicios()) {
            this.lstServiciosPorEjercicio.add(new SelectItem(servicio.getId(), servicio.getCodigo() + "-" + servicio.getDescripcion()));
        }
    }

    public void quitarRepresentante() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idRepresentante = Long.parseLong(myRequest.getParameter("id"));
        this.getLstIdRepresentantesAQuitar().add(idRepresentante);
        this.getCuentaBancaria().getRepresentantes().removeIf(representante -> representante.getId().equals(idRepresentante));
    }

    public void lstAcumuladores(Long idCuenta) {
        CuentaBancaria cuenta = this.cuentaBancariaFacade.find(idCuenta);
        List<Movimiento> movimientosAux = cuenta.getLstMovimientos();
        List<SaldoAcumulado> saldosAux = new ArrayList<>();
        for (int acu = 1; acu < 18; acu++) {
            BigDecimal big1 = BigDecimal.ZERO;
            BigDecimal big2 = BigDecimal.ZERO;
            SaldoAcumulado saldo = new SaldoAcumulado();
            saldo.setCuentaBancaria(cuenta);
            for (Movimiento mov : movimientosAux) {
                for (Funcion fun : mov.getFunciones()) {
                    if (fun.getTipoAcumulador().getId().equals(Long.valueOf(acu))) {
                        big2 = big1;
                        big1 = big1.add(mov.getImporte().multiply(BigDecimal.valueOf(fun.getTipoOperacion())));
                        saldo.setFechaActual(mov.getFechaAlta());
                    }
                }
            }
            saldo.setTipoAcumulador(this.tipoAcumuladorFacade.find(Long.valueOf(acu)));
            saldo.setSaldoActual(big1);
            saldo.setSaldoAnterior(big2);
            saldosAux.add(saldo);

            if (acu == 16) {
                CuentaBancaria cuentaAux = this.cuentaBancariaFacade.find(idCuenta);
                cuentaAux.setSaldo(big1);
                this.cuentaBancariaFacade.edit(cuentaAux);
            }
        }

        this.setLstSaldoAcumulado(saldosAux);

    }

}
