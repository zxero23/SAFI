package org.safi.web;

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
import org.safi.entity.Rol;
import org.safi.facade.RolFacadeLocal;
import org.safi.web.UtilManagedBean;
import org.safi.web.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class RolManagedBean extends UtilManagedBean implements Serializable {

    /**
     * Creates a new instance of RolManagedBean
     */
    @EJB
    private RolFacadeLocal rolFacade;
    private String nombre;
    private String nombreBsq;

    public RolManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoRol")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarRol")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarRol")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleRol"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public RolFacadeLocal getRolFacade() {
        return rolFacade;
    }

    public void setRolFacade(RolFacadeLocal rolFacade) {
        this.rolFacade = rolFacade;
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
        LazyDataModel<Rol> lstRolsAux = new LazyDataModel<Rol>() {

            @Override
            public List<Rol> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Rol> lstRolsAux = rolFacade.findAll(nombreBsq, first, pageSize);

                return lstRolsAux;
            }

        };
        lstRolsAux.setRowCount(rolFacade.countAll(nombreBsq).intValue());
        return lstRolsAux;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Rol> listRol = rolFacade.findAll(true);
        if (!(listRol.isEmpty())) {
            listRol.stream().forEach((rolAux) -> {
                selectItems.add(new SelectItem(rolAux.getId(), rolAux.getNombre()));
            });
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
            rolFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorRol");
            this.setMsgSuccessError("El rol ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRol");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            rolFacade.create(this.getNombre());
            this.setResultado("nuevoRol");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRol");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idRolAux = Long.parseLong(myRequest.getParameter("id"));
        Rol rolAux = this.rolFacade.find(idRolAux);
        this.setId(rolAux.getId());
        this.setNombre(rolAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idRolAux = Long.parseLong(myRequest.getParameter("id"));
            this.rolFacade.remove(idRolAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("rolConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRol");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idRolAux = Long.parseLong(myRequest.getParameter("id"));
        Rol rolAux = this.rolFacade.find(idRolAux);
        this.setId(rolAux.getId());
        this.setNombre(rolAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.rolFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorRol");
            this.setMsgSuccessError("El rol ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorRol");
        }
        return this.getResultado();
    }

}
