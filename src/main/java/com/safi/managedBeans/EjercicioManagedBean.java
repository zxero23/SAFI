package com.safi.managedBeans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.Ejercicio;
import com.safi.entity.EstadoEjercicio;
import com.safi.entity.Usuario;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Servicio;
import com.safi.enums.AccionEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.CierreEjercicioFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.EstadoEjercicioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.utilidad.Utilidad;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga
 */
@ManagedBean
@SessionScoped
public class EjercicioManagedBean extends UtilManagedBean implements Serializable {

    private final Long ID_ESTADO_TERMINADO = 2L;
    private final Long ID_ESTADO_FORMULADO = 3L;
    private final Long ID_ESTADO_ACTIVO = 1L;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private EstadoEjercicioFacadeLocal estadoEjercicioFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private CierreEjercicioFacadeLocal cierreEjercicioFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    private Integer anio;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaComplementaria;
    private Integer anioBsq;
    private Long idEstadoEjercicio;
    private Long idEstadoEjercicioActual;
    private Long idEstadoEjercicioBsq;
    private Ejercicio ejercicio;
    private List<SelectItem> itemsIzquierda = new ArrayList<>();
    private List<SelectItem> itemsDerecha = new ArrayList<>();
    private List<Servicio> lstServicio = new ArrayList<>();
    private List<SelectItem> selectItemsServicioDisponible = new ArrayList<>();
    private String tamanioListaServicios;
    private Integer codigoServicio;
    private String codigoBsq;
    private String descripcionBsq;
    private String estadoInicialEjercicio;
    private Date fechaInicialComplementaria;
    /**
     * ** Atributos de Cierre de Ejercicio **
     */
    private Long idEjercicio;
    private Long idServicio;
    private Usuario usuario;
    /**
     * ** Fin Atributos de Cierre de Ejercicio **
     */
    /**
     * ****Otras Acciones **
     */
    private boolean cerrarEjercicioServicio;
    private boolean cerrarEjercicioGeneral;
    private List<String> lstMensaje = new ArrayList<>();

    private boolean msjExitoCierreEjercicio;

