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
import com.safi.entity.TipoAcumulador;
import com.safi.facade.TipoAcumuladorFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class TipoAcumuladorManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoAcumuladorFacadeLocal tipoAcumuladorFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of TipoAcumuladorManagedBean
     */
    public TipoAcumuladorManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoAcumulador")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoAcumulador")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoAcumulador")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoAcumulador"))).forEach((_item) -> {
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
        LazyDataModel<TipoAcumulador> lstTipoAcumuladorsAux = new LazyDataModel<TipoAcumulador>() {

            @Override
            public List<TipoAcumulador> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoAcumulador> lstTipoAcumuladorsAux = tipoAcumuladorFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoAcumuladorsAux;
            }

        };
        lstTipoAcumuladorsAux.setRowCount(tipoAcumuladorFacade.countAll(nombreBsq).intValue());
        return lstTipoAcumuladorsAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoAcumuladorFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoAcumulador");
            this.setMsgSuccessError("El acumulador ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoAcumulador");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoAcumuladorFacade.create(this.getNombre());
            this.setResultado("nuevoTipoAcumulador");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoAcumulador");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoAcumuladorAux = Long.parseLong(myRequest.getParameter("id"));
        TipoAcumulador tipoAcumuladorAux = this.tipoAcumuladorFacade.find(idTipoAcumuladorAux);
        this.setId(tipoAcumuladorAux.getId());
        this.setNombre(tipoAcumuladorAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoImputacionAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoAcumuladorFacade.remove(idTipoImputacionAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoAcumuladorConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoAcumulador");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoAcumuladorAux = Long.parseLong(myRequest.getParameter("id"));
        TipoAcumulador tipoAcumuladorAux = this.tipoAcumuladorFacade.find(idTipoAcumuladorAux);
        this.setId(tipoAcumuladorAux.getId());
        this.setNombre(tipoAcumuladorAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoAcumuladorFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoAcumulador");
            this.setMsgSuccessError("El acumulador ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoAcumulador");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
