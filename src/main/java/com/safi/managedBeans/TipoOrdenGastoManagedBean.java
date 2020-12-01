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
import com.safi.entity.TipoOrdenGasto;
import com.safi.facade.TipoOrdenGastoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@RequestScoped
public class TipoOrdenGastoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoOrdenGastoFacadeLocal tipoOrdenGastoFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of TipoOrdenGastoManagedBean
     */
    public TipoOrdenGastoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoOrdenGasto")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoOrdenGasto")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoOrdenGasto")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoOrdenGasto"))).forEach((_item) -> {
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
        LazyDataModel<TipoOrdenGasto> lstTipoOrdenGastosAux = new LazyDataModel<TipoOrdenGasto>() {

            @Override
            public List<TipoOrdenGasto> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoOrdenGasto> lstTipoOrdenGastosAux = tipoOrdenGastoFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoOrdenGastosAux;
            }

        };
        lstTipoOrdenGastosAux.setRowCount(tipoOrdenGastoFacade.countAll(nombreBsq).intValue());
        return lstTipoOrdenGastosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoOrdenGastoFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoOrdenGasto");
            this.setMsgSuccessError("El código de orden ha sido generado con éxito");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoOrdenGasto");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoOrdenGastoFacade.create(this.getNombre());
            this.setResultado("nuevoTipoOrdenGasto");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoOrdenGasto");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoOrdenGastoAux = Long.parseLong(myRequest.getParameter("id"));
        TipoOrdenGasto tipoOrdenGastoAux = this.tipoOrdenGastoFacade.find(idTipoOrdenGastoAux);
        this.setId(tipoOrdenGastoAux.getId());
        this.setNombre(tipoOrdenGastoAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoOrdenGastosAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoOrdenGastoFacade.remove(idTipoOrdenGastosAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoOrdenGastoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoOrdenGasto");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoOrdenGastoAux = Long.parseLong(myRequest.getParameter("id"));
        TipoOrdenGasto tipoOrdenGastoAux = this.tipoOrdenGastoFacade.find(idTipoOrdenGastoAux);
        this.setId(tipoOrdenGastoAux.getId());
        this.setNombre(tipoOrdenGastoAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoOrdenGastoFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoOrdenGasto");
            this.setMsgSuccessError("El código de orden ha sido editado con éxito");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoOrdenGasto");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
