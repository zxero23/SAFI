package com.safi.managedBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.Banco;
import com.safi.facade.BancoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class BancoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private BancoFacadeLocal bancoFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of BancoManagedBean
     */
    public BancoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoBanco")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarBanco")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarBanco")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleBanco"))).forEach((_item) -> {
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
        LazyDataModel<Banco> lstBancosAux = new LazyDataModel<Banco>() {

            @Override
            public List<Banco> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Banco> lstBancosAux = bancoFacade.findAll(nombreBsq, first, pageSize);

                return lstBancosAux;
            }

        };
        lstBancosAux.setRowCount(bancoFacade.countAll(nombreBsq).intValue());
        return lstBancosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setNombreOriginal(null);
    }

    @Override
    public String crear() {
        try {
            this.bancoFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorBanco");
            this.setMsgSuccessError("El banco ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorBanco");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.bancoFacade.create(this.getNombre());
            this.setResultado("nuevoBanco");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorBanco");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idBancoAux = Long.parseLong(myRequest.getParameter("id"));
        Banco bancoAux = this.bancoFacade.find(idBancoAux);
        this.setId(bancoAux.getId());
        this.setNombre(bancoAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idBancoAux = Long.parseLong(myRequest.getParameter("id"));
            this.bancoFacade.remove(idBancoAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("bancoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorBanco");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idBancoAux = Long.parseLong(myRequest.getParameter("id"));
        Banco bancoAux = this.bancoFacade.find(idBancoAux);
        this.setId(bancoAux.getId());
        this.setNombre(bancoAux.getNombre());
        this.setNombreOriginal(bancoAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.bancoFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorBanco");
            this.setMsgSuccessError("El banco ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorBanco");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Comprueba que no exista el nombre del banco en el sistema.
     *
     * @author Doroñuk Gustavo
     * @param context
     * @param validate
     * @param value
     */
    public void validatorNombreBanco(FacesContext context, UIComponent validate, Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String evaluar = (String) value;
        List<Banco> lstBancoAux = this.bancoFacade.findByName(evaluar.toUpperCase());
        if (this.getNombreOriginal() != null) {
            if (!lstBancoAux.isEmpty() && !this.getNombreOriginal().equalsIgnoreCase(evaluar)) {
                FacesMessage msg = new FacesMessage("Ya existe un banco con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } else {
            if (!lstBancoAux.isEmpty()) {
                FacesMessage msg = new FacesMessage("Ya existe un banco con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

}
