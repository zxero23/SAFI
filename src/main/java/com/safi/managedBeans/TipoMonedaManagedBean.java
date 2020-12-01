package com.safi.managedBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.TipoMoneda;
import com.safi.facade.TipoMonedaFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@RequestScoped
public class TipoMonedaManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoMonedaFacadeLocal tipoMonedaFacade;
    private String nombre;
    private String nombreBsq;
    private String descripcion;

    /**
     * Creates a new instance of TipoMonedaManagedBean
     */
    public TipoMonedaManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoMoneda")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoMoneda")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoMoneda")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoMoneda"))).forEach((_item) -> {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<TipoMoneda> lstTipoMonedasAux = new LazyDataModel<TipoMoneda>() {

            @Override
            public List<TipoMoneda> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoMoneda> lstTipoMonedasAux = tipoMonedaFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoMonedasAux;
            }

        };
        lstTipoMonedasAux.setRowCount(tipoMonedaFacade.countAll(nombreBsq).intValue());
        return lstTipoMonedasAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setDescripcion(null);
        this.setNombreOriginal(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoMonedaFacade.create(this.getNombre(), this.getDescripcion());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoMoneda");
            this.setMsgSuccessError("El tipo de moneda ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoMoneda");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoMonedaFacade.create(this.getNombre(), this.getDescripcion());
            this.setResultado("nuevoTipoMoneda");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
             this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoMoneda");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoMonedaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMonedaAux);
        this.setId(tipoMonedaAux.getId());
        this.setDescripcion(tipoMonedaAux.getDescripcion());
        this.setNombre(tipoMonedaAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoMonedaAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoMonedaFacade.remove(idTipoMonedaAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoMonedaConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoMoneda");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoMonedaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMonedaAux);
        this.setId(tipoMonedaAux.getId());
        this.setNombre(tipoMonedaAux.getNombre());
        this.setDescripcion(tipoMonedaAux.getDescripcion());
        this.setNombreOriginal(tipoMonedaAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoMonedaFacade.edit(this.getId(), this.getNombre(), this.getDescripcion());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoMoneda");
            this.setMsgSuccessError("El tipo de moneda ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoMoneda");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Comprueba que no exista el nombre del tipo de moneda en el sistema.
     *
     * @author Doroñuk Gustavo
     * @param context
     * @param validate
     * @param value
     */
    public void validatorNombreTipoMoneda(FacesContext context, UIComponent validate, Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String evaluar = (String) value;
        List<TipoMoneda> lstTipoMonedaAux = this.tipoMonedaFacade.findByName(evaluar.toUpperCase());
        if (this.getNombreOriginal() != null) {
            if (!lstTipoMonedaAux.isEmpty() && !this.getNombreOriginal().equalsIgnoreCase(evaluar)) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de moneda con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } else {
            if (!lstTipoMonedaAux.isEmpty()) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de moneda con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

}
