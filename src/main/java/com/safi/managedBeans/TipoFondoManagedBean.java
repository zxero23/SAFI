package com.safi.managedBeans;

import java.io.Serializable;
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
import com.safi.entity.TipoFondo;
import com.safi.facade.TipoFondoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class TipoFondoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoFondoFacadeLocal tipoFondoFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of TipoFondoManagedBean
     */
    public TipoFondoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoFondo")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoFondo")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoFondo")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoFondo"))).forEach((_item) -> {
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
        LazyDataModel<TipoFondo> lstTipoFondosAux = new LazyDataModel<TipoFondo>() {

            @Override
            public List<TipoFondo> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoFondo> lstTipoFondosAux = tipoFondoFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoFondosAux;
            }

        };
        lstTipoFondosAux.setRowCount(tipoFondoFacade.countAll(nombreBsq).intValue());
        return lstTipoFondosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoFondoFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoFondo");
            this.setMsgSuccessError("La clase de fondo ha sido generada con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoFondo");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoFondoFacade.create(this.getNombre());
            this.setResultado("nuevoTipoFondo");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoFondo");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoFondoAux = Long.parseLong(myRequest.getParameter("id"));
        TipoFondo tipoFondoAux = this.tipoFondoFacade.find(idTipoFondoAux);
        this.setId(tipoFondoAux.getId());
        this.setNombre(tipoFondoAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoFondoAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoFondoFacade.remove(idTipoFondoAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoFondoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoFondo");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoFondoAux = Long.parseLong(myRequest.getParameter("id"));
        TipoFondo tipoFondoAux = this.tipoFondoFacade.find(idTipoFondoAux);
        this.setId(tipoFondoAux.getId());
        this.setNombre(tipoFondoAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoFondoFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoFondo");
            this.setMsgSuccessError("La clase de fondo ha sido editada con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoFondo");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
