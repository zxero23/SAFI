package com.safi.managedBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.ClasificadorOrganismo;
import com.safi.facade.ClasificadorOrganismoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga
 */
@ManagedBean
@RequestScoped
public class ClasificadorOrganismoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private ClasificadorOrganismoFacadeLocal clasificadorOrganismoFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of ClasificadorOrganismoManagedBean
     */
    public ClasificadorOrganismoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
               
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoClasificadorOrganismo")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarClasificadorOrganismo")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarClasificadorOrganismo")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleClasificadorOrganismo"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<ClasificadorOrganismo> lstClasificadorOrganismosAux = new LazyDataModel<ClasificadorOrganismo>() {

            @Override
            public List<ClasificadorOrganismo> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<ClasificadorOrganismo> lstClasificadorOrganismosAux = clasificadorOrganismoFacade.findAll(nombreBsq, first, pageSize);

                return lstClasificadorOrganismosAux;
            }

        };
        lstClasificadorOrganismosAux.setRowCount(clasificadorOrganismoFacade.countAll(nombreBsq).intValue());
        return lstClasificadorOrganismosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.clasificadorOrganismoFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorClasificadorOrganismo");
            this.setMsgSuccessError("El clasificador de organismo ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.clasificadorOrganismoFacade.create(this.getNombre());
            this.setResultado("nuevoClasificadorOrganismo");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        ClasificadorOrganismo clasificadorOrganismoAux = this.clasificadorOrganismoFacade.find(Long.parseLong(myRequest.getParameter("id")));
        this.setId(clasificadorOrganismoAux.getId());
        this.setNombre(clasificadorOrganismoAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            this.clasificadorOrganismoFacade.remove(Long.parseLong(myRequest.getParameter("id")));
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("clasificadorOrganismoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorOrganismo");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        ClasificadorOrganismo clasificadorOrganismoAux = this.clasificadorOrganismoFacade.find(Long.parseLong(myRequest.getParameter("id")));
        this.setId(clasificadorOrganismoAux.getId());
        this.setNombre(clasificadorOrganismoAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.clasificadorOrganismoFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorClasificadorOrganismo");
            this.setMsgSuccessError("El clasificador de organismos ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorClasificadorOrganismo");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

}
