package com.safi.managedBeans;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;

import com.safi.entity.Auditoria;
import com.safi.entity.Ejercicio;
import com.safi.entity.Servicio;
import com.safi.entity.Usuario;
import com.safi.enums.AccionEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.facade.UsuarioFacadeLocal;
import com.safi.utilidad.ReporteJava;
import com.safi.utilidad.SAFIReporteJava;
import com.safi.utilidad.Utilidad;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
//import net.sf.jasperreports.export.SimpleExporterInput;
//import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
//import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 *
 * @author Facundo Gonzalez
 */
@ManagedBean
@SessionScoped
public class AuditoriaManagedBean extends UtilManagedBean implements Serializable {

	private String nombreBsq;
	private String descripcion;
	private Date fecha;
	private Date fechaDesdeBuscar;
	private Date fechaHastaBuscar;
	private Usuario usuarioBsq;
	private String valorBsq;
	private List<SelectItem> usuariosDisponibles;
	private List<SelectItem> servicioDisponibles;
	private Long idUsuario;
	private Long idServicio;
	private Long idEjercicioBsq = 0L;
	private Long idServicioBsq = 0L;
	private Long idUsuarioBsq = 0L;
	private String accionBsq;
	private boolean lazy = true;
	private boolean mostrarReporte;
	private String query;

	@EJB
	private AuditoriaFacadeLocal auditoriaFacade;
	@EJB
	private UsuarioFacadeLocal usuarioFacade;
	@EJB
	private EjercicioFacadeLocal ejercicioFacade;
	@EJB
	private ServicioFacadeLocal servicioFacade;

	public AuditoriaManagedBean() {
	}

	@Override
	public void actualizar() {
		this.setNombreBsq("");
		this.setIdUsuario(null);
		this.setIdUsuarioBsq(null);
		this.setIdServicioBsq(null);
		this.setIdEjercicioBsq(null);
		this.setFechaDesdeBuscar(null);
		this.setFechaHastaBuscar(null);
		this.setFecha(null);
		this.setDescripcion("");
		this.setAccionBsq(null);
		this.setValorBsq(null);
		this.setIdServicio(null);
	}

