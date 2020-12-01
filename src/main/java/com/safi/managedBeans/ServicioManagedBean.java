package com.safi.managedBeans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.Organismo;
import com.safi.entity.Servicio;
import com.safi.entity.Usuario;
import com.safi.facade.OrganismoFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;
import com.safi.facade.UsuarioFacadeLocal;
import com.safi.entity.Grupo;
import com.safi.entity.CodigoExpediente;
import com.safi.entity.TipoServicio;
import com.safi.enums.GrupoUsuarioEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.GrupoFacadeLocal;
import com.safi.facade.CodigoExpedienteFacadeLocal;
import com.safi.facade.TipoServicioFacadeLocal;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Gonzalez Facundo,Angel Alvarenga
 */
@ManagedBean
@SessionScoped
public class ServicioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private OrganismoFacadeLocal organismoFacade;
    @EJB
    private TipoServicioFacadeLocal tipoServicioFacade;
    @EJB
    private CodigoExpedienteFacadeLocal codigoExpedienteFacade;
    @EJB
    private GrupoFacadeLocal grupoFacade;

    private Long idBsq;
    private Long idTipoTramiteBsq;
    private Long idTipoTramite;
    private String abreviaturaBsq;
    private String descripcionBsq;
    private String abreviatura;
    private String descripcion;
    private String servicioStr;
    private String codigo;
    private String codigoBsq;
    private List<SelectItem> itemsIzquierda = new ArrayList<>();
    private List<SelectItem> itemsDerecha = new ArrayList<>();
    private List<Organismo> organismos;
    private List<Usuario> usuarios;
    private Servicio servicio;
    /* Otras acciones */
    private boolean agregarUsuarioServicio;
    private boolean agregarOrganismoServicio;
    /* Fin otras acciones */
    private final long SERVICIO_ADMINISTRATIVO = 1L;
    private final long DIRECCION_ADMINISTRACION = 2L;
    private List<CodigoExpediente> lstcodigoOrganismo = new ArrayList<>();
    private Boolean esServicioAdministrativo;
    private Long codigoOrganismo;

    private List<SelectItem> itemsIzquierdaCodigoExpediente = new ArrayList<>();
    private List<SelectItem> itemsDerechaCodigoExpediente = new ArrayList<>();
    private List<Long> idItems = new ArrayList<>();

    private Long idCodigoExpAux;
    private List<SelectItem> selectItemsCodigosDisponibles = new ArrayList<>();

    /**
     * Creates a new instance of ServicioManagedBean
     */
    public ServicioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setServicio(sessionBean.getServicio());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoServicio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarServicio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarServicio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("agregarUsuarioServicio")) {
                            this.setAgregarUsuarioServicio(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("agregarOrganismoServicio")) {
                            this.setAgregarOrganismoServicio(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleServicio"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public List<SelectItem> getSelectItemsCodigosDisponibles() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<CodigoExpediente> lstCodigo = this.codigoExpedienteFacade.findAll(true);
        lstCodigo.removeAll(this.getLstcodigoOrganismo());
        if (!(lstCodigo.isEmpty())) {
            lstCodigo.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo()));
            });
        }
        this.setIdCodigoExpAux(0L);
        return selectItems;
    }


    public void setSelectItemsCodigosDisponibles(List<SelectItem> selectItemsCodigosDisponibles) {
        this.selectItemsCodigosDisponibles = selectItemsCodigosDisponibles;
    }

    public Long getIdCodigoExpAux() {
        return idCodigoExpAux;
    }

    public void setIdCodigoExpAux(Long idCodigoExpAux) {
        this.idCodigoExpAux = idCodigoExpAux;
    }

    public List<Long> getIdItems() {
        return idItems;
    }

    public void setIdItems(List<Long> idItems) {
        this.idItems = idItems;
    }

    public List<SelectItem> getItemsIzquierdaCodigoExpediente() {
        return itemsIzquierdaCodigoExpediente;
    }

    public void setItemsIzquierdaCodigoExpediente(List<SelectItem> itemsIzquierdaCodigoExpediente) {
        this.itemsIzquierdaCodigoExpediente = itemsIzquierdaCodigoExpediente;
    }

    public List<SelectItem> getItemsDerechaCodigoExpediente() {
        return itemsDerechaCodigoExpediente;
    }

    public void setItemsDerechaCodigoExpediente(List<SelectItem> itemsDerechaCodigoExpediente) {
        this.itemsDerechaCodigoExpediente = itemsDerechaCodigoExpediente;
    }

    public Long getCodigoOrganismo() {
        return codigoOrganismo;
    }

    public void setCodigoOrganismo(Long codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }

    public Boolean getEsServicioAdministrativo() {
        return esServicioAdministrativo;
    }

    public void setEsServicioAdministrativo(Boolean esServicioAdministrativo) {
        this.esServicioAdministrativo = esServicioAdministrativo;
    }

    public List<CodigoExpediente> getLstcodigoOrganismo() {
        return lstcodigoOrganismo;
    }

    public List<CodigoExpediente> getLstcodigoOrganismoSorted() {
        return lstcodigoOrganismo.stream()
                .sorted(Comparator.comparing(CodigoExpediente::getCodigo))
                .collect(Collectors.toList());
    }

    public void setLstcodigoOrganismo(List<CodigoExpediente> lstcodigoOrganismo) {
        this.lstcodigoOrganismo = lstcodigoOrganismo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Long getIdBsq() {
        return idBsq;
    }

    public void setIdBsq(Long idBsq) {
        this.idBsq = idBsq;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isAgregarUsuarioServicio() {
        return agregarUsuarioServicio;
    }

    public void setAgregarUsuarioServicio(boolean agregarUsuarioServicio) {
        this.agregarUsuarioServicio = agregarUsuarioServicio;
    }

    public boolean isAgregarOrganismoServicio() {
        return agregarOrganismoServicio;
    }

    public void setAgregarOrganismoServicio(boolean agregarOrganismoServicio) {
        this.agregarOrganismoServicio = agregarOrganismoServicio;
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

    public String getAbreviaturaBsq() {
        return abreviaturaBsq;
    }

    public void setAbreviaturaBsq(String abreviaturaBsq) {
        this.abreviaturaBsq = abreviaturaBsq;
    }

    public String getDescripcionBsq() {
        return descripcionBsq;
    }

    public void setDescripcionBsq(String descripcionBsq) {
        this.descripcionBsq = descripcionBsq;
    }

    public String getServicioStr() {
        return servicioStr;
    }

    public void setServicioStr(String servicioStr) {
        this.servicioStr = servicioStr;
    }

    public List<Organismo> getOrganismos() {
        return organismos;
    }

    public void setOrganismos(List<Organismo> organismos) {
        this.organismos = organismos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoBsq() {
        return codigoBsq;
    }

    public void setCodigoBsq(String codigoBsq) {
        this.codigoBsq = codigoBsq;
    }

    public Long getIdTipoTramiteBsq() {
        return idTipoTramiteBsq;
    }

    public void setIdTipoTramiteBsq(Long idTipoTramiteBsq) {
        this.idTipoTramiteBsq = idTipoTramiteBsq;
    }

    public Long getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(Long idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Servicio> lstServiciosAux = new LazyDataModel<Servicio>() {
            @Override
            public List<Servicio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Servicio> lstServiciosAux = servicioFacade.findAll(idBsq, codigoBsq, abreviaturaBsq, descripcionBsq, idTipoTramiteBsq, first, pageSize);
                return lstServiciosAux;
            }
        };
        lstServiciosAux.setRowCount(servicioFacade.countAll(idBsq, codigoBsq, abreviaturaBsq, descripcionBsq, idTipoTramiteBsq).intValue());
        return lstServiciosAux;
    }

    @Override
    public void limpiar() {
        this.setItemsDerecha(null);
        this.setItemsIzquierda(null);
        this.setServicioStr(null);
        this.setOrganismos(null);
        this.setDescripcion(null);
        this.setAbreviatura(null);
        this.setCodigo(null);
        this.setServicio(null);
        this.setIdTipoTramite(null);
        this.getLstcodigoOrganismo().clear();
    }

    public void limpiarCodigos() {
        this.setItemsDerechaCodigoExpediente(null);
        this.setItemsIzquierdaCodigoExpediente(null);
    }

    @Override
    public String crear() {
        try {
            if (getLstcodigoOrganismo().isEmpty()) {
                throw new Exception("Debe seleccionar al menos un Código.");
            }
            this.servicioFacade.create(this.getCodigo(), this.getAbreviatura(), this.getDescripcion(), this.getIdTipoTramite(), this.getLstcodigoOrganismo());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorServicio");
            this.setMsgSuccessError("El servicio administrativo ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            if (getLstcodigoOrganismo().isEmpty()) {
                throw new Exception("Debe seleccionar al menos un Código.");
            }
            this.servicioFacade.create(this.getCodigo(), this.getAbreviatura(), this.getDescripcion(), this.getIdTipoTramite(), this.getLstcodigoOrganismo());
            this.setResultado("nuevoServicio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idServicioAux = Long.parseLong(myRequest.getParameter("id"));
        this.setServicio(this.servicioFacade.find(idServicioAux));
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idServicioAux = Long.parseLong(myRequest.getParameter("id"));
            this.servicioFacade.remove(idServicioAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("servicioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
    }

    @Override
    public void prepararParaEditar() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idServicioAux = Long.parseLong(myRequest.getParameter("id"));
            Servicio servicioAux = this.servicioFacade.find(idServicioAux);
            this.setServicio(servicioAux);
            this.setId(servicioAux.getId());
            this.setCodigo(servicioAux.getCodigo());
            this.setAbreviatura(servicioAux.getAbreviatura());
            this.setDescripcion(servicioAux.getDescripcion());
            this.prepararParaAgregarCodigosExpedientes(servicioAux);
            this.setIdTipoTramite(servicioAux.getTipoServicio().getId());
            this.setLstcodigoOrganismo(servicioAux.getLstCodigoExpediente());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            List<Long> lstAux = new ArrayList<>();
            if (!this.lstcodigoOrganismo.isEmpty()) {
                this.getLstcodigoOrganismo().stream().forEach((object) -> {
                    lstAux.add(object.getId());
                });
            } else {
                lstAux.clear();
            }
            this.servicioFacade.edit(this.getId(), this.getCodigo(), this.getAbreviatura(), this.getDescripcion(), this.getIdTipoTramite(), lstAux);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorServicio");
            this.setMsgSuccessError("El servicio administrativo ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
        return this.getResultado();
    }

    @Override
    public void actualizar() {
        try {
            this.setIdBsq(null);
            this.setAbreviaturaBsq(null);
            this.setDescripcionBsq(null);
            this.setCodigoBsq(null);
            this.setIdTipoTramiteBsq(null);

        } catch (Exception ex) {
            Logger.getLogger(ServicioManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Servicio> lstServicio = this.servicioFacade.findAll(true);
        if (!(lstServicio.isEmpty())) {
            lstServicio.stream().forEach((servicioAux) -> {
                selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getAbreviatura()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoServicio() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoServicio> lstTiposServicio = this.tipoServicioFacade.findAll(true);
        if (!(lstTiposServicio.isEmpty())) {
            lstTiposServicio.stream().forEach((tipoServicioAux) -> {
                selectItems.add(new SelectItem(tipoServicioAux.getId(), tipoServicioAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsCodigoOrganismo() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Organismo> lstOrganismo = this.organismoFacade.findAll(true);
        if (!(lstOrganismo.isEmpty())) {
            lstOrganismo.stream().forEach((organismoAux) -> {
                selectItems.add(new SelectItem(organismoAux.getCodigoOrganismo(), organismoAux.getNombre() + "-" + organismoAux.getCodigoOrganismo()));
            });
        }
        return selectItems;
    }

    public void prepararParaAgregarUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idServicioAux = Long.parseLong(myRequest.getParameter("id"));
        Servicio servicioAux = this.servicioFacade.find(idServicioAux);
        this.limpiar();
        if (servicioAux != null) {
            this.setId(servicioAux.getId());
            this.setServicioStr(Utilidad.ceroIzquierda(Long.parseLong(servicioAux.getCodigo())) + " - " + servicioAux.getDescripcion());
            this.usuariosSeleccionadas(servicioAux);
            this.usuariosDisponibles();
        } else {
            this.setItemsDerecha(null);
            this.setItemsIzquierda(null);
        }
    }

    public void prepararParaAgregarOrganismo() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idServicioAux = Long.parseLong(myRequest.getParameter("id"));
        Servicio servicioAux = this.servicioFacade.find(idServicioAux);
        this.limpiar();
        if (servicioAux != null) {
            this.setId(servicioAux.getId());
            this.setServicioStr(Utilidad.ceroIzquierda(Long.parseLong(servicioAux.getCodigo())) + " - " + servicioAux.getDescripcion());
            this.organismosSeleccionadas(servicioAux);
            this.organismosDisponibles(servicioAux);
        } else {
            this.setItemsDerecha(null);
            this.setItemsIzquierda(null);
        }
    }

    public void organismosSeleccionadas(Servicio servicio) {
        List<Organismo> listOrganismoItems = servicio.getOrganismos();
        this.itemsIzquierda = new ArrayList<>();
        if (!(listOrganismoItems.isEmpty())) {
            listOrganismoItems.stream()
                    .filter((organismo) -> (organismo.isEstado()))
                    .sorted((o1, o2) -> (o1.getCodigoOrganismo().compareTo(o2.getCodigoOrganismo())))
                    .forEach((organismoAux) -> {
                        this.itemsIzquierda.add(new SelectItem(organismoAux.getId(), Utilidad.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre()));
                    });
        }
    }

    public void organismosDisponibles(Servicio servicio) {
        List<Organismo> listOrganismosItemsAll = this.organismoFacade.findAllNotServicio(true);
        this.itemsDerecha = new ArrayList<>();
        listOrganismosItemsAll.removeAll(servicio.getOrganismos());
        if (!(listOrganismosItemsAll.isEmpty())) {
            listOrganismosItemsAll.stream()
                    .filter((organismo) -> (organismo.isEstado()))
                    .sorted((o1, o2) -> (o1.getCodigoOrganismo().compareTo(o2.getCodigoOrganismo())))
                    .forEach((organismoAux) -> {
                        this.itemsDerecha.add(new SelectItem(organismoAux.getId(), Utilidad.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre()));
                    });
        }
    }

    public void usuariosSeleccionadas(Servicio servicio) {
        List<Usuario> listUsuariosItems = servicio.getUsuarios();
        this.itemsIzquierda = new ArrayList<>();
        if (!(listUsuariosItems.isEmpty())) {
            listUsuariosItems.stream().filter((usuario) -> (usuario.isEstado())).forEach((usuarioAux) -> {
                this.itemsIzquierda.add(new SelectItem(usuarioAux.getId(), usuarioAux.getApellidoNombre()));
            });
        }
    }

    public void usuariosDisponibles() {
        List<Usuario> listUsuariosItemsAll = this.usuarioFacade.findAllNotServicio(true);
        Grupo grupoServicio = this.grupoFacade.find(GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO.getId());
        Grupo grupoServicioReportes = this.grupoFacade.find(GrupoUsuarioEnum.SERVICIOADMINISTRATIVOREPORTES.getId());
        this.itemsDerecha = new ArrayList<>();
        if (!(listUsuariosItemsAll.isEmpty())) {
            listUsuariosItemsAll.stream().filter((usuario) -> (usuario.isEstado())).filter(s -> s.isEstado() == true)
                    .filter(s -> s.getGrupos().contains(grupoServicio) || s.getGrupos().contains(grupoServicioReportes))
                    .forEach((actionAux) -> {
                        this.itemsDerecha.add(new SelectItem(actionAux.getId(), actionAux.getApellidoNombre()));
                    });
        }
    }

    public String editarUsuariosServicio() {
        try {
            List<Long> lstAuxIzquierda = new ArrayList<>();
            this.getItemsIzquierda().stream().forEach((object) -> {
                lstAuxIzquierda.add((Long) object.getValue());
            });
            List<Long> lstAuxDerecha = new ArrayList<>();
            this.getItemsDerecha().stream().forEach((object) -> {
                lstAuxDerecha.add((Long) object.getValue());
            });
            this.servicioFacade.editUsuariosServicio(this.getId(), lstAuxIzquierda, lstAuxDerecha);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorServicio");
            this.setMsgSuccessError("Los usuarios han sido agregados al servicio con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
        return this.getResultado();
    }

    public String editarOrganismosServicio() {
        try {
            List<Long> lstAux = new ArrayList<>();
            if (!this.itemsIzquierda.isEmpty()) {
                this.getItemsIzquierda().stream().forEach((object) -> {
                    lstAux.add((Long) object.getValue());
                });
            } else {
                lstAux.clear();
            }

            this.servicioFacade.editOrganismosServicio(this.getId(), lstAux);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorServicio");
            this.setMsgSuccessError("Los organismos han sido agregados al servicio con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorServicio");
        }
        return this.getResultado();
    }

 

    public void validarCodigo(final FacesContext context, final UIComponent validate, final Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        List<Servicio> lstServicio = this.servicioFacade.findAll(true);
        for (Servicio s : lstServicio) {
            Long c = Long.parseLong(s.getCodigo());
            if (c.equals(Long.parseLong(value.toString().trim()))) {
                final FacesMessage msg = new FacesMessage("El código de servicio administrativo ya existe.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    /**
     *
     * @author Gonzalez Facundo
     * @param context
     * @param validate
     * @param value
     * @see Comprueba el tipo de servicio
     */
    public void validatorServicio(ValueChangeEvent event) {
        Long value = (Long) event.getNewValue();
        this.setEsServicioAdministrativo(SERVICIO_ADMINISTRATIVO == value);

    }

    public List<SelectItem> getSelectItemsCodigo() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<CodigoExpediente> listCodigoExpediente = this.codigoExpedienteFacade.findAll().stream().filter(x -> x.isEstado()).collect(Collectors.toList());
        if (!(listCodigoExpediente.isEmpty())) {
            listCodigoExpediente.stream().forEach((grupoAux) -> {
                selectItems.add(new SelectItem(grupoAux.getId(), grupoAux.getCodigo()));
            });

        }
        return selectItems;
    }

    public void prepararParaAgregarCodigosExpedientes(Servicio servicioAux) {
        this.limpiarCodigos();
        if (servicioAux != null) {
            this.setId(servicioAux.getId());
            this.setServicioStr(Utilidad.ceroIzquierda(Long.parseLong(servicioAux.getCodigo())) + " - " + servicioAux.getDescripcion());
            this.codigosDisponibles(servicioAux);
            this.codigosSeleccionadas(servicioAux);
        } else {
            this.setItemsIzquierdaCodigoExpediente(null);
            this.setItemsDerechaCodigoExpediente(null);
        }
    }

    public void codigosSeleccionadas(Servicio servicio) {
        List<CodigoExpediente> listCodigosExpedienteItems = servicio.getLstCodigoExpediente();
        this.itemsIzquierdaCodigoExpediente = new ArrayList<>();
        if (!(listCodigosExpedienteItems.isEmpty())) {
            listCodigosExpedienteItems.stream()
                    .filter((codigoExpediente) -> (codigoExpediente.isEstado()))
                    .sorted((o1, o2) -> (o1.getCodigo().compareTo(o2.getCodigo())))
                    .forEach((codigoExpediente) -> {
                        this.itemsIzquierdaCodigoExpediente.add(new SelectItem(codigoExpediente.getId(), codigoExpediente.getCodigo()));
                    });
        }
    }

    public void codigosDisponibles(Servicio servicio) {
        List<CodigoExpediente> listCodigosExpedienteItems = this.codigoExpedienteFacade.findAll();
        this.itemsDerechaCodigoExpediente = new ArrayList<>();
        listCodigosExpedienteItems.removeAll(servicio.getLstCodigoExpediente());
        if (!(listCodigosExpedienteItems.isEmpty())) {
            listCodigosExpedienteItems.stream()
                    .filter((codigoExpediente) -> (codigoExpediente.isEstado()))
                    .sorted((o1, o2) -> (o1.getCodigo().compareTo(o2.getCodigo())))
                    .forEach((codigoExpedienteAux) -> {
                        this.itemsDerechaCodigoExpediente.add(new SelectItem(codigoExpedienteAux.getId(), codigoExpedienteAux.getCodigo()));
                    });
        }
    }

    /**
     * Agregar codigos de expediente al servicio
     *
     * @author Gonzalez Facundo
     * @param e
     */
    public void cargaCodigos(ValueChangeEvent e) {
        Long idCodigoAux = (Long) e.getNewValue();
        if (idCodigoAux != null && idCodigoAux != 0L) {
            CodigoExpediente codigoAux = this.codigoExpedienteFacade.find(idCodigoAux);
            this.getLstcodigoOrganismo().add(codigoAux);
            List<CodigoExpediente> listCodigos = codigoExpedienteFacade.findAll(true);
            listCodigos.removeAll(this.getLstcodigoOrganismo());
            if (!(listCodigos.isEmpty())) {
                listCodigos.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((codigoExpAux) -> {
                    this.selectItemsCodigosDisponibles.add(new SelectItem(codigoExpAux.getId(), codigoExpAux.getCodigo()));
                });
            }

        }
        this.setIdCodigoExpAux(0L);
    }

    /**
     * quita codigos de la tabla
     *
     * * @author Gonzalez Facundo
     */
    public void quitarCodigo() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            //Busco servicio con el ID
            CodigoExpediente unCodigo = this.codigoExpedienteFacade.find(idAux);
            //Elimino la Actividad Economica de la tabla
            this.getLstcodigoOrganismo().remove(unCodigo);
            //Elimino todos servicios del Combo
            this.selectItemsCodigosDisponibles.clear();
            //Busco todas las actividades Economicas Disponibles
            List<CodigoExpediente> listCodigo = codigoExpedienteFacade.findAll();
            //Elimino los servicios del combo que esta en la tabla  
            listCodigo.removeAll(this.getLstcodigoOrganismo());
            if (!(listCodigo.isEmpty())) {
                listCodigo.stream().forEach((codigoExpAux) -> {
                    this.selectItemsCodigosDisponibles.add(new SelectItem(codigoExpAux.getId(), codigoExpAux.getCodigo()));
                });
            }
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAltaServicio");
        }
    }

}
