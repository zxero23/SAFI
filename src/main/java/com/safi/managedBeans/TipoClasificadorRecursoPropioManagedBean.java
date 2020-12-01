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
import com.safi.entity.TipoClasificadorRecursoPropio;
import com.safi.facade.TipoClasificadorRecursoPropioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga
 */
@ManagedBean
@SessionScoped
public class TipoClasificadorRecursoPropioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoClasificadorRecursoPropioFacadeLocal tipoClasificadorFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of TipoClasificadorRecursoPropioManagedBean
     */
    public TipoClasificadorRecursoPropioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoClasificadorRecursoPropio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoClasificadorRecursoPropio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoClasificadorRecursoPropio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoClasificadorRecursoPropio"))).forEach((_item) -> {
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
        LazyDataModel<TipoClasificadorRecursoPropio> lstTipoClasificadorRecursoPropiosAux = new LazyDataModel<TipoClasificadorRecursoPropio>() {

            @Override
            public List<TipoClasificadorRecursoPropio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoClasificadorRecursoPropio> lstTipoClasificadorRecursoPropiosAux = tipoClasificadorFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoClasificadorRecursoPropiosAux;
            }

        };
        lstTipoClasificadorRecursoPropiosAux.setRowCount(tipoClasificadorFacade.countAll(nombreBsq).intValue());
        return lstTipoClasificadorRecursoPropiosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crear() {
        try {
            this.tipoClasificadorFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
            this.setMsgSuccessError("El tipo de clasificador de recurso propio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoClasificadorFacade.create(this.getNombre());
            this.setResultado("nuevoTipoClasificadorRecursoPropio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.tipoClasificadorFacade.find(idTipoClasificadorRecursoPropioAux);
        this.setId(tipoClasificadorRecursoPropioAux.getId());
        this.setNombre(tipoClasificadorRecursoPropioAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoClasificadorFacade.remove(idTipoClasificadorRecursoPropioAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoClasificadorRecursoPropioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoClasificadorRecursoPropioAux = Long.parseLong(myRequest.getParameter("id"));
        TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.tipoClasificadorFacade.find(idTipoClasificadorRecursoPropioAux);
        this.setId(tipoClasificadorRecursoPropioAux.getId());
        this.setNombre(tipoClasificadorRecursoPropioAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoClasificadorFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
            this.setMsgSuccessError("El tipo de clasificador de recursos propios ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoClasificadorRecursoPropio");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
