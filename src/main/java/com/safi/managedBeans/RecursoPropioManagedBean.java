package com.safi.managedBeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.util.JavaScriptRunner;
import com.safi.entity.Usuario;
import com.safi.entity.Caracter;
import com.safi.entity.ClasificadorRecursoPropio;
import com.safi.entity.Ejercicio;
import com.safi.entity.InstrumentoLegal;
import com.safi.entity.Organismo;
import com.safi.entity.RecursoModificado;
import com.safi.entity.RecursoPropio;
import com.safi.entity.Servicio;
import com.safi.entity.TipoClasificadorRecursoPropio;
import com.safi.entity.TipoMoneda;
import com.safi.enums.AccionEnum;
import com.safi.enums.TipoRecursoModificadoEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.CaracterFacadeLocal;
import com.safi.facade.ClasificadorRecursoPropioFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.InstrumentoLegalFacadeLocal;
import com.safi.facade.OrganismoFacadeLocal;
import com.safi.facade.RecursoModificadoFacadeLocal;
import com.safi.facade.RecursoPropioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.facade.TipoClasificadorRecursoPropioFacadeLocal;
import com.safi.facade.TipoMonedaFacadeLocal;
import com.safi.utilidad.Utilidad;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Gonzalez Facundo,Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class RecursoPropioManagedBean extends UtilManagedBean implements Serializable {

    /**
     * Creates a new instance of RecursoPropioManagedBean
     */
    @EJB
    private RecursoPropioFacadeLocal recursoPropioFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private TipoMonedaFacadeLocal tipoMonedaFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private OrganismoFacadeLocal organismoFacade;
    @EJB
    private ClasificadorRecursoPropioFacadeLocal clasificadorRPFacade;
    @EJB
    private ClasificadorRecursoPropioFacadeLocal clasificadorRecursoPropioFacade;
    @EJB
    private TipoClasificadorRecursoPropioFacadeLocal tipoClasificadorRecursoPropioFacade;
    @EJB
    private RecursoModificadoFacadeLocal recursoModificadoFacade;
    @EJB
    private CaracterFacadeLocal caracterFacade;
    @EJB
    private InstrumentoLegalFacadeLocal instrumentoFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;

    /**
     * *************Atributos******************
     */
    private String descripcion;
    private Long idEjercicio;
    private Long idOrganismo;
    private Long idServicio;
    private Long idClasificador;
    private Long idTipoMoneda;
    private Long idClasificadorRecursoPropioPadre;
    private String clasificadorRecursoPropioStr;
    private String tipoClasificadorRecursoPropioStr;
    private Long codigo;
    private String codigoStr;
    private BigDecimal importe;
    private RecursoPropio recursoPropio; //Atributo para el detalle
    private RecursoModificado recursoModificado;
    private String concepto;
    private ClasificadorRecursoPropio clasificadorRecurso;
    private String genero;
    private String jurisdiccion;
    private String tipoRec;
    private List<RecursoModificado> listaRecursoModificado;
    private Long idCaracter;
    private Date fechaModificacion;
    private Usuario usuario;
    private Servicio servicio;
    private Date fechaAlta;
    private boolean modificacionRecursoPropio;
    private Long idInstrumentoLegal;
    /**
     * ************Fin atributos*************
     */

    /**
     * *********Atributos de Busqueda******************
     */
    private String descripcionBsq;
    private Long idEjercicioBsq;
    private Long idOrganismoBsq;
    private Long idServicioBsq;
    private Long idClasificadorBsq;
    private Long idTipoMonedaBsq;
    private Long idTipoClasificadorRecursoPropioBsq;
    private BigDecimal importeDesdeBsq;
    private BigDecimal importeHastaBsq;
    private String nroInstBsq;

    /* atributos recursos modificados*/
    private String descripcionConcepto;
    private Integer numeroInstrumento;
    private Long descripcionInstrumento;
    private BigDecimal importeRecMod;
    private Date fechaInstrumento;
    private Organismo organismo;
    final private long TIPO_MONEDA_PESOS_ARG = 1L;
    final private long ESTADO_ACTIVO = 1l;
    final private long ESTADO_FORMULACION = 3L;
    private boolean desactivarImporte;
    private TipoRecursoModificadoEnum tipoRecurso;
    final private int MAX_CANTIDAD_DE_DECIMALES = 2;

    /**
     * ********Fin atributos de busqueda*************
     */
    //atributos para optimización de carga
    private List<RecursoPropio> recursoPropioAux;
    public List<RecursoPropio> lista = new ArrayList<>();
    public int totalTuplas;
    public int first, pageSize;

    public RecursoPropioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        this.setBusqueda(true);
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setUsuario(sessionBean.getUsuario());
                this.setIdUsuarioResponsable(sessionBean.getUsuario().getId());
                this.setServicio(sessionBean.getServicio());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("modificacionRecursoPropio")) {
                            this.setModificacionRecursoPropio(true);
                        } else if (accionAux.getNombre().equalsIgnoreCase("nuevoRecursoPropio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarRecursoPropio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarRecursoPropio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleRecursoPropio"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });

                }
            } catch (Exception e) {

            }
        }
    }

    public Long getIdInstrumentoLegal() {
        return idInstrumentoLegal;
    }

    public void setIdInstrumentoLegal(Long idInstrumentoLegal) {
        this.idInstrumentoLegal = idInstrumentoLegal;
    }

    public int getTotalTuplas() {
        return totalTuplas;
    }

    public void setTotalTuplas(int totalTuplas) {
        this.totalTuplas = totalTuplas;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<RecursoPropio> getLista() {
        return lista;
    }

    public void setLista(List<RecursoPropio> lista) {
        this.lista = lista;
    }

    public List<RecursoPropio> getRecursoPropioAux(int first, int pageSize) {
        
        if (busqueda) {
            if (flagMovimiento || this.getFirst() != first || this.getPageSize() != pageSize) {
                List<RecursoPropio> lstRecursoPropiosAux = recursoPropioFacade.findAll(idEjercicioBsq,
                        idServicioBsq,
                        idOrganismoBsq,
                        idTipoMonedaBsq,
                        importeDesdeBsq,
                        importeHastaBsq,
                        descripcionBsq,
                        idClasificadorBsq,
                        idTipoClasificadorRecursoPropioBsq, first, pageSize);
                this.setTotalTuplas(                        
                        recursoPropioFacade.countAll(idEjercicioBsq,
                                idServicioBsq,
                                idOrganismoBsq,
                                idTipoMonedaBsq,
                                importeDesdeBsq,
                                importeHastaBsq,
                                descripcionBsq,
                                idClasificadorBsq,
                                idTipoClasificadorRecursoPropioBsq).intValue());
                this.setFirst(first);
                this.setPageSize(pageSize);
                this.setFlagMovimiento(false);
                this.setLista(lstRecursoPropiosAux);
            }
            return this.getLista();

        } else {
            return new ArrayList<>();
        }
    }

    public void setRecursoPropioAux(List<RecursoPropio> recursoPropioAux) {
        this.recursoPropioAux = recursoPropioAux;
    }

    public boolean isModificacionRecursoPropio() {
        return modificacionRecursoPropio;
    }

    public void setModificacionRecursoPropio(boolean modificacionRecursoPropio) {
        this.modificacionRecursoPropio = modificacionRecursoPropio;
    }

    public boolean isDesactivarImporte() {
        return desactivarImporte;
    }

    public void setDesactivarImporte(boolean desactivarImporte) {
        this.desactivarImporte = desactivarImporte;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdCaracter() {
        return idCaracter;
    }

    public void setIdCaracter(Long idCaracter) {
        this.idCaracter = idCaracter;
    }

    public String getDescripcionConcepto() {
        return descripcionConcepto;
    }

    public void setDescripcionConcepto(String descripcionConcepto) {
        this.descripcionConcepto = descripcionConcepto;
    }

    public Integer getNumeroInstrumento() {
        return numeroInstrumento;
    }

    public void setNumeroInstrumento(Integer numeroInstrumento) {
        this.numeroInstrumento = numeroInstrumento;
    }

    public Long getDescripcionInstrumento() {
        return descripcionInstrumento;
    }

    public void setDescripcionInstrumento(Long descripcionInstrumento) {

        this.descripcionInstrumento = descripcionInstrumento;
    }

    public BigDecimal getImporteRecMod() {
        return importeRecMod;
    }

    public void setImporteRecMod(BigDecimal importeRecMod) {
        this.importeRecMod = importeRecMod;
    }

    public Date getFechaInstrumento() {
        return fechaInstrumento;
    }

    public void setFechaInstrumento(Date fechaInstrumento) {
        this.fechaInstrumento = fechaInstrumento;
    }

    public List<RecursoModificado> getListaRecursoModificado() {
        return listaRecursoModificado;
    }

    public void setListaRecursoModificado(List<RecursoModificado> listaRecursoModificado) {
        this.listaRecursoModificado = listaRecursoModificado;
    }

    public String getNroInstBsq() {
        return nroInstBsq;
    }

    public void setNroInstBsq(String nroInstBsq) {
        this.nroInstBsq = nroInstBsq;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getJurisdiccion() {
        return jurisdiccion;
    }

    public void setJurisdiccion(String jurisdiccion) {
        this.jurisdiccion = jurisdiccion;
    }

    public String getTipoRec() {
        return tipoRec;
    }

    public void setTipoRec(String tipoRec) {
        this.tipoRec = tipoRec;
    }

    public ClasificadorRecursoPropio getClasificadorRecurso() {
        return clasificadorRecurso;
    }

    public void setClasificadorRecurso(ClasificadorRecursoPropio clasificadorRecurso) {
        this.clasificadorRecurso = clasificadorRecurso;
    }

    public RecursoModificado getRecursoModificado() {
        return recursoModificado;
    }

    public void setRecursoModificado(RecursoModificado recursoModificado) {
        this.recursoModificado = recursoModificado;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionBsq() {
        return descripcionBsq;
    }

    public void setDescripcionBsq(String descripcionBsq) {
        this.descripcionBsq = descripcionBsq;
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

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public Long getIdOrganismoBsq() {
        return idOrganismoBsq;
    }

    public void setIdOrganismoBsq(Long idOrganismoBsq) {
        this.idOrganismoBsq = idOrganismoBsq;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdServicioBsq() {
        return idServicioBsq;
    }

    public void setIdServicioBsq(Long idServicioBsq) {
        this.idServicioBsq = idServicioBsq;
    }

    public Long getIdClasificador() {
        return idClasificador;
    }

    public void setIdClasificador(Long idClasificador) {
        this.idClasificador = idClasificador;
    }

    public Long getIdClasificadorBsq() {
        return idClasificadorBsq;
    }

    public void setIdClasificadorBsq(Long idClasificadorBsq) {
        this.idClasificadorBsq = idClasificadorBsq;
    }

    public Long getIdClasificadorRecursoPropioPadre() {
        return idClasificadorRecursoPropioPadre;
    }

    public void setIdClasificadorRecursoPropioPadre(Long idClasificadorRecursoPropioPadre) {
        this.idClasificadorRecursoPropioPadre = idClasificadorRecursoPropioPadre;
    }

    public String getClasificadorRecursoPropioStr() {
        return clasificadorRecursoPropioStr;
    }

    public void setClasificadorRecursoPropioStr(String clasificadorRecursoPropioStr) {
        this.clasificadorRecursoPropioStr = clasificadorRecursoPropioStr;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCodigoStr() {
        return codigoStr;
    }

    public void setCodigoStr(String codigoStr) {
        this.codigoStr = codigoStr;
    }

    public Long getIdTipoClasificadorRecursoPropioBsq() {
        return idTipoClasificadorRecursoPropioBsq;
    }

    public void setIdTipoClasificadorRecursoPropioBsq(Long idTipoClasificadorRecursoPropioBsq) {
        this.idTipoClasificadorRecursoPropioBsq = idTipoClasificadorRecursoPropioBsq;
    }

    public String getTipoClasificadorRecursoPropioStr() {
        return tipoClasificadorRecursoPropioStr;
    }

    public void setTipoClasificadorRecursoPropioStr(String tipoClasificadorRecursoPropioStr) {
        this.tipoClasificadorRecursoPropioStr = tipoClasificadorRecursoPropioStr;
    }

    public Long getIdTipoMoneda() {
        return idTipoMoneda;
    }

    public void setIdTipoMoneda(Long idTipoMoneda) {
        this.idTipoMoneda = idTipoMoneda;
    }

    public Long getIdTipoMonedaBsq() {
        return idTipoMonedaBsq;
    }

    public void setIdTipoMonedaBsq(Long idTipoMonedaBsq) {
        this.idTipoMonedaBsq = idTipoMonedaBsq;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public BigDecimal getImporteDesdeBsq() {
        return importeDesdeBsq;
    }

    public void setImporteDesdeBsq(BigDecimal importeDesdeBsq) {
        this.importeDesdeBsq = importeDesdeBsq;
    }

    public BigDecimal getImporteHastaBsq() {
        return importeHastaBsq;
    }

    public void setImporteHastaBsq(BigDecimal importeHastaBsq) {
        this.importeHastaBsq = importeHastaBsq;
    }

    public RecursoPropio getRecursoPropio() {
        return recursoPropio;
    }

    public void setRecursoPropio(RecursoPropio recursoPropio) {
        this.recursoPropio = recursoPropio;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    @Override
    public LazyDataModel getListElements() {
       
        LazyDataModel<RecursoPropio> lstRecursoPropiosAux = new LazyDataModel<RecursoPropio>() {

            @Override
            public List<RecursoPropio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                return getRecursoPropioAux(first, pageSize);

            }

        };
        lstRecursoPropiosAux.setRowCount(getTotalTuplas());
        return lstRecursoPropiosAux;

    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @author Doroñuk Gustavo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true).stream()
                .filter(e -> e.getEstadoEjercicio().getId().equals(this.ESTADO_ACTIVO) || e.getEstadoEjercicio().getId().equals(this.ESTADO_FORMULACION))
                .collect(Collectors.toList());
        if (!(lstEjercicioAux.isEmpty())) {
            lstEjercicioAux.stream().sorted((p1, p2) -> (p1.getAnio().compareTo(p2.getAnio()))).forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        this.setDesactivarImporte(!this.isEjercicioEnFormulacion(idEjercicio));
        return selectItems;
    }

    /**
     * @author Gonzalez Facundo Combo ejercicio Búsqueda
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicioBsq() {
        
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true);
        if (!(lstEjercicioAux.isEmpty())) {
            lstEjercicioAux.stream().sorted((p1, p2) -> (p1.getAnio().compareTo(p2.getAnio()))).forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }

        return selectItems;
    }

    /**
     * @author Gonzalez Facundo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicioAltaRecursoPropio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true).stream()
                .filter(e -> e.getEstadoEjercicio().getId().equals(this.ESTADO_ACTIVO) || e.getEstadoEjercicio().getId().equals(this.ESTADO_FORMULACION))
                .collect(Collectors.toList());
        if (!(lstEjercicioAux.isEmpty())) {
            lstEjercicioAux.stream().sorted((p1, p2) -> (p1.getAnio().compareTo(p2.getAnio()))).forEach((ejercicioAux) -> {
                selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        this.setDesactivarImporte(!this.isEjercicioEnFormulacion(idEjercicio));
        this.setImporte(BigDecimal.ZERO);
        return selectItems;
    }

    /**
     * @author Doroñuk Gustavo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsOrganismo() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Organismo> lstOrganismo = this.organismoFacade.findAll(idServicio, true);
        if (!(lstOrganismo.isEmpty())) {
            lstOrganismo.stream().forEach((organismoAux) -> {
                selectItems.add(new SelectItem(organismoAux.getId(), organismoAux.getCodigoOrganismo() + " - " + organismoAux.getNombre()));
            });
        }

        return selectItems;
    }

    /**
     * Para el select en la búsqueda de Recursos Propios
     *
     * @author Doroñuk Gustavo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsOrganismoBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Organismo> lstOrganismo = this.organismoFacade.findAll(this.getIdServicioBsq(), true);
        if (!(lstOrganismo.isEmpty())) {
            lstOrganismo.stream().forEach((organismoAux) -> {
                selectItems.add(new SelectItem(organismoAux.getId(), organismoAux.getCodigoOrganismo() + " - " + organismoAux.getNombre()));
            });
        }
        return selectItems;
    }

    /**
     * @author Zakowicz Matias
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsClasificadorRecursoPropio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<ClasificadorRecursoPropio> listClasificadorRecursoPropio = (List<ClasificadorRecursoPropio>) this.clasificadorRecursoPropioFacade.findAll(true);
        if (!listClasificadorRecursoPropio.isEmpty()) {
            listClasificadorRecursoPropio.stream().forEach(clasificadorRecursoPropioAux -> selectItems.add(new SelectItem((Object) clasificadorRecursoPropioAux.getId(), clasificadorRecursoPropioAux.getNombre().concat(" - ").concat(clasificadorRecursoPropioAux.obtenerCodigoCompleto(clasificadorRecursoPropioAux)))));
        }
        return selectItems;
    }

    /**
     * @author Zakowicz Matias
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsTipoClasificaorRecursoPropio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoClasificadorRecursoPropio> listTipoClasificadorRecursoPropio = (List<TipoClasificadorRecursoPropio>) this.tipoClasificadorRecursoPropioFacade.findAll(true);
        if (!listTipoClasificadorRecursoPropio.isEmpty()) {
            listTipoClasificadorRecursoPropio.stream().forEach(tipoClasificadorRecursoPropioAux -> selectItems.add(new SelectItem((Object) tipoClasificadorRecursoPropioAux.getId(), tipoClasificadorRecursoPropioAux.getNombre())));
        }
        return selectItems;
    }

    /**
     * @author Zakowicz Matias
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsTipoMoneda() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoMoneda> listTipoMoneda = (List<TipoMoneda>) this.tipoMonedaFacade.findAll(true);
        if (!listTipoMoneda.isEmpty()) {
            listTipoMoneda.stream().forEach(tipoMonedaAux -> selectItems.add(new SelectItem((Object) tipoMonedaAux.getId(), tipoMonedaAux.getNombre())));
        }
        return selectItems;
    }

    /**
     * @author Doroñuk Gustavo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsServicio() {
        this.setIdOrganismo(0L);
        this.getSelectItemsOrganismo();
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = servicioFacade.findAll(this.getIdEjercicio());
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigoAbreviatura()));
            });
        }
        return selectItems;
    }

    /**
     * Para el select en la búsqueda de Recursos Propios
     *
     * @author Doroñuk Gustavo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsServicioBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = servicioFacade.findAll(this.getIdEjercicioBsq());
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsClasificador() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<ClasificadorRecursoPropio> lstClasificadorRP = this.clasificadorRPFacade.findAll(true);
        if (!(lstClasificadorRP.isEmpty())) {
            lstClasificadorRP.stream().filter((clasificador) -> (clasificador.getTipoClasificadorRecursoPropio().getId().equals(4L)))
                    .forEach((clasificadorRPAux) -> {
                        selectItems.add(new SelectItem(clasificadorRPAux.getId(), clasificadorRPAux.obtenerCodigoCompleto(clasificadorRPAux) + " - " + clasificadorRPAux.getNombre()));
                    });
        }
        return selectItems;
    }

    /**
     * @author Gonzalez Facundo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsCaracter() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Caracter> lstCaracterRP = this.caracterFacade.findAll();
        if (!(lstCaracterRP.isEmpty())) {
            lstCaracterRP.stream().forEach((lstCaracterRPAux) -> {
                selectItems.add(new SelectItem(lstCaracterRPAux.getId(), lstCaracterRPAux.getId() + "-" + lstCaracterRPAux.getNombre()));
            });
        }
        return selectItems;
    }

    @Override
    public void limpiar() {
        this.setDescripcion(null);
        this.setIdEjercicio(null);
        this.setIdOrganismo(null);
        this.setIdServicio(null);
        this.setIdClasificador(null);
        this.setRecursoPropio(null);
        this.setListaRecursoModificado(null);
        this.setDescripcionConcepto(null);
        this.setNumeroInstrumento(null);
        this.setFechaInstrumento(null);
        this.setDescripcionInstrumento(null);
        this.setImporteRecMod(null);
        this.setNroInstBsq(null);
        this.setFechaModificacion(null);
        this.setIdTipoMoneda(null);
        this.setIdCaracter(null);
        this.setImporte(null);
        this.setConcepto(null);
        this.setIdClasificadorRecursoPropioPadre(null);
        this.setOrganismo(null);
        this.aplicarFiltro();
    }

    public void limpiarCrearOtroRecuMod() {
        this.setIdEjercicio(null);
        this.setIdOrganismo(null);
        this.setIdServicio(null);
        this.setIdClasificador(null);
        this.setListaRecursoModificado(null);
        this.setDescripcionConcepto(null);
        this.setNumeroInstrumento(null);
        this.setFechaInstrumento(null);
        this.setDescripcionInstrumento(null);
        this.setImporteRecMod(null);
        this.setNroInstBsq(null);
        this.setFechaModificacion(null);
        this.setIdCaracter(null);
        this.setImporte(null);
        this.setIdInstrumentoLegal(0L);
        this.setIdClasificadorRecursoPropioPadre(null);

    }

    @Override
    public String crear() {
        try {
            this.setServicio(this.servicioFacade.find(this.getIdServicio() == null ? this.getUsuario().getServicio().getId() : this.getIdServicio()));
            this.getUsuario().setServicio(this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio());
            if (!this.isEjercicioEnFormulacion(idEjercicio)) {
                this.setImporte(BigDecimal.ZERO);
            }
            this.recursoPropioFacade.create(
                    this.getIdEjercicio(),
                    this.getIdServicio(),
                    this.getIdOrganismo(),
                    this.TIPO_MONEDA_PESOS_ARG,
                    this.getImporte(),
                    this.getDescripcion(),
                    this.getIdClasificador(),
                    this.getConcepto(),
                    this.getIdCaracter());
            //INICIO AUDITORIA//
            Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
            Servicio servicioAux = this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio();
            Organismo organismoAux = this.organismoFacade.find(this.getIdOrganismo());
            String organismoElegido = Utilidad.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre();
            Caracter caracterAux = this.caracterFacade.find(this.getIdCaracter());
            String caracterElegido = caracterAux.getId() + " - " + caracterAux.getNombre();
            ClasificadorRecursoPropio clasificadorAux = this.clasificadorRecursoPropioFacade.find(this.getIdClasificador());
            String clasificadorElegido = clasificadorAux.obtenerCodigoCompleto(clasificadorAux) + " - " + clasificadorAux.getNombre();
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(this.TIPO_MONEDA_PESOS_ARG);
            this.setAuditoriaActual(
                    "Ejercicio: " + ejercicioAux.getAnio() + "\n"
                    + "Servicio: " + servicioAux.getCodigoAbreviatura() + "\n"
                    + "Organismo: " + organismoElegido + "\n"
                    + "Carácter: " + caracterElegido + "\n"
                    + "Clasificador: " + clasificadorElegido + "\n"
                    + "Concepto: " + this.getConcepto() + "\n"
                    + "Descripción: " + this.getDescripcion().toUpperCase() + "\n"
                    + "Tipo Moneda: " + tipoMonedaAux.getNombre() + "\n"
                    + "Importe Original: " + this.getImporte());
            this.auditoriaFacade.create(AccionEnum.ALTA_RECURSO_PROPIO.getName(), new Date(), this.getIdUsuarioResponsable(), "-", this.getAuditoriaActual(), ejercicioAux.getId());
            this.limpiarAuditorias();
            //FIN AUDITORIA//
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorRecursoPropio");
            this.setMsgSuccessError("El recurso propio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            String abreviaturaServicio="";
            this.getUsuario().setServicio(this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio());
            this.recursoPropioFacade.create(this.getIdEjercicio(), this.getIdServicio(),
                    this.getIdOrganismo(), this.TIPO_MONEDA_PESOS_ARG, this.getImporte(),
                    this.getDescripcion(), this.getIdClasificador(), this.getConcepto(), this.getIdCaracter());
            //INICIO AUDITORIA//
            Ejercicio ejercicioAux = this.ejercicioFacade.find(this.getIdEjercicio());
            Servicio servicioAux = this.getUsuario().getServicio() == null ? this.getServicio() : this.getUsuario().getServicio();            
            if (servicioAux != null){
                abreviaturaServicio=servicioAux.getAbreviatura();
            }else{
                abreviaturaServicio="";
            }                    
            Organismo organismoAux = this.organismoFacade.find(this.getIdOrganismo());
            String organismoElegido = Utilidad.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre();
            Caracter caracterAux = this.caracterFacade.find(this.getIdCaracter());
            String caracterElegido = caracterAux.getId() + " - " + caracterAux.getNombre();
            ClasificadorRecursoPropio clasificadorAux = this.clasificadorRecursoPropioFacade.find(this.getIdClasificador());
            String clasificadorElegido = clasificadorAux.obtenerCodigoCompleto(clasificadorAux) + " - " + clasificadorAux.getNombre();
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(this.TIPO_MONEDA_PESOS_ARG);
            this.setAuditoriaActual(
                    "Ejercicio: " + ejercicioAux.getAnio() + "\n"
                    + "Servicio: " + abreviaturaServicio + "\n"
                    + "Organismo: " + organismoElegido + "\n"
                    + "Carácter: " + caracterElegido + "\n"
                    + "Clasificador: " + clasificadorElegido + "\n"
                    + "Concepto: " + this.getConcepto() + "\n"
                    + "Descripción: " + this.getDescripcion().toUpperCase() + "\n"
                    + "Tipo Moneda: " + tipoMonedaAux.getNombre() + "\n"
                    + "Importe Original: " + this.getImporte());
            this.auditoriaFacade.create(AccionEnum.ALTA_RECURSO_PROPIO.getName(), new Date(), this.getIdUsuarioResponsable(), "-", this.getAuditoriaActual(), ejercicioAux.getId());
            this.limpiarAuditorias();
            //FIN AUDITORIA//
            this.setResultado("nuevoRecursoPropio");
            this.setIdTipoMoneda(null);
            this.setImporte(null);
            this.setDescripcion(null);
            this.setIdClasificadorRecursoPropioPadre(null);
            this.setEsCorrecto(true);
            this.limpiar();
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(idRecursoPropioAux);
        this.setId(recursoPropioAux.getId());
        this.setRecursoPropio(recursoPropioAux);
        this.setClasificadorRecurso(recursoPropioAux.getClasificadorRecurso());
        this.setGenero(this.getClasificadorRecurso().obtenerCodigoCompleto(this.getClasificadorRecurso()));
        this.setConcepto(recursoPropioAux.getConcepto());

    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
            //INICIO AUDITORIA//
            RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(idRecursoPropioAux);
            this.setAuditoriaAnterior(
                    "Ejercicio: " + recursoPropioAux.getEjercicio().getAnio() + "\n"
                    + "Servicio: " + recursoPropioAux.getServicio().getCodigoAbreviatura() + "\n"
                    + "Organismo: " + Utilidad.ceroIzquierda(recursoPropioAux.getOrganismo().getCodigoOrganismo()) + " - " + recursoPropioAux.getOrganismo().getNombre() + "\n"
                    + "Carácter: " + recursoPropioAux.getCaracter().getId() + " - " + recursoPropioAux.getCaracter().getNombre() + "\n"
                    + "Clasificador: " + recursoPropioAux.getClasificadorRecurso().obtenerCodigoCompleto(recursoPropioAux.getClasificadorRecurso()) + " - " + recursoPropioAux.getClasificadorRecurso().getNombre() + "\n"
                    + "Concepto: " + recursoPropioAux.getConcepto() + "\n"
                    + "Descripción: " + recursoPropioAux.getDescripcion().toUpperCase() + "\n"
                    + "Tipo Moneda: " + recursoPropioAux.getTipoMoneda().getNombre() + "\n"
                    + "Importe Original: " + recursoPropioAux.getImporteOriginal() + "\n"
                    + "Importe Actual: " + recursoPropioAux.getImporteActual());
            //FIN AUDITORIA//
            this.recursoPropioFacade.remove(idRecursoPropioAux);
            this.auditoriaFacade.create(AccionEnum.ELIMINACION_RECURSO_PROPIO.getName(), new Date(), this.getIdUsuarioResponsable(), this.getAuditoriaAnterior(), "-", recursoPropioAux.getEjercicio().getId());
            this.limpiarAuditorias();
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setBusqueda(true);
            this.aplicarFiltro();
            this.setResultado("recursoPropioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }
    }

    @Override
    public void prepararParaEditar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
            RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(idRecursoPropioAux);
            this.setId(recursoPropioAux.getId());
            this.setIdEjercicio(recursoPropioAux.getEjercicio().getId());
            this.setIdServicio(recursoPropioAux.getServicio().getId());
            this.setIdOrganismo(recursoPropioAux.getOrganismo().getId());
            this.setIdTipoMoneda(recursoPropioAux.getTipoMoneda().getId());
            this.setImporte(recursoPropioAux.getImporteOriginal());
            this.setDescripcion(recursoPropioAux.getDescripcion());
            this.setConcepto(recursoPropioAux.getConcepto());
            this.setIdClasificador(recursoPropioAux.getClasificadorRecurso().getId());
            this.setIdCaracter(recursoPropioAux.getCaracter().getId());
            this.setFechaAlta(recursoPropioAux.getFechaAlta());
            if (recursoPropioAux.getClasificadorRecurso().getClasificadorRecursoPropio() != null) {
                this.setIdClasificadorRecursoPropioPadre(recursoPropioAux.getClasificadorRecurso().getClasificadorRecursoPropio().getId());
            }
            //INICIO AUDITORIA//
            this.setAuditoriaAnterior(
                    "Ejercicio: " + recursoPropioAux.getEjercicio().getAnio() + "\n"
                    + "Servicio: " + recursoPropioAux.getServicio().getCodigoAbreviatura() + "\n"
                    + "Organismo: " + Utilidad.ceroIzquierda(recursoPropioAux.getOrganismo().getCodigoOrganismo()) + " - " + recursoPropioAux.getOrganismo().getNombre() + "\n"
                    + "Carácter: " + recursoPropioAux.getCaracter().getId() + " - " + recursoPropioAux.getCaracter().getNombre() + "\n"
                    + "Clasificador: " + recursoPropioAux.getClasificadorRecurso().obtenerCodigoCompleto(recursoPropioAux.getClasificadorRecurso()) + " - " + recursoPropioAux.getClasificadorRecurso().getNombre() + "\n"
                    + "Concepto: " + recursoPropioAux.getConcepto() + "\n"
                    + "Descripción: " + recursoPropioAux.getDescripcion().toUpperCase() + "\n"
                    + "Tipo Moneda: " + recursoPropioAux.getTipoMoneda().getNombre() + "\n"
                    + "Importe Original: " + recursoPropioAux.getImporteOriginal() + "\n"
                    + "Importe Actual: " + recursoPropioAux.getImporteActual());
            //FIN AUDITORIA//
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            this.recursoPropioFacade.edit(this.getId(), this.getIdEjercicio(), this.getIdServicio(),
                    this.getIdOrganismo(), this.TIPO_MONEDA_PESOS_ARG, this.getImporte(),
                    this.getDescripcion(), this.getIdClasificador(), this.completarConcepto(this.getConcepto()), this.getIdCaracter());
            //INICIO AUDITORIA//
            RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(this.getId());
            this.setAuditoriaActual(
                    "Ejercicio: " + recursoPropioAux.getEjercicio().getAnio() + "\n"
                    + "Servicio: " + recursoPropioAux.getServicio().getCodigoAbreviatura() + "\n"
                    + "Organismo: " + Utilidad.ceroIzquierda(recursoPropioAux.getOrganismo().getCodigoOrganismo()) + " - " + recursoPropioAux.getOrganismo().getNombre() + "\n"
                    + "Carácter: " + recursoPropioAux.getCaracter().getId() + " - " + recursoPropioAux.getCaracter().getNombre() + "\n"
                    + "Clasificador: " + recursoPropioAux.getClasificadorRecurso().obtenerCodigoCompleto(recursoPropioAux.getClasificadorRecurso()) + " - " + recursoPropioAux.getClasificadorRecurso().getNombre() + "\n"
                    + "Concepto: " + recursoPropioAux.getConcepto() + "\n"
                    + "Descripción: " + recursoPropioAux.getDescripcion().toUpperCase() + "\n"
                    + "Tipo Moneda: " + recursoPropioAux.getTipoMoneda().getNombre() + "\n"
                    + "Importe Original: " + recursoPropioAux.getImporteOriginal() + "\n"
                    + "Importe Actual: " + recursoPropioAux.getImporteActual());
            this.auditoriaFacade.create(AccionEnum.EDICION_RECURSO_PROPIO.getName(), new Date(), this.getIdUsuarioResponsable(), this.getAuditoriaAnterior(), this.getAuditoriaActual(), recursoPropioAux.getEjercicio().getId());
            this.limpiarAuditorias();
            this.aplicarFiltro();
            //FIN AUDITORIA//            
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorRecursoPropio");
            this.setMsgSuccessError("El recurso propio ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public void actualizar() {
        try {
            this.setDescripcionBsq("");
            this.setIdEjercicioBsq(null);
            this.setIdOrganismoBsq(null);
            this.setIdServicioBsq(null);
            this.setIdTipoClasificadorRecursoPropioBsq(null);
            this.setIdTipoMonedaBsq(null);
            this.setIdClasificadorBsq(null);
            this.setImporteDesdeBsq(null);
            this.setImporteHastaBsq(null);
            this.setNroInstBsq("");
            this.aplicarFiltroRecMod();
            super.actualizar();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void limpiarComboClasificadorRecursoPropioPadre(ValueChangeEvent e) {
        Object value = e.getNewValue();
        this.setIdClasificadorRecursoPropioPadre((Long) value);

    }

    //detalle del recurso modificado del recurso propio    
    public void verDetalleRecMod() {
        this.limpiar();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(idRecursoPropioAux);
        this.setId(recursoPropioAux.getId());
        this.setRecursoPropio(recursoPropioAux);
        this.setClasificadorRecurso(recursoPropioAux.getClasificadorRecurso());
        this.setGenero(this.getClasificadorRecurso().obtenerCodigoCompleto(this.getClasificadorRecurso()));
        this.setConcepto(recursoPropioAux.getConcepto());
        this.setDescripcion(recursoPropioAux.getDescripcion());
        this.setListaRecursoModificado(recursoPropioAux.getLstRecursoModificado());
        this.setIdTipoMoneda(recursoPropioAux.getTipoMoneda().getId());
        this.setFechaAlta(recursoPropioAux.getFechaAlta());
        this.setOrganismo(recursoPropioAux.getOrganismo());
        this.focusFechaModificacion();
    }

    //búsqueda instrumento legal recursos modificados
    public void aplicarFiltroRecMod() {
        try {
            this.setListaRecursoModificado(
                    !this.getNroInstBsq().equals("")
                            ? this.getListaRecursoModificado()
                            .stream()
                            .filter(RecursoModificado -> RecursoModificado.getInstrumentoLegal().getNumero().equals(this.getNroInstBsq()))
                            .collect(Collectors.toList())
                            : this.getRecursoPropio().getLstRecursoModificado()
            );
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    //creamos el recurso modificado del recurso propio al cual modifica su importe actual.
    public String crearRecursoModificado() {
        try {
            RecursoModificado recursoModificadoAux = new RecursoModificado();
            recursoModificadoAux.setDescripcion(descripcionConcepto);
            recursoModificadoAux.setFechaMoficacion(this.getFechaModificacion());
            recursoModificadoAux.setTipoMoneda(this.tipoMonedaFacade.find(this.TIPO_MONEDA_PESOS_ARG));
            InstrumentoLegal instrumentoLegal = new InstrumentoLegal();
            instrumentoLegal.setId(this.getIdInstrumentoLegal());
            instrumentoLegal.setDescripcion(TipoRecursoModificadoEnum.getById(this.getIdInstrumentoLegal()).getName());
            instrumentoLegal.setEstado(true);
            instrumentoLegal.setNumero(numeroInstrumento.toString());
            this.instrumentoFacade.create(instrumentoLegal);
            recursoModificadoAux.setInstrumentoLegal(this.instrumentoFacade.find(instrumentoLegal.getId()));
            recursoModificadoAux.setImporte(importeRecMod);
            BigDecimal importeAct = this.getRecursoPropio().getImporteActual().add(importeRecMod);
            //editamos el importe del recurso propio
            RecursoPropio recursoPropioAux = recursoPropioFacade.find(this.recursoPropio.getId());

            //INICIO AUDITORÍA
            String recursoElegido = recursoPropioAux.getOrganismo().getClasificadorOrganismo().getId() + "."
                    + Utilidad.ceroIzquierda(recursoPropioAux.getOrganismo().getCodigoOrganismo()) + "."
                    + recursoPropioAux.getCaracter().getId() + "."
                    + recursoPropioAux.getClasificadorRecurso().obtenerCodigoCompleto(recursoPropioAux.getClasificadorRecurso()) + "."
                    + recursoPropioAux.getConcepto() + " - "
                    + recursoPropioAux.getDescripcion();
            this.setAuditoriaAnterior(
                    "Recurso Propio: " + recursoElegido + "\n"
                    + "Importe Original: " + recursoPropioAux.getImporteOriginal() + "\n"
                    + "Importe Actual: " + recursoPropioAux.getImporteActual());
            //FIN AUDITORÍA
            recursoPropioAux.setImporteActual(importeAct);//sumamos el importe al importe actual del recurso propio                    
            this.recursoPropioFacade.edit(recursoPropioAux);
            //seteamos el recurso propio al recurso modificado
            recursoModificadoAux.setRecursoPropio(this.getRecursoPropio());
            recursoModificadoAux.setFechaAlta(new Date());
            if (this.recursoModificadoFacade.maximoNroModificacion(recursoPropioAux.getId()) != null) {
                recursoModificadoAux.setNumeroModificacion(this.recursoModificadoFacade.maximoNroModificacion(recursoPropioAux.getId()) + 1);//proxima modificación
            } else {
                recursoModificadoAux.setNumeroModificacion(1);
            }
            recursoModificadoAux.setEstado(true);
            this.recursoModificadoFacade.create(recursoModificadoAux);
            //INICIO AUDITORIA//
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            this.setAuditoriaActual(
                    "Recurso Propio: " + recursoElegido + "\n"
                    + "Importe Original: " + recursoPropioAux.getImporteOriginal() + "\n"
                    + "Importe Actual: " + recursoPropioAux.getImporteActual() + "\n"
                    + "Fecha Modificacion: " + formato.format(this.getFechaModificacion()) + "\n"
                    + "Instrumento Legal: N° " + instrumentoLegal.getNumero() + " - " + instrumentoLegal.getDescripcion() + "\n"
                    + "Importe: " + this.getImporteRecMod());
            this.auditoriaFacade.create(AccionEnum.ALTA_MODIFICACION_RECURSO_PROPIO.getName(), new Date(), this.getIdUsuarioResponsable(), this.getAuditoriaAnterior(), this.getAuditoriaActual(), recursoPropioAux.getEjercicio().getId());
            this.limpiarAuditorias();
            //FIN AUDITORIA//            
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorRecursoPropio");
            this.setMsgSuccessError("El recurso modificado ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }

        return this.getResultado();

    }

    public String crearOtroRecursoModificado() {
        try {
            RecursoModificado recursoModificadoAux = new RecursoModificado();
            recursoModificadoAux.setDescripcion(descripcionConcepto);
            recursoModificadoAux.setFechaMoficacion(this.getFechaModificacion());
            recursoModificadoAux.setTipoMoneda(this.tipoMonedaFacade.find(this.TIPO_MONEDA_PESOS_ARG));
            System.out.println(this.getIdInstrumentoLegal());
            InstrumentoLegal instrumentoLegal = new InstrumentoLegal();
            instrumentoLegal.setId(this.getIdInstrumentoLegal());
            instrumentoLegal.setDescripcion(TipoRecursoModificadoEnum.getById(this.getIdInstrumentoLegal()).getName());
            instrumentoLegal.setEstado(true);
            instrumentoLegal.setNumero(numeroInstrumento.toString());
            this.instrumentoFacade.create(instrumentoLegal);
            recursoModificadoAux.setInstrumentoLegal(this.instrumentoFacade.find(instrumentoLegal.getId()));
            recursoModificadoAux.setImporte(importeRecMod);
            BigDecimal importeAct = this.getRecursoPropio().getImporteActual().add(importeRecMod);
            //editamos el importe del recurso propio
            RecursoPropio recursoPropioAux = recursoPropioFacade.find(this.recursoPropio.getId());
            recursoPropioAux.setImporteActual(importeAct);//sumamos el importe al importe actual del recurso propio
            this.recursoPropioFacade.edit(recursoPropioAux);
            //fin edición recurso propio
            //seteamos el recurso propio al recurso modificado
            recursoModificadoAux.setRecursoPropio(this.getRecursoPropio());
            recursoModificadoAux.setFechaAlta(new Date());
            if (this.recursoModificadoFacade.maximoNroModificacion(recursoPropioAux.getId()) != null) {
                recursoModificadoAux.setNumeroModificacion(this.recursoModificadoFacade.maximoNroModificacion(recursoPropioAux.getId()) + 1);//proxima modificación
            } else {
                recursoModificadoAux.setNumeroModificacion(1);
            }
            recursoModificadoAux.setEstado(true);
            this.recursoModificadoFacade.create(recursoModificadoAux);
            this.limpiarCrearOtroRecuMod();
            this.focusFechaModificacion();
            this.actualizarRecurso();
            this.setResultado("nuevoRecursoModificado");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRecursoPropio");
        }

        return this.getResultado();

    }

//para la edición
    /**
     * @author Gonzalez Facundo
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsServicioEdicion() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = this.ejercicioFacade.findAllServ(this.getIdEjercicio(), true);
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
            });
        }
        return selectItems;
    }

    public String ceroIzquierda(Long codigo) {
        return Utilidad.ceroIzquierda(codigo);
    }

    public Double sumatoriaRecursoModificado(Integer idRecursoPropioAux) {
        double retorno = 0.0;
        RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(idRecursoPropioAux);
        for (RecursoModificado rm : recursoPropioAux.getLstRecursoModificado()) {
            if (rm.getImporte() != null) {
                retorno = retorno + rm.getImporte().doubleValue();
            }
        }
        this.setListaRecursoModificado(recursoPropioAux.getLstRecursoModificado());
        return retorno;
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
    public void validarFechaRecModificado(final FacesContext context, final UIComponent validate, final Object value) throws Exception {
        if ((Date) value != null) {
            Date fechaModificacion = (Date) value;
            if (fechaModificacion.after(new Date())) {
                final FacesMessage msg = new FacesMessage("La fecha de alta no debe superar a la fecha actual.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
            if (fechaModificacion.getYear() != new Date().getYear()) {
                final FacesMessage msg = new FacesMessage("La modificación debe realizarse dentro del ejercicio actual.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }

        }
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Valida que el importe del recurso modificado no deje al recurso en
     * un valor negativo
     *
     * @param context
     * @param validate
     * @param value
     */
    public void validarImporteRecursoModificado(final FacesContext context, final UIComponent validate, final Object value) throws Exception {
        if ((BigDecimal) value != null) {
            BigDecimal importeRecModificado = (BigDecimal) value;
            BigDecimal importeRecPropio = this.recursoPropioFacade.find(this.getRecursoPropio().getId()).getImporteActual();
            BigDecimal resultado = importeRecPropio.add(importeRecModificado);
            if (resultado.doubleValue() < 0) {
                final FacesMessage msg = new FacesMessage("El monto de la modificación no debe dejar negativo al recurso propio.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }

            if (getNumberOfDecimalPlaces(importeRecModificado) > MAX_CANTIDAD_DE_DECIMALES) {
                final FacesMessage msg = new FacesMessage("Solo " + MAX_CANTIDAD_DE_DECIMALES + " decimales por importe.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Valida los decimales del importe de recurso propio
     *
     * @param context
     * @param validate
     * @param value
     */
    public void validarImporteRecursoPropio(final FacesContext context, final UIComponent validate, final Object value) throws Exception {
        if ((BigDecimal) value != null) {
            BigDecimal importeRecModificado = (BigDecimal) value;
            if (getNumberOfDecimalPlaces(importeRecModificado) > MAX_CANTIDAD_DE_DECIMALES) {
                final FacesMessage msg = new FacesMessage("Solo " + MAX_CANTIDAD_DE_DECIMALES + " decimales por importe.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
             if (importeRecModificado.doubleValue() < 0) {
                final FacesMessage msg = new FacesMessage("El importe no puede ser negativo");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }

    }

    /**
     * author Gonzalez Facundo
     *
     * @see Devuelve el total de decimales de un BigDecimal.
     */
    private int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }

    /**
     * author Gonzalez Facundo
     *
     * @see Setea el focus en fecha de modificacion en nuevoRecursoModificado.
     *
     */
    public void focusFechaModificacion() {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "setCursor();");
    }

    /**
     * author Gonzalez Facundo
     *
     * @see para saber si el ejercicio esta en formulación.
     * @param idEjercicio
     */
    //
    public boolean isEjercicioEnFormulacion(Long idEjercicio) {
        boolean estaEnFormulacion = false;
        if (this.idEjercicio != null) {
            estaEnFormulacion = this.ejercicioFacade.find(idEjercicio).getEstadoEjercicio().getId().equals(this.ESTADO_FORMULACION);
        }
        return estaEnFormulacion;
    }

    /**
     * author Gonzalez Facundo
     *
     * @see No se debe borrar un recurso propio ni editar su clasificador si
     * tiene movimientos o rec. modificados.
     * @param idRecurso
     */
    public boolean tieneMovimientos_RecModificados(Long idRecurso) {
        RecursoPropio recursoAux = this.recursoPropioFacade.find(idRecurso);
        return !recursoAux.getLstMovimientos().isEmpty() || !recursoAux.getLstRecursoModificado().isEmpty();
    }

    //si el concepto es menor a 10 lo completa con cero a la izquierda.
    private String completarConcepto(String concepto) {
        if (Integer.valueOf(concepto) < 10 && concepto.length() < 2) {
            return (String) Utilidad.ceroIzquierda(Long.valueOf(concepto));
        } else {
            return concepto;
        }

    }

    /**
     * @author Gonzalez Facundo
     * @see Actualiza recurso en pantalla de nuevo recurso modificado.
     */
    public void actualizarRecurso() {
        RecursoPropio recursoPropioAux = this.recursoPropioFacade.find(this.getRecursoPropio().getId());
        this.setRecursoPropio(recursoPropioAux);
        this.setClasificadorRecurso(recursoPropioAux.getClasificadorRecurso());
        this.setGenero(this.getClasificadorRecurso().obtenerCodigoCompleto(this.getClasificadorRecurso()));
        this.setConcepto(recursoPropioAux.getConcepto());
        this.setDescripcion(recursoPropioAux.getDescripcion());
        this.setListaRecursoModificado(recursoPropioAux.getLstRecursoModificado());
        this.setIdTipoMoneda(recursoPropioAux.getTipoMoneda().getId());
        this.setFechaAlta(recursoPropioAux.getFechaAlta());
        this.setOrganismo(recursoPropioAux.getOrganismo());
    }

    /**
     * @author Gonzalez Facundo
     * @see ComboBox instrumentos legales
     */
    public List<SelectItem> getSelectItemsInstrumentosLegales() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<InstrumentoLegal> instrumentos = new ArrayList<>();
        for (int i = 0; i < this.tipoRecurso.values().length; i++) {
            InstrumentoLegal instrumento = new InstrumentoLegal();
            instrumento.setId(tipoRecurso.values()[i].getId());
            instrumento.setDescripcion(tipoRecurso.values()[i].getName());
            instrumentos.add(instrumento);
        }
        if (!(instrumentos.isEmpty())) {
            instrumentos.stream().sorted((p1, p2) -> (p1.getId().compareTo(p2.getId()))).forEach((instrumentoAux) -> {
                selectItems.add(new SelectItem(instrumentoAux.getId(), instrumentoAux.getDescripcion()));
            });
        }
        return selectItems;
    }

    /**
     * author Gonzalez Facundo
     *
     * @see No se debe editar un recurso propio si tiene ejercicio en
     * formulación
     * @param idRecurso
     * @return boolean
     */
    public boolean tieneEjercicioEnFormulacion(Long idRecurso) {
        RecursoPropio recursoAux = this.recursoPropioFacade.find(idRecurso);
        return recursoAux.getEjercicio().getEstadoEjercicio().getId().equals(ESTADO_FORMULACION);
    }

}
