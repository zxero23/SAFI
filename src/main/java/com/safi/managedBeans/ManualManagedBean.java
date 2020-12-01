package com.safi.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.util.JavaScriptRunner;
import com.safi.enums.ManualEnum;

/**
 * Controlador de vista para el manual de usuario.
 *
 * @author Doroñuk Gustavo
 */
@ManagedBean
@RequestScoped
public class ManualManagedBean extends UtilManagedBean implements Serializable {

    private boolean verManualFondosValoresOperador;
    private boolean verManualFondosValoresAdministrador;
    private boolean verManualProveedores;
    private boolean verManualFondosValoresReportes;
    private boolean verManualFondosValoresPresupuesto;
    private int dropDown;
    private String linkDescarga;
    private String nombreDescarga;

    public ManualManagedBean() {
        this.verManualFondosValoresOperador = false;
        this.verManualFondosValoresAdministrador = false;
        this.verManualFondosValoresReportes = false;
        this.verManualFondosValoresPresupuesto = false;
        this.verManualProveedores = false;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("detalleManualProveedores")) {
                            this.setVerManualProveedores(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("detalleManualFondosValoresAdministrador")) {
                            this.setVerManualFondosValoresAdministrador(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("detalleManualFondosValoresReportes")) {
                            this.setVerManualFondosValoresReportes(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("detalleManualFondosValoresPresupuesto")) {
                            this.setVerManualFondosValoresPresupuesto(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleManualFondosValoresOperador"))).forEach((_item) -> {
                        this.setVerManualFondosValoresOperador(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public boolean isVerManualFondosValoresOperador() {
        return verManualFondosValoresOperador;
    }

    public void setVerManualFondosValoresOperador(boolean verManualFondosValoresOperador) {
        this.verManualFondosValoresOperador = verManualFondosValoresOperador;
    }

    public boolean isVerManualFondosValoresAdministrador() {
        return verManualFondosValoresAdministrador;
    }

    public void setVerManualFondosValoresAdministrador(boolean verManualFondosValoresAdministrador) {
        this.verManualFondosValoresAdministrador = verManualFondosValoresAdministrador;
    }

    public boolean isVerManualFondosValoresReportes() {
        return verManualFondosValoresReportes;
    }

    public void setVerManualFondosValoresReportes(boolean verManualFondosValoresReportes) {
        this.verManualFondosValoresReportes = verManualFondosValoresReportes;
    }

    public boolean isVerManualProveedores() {
        return verManualProveedores;
    }

    public void setVerManualProveedores(boolean verManualProveedores) {
        this.verManualProveedores = verManualProveedores;
    }

    public boolean isVerManualFondosValoresPresupuesto() {
        return verManualFondosValoresPresupuesto;
    }

    public void setVerManualFondosValoresPresupuesto(boolean verManualFondosValoresPresupuesto) {
        this.verManualFondosValoresPresupuesto = verManualFondosValoresPresupuesto;
    }

    public int getDropDown() {
        return dropDown;
    }

    public void setDropDown(int dropDown) {
        this.dropDown = dropDown;
    }

    public String getLinkDescarga() {
        return linkDescarga;
    }

    public void setLinkDescarga(String linkDescarga) {
        this.linkDescarga = linkDescarga;
    }

    public String getNombreDescarga() {
        return nombreDescarga;
    }

    public void setNombreDescarga(String nombreDescarga) {
        this.nombreDescarga = nombreDescarga;
    }

    public void clickBoton() {
        for (ManualEnum manual : ManualEnum.values()) {
            if (this.getDropDown() == manual.getId()) {
                this.setLinkDescarga(manual.getPath());
                this.setNombreDescarga(manual.getFileName());
            }
        }
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "download()");
    }

    @Override
    public LazyDataModel<?> getListElements() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> items = new ArrayList<>();
        if (verManualProveedores) {
            items.add(new SelectItem(ManualEnum.MANUAL_PROVEEDORES.getId(), ManualEnum.MANUAL_PROVEEDORES.getName()));
        }
        if (verManualFondosValoresAdministrador) {
            items.add(new SelectItem(ManualEnum.MANUAL_FONDOS_VALORES_ADMINISTRADOR.getId(), ManualEnum.MANUAL_FONDOS_VALORES_ADMINISTRADOR.getName()));
        } else if (verManualFondosValoresOperador) {
            items.add(new SelectItem(ManualEnum.MANUAL_FONDOS_VALORES_OPERADOR.getId(), ManualEnum.MANUAL_FONDOS_VALORES_OPERADOR.getName()));
        }
        if (verManualFondosValoresReportes) {
            items.add(new SelectItem(ManualEnum.MANUAL_FONDOS_VALORES_REPORTES.getId(), ManualEnum.MANUAL_FONDOS_VALORES_REPORTES.getName()));
        }
        if (verManualFondosValoresPresupuesto) {
            items.add(new SelectItem(ManualEnum.MANUAL_FONDOS_VALORES_PRESUPUESTO.getId(), ManualEnum.MANUAL_FONDOS_VALORES_PRESUPUESTO.getName()));
        }
        return items.stream()
                .sorted((s1, s2) -> (s1.getLabel().compareToIgnoreCase(s2.getLabel())))
                .collect(Collectors.toList());
    }

    @Override
    public void limpiar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String crear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verDetalle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void guardarBorrado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