    /**
     * ***Fin Otras Acciones*****
     */
    /**
     * Creates a new instance of EjercicioManagedBean
     */
    public EjercicioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setUsuario(sessionBean.getUsuario());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoEjercicio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarEjercicio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarEjercicio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("cierreEjercicioServicio")) {
                            this.setCerrarEjercicioServicio(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("cierreEjercicioGeneral")) {
                            this.setCerrarEjercicioGeneral(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleEjercicio"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public boolean isMsjExitoCierreEjercicio() {
        return msjExitoCierreEjercicio;
    }

    public void setMsjExitoCierreEjercicio(boolean msjExitoCierreEjercicio) {
        this.msjExitoCierreEjercicio = msjExitoCierreEjercicio;
    }

    public String getTamanioListaServicios() {
        return "Total Servicio/s:" + String.valueOf(this.lstServicio.size());
    }

    public void setTamanioListaServicios(String tamanioListaServicios) {
        this.tamanioListaServicios = tamanioListaServicios;
    }

    public Integer getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(Integer codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public List<Servicio> getLstServicio() {
        return lstServicio;
    }

    public List<Servicio> getLstServicioSorted() {
        return this.getLstServicio().stream()
                .sorted((s1, s2) -> (Integer.valueOf(s1.getCodigo()).compareTo(Integer.valueOf(s2.getCodigo()))))
                .collect(Collectors.toList());
    }

    public void aplicarFiltroServicios() {
        List<Servicio> lstServicio = this.ejercicioFacade.find(this.getIdEjercicio()).getLstServicios();
        if ((this.getCodigoBsq() != null && !this.getCodigoBsq().isEmpty()) && (this.getDescripcionBsq() != null && !this.getDescripcionBsq().isEmpty())) {
            lstServicio = lstServicio.stream()
                    .filter(servicio -> servicio.getCodigo()
                            .contains(this.getCodigoBsq()))
                    .filter(servicio -> Utilidad.desacentuar(servicio.getDescripcion().toUpperCase())
                            .contains(Utilidad.desacentuar(this.getDescripcionBsq().toUpperCase())))
                    .collect(Collectors.toList());
        } else if ((this.getCodigoBsq() != null && !this.getCodigoBsq().isEmpty()) && !(this.getDescripcionBsq() != null && !this.getDescripcionBsq().isEmpty())) {
            lstServicio = lstServicio.stream()
                    .filter(servicio -> servicio.getCodigo()
                            .contains(this.getCodigoBsq()))
                    .collect(Collectors.toList());
        } else if ((this.getDescripcionBsq() != null && !this.getDescripcionBsq().isEmpty()) && !(this.getCodigoBsq() != null && !this.getCodigoBsq().isEmpty())) {
            lstServicio = lstServicio.stream()
                    .filter(servicio -> Utilidad.desacentuar(servicio.getDescripcion().toUpperCase())
                            .contains(Utilidad.desacentuar(this.getDescripcionBsq().toUpperCase())))
                    .collect(Collectors.toList());
        }
        this.setLstServicio(lstServicio);
    }

    public void setLstServicio(List<Servicio> lstServicio) {
        this.lstServicio = lstServicio;
    }

    public Date getFechaComplementaria() {
        return fechaComplementaria;
    }

    public void setFechaComplementaria(Date fechaComplementaria) {
        this.fechaComplementaria = fechaComplementaria;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Integer getAnioBsq() {
        return anioBsq;
    }

    public void setAnioBsq(Integer anioBsq) {
        this.anioBsq = anioBsq;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public Long getIdEstadoEjercicioBsq() {
        return idEstadoEjercicioBsq;
    }

    public void setIdEstadoEjercicioBsq(Long idEstadoEjercicioBsq) {
        this.idEstadoEjercicioBsq = idEstadoEjercicioBsq;
    }

    public boolean isCerrarEjercicioServicio() {
        return cerrarEjercicioServicio;
    }

    public void setCerrarEjercicioServicio(boolean cerrarEjercicioServicio) {
        this.cerrarEjercicioServicio = cerrarEjercicioServicio;
    }

    public boolean isCerrarEjercicioGeneral() {
        return cerrarEjercicioGeneral;
    }

    public void setCerrarEjercicioGeneral(boolean cerrarEjercicioGeneral) {
        this.cerrarEjercicioGeneral = cerrarEjercicioGeneral;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public Long getIdEstadoEjercicioActual() {
        return idEstadoEjercicioActual;
    }

    public void setIdEstadoEjercicioActual(Long idEstadoEjercicioActual) {
        this.idEstadoEjercicioActual = idEstadoEjercicioActual;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Long getIdEstadoEjercicio() {
        return idEstadoEjercicio;
    }

    public void setIdEstadoEjercicio(Long idEstadoEjercicio) {
        this.idEstadoEjercicio = idEstadoEjercicio;
    }

    public String getCodigoBsq() {
        return codigoBsq;
    }

    public void setCodigoBsq(String codigoBsq) {
        this.codigoBsq = codigoBsq;
    }

    public String getDescripcionBsq() {
        return descripcionBsq;
    }

    public void setDescripcionBsq(String descripcionBsq) {
        this.descripcionBsq = descripcionBsq;
    }

    public List<String> getLstMensaje() {
        return lstMensaje;
    }

    public void setLstMensaje(List<String> lstMensaje) {
        this.lstMensaje = lstMensaje;
    }

    public String getEstadoInicialEjercicio() {
        return estadoInicialEjercicio;
    }

    public void setEstadoInicialEjercicio(String estadoInicialEjercicio) {
        this.estadoInicialEjercicio = estadoInicialEjercicio;
    }

    public Date getFechaInicialComplementaria() {
        return fechaInicialComplementaria;
    }

    public void setFechaInicialComplementaria(Date fechaInicialComplementaria) {
        this.fechaInicialComplementaria = fechaInicialComplementaria;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Ejercicio> lstEjerciciosAux = new LazyDataModel<Ejercicio>() {

            @Override
            public List<Ejercicio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Ejercicio> lstEjerciciosAux = ejercicioFacade.findAll(anioBsq, idEstadoEjercicioBsq, first, pageSize);

                return lstEjerciciosAux;
            }

        };
        lstEjerciciosAux.setRowCount(ejercicioFacade.countAll(anioBsq, idEstadoEjercicioBsq).intValue());
        return lstEjerciciosAux;
    }

    @Override
    public void limpiar() {
        this.setAnio(null);
        this.setFechaInicio(null);
        this.setFechaFin(null);
        this.setIdEstadoEjercicio(null);
        this.setFechaComplementaria(null);
        this.getItemsDerecha().clear();
        this.getItemsIzquierda().clear();
        this.getLstMensaje().clear();
    }

    @Override
    public String crear() {
        try {
            this.ejercicioFacade.create(this.getAnio(), this.getFechaComplementaria());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEjercicio");
            this.setMsgSuccessError("El ejercicio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al intentar crear el ejercicio: " + ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.ejercicioFacade.create(this.getAnio(), this.getFechaComplementaria());
            this.setResultado("nuevoEjercicio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al intentar crear el ejercicio: " + ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        this.lstServicio.clear();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
        Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicioAux);
        this.setEjercicio(ejercicioAux);
        this.setIdEjercicio(ejercicioAux.getId());
        this.lstServicio = ejercicioAux.getLstServicios();
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
            this.ejercicioFacade.remove(idEjercicioAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("ejercicioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
        Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicioAux);
        this.setEjercicio(ejercicioAux);
        this.setIdEstadoEjercicioActual(ejercicioAux.getEstadoEjercicio().getId());
        this.setId(ejercicioAux.getId());
        this.setAnio(ejercicioAux.getAnio());
        this.setIdEstadoEjercicio(ejercicioAux.getEstadoEjercicio().getId());
        this.setFechaComplementaria(ejercicioAux.getFechaComplementaria());
        this.setFechaInicialComplementaria(ejercicioAux.getFechaComplementaria());
        this.setEstadoInicialEjercicio(ejercicioAux.getEstadoEjercicio().getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            if (this.getIdEstadoEjercicio().equals(this.ID_ESTADO_TERMINADO) && new Date().before(this.getFechaComplementaria())) {
                throw new Exception("No se puede terminar el ejercicio porque no se llegó a la fecha complemetaria.");
            }
            if (this.getIdEstadoEjercicio().equals(this.ID_ESTADO_TERMINADO) && this.getIdEstadoEjercicioActual().equals(this.ID_ESTADO_FORMULADO)) {
                throw new Exception("No se puede cerrar un ejercicio en formulación.");
            }
            if (this.getIdEstadoEjercicio().equals(this.ID_ESTADO_FORMULADO) && this.getIdEstadoEjercicioActual().equals(this.ID_ESTADO_ACTIVO)) {
                throw new Exception("No se puede cambiar a formulación un ejercicio activo.");
            }
            if (this.getIdEstadoEjercicio().equals(this.ID_ESTADO_FORMULADO) && this.getIdEstadoEjercicioActual().equals(this.ID_ESTADO_TERMINADO)) {
                throw new Exception("No se puede cambiar a formulación un ejercicio cerrado.");
            }
            this.ejercicioFacade.edit(this.getId(), this.getIdEstadoEjercicio(), this.getFechaComplementaria());
            //inicio auditoria
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            this.setAuditoriaAnterior("Estado inicial: " + this.getEstadoInicialEjercicio() + "\n"
                    + "Fecha complementaria: " + formato.format(this.getFechaInicialComplementaria()));
            System.out.println("this.getEstadoInicialEjercicio() " + this.getEstadoInicialEjercicio());
            System.out.println("this.estadoEjercicioFacade.find(this.getIdEstadoEjercicio()).getNombre() " + this.estadoEjercicioFacade.find(this.getIdEstadoEjercicio()).getNombre());
            System.out.println("this.getEjercicio().getId() " + this.getEjercicio().getId());
            this.setAuditoriaActual("Estado actual: " + this.estadoEjercicioFacade.find(this.getIdEstadoEjercicio()).getNombre() + "\n"
                    + "Fecha complementaria: " + formato.format(this.getFechaComplementaria()));
            this.auditoriaFacade.create(AccionEnum.EDICION_EJERCICIO.getName(), new Date(), this.getUsuario().getId(), this.getAuditoriaAnterior(), this.getAuditoriaActual(), this.getEjercicio().getId());
            //FIN AUDITORIA//
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEjercicio");
            this.setMsgSuccessError("El ejercicio ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError("Error al intentar editar el ejercicio: " + ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
        return this.getResultado();
    }

    @Override
    public void actualizar() {
        this.setAnioBsq(null);
        this.setIdEstadoEjercicioBsq(null);
    }

    public void actualizarServicios() {
        this.setCodigoBsq(null);
        this.setDescripcionBsq(null);
        this.aplicarFiltroServicios();
    }

    /**
     * Carga los estado del ejercicio en las pantallas relacionadas al
     * ejercicio.
     *
     * @autor eugenio
     * @return
     */
    public List<SelectItem> getSelectItemsEstadoEjercicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<EstadoEjercicio> lstEstadoEjercicio = this.estadoEjercicioFacade.findAll("");
        if (!(lstEstadoEjercicio.isEmpty())) {
            lstEstadoEjercicio.stream().forEach((estadoEjercicioAux) -> {
                selectItems.add(new SelectItem(estadoEjercicioAux.getId(), estadoEjercicioAux.getNombre()));
            });
        }
        return selectItems;
    }

    /*Cierre ejercicio por servicio administrativo*/

    /*
     * Actualiza el la lista de Servicios Disponibles
     * @author Matias Zakowicz
     * @param event
     */
    public void updateListServicios(ValueChangeEvent event) {
        Ejercicio ejercicioAux = this.ejercicioFacade.find(Long.parseLong(event.getNewValue().toString()));
        if (ejercicioAux != null) {
            this.serviciosDisponibles(ejercicioAux);
        } else {
            this.setItemsDerecha(new ArrayList());
            this.setItemsIzquierda(new ArrayList());
        }
    }

    /**
     * Carga la lista de servicios disponibles
     *
     * @author Matias Zakowicz
     * @param ejercicio
     */
    public void serviciosDisponibles(Ejercicio ejercicio) {
        List<Servicio> lstServicioItemsAll = this.servicioFacade.findAll(true);
        this.itemsDerecha = new ArrayList<>();
        lstServicioItemsAll.removeAll(usuario.getAcciones());
        if (!(lstServicioItemsAll.isEmpty())) {
            lstServicioItemsAll.stream().forEach((actionAux) -> {
                this.itemsDerecha.add(new SelectItem(actionAux.getId(), actionAux.getDescripcion()));
            });
        }
    }

    public String crearCierreEjercicioServicio() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long id = Long.parseLong(myRequest.getParameter("idServicio"));
            List<Long> lstIdServicios = new ArrayList<>();
            lstIdServicios.add(id);
            this.setLstMensaje(this.ejercicioFacade.cerrarEjercicioServAdministrativo(this.getIdEjercicio(), lstIdServicios, this.getUsuario().getId()));
            if (this.getLstMensaje().isEmpty()) {
                  this.setEsCorrecto(true);
                this.setResultado("successErrorGestionarServicio");
                this.setTitle("Proceso completo...");
                this.setImages("fa fa-check-circle-o");
                this.setMsgSuccessError("El ejercicio ha sido cerrado con éxito.");
                //inicio auditoria
                for (Long idServicio : lstIdServicios) {
                    Servicio servicioAux = this.servicioFacade.find(idServicio);
                    this.setAuditoriaActual("Ejercicio = " + this.ejercicioFacade.find(this.getIdEjercicio()).getAnio() + "\n"
                            + "Servicio: \n" + servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura());
                    this.auditoriaFacade.create(AccionEnum.CIERRE_EJERCICIO.getName(), new Date(), this.getUsuario().getId(), "-", this.getAuditoriaActual(), this.getIdEjercicio());
                }

                //FIN AUDITORIA//
            } else {
                this.setEsCorrecto(false);
                this.setMsjExitoCierreEjercicio(false);
                this.setTitle("¡Error!");
                this.setImages("fa fa-times-circle-o");
                this.setMsgSuccessError("No es posible cerrar el ejercicio porque existen cuentas bancarias que no tienen saldo 0 con 0/100.");
                this.setResultado("successErrorGestionarServicio");
            }

        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGestionarServicio");
        }
        return this.getResultado();
    }

    /*Fin cierre ejercicio por servicio administrativo*/
    /**
     * Carga los Servicios Disponibles para Cerrar el ejercicio
     *
     * @autor Matias Zakowicz
     * @return
     */
    public List<SelectItem> getSelectItemsServicios() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicios = this.servicioFacade.findAll(true);
        lstServicios.removeAll(this.getLstServicio());
        if (!(lstServicios.isEmpty())) {
            lstServicios.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo().concat(" - ").concat(servicioAux.getAbreviatura())));
            });
        }
        this.setIdServicio(0L);
        return selectItems;
    }

    /**
     * Lista los ejercicios cuyo estado sea igual a true
     *
     * @autor eugenio
     * @return
     */
    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicio = this.ejercicioFacade.findAll(null, null);
        if (!(lstEjercicio.isEmpty())) {
            lstEjercicio.stream().forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        return selectItems;
    }

    /*Cierre del Ejercicio General */
    public String crearCierreEjercicioGeneral() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
            this.setLstMensaje(this.ejercicioFacade.cerrarEjercicio(idEjercicioAux, this.getUsuario().getId()));
            if (this.getLstMensaje().isEmpty()) {
                this.setResultado("successErrorEjercicio");
                this.setTitle("Proceso completo...");
                this.setImages("fa fa-check-circle-o");
                this.setMsgSuccessError("El ejercicio " + this.ejercicioFacade.find(idEjercicioAux).getAnio() + " ha sido cerrado con éxito.");
                //inicio auditoria
                this.setAuditoriaActual("Ejercicio : " + this.ejercicioFacade.find(idEjercicioAux).getAnio() + "\n"
                        + "Servicio: TODOS\n");
                this.auditoriaFacade.create(AccionEnum.CIERRE_EJERCICIO.getName(), new Date(), this.getUsuario().getId(), "-", this.getAuditoriaActual(), this.ejercicioFacade.find(idEjercicioAux).getId());
                //FIN AUDITORIA//
                this.setEsCorrecto(true);
            } else {
                this.setEsCorrecto(false);
                this.setTitle("¡Error!");
                this.setImages("fa fa-times-circle-o");
                this.setMsgSuccessError("No es posible cerrar el ejercicio porque hay servicios administrativos con cuentas sin saldar.");
                this.setResultado("successErrorEjercicio");
            }
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
        return this.getResultado();
    }
    /* Fin Cierre del Ejercicio General */

    /**
     * Agregar servicios al ejercicio
     *
     * @author Gonzalez Facundo
     * @param e
     */
    public void cargaServicios(ValueChangeEvent e) {
        Long idServicioAux = (Long) e.getNewValue();
        if (idServicioAux != null && idServicioAux != 0L) {
            Servicio servicio = this.servicioFacade.find(idServicioAux);
            this.getLstServicio().add(servicio);
            List<Servicio> listServicios = servicioFacade.findAll(true);
            listServicios.removeAll(this.getLstServicio());
            if (!(listServicios.isEmpty())) {
                listServicios.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((grupoAux) -> {
                    this.selectItemsServicioDisponible.add(new SelectItem(grupoAux.getId(), grupoAux.getCodigo() + "-" + grupoAux.getDescripcion()));
                });
            }

        }
        this.setIdServicio(0L);

    }

    /**
     * quita servicios de la tabla
     *
     * * @author Gonzalez Facundo
     */
    public void quitarServicio() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            //Busco servicio con el ID
            Servicio unServicio = this.servicioFacade.find(idAux);
            //Elimino la Actividad Economica de la tabla
            this.getLstServicio().remove(unServicio);
            //Elimino todos servicios del Combo
            this.selectItemsServicioDisponible.clear();
            //Busco todas las actividades Economicas Disponibles
            List<Servicio> listServicios = servicioFacade.findAll(true);
            //Elimino los servicios del combo que esta en la tabla  
            listServicios.removeAll(this.getLstServicio());
            if (!(listServicios.isEmpty())) {
                listServicios.stream().forEach((grupoAux) -> {
                    this.selectItemsServicioDisponible.add(new SelectItem(grupoAux.getId(), grupoAux.getCodigo() + "-" + grupoAux.getDescripcion()));
                });
            }
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGestionarServicio");
        }
    }

    public String guardarServicios() {
        try {
            Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
            ejercicioAux.setLstServicios(this.getLstServicio());
            this.ejercicioFacade.edit(ejercicioAux);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEjercicio");
            this.setMsgSuccessError("Se asignaron Servicios al ejercicio con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEjercicio");
        }
        return this.getResultado();
    }

    /**
     * Valida que el año a ingresar en nuevoEjercicio sea igual o mayor al año
     * actual
     *
     * @author Doroñuk Gustavo
     * @param context
     * @param validate
     * @param value
     */
    public void validarAnioNewEjercicio(FacesContext context, UIComponent validate, Object value) throws ValidatorException {
        try {
            int anioAux = (int) value;
            Date hoy = new Date();
            int anio = hoy.getYear() + 1900;
            if (anioAux < anio) {
                ((UIInput) validate).setValid(false);
                final FacesMessage msg = new FacesMessage("El año debe ser mayor o igual al actual.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } catch (Exception e) {
            System.err.println("Error: ".concat(e.getMessage()));
        }
    }

    public boolean isExcerciseCloseForService(Long idServicio) {
        try {
            if (this.cierreEjercicioFacade.findAll(this.getIdEjercicio(), idServicio).isEmpty()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public String exerciseStatus(Long idServicio) {
        try {
            String txt = "-";
            if (this.isExcerciseCloseForService(idServicio)) {
                txt = "CERRADO";
            } else {
                txt = "ACTIVO";
            }
            return txt;
        } catch (Exception e) {
            return "-";
        }
    }

    public String reOpenExercise() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("idServicio"));
            this.ejercicioFacade.reOpenExercise(this.getIdEjercicio(), idAux);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorGestionarServicio");
            this.setMsgSuccessError("Se reabrió el ejercicio con éxito.");
            //inicio auditoria
            Servicio servicioAux = this.servicioFacade.find(idAux);
            this.setAuditoriaActual("Ejercicio : " + this.ejercicioFacade.find(this.getIdEjercicio()).getAnio() + "\n"
                    + "Servicio: \n" + servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura());
            this.auditoriaFacade.create(AccionEnum.APERTURA_EJERCICIO.getName(), new Date(), this.getUsuario().getId(), "-", this.getAuditoriaActual(), this.getIdEjercicio());
            //FIN AUDITORIA//
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception e) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(e.getMessage());
            this.setResultado("successErrorGestionarServicio");
        }
        return this.getResultado();
    }

}
