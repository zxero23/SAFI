package com.safi.managedBeans;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.ClasificadorOrganismo;
import com.safi.facade.OrganismoFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;
import com.safi.entity.Organismo;
import com.safi.entity.Servicio;
import com.safi.facade.ClasificadorOrganismoFacadeLocal;
import com.safi.utilidad.Utilidad;

@ManagedBean
@SessionScoped
public class OrganismoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private OrganismoFacadeLocal organismoFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private ClasificadorOrganismoFacadeLocal clasificadorOrganismoFacade;
    private Long idOrganismoPadre;
    private Long codigoOrganismo;
    private String nombre;
    private String nombreBsq;
    private String organismoPadre;
    private Long idClasificadorOrganismo;
    private String clasificadorOrganismo;
    private List<Organismo> organismosHijos = new ArrayList<>();
    private List<Servicio> servicios = new ArrayList<>();
    //para busqueda
    private Long codigoOrganismoBsq;
    private Long idOrganismoPadreBsq;
    private Long idClasificadorOrganismoBsq;
    /*Información útil del organismo */
    private String direccion;
    private String telefonos;
    private String correoOficial;
    /* fin inf util*/
    private List<SelectItem> itemsIzquierda;
    private List<SelectItem> itemsDerecha;

    /**
     * Creates a new instance of OrganismoManagedBean
     */
    public OrganismoManagedBean() {
        this.itemsIzquierda = null;
        this.itemsDerecha = null;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoOrganismo")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarOrganismo")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarOrganismo")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleOrganismo"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public Long getIdOrganismoPadre() {
        return idOrganismoPadre;
    }

    public void setIdOrganismoPadre(Long idOrganismoPadre) {
        this.idOrganismoPadre = idOrganismoPadre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getCorreoOficial() {
        return correoOficial;
    }

    public void setCorreoOficial(String correoOficial) {
        this.correoOficial = correoOficial;
    }

    public List<Organismo> getOrganismosHijos() {
        return organismosHijos;
    }

    public void setOrganismosHijos(List<Organismo> organismosHijos) {
        this.organismosHijos = organismosHijos;
    }

    public Long getIdOrganismoPadreBsq() {
        return idOrganismoPadreBsq;
    }

    public void setIdOrganismoPadreBsq(Long idOrganismoPadreBsq) {
        this.idOrganismoPadreBsq = idOrganismoPadreBsq;
    }

    public Long getCodigoOrganismo() {
        return codigoOrganismo;
    }

    public void setCodigoOrganismo(Long codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }

    public Long getCodigoOrganismoBsq() {
        return codigoOrganismoBsq;
    }

    public void setCodigoOrganismoBsq(Long codigoOrganismoBsq) {
        this.codigoOrganismoBsq = codigoOrganismoBsq;
    }

    public OrganismoFacadeLocal getOrganismoFacade() {
        return organismoFacade;
    }

    public void setOrganismoFacade(OrganismoFacadeLocal organismoFacade) {
        this.organismoFacade = organismoFacade;
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

    public String getOrganismoPadre() {
        return organismoPadre;
    }

    public void setOrganismoPadre(String organismoPadre) {
        this.organismoPadre = organismoPadre;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Long getIdClasificadorOrganismo() {
        return idClasificadorOrganismo;
    }

    public void setIdClasificadorOrganismo(Long idClasificadorOrganismo) {
        this.idClasificadorOrganismo = idClasificadorOrganismo;
    }

    public String getClasificadorOrganismo() {
        return clasificadorOrganismo;
    }

    public void setClasificadorOrganismo(String clasificadorOrganismo) {
        this.clasificadorOrganismo = clasificadorOrganismo;
    }

    public Long getIdClasificadorOrganismoBsq() {
        return idClasificadorOrganismoBsq;
    }

    public void setIdClasificadorOrganismoBsq(Long idClasificadorOrganismoBsq) {
        this.idClasificadorOrganismoBsq = idClasificadorOrganismoBsq;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Organismo> lstOrganismosAux = new LazyDataModel<Organismo>() {

            @Override
            public List<Organismo> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Organismo> lstOrganismosAux = organismoFacade.findAll(codigoOrganismoBsq,
                        nombreBsq, idOrganismoPadreBsq,
                        idClasificadorOrganismoBsq, first, pageSize);

                return lstOrganismosAux;
            }

        };
        lstOrganismosAux.setRowCount(organismoFacade.countAll(codigoOrganismoBsq,
                nombreBsq, idOrganismoPadreBsq,
                idClasificadorOrganismoBsq).intValue());
        return lstOrganismosAux;
    }

    @Override
    public void limpiar() {
        this.setIdClasificadorOrganismo(null);
        this.setIdOrganismoPadre(null);
        this.setCodigoOrganismo(null);
        this.setNombre(null);
        this.setDireccion(null);
        this.setTelefonos(null);
        this.setCorreoOficial(null);
        this.setOrganismosHijos(new ArrayList());
        this.setServicios(new ArrayList());
        this.serviciosDisponibles(new ArrayList());
        this.setClasificadorOrganismo(null);
        this.setFlag(true);
        this.setOrganismoPadre(null);
    }

    @Override
    public String crear() {
        try {
            this.organismoFacade.create(this.getIdOrganismoPadre(),
                    this.getIdClasificadorOrganismo(),
                    this.getCodigoOrganismo(), this.getNombre(),
                    this.getDireccion(), this.getTelefonos(), this.getCorreoOficial());
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorOrganismo");
            this.setMsgSuccessError("El organismo ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.organismoFacade.create(this.getIdOrganismoPadre(),
                    this.getIdClasificadorOrganismo(),
                    this.getCodigoOrganismo(), this.getNombre(),
                    this.getDireccion(), this.getTelefonos(), this.getCorreoOficial());
            this.setResultado("nuevoOrganismo");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idObjectAux = Long.parseLong(myRequest.getParameter("id"));
            Organismo organismoAux = this.organismoFacade.find(idObjectAux);
            this.setId(organismoAux.getId());
            this.setNombre(organismoAux.getNombre());
            this.setCodigoOrganismo(organismoAux.getCodigoOrganismo());
            this.setCorreoOficial(organismoAux.getCorreoOficial());
            this.setDireccion(organismoAux.getDireccion());
            this.setTelefonos(organismoAux.getTelefonos());
            this.setServicios(organismoAux.getServiciosSorted());
            if (organismoAux.getClasificadorOrganismo() != null) {
                this.setClasificadorOrganismo(organismoAux.getClasificadorOrganismo().getNombre());
            }
            if (organismoAux.getOrganismoPadre() != null) {
                this.setOrganismoPadre(this.ceroIzquierda(organismoAux.getOrganismoPadre().getCodigoOrganismo()) + " - " + organismoAux.getOrganismoPadre().getNombre());
            } else {
                this.setOrganismoPadre("-");
            }
            if (organismoAux.getOrganismos() != null) {
                this.setOrganismosHijos(organismoAux.getOrganismos());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idObjectAux = Long.parseLong(myRequest.getParameter("id"));
            this.organismoFacade.remove(idObjectAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("organismoConf");
            this.setFlag(true);
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorOrganismo");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idObjectAux = Long.parseLong(myRequest.getParameter("id"));
        Organismo organismoAux = this.organismoFacade.find(idObjectAux);
        this.setId(organismoAux.getId());
        this.setNombre(organismoAux.getNombre());
        this.setCodigoOrganismo(organismoAux.getCodigoOrganismo());
        this.setDireccion(organismoAux.getDireccion());
        this.setTelefonos(organismoAux.getTelefonos());
        this.setCorreoOficial(organismoAux.getCorreoOficial());
        if (organismoAux.getClasificadorOrganismo() != null) {
            this.setIdClasificadorOrganismo(organismoAux.getClasificadorOrganismo().getId());
        }
        if (organismoAux.getOrganismoPadre() != null) {
            this.setIdOrganismoPadre(organismoAux.getOrganismoPadre().getId());
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            this.organismoFacade.edit(this.getId(), this.getIdOrganismoPadre(),
                    this.getIdClasificadorOrganismo(), this.getCodigoOrganismo(),
                    this.getNombre(),
                    this.getDireccion(), this.getTelefonos(),
                    this.getCorreoOficial());
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorOrganismo");
            this.setMsgSuccessError("El organismo ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Organismo> lstOrganismo = this.organismoFacade.findAll(true);
        if (!(lstOrganismo.isEmpty())) {
            lstOrganismo.stream().forEach((organismoAux) -> {
                selectItems.add(new SelectItem(organismoAux.getId(), this.ceroIzquierda(organismoAux.getCodigoOrganismo()) + " - " + organismoAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsClasificadorOrganismo() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<ClasificadorOrganismo> lstClasificadorOrganismo = this.clasificadorOrganismoFacade.findAll(true);
        if (!(lstClasificadorOrganismo.isEmpty())) {
            lstClasificadorOrganismo.stream().forEach((clasificadorOrganismoAux) -> {
                selectItems.add(new SelectItem(clasificadorOrganismoAux.getId(), clasificadorOrganismoAux.getId() + " - " + clasificadorOrganismoAux.getNombre()));
            });
        }
        return selectItems;
    }

    @Override
    public void actualizar() {
        try {
            this.setCodigoOrganismoBsq(null);
            this.setIdOrganismoPadreBsq(null);
            this.setIdClasificadorOrganismoBsq(null);
            this.setNombreBsq("");
            super.actualizar();
        } catch (Exception ex) {
            Logger.getLogger(OrganismoManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void serviciosSeleccionados() {
        List<Servicio> listServicioItems = new ArrayList();
        this.itemsIzquierda = new ArrayList<>();
        if (!(listServicioItems.isEmpty())) {
            listServicioItems.stream().forEach((servicioAux) -> {
                this.itemsIzquierda.add(new SelectItem(servicioAux.getId(), servicioAux.getAbreviatura()));
            });
        }
    }

    public void serviciosDisponibles(List<Servicio> lstServicios) {
        List<Servicio> listServicioItemsAll = servicioFacade.findAll(true);
        this.itemsDerecha = new ArrayList<>();
        listServicioItemsAll.removeAll(lstServicios);
        if (!(listServicioItemsAll.isEmpty())) {
            listServicioItemsAll.stream().forEach((servicioAux) -> {
                this.itemsDerecha.add(new SelectItem(servicioAux.getId(), servicioAux.getAbreviatura()));
            });
        }
    }

    public void emailValido(FacesContext context, UIComponent validate, Object value) {
        String email = (String) value;
        if (!Utilidad.isValidEmailAddress(email)) {
            FacesMessage msg = new FacesMessage("El email ingresado no tiene el formato correcto.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

  

    public void validarCodigo(final FacesContext context, final UIComponent validate, final Object value) {
        if (Long.parseLong(value.toString()) < 0) {
            final FacesMessage msg = new FacesMessage("El código debe ser un número positivo.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
        if (!this.organismoFacade.existsOrganismo(Long.parseLong(value.toString()))) {
            final FacesMessage msg = new FacesMessage("El organismo ya existe.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public String ceroIzquierda(Long codigo) {
        return Utilidad.ceroIzquierda(codigo);
    }

}
