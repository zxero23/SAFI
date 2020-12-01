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
import com.safi.entity.Grupo;
import com.safi.facade.GrupoFacadeLocal;

/**
 *
 * @author matias
 */
@ManagedBean
@SessionScoped
public class GrupoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private GrupoFacadeLocal grupoFacade;
    private String nombre;
    private String nombreBsq;
    private String descripcion;
   
    public GrupoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoGrupo")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarGrupo")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarGrupo")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleGrupo"))).forEach((_item) -> {
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

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
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
        LazyDataModel<Grupo> lstGruposAux = new LazyDataModel<Grupo>() {

            @Override
            public List<Grupo> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Grupo> lstGruposAux = grupoFacade.findAll(nombreBsq, first, pageSize);

                return lstGruposAux;
            }

        };
        lstGruposAux.setRowCount(grupoFacade.countAll(nombreBsq).intValue());
        return lstGruposAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setDescripcion(null);
    }

    @Override
    public String crear() {
        try {
            grupoFacade.create(this.getNombre(), this.getDescripcion());
            this.setResultado("successErrorGrupo");
            this.setMsgSuccessError("El grupo ha sido generado con éxito.");
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            grupoFacade.create(this.getNombre(), this.getDescripcion());
            this.setResultado("nuevoGrupo");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idGrupoAux = Long.parseLong(myRequest.getParameter("id"));
        Grupo grupoAux = this.grupoFacade.find(idGrupoAux);
        this.setId(grupoAux.getId());
        this.setNombre(grupoAux.getNombre());
        this.setDescripcion(grupoAux.getDescripcion());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.grupoFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("grupoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Grupo grupoAux = this.grupoFacade.find(idAux);
        this.setId(grupoAux.getId());
        this.setNombre(grupoAux.getNombre());
        this.setDescripcion(grupoAux.getDescripcion());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.grupoFacade.edit(this.getId(), this.getNombre(), this.getDescripcion());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorGrupo");
            this.setMsgSuccessError("El grupo ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorGrupo");
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
