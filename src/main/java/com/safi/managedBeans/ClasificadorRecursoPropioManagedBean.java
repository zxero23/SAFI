package com.safi.managedBeans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import com.safi.entity.TipoClasificadorRecursoPropio;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import com.safi.entity.ClasificadorRecursoPropio;
import javax.annotation.PostConstruct;
import com.safi.managedBeans.WebManagedBean;
import java.util.ArrayList;
import java.util.List;
import com.safi.facade.TipoClasificadorRecursoPropioFacadeLocal;
import javax.ejb.EJB;
import com.safi.facade.ClasificadorRecursoPropioFacadeLocal;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateCreationCallback;
import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.util.JavaScriptRunner;
import com.safi.entity.Ejercicio;
import com.safi.entity.RecursoPropio;
import com.safi.entity.Servicio;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.RecursoPropioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class ClasificadorRecursoPropioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private RecursoPropioFacadeLocal recursoPropioFacade;
    @EJB
    private ClasificadorRecursoPropioFacadeLocal clasificadorRecursoPropioFacade;
    @EJB
    private TipoClasificadorRecursoPropioFacadeLocal tipoClasificadorRecursoPropioFacade;
    private Long idClasificadorRecursoPropioPadre;
    private String clasificadorRecursoPropioStr;
    private Long idTipoClasificadorRecursoPropio;
    private String tipoClasificadorRecursoPropioStr;
    private Long codigo;
    private String codigoStr;
    private String nombre;
    private String nombreBsq;
    private Long codigoBsq;
    private Long idTipoClasificadorRecursoPropioBsq;
    private Long idClasificadorRecursoPropioPadreBsq;
    private NodeStateMap stateMap;
    private transient NodeStateCreationCallback contractProvinceInit = new TreeNodeStateCreationCallback();
    private List<RecursoPropio> recursosPropios;
    private List<SelectItem> selectItemsServicio;
    private List<SelectItem> selectItemsEjercicio;
    private Long idEjercicio = 1L;
    private Long idServicio = 0L;
    private String codigoServicio;
    private String abreviaturaServicio;
    private final Long ESTADO_ACTIVO=1L;
   

    public List<ClasificadorRecursoPropio> getTreeRoots() {
        return this.clasificadorRecursoPropioFacade.findAll(null, this.getCodigoBsq(), this.getIdTipoClasificadorRecursoPropioBsq(), this.getNombreBsq());
    }

    public void print(String text) {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
                "alert('" + text + "');");
    }

    public NodeStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(NodeStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public NodeStateCreationCallback getContractProvinceInit() {
        return contractProvinceInit;
    }

    public void setContractProvinceInit(NodeStateCreationCallback contractProvinceInit) {
        this.contractProvinceInit = contractProvinceInit;
    }

    public Long getIdClasificadorRecursoPropioPadreBsq() {
        return idClasificadorRecursoPropioPadreBsq;
    }

    public void setIdClasificadorRecursoPropioPadreBsq(Long idClasificadorRecursoPropioPadreBsq) {
        this.idClasificadorRecursoPropioPadreBsq = idClasificadorRecursoPropioPadreBsq;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    public List<RecursoPropio> getRecursosPropios() {
        return recursosPropios;
    }

    public void setRecursosPropios(List<RecursoPropio> recursosPropios) {
        this.recursosPropios = recursosPropios;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(String codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getAbreviaturaServicio() {
        return abreviaturaServicio;
    }

    public void setAbreviaturaServicio(String abreviaturaServicio) {
        this.abreviaturaServicio = abreviaturaServicio;
    }
    /* Proxy method to avoid JBossEL accessing stateMap like map for method invocations */

    public List getSelected() {
        if (stateMap == null) {
            return Collections.emptyList();
        }
        return stateMap.getSelected();
    }

    private static class TreeNodeStateCreationCallback implements NodeStateCreationCallback, Serializable {

        @Override
        public NodeState initializeState(NodeState newState, Object node) {
            ClasificadorRecursoPropio loc = (ClasificadorRecursoPropio) node;
            if (loc.getTipoClasificadorRecursoPropio().getNombre().equals("country")) {
                newState.setExpanded(true);
            }
            return newState;
        }

    }

    public ClasificadorRecursoPropioManagedBean() {

    }

    @PostConstruct
    private void init() {
        
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoClasificadorRecursoPropio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map(accionAux -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarClasificadorRecursoPropio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map(accionAux -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarClasificadorRecursoPropio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter(accionAux -> accionAux.getNombre().equalsIgnoreCase("detalleClasificadorRecursoPropio")).forEach(_item -> this.setDetalle(true));
                }
            } catch (Exception e) {

            }
        }
    }

    public ClasificadorRecursoPropioFacadeLocal getClasificadorRecursoPropioFacade() {
        return this.clasificadorRecursoPropioFacade;
    }

    public void setClasificadorRecursoPropioFacade(ClasificadorRecursoPropioFacadeLocal clasificadorRecursoPropioFacade) {
        this.clasificadorRecursoPropioFacade = clasificadorRecursoPropioFacade;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCodigo() {
        return this.codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCodigoStr() {
        return this.codigoStr;
    }

    public void setCodigoStr(String codigoStr) {
        this.codigoStr = codigoStr;
    }

    public Long getIdClasificadorRecursoPropioPadre() {
        return this.idClasificadorRecursoPropioPadre;
    }

    public void setIdClasificadorRecursoPropioPadre(Long idClasificadorRecursoPropioPadre) {
        this.idClasificadorRecursoPropioPadre = idClasificadorRecursoPropioPadre;
    }

    public Long getIdTipoClasificadorRecursoPropio() {
        return this.idTipoClasificadorRecursoPropio;
    }

    public void setIdTipoClasificadorRecursoPropio(Long idTipoClasificadorRecursoPropio) {
        this.idTipoClasificadorRecursoPropio = idTipoClasificadorRecursoPropio;
    }

    public String getClasificadorRecursoPropioStr() {
        return this.clasificadorRecursoPropioStr;
    }

    public void setClasificadorRecursoPropioStr(String clasificadorRecursoPropioStr) {
        this.clasificadorRecursoPropioStr = clasificadorRecursoPropioStr;
    }

    public String getTipoClasificadorRecursoPropioStr() {
        return this.tipoClasificadorRecursoPropioStr;
    }

    public void setTipoClasificadorRecursoPropioStr(String tipoClasificadorRecursoPropioStr) {
        this.tipoClasificadorRecursoPropioStr = tipoClasificadorRecursoPropioStr;
    }

    public Long getCodigoBsq() {
        return this.codigoBsq;
    }

    public void setCodigoBsq(Long codigoBsq) {
        this.codigoBsq = codigoBsq;
    }

    public Long getIdTipoClasificadorRecursoPropioBsq() {
        return this.idTipoClasificadorRecursoPropioBsq;
    }

    public void setIdTipoClasificadorRecursoPropioBsq(Long idTipoClasificadorRecursoPropioBsq) {
        this.idTipoClasificadorRecursoPropioBsq = idTipoClasificadorRecursoPropioBsq;
    }

    public List<ClasificadorRecursoPropio> getArbol() {
        this.setList(this.clasificadorRecursoPropioFacade
        .findAll(this.getIdClasificadorRecursoPropioPadreBsq(), this.getCodigoBsq(), this.getIdTipoClasificadorRecursoPropioBsq(), this.getNombreBsq()));
        return (List<ClasificadorRecursoPropio>) this.getList();
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<ClasificadorRecursoPropio> lstClasificadorRecursoPropiosAux = new LazyDataModel<ClasificadorRecursoPropio>() {

            @Override
            public List<ClasificadorRecursoPropio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<ClasificadorRecursoPropio> lstClasificadorRecursoPropiosAux = 
                        clasificadorRecursoPropioFacade.findAll(idClasificadorRecursoPropioPadreBsq,
                        codigoBsq, idTipoClasificadorRecursoPropioBsq, nombreBsq, first, pageSize);

                return lstClasificadorRecursoPropiosAux;
            }

        };
        lstClasificadorRecursoPropiosAux.setRowCount(clasificadorRecursoPropioFacade.countAll(idClasificadorRecursoPropioPadreBsq, codigoBsq,
                idTipoClasificadorRecursoPropioBsq, nombreBsq).intValue());
        return lstClasificadorRecursoPropiosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setCodigo(null);
        this.setIdClasificadorRecursoPropioPadre(null);
        this.setIdTipoClasificadorRecursoPropio(null);
        this.setClasificadorRecursoPropioStr(null);
        this.setTipoClasificadorRecursoPropioStr(null);
    }

    @Override
    public String crear() {
        try {
            this.clasificadorRecursoPropioFacade.create(this.getNombre(), this.getIdClasificadorRecursoPropioPadre(), this.getIdTipoClasificadorRecursoPropio());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorClasificadorRecursoPropio");
            this.setMsgSuccessError("El clasificador de recurso propio ha sido generado con \u00e9xito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorRecursoPropio");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.clasificadorRecursoPropioFacade.create(this.getNombre(), this.getIdClasificadorRecursoPropioPadre(), this.getIdTipoClasificadorRecursoPropio());
            this.setResultado("nuevoClasificadorRecursoPropio");
            this.setNombre(null);
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorRecursoPropio");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        final Long idClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        final ClasificadorRecursoPropio clasificadorRecursoPropioAux = this.clasificadorRecursoPropioFacade.find((Object) idClasificadorRecursoPropioAux);
        this.setId(clasificadorRecursoPropioAux.getId());
        this.setNombre(clasificadorRecursoPropioAux.getNombre());
        this.setCodigoStr(clasificadorRecursoPropioAux.obtenerCodigoCompleto(clasificadorRecursoPropioAux));
        this.setClasificadorRecursoPropioStr(clasificadorRecursoPropioAux.getClasificadorRecursoPropio() != null ? clasificadorRecursoPropioAux.getClasificadorRecursoPropio().getNombre() : "-");
        this.setTipoClasificadorRecursoPropioStr(clasificadorRecursoPropioAux.getTipoClasificadorRecursoPropio() != null ? clasificadorRecursoPropioAux.getTipoClasificadorRecursoPropio().getNombre() : "-");
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
            this.clasificadorRecursoPropioFacade.remove(idClasificadorRecursoPropioAux);
            this.setTitle((String) null);
            this.setImages((String) null);
            this.setMsgSuccessError((String) null);
            this.setResultado("menuConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorRecursoPropio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        ClasificadorRecursoPropio clasificadorRecursoPropioAux = this.clasificadorRecursoPropioFacade.find((Object) idClasificadorRecursoPropioAux);
        this.setId(clasificadorRecursoPropioAux.getId());
        this.setNombre(clasificadorRecursoPropioAux.getNombre());
        this.setCodigo(clasificadorRecursoPropioAux.getCodigo());
        if (clasificadorRecursoPropioAux.getClasificadorRecursoPropio() != null) {
            this.setIdClasificadorRecursoPropioPadre(clasificadorRecursoPropioAux.getClasificadorRecursoPropio().getId());
        }
        if (clasificadorRecursoPropioAux.getTipoClasificadorRecursoPropio() != null) {
            this.setIdTipoClasificadorRecursoPropio(clasificadorRecursoPropioAux.getTipoClasificadorRecursoPropio().getId());
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            this.clasificadorRecursoPropioFacade.edit(this.getId(), this.getNombre(), this.getIdClasificadorRecursoPropioPadre(), this.getIdTipoClasificadorRecursoPropio());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorClasificadorRecursoPropio");
            this.setMsgSuccessError("El Clasificador del Recurso Propio ha sido editado con \u00e9xito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<ClasificadorRecursoPropio> listClasificadorRecursoPropio = (List<ClasificadorRecursoPropio>) this.clasificadorRecursoPropioFacade.findAll(true);
        if (!listClasificadorRecursoPropio.isEmpty()) {
            for (ClasificadorRecursoPropio clasificadorRecursoPropioAux : listClasificadorRecursoPropio) {
                if (clasificadorRecursoPropioAux.getId() != null && clasificadorRecursoPropioAux.getNombre() != null) {
                    selectItems.add(new SelectItem(clasificadorRecursoPropioAux.getId(), clasificadorRecursoPropioAux.obtenerCodigoCompleto(clasificadorRecursoPropioAux).concat(" - ").concat(clasificadorRecursoPropioAux.getNombre())));
                }
            }
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoClasificaorRecursoPropio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoClasificadorRecursoPropio> listTipoClasificadorRecursoPropio = (List<TipoClasificadorRecursoPropio>) this.tipoClasificadorRecursoPropioFacade.findAll(true);
        if (!listTipoClasificadorRecursoPropio.isEmpty()) {
            listTipoClasificadorRecursoPropio.stream().forEach(tipoClasificadorRecursoPropioAux -> selectItems.add(new SelectItem((Object) tipoClasificadorRecursoPropioAux.getId(), tipoClasificadorRecursoPropioAux.getNombre())));
        }
        return selectItems;
    }

    public void codigoValido(final FacesContext context, final UIComponent validate, final Object value) {
        Long codigoClasificador = (Long) value;
        List<ClasificadorRecursoPropio> clasificadores = (List<ClasificadorRecursoPropio>) this.clasificadorRecursoPropioFacade.findAll(this.getIdClasificadorRecursoPropioPadre(), codigoClasificador, this.getIdTipoClasificadorRecursoPropio(), (String) null);
        if (!clasificadores.isEmpty()) {
            final FacesMessage msg = new FacesMessage("El c\u00f3digo ingresado ya se encuentra en uso.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    @Override
    public void actualizar() {
        try {
            this.setIdTipoClasificadorRecursoPropioBsq(null);
            this.setCodigoBsq(null);
            this.setNombreBsq(null);
            this.setIdClasificadorRecursoPropioPadreBsq(null);
            super.actualizar();
        } catch (Exception ex) {
            Logger.getLogger(ClasificadorRecursoPropioManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //Obtiene los recursos propios por ejercicio y servicio
    public List<RecursoPropio> obtenerRecusroPropioPorClasificadorRecurso(ClasificadorRecursoPropio unClasificadorRecursoPropio) {                  
            this.recursosPropios = this.recursoPropioFacade.obtenerRecursosPropios(unClasificadorRecursoPropio.getId(), this.getIdEjercicio(), this.getIdServicio());
            this.setFlag(false);
            return this.recursosPropios;        
        
    }

    //combo busqueda de servicio
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
            return null;
        }

    }

    /**
     * @author Gonzalez Facundo
     * @see Actualiza combo de ejercicio 
     * @return List SelectItem
     */
    public List<SelectItem> getSelectItemsEjercicio() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true)
                    .stream()
                    .sorted((x,x1)-> x.getAnio().compareTo(x1.getAnio())).collect(Collectors.toList());// 1L = Estado Activo
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
    
    
    
    
}
