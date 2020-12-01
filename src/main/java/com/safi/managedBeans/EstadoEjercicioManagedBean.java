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
import com.safi.entity.EstadoEjercicio;
import com.safi.facade.EstadoEjercicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class EstadoEjercicioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EstadoEjercicioFacadeLocal estadoEjercicioFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of EstadoEjercicioManagedBean
     */
    public EstadoEjercicioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoEstadoEjercicio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarEstadoEjercicio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarEstadoEjercicio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleEstadoEjercicio"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public EstadoEjercicioFacadeLocal getEstadoEjercicioFacade() {
        return estadoEjercicioFacade;
    }

    public void setEstadoEjercicioFacade(EstadoEjercicioFacadeLocal estadoEjercicioFacade) {
        this.estadoEjercicioFacade = estadoEjercicioFacade;
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
        LazyDataModel<EstadoEjercicio> lstEstadoEjerciciosAux = new LazyDataModel<EstadoEjercicio>() {

            @Override
            public List<EstadoEjercicio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<EstadoEjercicio> lstEstadoEjerciciosAux = estadoEjercicioFacade.findAll(nombreBsq, first, pageSize);

                return lstEstadoEjerciciosAux;
            }

        };
        lstEstadoEjerciciosAux.setRowCount(estadoEjercicioFacade.countAll(nombreBsq).intValue());
        return lstEstadoEjerciciosAux;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<EstadoEjercicio> listEstadoEjercicio = estadoEjercicioFacade.findAll(true);
        if (!(listEstadoEjercicio.isEmpty())) {
            for (EstadoEjercicio estadoEjercicioAux : listEstadoEjercicio) {
                selectItems.add(new SelectItem(estadoEjercicioAux.getId(), estadoEjercicioAux.getNombre()));
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
        super.actualizar();
    }

    @Override
    public String crear() {
        try {
            estadoEjercicioFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoEjercicio");
            this.setMsgSuccessError("El estado de ejercicio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoEjercicio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            estadoEjercicioFacade.create(this.getNombre());
            this.setResultado("nuevoEstadoEjercicio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoEjercicio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoEjercicio estadoEjercicioAux = this.estadoEjercicioFacade.find(idEstadoEjercicioAux);
        this.setId(estadoEjercicioAux.getId());
        this.setNombre(estadoEjercicioAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAccionAux = Long.parseLong(myRequest.getParameter("id"));
            this.estadoEjercicioFacade.remove(idAccionAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("estadoEjercicioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successEstadoEjercicio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoEjercicioAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoEjercicio estadoEjercicioAux = this.estadoEjercicioFacade.find(idEstadoEjercicioAux);
        this.setId(estadoEjercicioAux.getId());
        this.setNombre(estadoEjercicioAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.estadoEjercicioFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoEjercicio");
            this.setMsgSuccessError("El estado del ejercicio ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoEjercicio");
        }
        return this.getResultado();
    }

}