	@Override
	public String crear() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public String crearOtro() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public String getAccionBsq() {
		return accionBsq;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public Date getFechaDesdeBuscar() {
		return fechaDesdeBuscar;
	}

	public Date getFechaHastaBuscar() {
		return fechaHastaBuscar;
	}

	public Resource getGenerarReportePDF() throws JRException, FileNotFoundException, IOException, Exception {
		SAFIReporteJava miRecurso;

		JasperPrint jasperResultado = new ReporteJava().exportAuditoria(this.getQuery());
		byte[] bites = JasperExportManager.exportReportToPdf(jasperResultado);
		miRecurso = new SAFIReporteJava(bites);
		return miRecurso;
	}

//	public Resource getGenerarReporteXLS() throws Exception {
//		JasperPrint jasperResultado = new ReporteJava().exportAuditoria(this.getQuery());
//		SAFIReporteJava miRecurso = null;
//		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
//		jasperResultado.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
//		jasperResultado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.keep.first.band.1", "pageHeader");
//		jasperResultado.setProperty("net.sf.jasperreports.export.xls.exclude.origin.band.2", "pageFooter");
//		JRXlsExporter exporter = new JRXlsExporter();
//		exporter.setExporterInput(new SimpleExporterInput(jasperResultado));
//		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
//		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
//		configuration.setOnePagePerSheet(false);
//		configuration.setMaxRowsPerSheet(60000);
//		configuration.setIgnorePageMargins(true);
//		configuration.setWhitePageBackground(true);
//		configuration.setRemoveEmptySpaceBetweenRows(true);
//		configuration.setDetectCellType(true);
//		configuration.setCollapseRowSpan(false);
//		exporter.setConfiguration(configuration);
//		exporter.exportReport();
//		byte[] bites = xlsReport.toByteArray();
//		miRecurso = new SAFIReporteJava(bites);
//		return miRecurso;
//	}

	public Long getIdEjercicioBsq() {
		return idEjercicioBsq;
	}

	public Long getIdServicio() {
		return idServicio;
	}

	public Long getIdServicioBsq() {
		return idServicioBsq;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public Long getIdUsuarioBsq() {
		return idUsuarioBsq;
	}

	@Override
	public LazyDataModel getListElements() {
		LazyDataModel<Auditoria> lstAuditoria = new LazyDataModel<Auditoria>() {
			@Override
			public List<Auditoria> load(int first, int pageSize, final SortCriteria[] criteria,
					final Map<String, String> filters) {
				List<Auditoria> lstAuditoriaAux = auditoriaFacade.findAll(idEjercicioBsq, idServicioBsq, idUsuarioBsq,
						accionBsq, valorBsq, fechaDesdeBuscar, fechaHastaBuscar, first, pageSize);

				return lstAuditoriaAux;
			}
		};
		lstAuditoria.setRowCount(auditoriaFacade.countAll(idEjercicioBsq, idServicioBsq, idUsuarioBsq, accionBsq,
				valorBsq, fechaDesdeBuscar, fechaHastaBuscar).intValue());
		return lstAuditoria;
	}

	public List<Auditoria> getListElementsWithoutLazy() {
		return auditoriaFacade.findAll(idEjercicioBsq, idServicioBsq, idUsuarioBsq, accionBsq, valorBsq,
				fechaDesdeBuscar, fechaHastaBuscar);
	}

	public String getNombreBsq() {
		return nombreBsq;
	}

	public String getQuery() {
		return query;
	}

	@Override
	public List<SelectItem> getSelectItems() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public List<SelectItem> getSelectItemsAcciones() {
		try {
			List<SelectItem> selectItems = new ArrayList<>();
			for (AccionEnum a : AccionEnum.values()) {
				selectItems.add(new SelectItem(a.getName(), a.getName()));
			}
			return selectItems;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	/**
	 * @author Gonzalez Facundo
	 * @return List SelectItem
	 */
	public List<SelectItem> getSelectItemsEjercicio() {
		try {
			List<SelectItem> selectItems = new ArrayList<>();
			List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(true);
			if (!(lstEjercicioAux.isEmpty())) {
				lstEjercicioAux.stream().forEach((ejercicioAux) -> {
					selectItems.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
				});
			}
			return selectItems;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public List<SelectItem> getSelectItemsServicio() {
		try {
			List<SelectItem> selectItems = new ArrayList<>();
			List<Servicio> lstServicioAux = this.servicioFacade.findAll(true);
			if (!(lstServicioAux.isEmpty())) {
				lstServicioAux.stream().sorted(
						(p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo()))))
						.forEach((servicioAux) -> {
							selectItems.add(new SelectItem(servicioAux.getId(),
									servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
						});
			}
			return selectItems;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public List<SelectItem> getServicioDisponibles() {
		List<SelectItem> selectItems = new ArrayList<>();
		List<Servicio> lstAuditoriaAux = this.auditoriaFacade.findServicios();
		if (!(lstAuditoriaAux.isEmpty())) {
			lstAuditoriaAux.stream().forEach((ServicioAux) -> {
				selectItems.add(new SelectItem(ServicioAux.getId(),
						ServicioAux.getCodigo() + " - " + ServicioAux.getAbreviatura()));
			});
		}
		return selectItems;
	}

	public Usuario getUsuarioBsq() {
		return this.getIdUsuario() == null ? usuarioBsq : this.usuarioFacade.find(this.getIdUsuario());
	}

	public List<SelectItem> getUsuariosDisponibles() {
		List<SelectItem> selectItems = new ArrayList<>();
		List<Usuario> lstUsuario = new ArrayList<>();
		if (idServicioBsq != null && !idServicioBsq.equals(0L)) {
			lstUsuario = this.usuarioFacade.findAll(true, this.getIdServicioBsq());
		} else {
			lstUsuario = this.usuarioFacade.findAll(true);
		}
		if (!(lstUsuario.isEmpty())) {
			lstUsuario.stream().sorted((u1, u2) -> (u1.getApellidoNombre().compareToIgnoreCase(u2.getApellidoNombre())))
					.forEach((usuarioAux) -> {
						selectItems.add(new SelectItem(usuarioAux.getId(),
								usuarioAux.getApellidoNombre() + " (" + usuarioAux.getNombreUsuario() + ")"));
					});
		}
		return selectItems;
	}

	public String getValorBsq() {
		return valorBsq;
	}

	@Override
	public void guardarBorrado() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public String guardarEdicion() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public boolean isLazy() {
		return lazy;
	}

	public boolean isMostrarReporte() {
		return mostrarReporte;
	}

	@Override
	public void limpiar() {
		this.setFechaDesdeBuscar(null);
		this.setFechaHastaBuscar(null);
		this.setFecha(null);
		this.setDescripcion(null);
		this.setIdUsuarioBsq(null);
		this.setIdServicioBsq(null);
		this.setIdEjercicioBsq(null);
		this.setAccionBsq(null);
		this.setValorBsq(null);
	}

	public void postExporter() {
		this.setLazy(true);
	}

	public void preExporter() {
		this.setLazy(false);
	}

	@Override
	public void prepararParaEditar() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public void prepararReportes() throws Exception {
		try {
			if (auditoriaFacade.countAll(idEjercicioBsq, idServicioBsq, idUsuarioBsq, accionBsq, valorBsq,
					fechaDesdeBuscar, fechaHastaBuscar).intValue() > 50000) {
				throw new Exception("El número de resultados excede el máximo permitido.");
			}
			StringBuilder query = new StringBuilder();
			query.append(" ");
			if (fechaDesdeBuscar != null) {
				Timestamp fDesde = new Timestamp(fechaDesdeBuscar.getTime());
				query.append("  and auditoria.fecha >= '").append(fDesde).append("' ");
			}
			if (fechaHastaBuscar != null) {
				Timestamp fHasta = new Timestamp(fechaHastaBuscar.getTime());
				fHasta.setHours(23);
				fHasta.setMinutes(59);
				fHasta.setSeconds(59);
				query.append("  and auditoria.fecha <= '").append(fHasta).append("' ");
			}
			if (accionBsq != null && !accionBsq.isEmpty()) {
				query.append("  and upper(unaccent(auditoria.accion)) like '%")
						.append(Utilidad.desacentuar(accionBsq).toUpperCase()).append("%' ");
			}
			if (idEjercicioBsq != null && idEjercicioBsq != 0L) {
				query.append("   and auditoria.ejercicio_id = ").append(idEjercicioBsq);
			}
			if (idServicioBsq != null && idServicioBsq != 0L) {
				query.append("   and auditoria.servicio_id = ").append(idServicioBsq);
			}
			if (idUsuarioBsq != null && idUsuarioBsq != 0L) {
				query.append("   and auditoria.usuario_id = ").append(idUsuarioBsq);
			}
			if (valorBsq != null && !valorBsq.isEmpty()) {
				query.append(" and (");
				query.append(" (upper(unaccent(auditoria.valoranterior)) like '%")
						.append(Utilidad.desacentuar(valorBsq).toUpperCase()).append("%') ");
				query.append(" or");
				query.append(" (upper(unaccent(auditoria.valoractual)) like '%")
						.append(Utilidad.desacentuar(valorBsq).toUpperCase()).append("%') ");
				query.append(")");
			}
			query.append(" ORDER BY auditoria.fecha DESC");
			this.setMostrarReporte(true);
			this.setQuery(query.toString());
			this.setTitle("Proceso completo...");
			this.setImages("fa fa-check-circle-o");
			this.setMsgSuccessError("El listado de auditorías ha sido generado con éxito.");
			this.setEsCorrecto(true);
		} catch (Exception ex) {
			this.setEsCorrecto(false);
			this.setMostrarReporte(false);
			this.setTitle("¡Error!");
			this.setImages("fa fa-times-circle-o");
			this.setMsgSuccessError(ex.getMessage());
			this.setResultado("successErrorMovimiento");
		}
	}

	public void setAccionBsq(String accionBsq) {
		this.accionBsq = accionBsq;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setFechaDesdeBuscar(Date fechaDesdeBuscar) {
		this.fechaDesdeBuscar = fechaDesdeBuscar;
	}

	public void setFechaHastaBuscar(Date fechaHastaBuscar) {
		this.fechaHastaBuscar = fechaHastaBuscar;
	}

	public void setIdEjercicioBsq(Long idEjercicioBsq) {
		this.idEjercicioBsq = idEjercicioBsq;
	}

	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}

	public void setIdServicioBsq(Long idServicioBsq) {
		this.idServicioBsq = idServicioBsq;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setIdUsuarioBsq(Long idUsuarioBsq) {
		this.idUsuarioBsq = idUsuarioBsq;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	public void setMostrarReporte(boolean mostrarReporte) {
		this.mostrarReporte = mostrarReporte;
	}

	public void setNombreBsq(String nombreBsq) {
		this.nombreBsq = nombreBsq;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setServicioDisponibles(List<SelectItem> servicioDisponibles) {
		this.servicioDisponibles = servicioDisponibles;
	}

	public void setUsuarioBsq(Usuario usuarioBsq) {
		this.usuarioBsq = usuarioBsq;
	}

	public void setUsuariosDisponibles(List<SelectItem> usuariosDisponibles) {
		this.usuariosDisponibles = usuariosDisponibles;
	}

	public void setValorBsq(String valorBsq) {
		this.valorBsq = valorBsq;
	}

	@Override
	public void verDetalle() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

}
