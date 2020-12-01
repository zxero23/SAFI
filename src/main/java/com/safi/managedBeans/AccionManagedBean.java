package com.safi.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.ActionItem;
import com.safi.facade.ActionItemFacadeLocal;

/**
 *
 * @author matias
 */
@ManagedBean
@SessionScoped
public class AccionManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private ActionItemFacadeLocal actionFacade;

    private String nombre;
    private String nombreBsq;
    private String idMenuPadre;

    /**
     * Creates a new instance of AccionManagedBean
     */
    public AccionManagedBean() {

    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevaAccion")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarAccion")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarAccion")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleAccion"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdMenuPadre(String idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
    }

    public String getIdMenuPadre() {
        return idMenuPadre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<ActionItem> lstActionItemsAux = new LazyDataModel<ActionItem>() {

            @Override
            public List<ActionItem> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<ActionItem> lstActionItemsAux = actionFacade.findAll(nombreBsq, first, pageSize);

                return lstActionItemsAux;
            }

        };
        lstActionItemsAux.setRowCount(actionFacade.countAll(nombreBsq).intValue());
        return lstActionItemsAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            actionFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorAccion");
            this.setMsgSuccessError("La acción ha sido generada con éxito.");
            this.setEsCorrecto(true);
            this.limpiar();
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAccion");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            actionFacade.create(this.getNombre());
            this.setResultado("nuevaAccion");
            this.setEsCorrecto(true);
            this.limpiar();
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAccion");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAccionAux = Long.parseLong(myRequest.getParameter("id"));
        ActionItem actionItemsAux = this.actionFacade.find(idAccionAux);
        this.setId(actionItemsAux.getId());
        this.setNombre(actionItemsAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.actionFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("accionConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAccion");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAccionAux = Long.parseLong(myRequest.getParameter("id"));
        ActionItem actionItemsAux = this.actionFacade.find(idAccionAux);
        this.setId(actionItemsAux.getId());
        this.setNombre(actionItemsAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.actionFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorAccion");
            this.setMsgSuccessError("La acción ha sido editada con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAccion");
        }
        return this.getResultado();
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
