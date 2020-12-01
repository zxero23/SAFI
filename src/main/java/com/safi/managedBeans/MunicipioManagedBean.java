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
import com.safi.entity.Municipio;
import com.safi.entity.Provincia;
import com.safi.facade.DepartamentoFacadeLocal;
import com.safi.facade.MunicipioFacadeLocal;
import com.safi.facade.ProvinciaFacadeLocal;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class MunicipioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    @EJB
    private MunicipioFacadeLocal municipioFacade;
    private Long idDepartamento;
    private Long idProvincia;
    private String nombre;
    private String nombreBsq;
    private String codigoPostal;
    private Municipio municipio;
    /* Inicio atributos de búsqueda */
    private Long idProvinciaBsq = 0L;
    private Long idDepartamentoBsq = 0L;
    private String codigoPostalBsq = "";
     /* Fin atributos de búsqueda */

    /**
     * Creates a new instance of MunicipioManagedBean
     */
    public MunicipioManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoMunicipio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarMunicipio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarMunicipio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleMunicipio"))).forEach((_item) -> {
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Long getIdProvinciaBsq() {
        return idProvinciaBsq;
    }

    public void setIdProvinciaBsq(Long idProvinciaBsq) {
        this.idProvinciaBsq = idProvinciaBsq;
    }

    public Long getIdDepartamentoBsq() {
        return idDepartamentoBsq;
    }

    public void setIdDepartamentoBsq(Long idDepartamentoBsq) {
        this.idDepartamentoBsq = idDepartamentoBsq;
    }

    public String getCodigoPostalBsq() {
        return codigoPostalBsq;
    }

    public void setCodigoPostalBsq(String codigoPostalBsq) {
        this.codigoPostalBsq = codigoPostalBsq;
    }

    public Long getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Long idProvincia) {
        this.idProvincia = idProvincia;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Municipio> lstMunicipiosAux = new LazyDataModel<Municipio>() {
            @Override
            public List<Municipio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Municipio> lstMunicipiosAux = municipioFacade.findAll(idProvinciaBsq, idDepartamentoBsq, nombreBsq, codigoPostalBsq, first, pageSize);
                return lstMunicipiosAux;
            }
        };
        lstMunicipiosAux.setRowCount(municipioFacade.countAll(idProvinciaBsq, idDepartamentoBsq, nombreBsq, codigoPostalBsq).intValue());
        return lstMunicipiosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setCodigoPostal(null);
        this.setCodigoPostal(null);
        this.setIdProvincia(null);
        this.setIdDepartamento(null);
        this.setMunicipio(null);
    }

    @Override
    public String crear() {
        try {
            this.municipioFacade.create(this.getIdDepartamento(), this.getCodigoPostal(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorMunicipio");
            this.setMsgSuccessError("El municipio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMunicipio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.municipioFacade.create(this.getIdDepartamento(), this.getCodigoPostal(), this.getNombre());
            this.setResultado("nuevoMunicipio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMunicipio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Municipio municipioAux = this.municipioFacade.find(idAux);
        this.setId(municipioAux.getId());
        this.setMunicipio(municipioAux);
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.municipioFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("municipioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMunicipio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Municipio municipioAux = this.municipioFacade.find(idAux);
        this.setId(municipioAux.getId());
        this.setIdDepartamento(municipioAux.getDepartamento().getId());
        this.setIdProvincia(municipioAux.getDepartamento().getProvincia().getId());
        this.setCodigoPostal(municipioAux.getCodigoPostal());
        this.setNombre(municipioAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.municipioFacade.edit(this.getId(), this.getCodigoPostal(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorMunicipio");
            this.setMsgSuccessError("El municipio ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorMunicipio");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Departamento> lstDepto = departamentoFacade.findAllByProvincia(this.getIdProvincia());
        if (!(lstDepto.isEmpty())) {
            lstDepto.stream().forEach((deptoAux) -> {
                selectItems.add(new SelectItem(deptoAux.getId(), deptoAux.getNombre()));
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

    public List<SelectItem> getSelectItemsDepartamentoBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Departamento> lstDepto = departamentoFacade.findAllByProvincia(this.getIdProvinciaBsq());
        if (!(lstDepto.isEmpty())) {
            lstDepto.stream().forEach((deptoAux) -> {
                selectItems.add(new SelectItem(deptoAux.getId(), deptoAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsProvinciaBsq() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<Provincia> lstProvincia = this.provinciaFacade.findAll(true);
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
        this.setNombreBsq(null);
        this.setIdDepartamentoBsq(0L);
        this.setIdProvinciaBsq(0L);
        this.setCodigoPostalBsq("");
        super.actualizar();
    }

}
