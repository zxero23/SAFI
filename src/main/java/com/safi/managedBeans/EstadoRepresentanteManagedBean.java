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
import com.safi.entity.EstadoRepresentante;
import com.safi.facade.EstadoRepresentanteFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class EstadoRepresentanteManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EstadoRepresentanteFacadeLocal estadoRepresentanteFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of EstadoRepresentanteManagedBean
     */
    public EstadoRepresentanteManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoEstadoRepresentante")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarEstadoRepresentante")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarEstadoRepresentante")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleEstadoRepresentante"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public EstadoRepresentanteFacadeLocal getEstadoRepresentanteFacade() {
        return estadoRepresentanteFacade;
    }

    public void setEstadoRepresentanteFacade(EstadoRepresentanteFacadeLocal estadoRepresentanteFacade) {
        this.estadoRepresentanteFacade = estadoRepresentanteFacade;
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
        LazyDataModel<EstadoRepresentante> lstEstadoRepresentantesAux = new LazyDataModel<EstadoRepresentante>() {

            @Override
            public List<EstadoRepresentante> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<EstadoRepresentante> lstEstadoRepresentantesAux = estadoRepresentanteFacade.findAll(nombreBsq, first, pageSize);

                return lstEstadoRepresentantesAux;
            }

        };
        lstEstadoRepresentantesAux.setRowCount(estadoRepresentanteFacade.countAll(nombreBsq).intValue());
        return lstEstadoRepresentantesAux;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<EstadoRepresentante> listEstadoRepresentante = estadoRepresentanteFacade.findAll(true);
        if (!(listEstadoRepresentante.isEmpty())) {
            for (EstadoRepresentante estadoRepresentanteAux : listEstadoRepresentante) {
                selectItems.add(new SelectItem(estadoRepresentanteAux.getId(), estadoRepresentanteAux.getNombre()));
            }
        }
        return selectItems;
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
            estadoRepresentanteFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoRepresentante");
            this.setMsgSuccessError("El estado de representante ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoRepresentante");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            estadoRepresentanteFacade.create(this.getNombre());
            this.setResultado("nuevoEstadoRepresentante");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoRepresentante");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoRepresentanteAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoRepresentante estadoRepresentanteAux = this.estadoRepresentanteFacade.find(idEstadoRepresentanteAux);
        this.setId(estadoRepresentanteAux.getId());
        this.setNombre(estadoRepresentanteAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAccionAux = Long.parseLong(myRequest.getParameter("id"));
            this.estadoRepresentanteFacade.remove(idAccionAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("estadoRepresentanteConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successEstadoRepresentante");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoRepresentanteAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoRepresentante estadoRepresentanteAux = this.estadoRepresentanteFacade.find(idEstadoRepresentanteAux);
        this.setId(estadoRepresentanteAux.getId());
        this.setNombre(estadoRepresentanteAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.estadoRepresentanteFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoRepresentante");
            this.setMsgSuccessError("El estado del representante ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoRepresentante");
        }
        return this.getResultado();
    }

}
