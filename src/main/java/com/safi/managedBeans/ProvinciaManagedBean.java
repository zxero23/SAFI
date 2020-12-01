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
import com.safi.entity.Provincia;
import com.safi.facade.ProvinciaFacadeLocal;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class ProvinciaManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    private String nombre;
    private String nombreBsq;
    private Provincia provincia;
 

    /**
     * Creates a new instance of ProvinciaManagedBean
     */
    public ProvinciaManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevaProvincia")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarProvincia")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarProvincia")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleProvincia"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
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
        LazyDataModel<Provincia> lstProvinciasAux = new LazyDataModel<Provincia>() {
            @Override
            public List<Provincia> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Provincia> lstProvinciasAux = provinciaFacade.findAll(nombreBsq, first, pageSize);
                return lstProvinciasAux;
            }
        };
        lstProvinciasAux.setRowCount(provinciaFacade.countAll(nombreBsq).intValue());
        return lstProvinciasAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setProvincia(null);
    }

    @Override
    public String crear() {
        try {
            this.provinciaFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorProvincia");
            this.setMsgSuccessError("La provincia ha sido generada con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorProvincia");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.provinciaFacade.create(this.getNombre());
            this.setResultado("nuevoProvincia");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorProvincia");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Provincia provinciaAux = this.provinciaFacade.find(idAux);
        this.setProvincia(provinciaAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.provinciaFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("provinciaConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorProvincia");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Provincia provinciaAux = this.provinciaFacade.find(idAux);
        this.setId(provinciaAux.getId());
        this.setNombre(provinciaAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.provinciaFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorProvincia");
            this.setMsgSuccessError("La Provincia ha sido editada con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorProvincia");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Provincia> lstProvincia = this.provinciaFacade.findAll();
        if (!(lstProvincia.isEmpty())) {
            lstProvincia.stream().forEach((provinciaAux) -> {
                selectItems.add(new SelectItem(provinciaAux.getId(), provinciaAux.getNombre()));
            });
        }
        return selectItems;
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar();
    }
}
