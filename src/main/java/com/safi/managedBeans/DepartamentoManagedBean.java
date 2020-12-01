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
import com.safi.entity.Departamento;
import com.safi.entity.Provincia;
import com.safi.facade.DepartamentoFacadeLocal;
import com.safi.facade.ProvinciaFacadeLocal;

/**
 *
 * @author Alvarenga Angel
 */
@ManagedBean
@SessionScoped
public class DepartamentoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    @EJB
    private ProvinciaFacadeLocal provinciaFacade;

    private String nombre;
    private String nombreBsq;
    private Long idProvincia;
    private Long idPais;
    private Departamento departamento;
    // ** atributos para la búsqueda **//
    private Long idProvinciaBsq = 0L;
    private Long idPaisBsq = 0L;
  
    // ** fin atributos para la búsqueda **//

    /**
     * Creates a new instance of DepartamentoManagedBean
     */
    public DepartamentoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoDepartamento")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarDepartamento")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarDepartamento")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleDepartamento"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Long getIdProvinciaBsq() {
        return idProvinciaBsq;
    }

    public void setIdProvinciaBsq(Long idProvinciaBsq) {
        this.idProvinciaBsq = idProvinciaBsq;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Long idProvincia) {
        this.idProvincia = idProvincia;
    }

    public DepartamentoFacadeLocal getDepartamentoFacade() {
        return departamentoFacade;
    }

    public void setDepartamentoFacade(DepartamentoFacadeLocal departamentoFacade) {
        this.departamentoFacade = departamentoFacade;
    }

    public ProvinciaFacadeLocal getProvinciaFacade() {
        return provinciaFacade;
    }

    public void setProvinciaFacade(ProvinciaFacadeLocal provinciaFacade) {
        this.provinciaFacade = provinciaFacade;
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public Long getIdPaisBsq() {
        return idPaisBsq;
    }

    public void setIdPaisBsq(Long idPaisBsq) {
        this.idPaisBsq = idPaisBsq;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Departamento> lstDepartamentosAux = new LazyDataModel<Departamento>() {
            @Override
            public List<Departamento> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Departamento> lstDepartamentosAux = departamentoFacade.findAll(idProvinciaBsq, nombreBsq, first, pageSize);
                return lstDepartamentosAux;
            }

        };
        lstDepartamentosAux.setRowCount(departamentoFacade.countAll(idProvinciaBsq, nombreBsq).intValue());
        return lstDepartamentosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setIdProvincia(null);
        this.setDepartamento(null);
        this.setIdPais(null);
    }

    @Override
    public String crear() {
        try {
            this.departamentoFacade.create(this.getIdProvincia(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorDepartamento");
            this.setMsgSuccessError("El departamento ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorDepartamento");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.departamentoFacade.create(this.getIdProvincia(), this.getNombre());
            this.setResultado("nuevoDepartamento");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorDepartamento");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Departamento deptoAux = this.departamentoFacade.find(idAux);
        this.setDepartamento(deptoAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.departamentoFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("departamentoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorDepartamento");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Departamento deptoAux = this.departamentoFacade.find(idAux);
        this.setId(deptoAux.getId());
        this.setNombre(deptoAux.getNombre());
        this.setIdProvincia(deptoAux.getProvincia().getId());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.departamentoFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorDepartamento");
            this.setMsgSuccessError("El departamento ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorDepartamento");
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

    public List<SelectItem> getSelectItemsProvincia() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Provincia> lstProvincia = this.provinciaFacade.findAll();
        if (!(lstProvincia.isEmpty())) {
            lstProvincia.stream().forEach((provinciaAux) -> {
                selectItems.add(new SelectItem(provinciaAux.getId(), provinciaAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsPais() {
        List<SelectItem> selectItems = new ArrayList<>();
        return selectItems;
    }

    @Override
    public void actualizar() {
        this.setIdProvinciaBsq(null);
        super.actualizar();
    }

}
