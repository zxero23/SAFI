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
import com.safi.entity.Menu;
import com.safi.facade.MenuFacadeLocal;

/**
 *
 * @author matias
 */
@ManagedBean
@SessionScoped
public class MenuManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private MenuFacadeLocal menuFacade;
    private String nombre;
    private String nombreBsq;
    private String idMenuPadre;
    private String NombreMenuPadre;
    private String link;
    private String icon;
  

    public MenuManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoMenu")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarMenu")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarMenu")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleMenu"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdMenuPadre(String idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
    }

    public String getIdMenuPadre() {
        return idMenuPadre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNombreMenuPadre() {
        return NombreMenuPadre;
    }

    public void setNombreMenuPadre(String NombreMenuPadre) {
        this.NombreMenuPadre = NombreMenuPadre;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Menu> lstMenusAux = new LazyDataModel<Menu>() {
            @Override
            public List<Menu> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Menu> lstMenuAux = menuFacade.findAll(nombreBsq, first, pageSize);
                return lstMenuAux;
            }
        };
        lstMenusAux.setRowCount(menuFacade.countAll(nombreBsq).intValue());
        return lstMenusAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setNombreMenuPadre(null);
        this.setLink(null);
        this.setIcon(null);
        this.setIdMenuPadre(null);
    }

    @Override
    public String crear() {
        try {
            menuFacade.create(Long.parseLong(this.getIdMenuPadre()), this.getNombre(), this.getLink(), this.getIcon());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorMenu");
            this.setMsgSuccessError("El menú ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMenu");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            menuFacade.create(Long.parseLong(this.getIdMenuPadre()), this.getNombre(), this.getLink(), this.getIcon());
            this.setResultado("nuevoMenu");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMenu");
            this.limpiar();
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Menu menuAux = this.menuFacade.find(idAux);
        this.setId(menuAux.getId());
        this.setNombre(menuAux.getNombre());
        this.setNombreMenuPadre(menuAux.getMenu() == null ? "NINGUNO" : menuAux.getMenu().getNombre());
        this.setLink(menuAux.getLink());
        this.setIcon(menuAux.getIcon());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.menuFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("menuConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMenu");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Menu menuAux = this.menuFacade.find(idAux);
        this.setId(menuAux.getId());
        this.setNombre(menuAux.getNombre());
        if (menuAux.getMenu() != null) {
            this.setIdMenuPadre(String.valueOf(menuAux.getMenu().getId()));
        }
        this.setLink(menuAux.getLink());
        this.setIcon(menuAux.getIcon());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.menuFacade.edit(this.getId(), Long.parseLong(this.getIdMenuPadre()), this.getNombre(), this.getLink(), this.getIcon());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorMenu");
            this.setMsgSuccessError("El menú ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMenu");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Menu> listMenu = menuFacade.findAll(true);
        if (!(listMenu.isEmpty())) {
            listMenu.stream().forEach((menuAux) -> {
                selectItems.add(new SelectItem(menuAux.getId(), menuAux.getNombre()));
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
