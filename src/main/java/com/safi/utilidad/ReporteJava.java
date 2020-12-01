package com.safi.utilidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author matias
 * @author Diana
 * @author Monchy
 */
public class ReporteJava {

    private JasperPrint masterPrint = null;
    private static java.net.URL url = null;
    private static JasperReport masterReport = null;
    private static final Map<String, Object> parametros = new HashMap();
    private static Connection conexion;

    public ReporteJava() {

    }

    public JasperPrint reporteDenegacion(Long idHistorialProveedor) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/denegatoria.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteRequisito(Long idTipoPersona, Long idTipoTramite, String token) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/requisitos.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("persona", idTipoPersona);
            parametros.put("tramite", idTipoTramite);
            parametros.put("token", token);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * Acuse de la inscripcion
     *
     * @param idHistorialProveedor
     * @return
     * @throws Exception
     * @throws JRException
     */
    public JasperPrint reporteAcuse(Long idHistorialProveedor) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/acuse.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteFormPreCompletado(Long idHistorialProveedor) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/formulario.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * Crea el reporte de inicio de trámite
     *
     * @author Doroñuk Gustavo
     * @param idHistorialProveedor
     * @return JasperPrint
     * @throws Exception
     * @throws JRException
     */
    public JasperPrint reporteInicioTramite(Long idHistorialProveedor) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/constanciaInicioTramite.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * Crea el reporte de inicio de trámite sin los 30 días
     *
     * @author Doroñuk Gustavo
     * @param idHistorialProveedor
     * @return JasperPrint
     * @throws Exception
     * @throws JRException
     */
    public JasperPrint reporteInicioTramiteSin30(Long idHistorialProveedor) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/constanciaInicioTramiteSin30.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * Crea el reporte de constancia definitiva
     *
     * @author Doroñuk Gustavo
     * @param idHistorialProveedor
     * @return JasperPrint
     * @throws Exception
     * @throws JRException
     */
    public JasperPrint reporteConstancia(Long idHistorialProveedor) throws Exception, JRException {
        try {
            /*Inicio carga del archivo properties*/
            Properties prop = new Properties();
            File propsFile = new File("/home/config.properties");
            try (InputStream is = new FileInputStream(propsFile)) {
                if (is != null) { //Evalua que el archivo exista
                    prop.load(is);
                } else {
                    throw new FileNotFoundException("Archivo properties '" + propsFile + "' no encontrado.");
                }
                is.close();
            }
            /**/
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/constanciaInscripcion.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            parametros.put("urlVerificacion", prop.getProperty("urlVerificacion"));
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteConstanciaSinFechaAlta(Long idHistorialProveedor) throws Exception, JRException {
        try {
            /*Inicio carga del archivo properties*/
            Properties prop = new Properties();
            File propsFile = new File("/home/config.properties");
            try (InputStream is = new FileInputStream(propsFile)) {
                if (is != null) { //Evalua que el archivo exista
                    prop.load(is);
                } else {
                    throw new FileNotFoundException("Archivo properties '" + propsFile + "' no encontrado.");
                }
                is.close();
            }
            /**/
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/constanciaInscripcionSinFechaAlta.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            parametros.put("urlVerificacion", prop.getProperty("urlVerificacion"));
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * @author Diana Olivera Descripcion: para la ejecucion de recursos.
     * @param reporte nombre del reporte
     * @param idEjercicio
     * @param idServicio
     * @param idOrganismo
     * @param fechaInicioAnio
     * @param fechaDesde
     * @param fechaHasta
     * @return
     * @throws java.lang.Exception
     * @throws net.sf.jasperreports.engine.JRException
     *
     */
    public JasperPrint reporteEjecucionRecursosPeriodo(String reporte, Long idEjercicio, Long idServicio, Long idOrganismo, Date fechaInicioAnio, Date fechaDesde, Date fechaHasta) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idServicio", idServicio);
            parametros.put("idOrganismo", idOrganismo);
            parametros.put("fechaInicioAnio", fechaInicioAnio);
            parametros.put("fechaDesde", fechaDesde);
            parametros.put("fechaHasta", fechaHasta);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteEjecucionRecursosDiario(String reporte, Long idEjercicio, Long idServicio, Long idOrganismo, Date fecha) throws Exception, JRException {

        try {

            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");

            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idServicio", idServicio);
            parametros.put("idOrganismo", idOrganismo);
            parametros.put("fecha", fecha);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (Exception e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            //throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * @author Diana Olivera Descripcion: para mostrar el estado de los recursos
     * luego de sus modificaciones
     * @param reporte nombre del reporte
     * @param idEjercicio
     * @param idServicio
     * @param fechaDesde
     * @param fechaHasta
     * @return
     * @throws java.lang.Exception
     * @throws net.sf.jasperreports.engine.JRException
     *
     */
    public JasperPrint reporteMovimientosRecursos(String reporte, Long idEjercicio, Long idServicio, Date fechaDesde, Date fechaHasta) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idServicio", idServicio);
            parametros.put("fechaDesde", fechaDesde);
            parametros.put("fechaHasta", fechaHasta);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * @author Diana Olivera
     * @param reporte nombre del reporte
     * @param idServicio
     * @param idEjercicio
     * @param idCuentaBancaria
     * @param fechaDesde
     * @param fechaHasta
     * @return
     * @throws java.lang.Exception
     * @throws net.sf.jasperreports.engine.JRException
     */
    public JasperPrint reporteBase(String reporte, Long idServicio, Long idEjercicio, Long idCuentaBancaria, Date fechaDesde, Date fechaHasta) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idServicio", idServicio);
            parametros.put("idCuenta", idCuentaBancaria);
            parametros.put("fechaDesde", fechaDesde);
            parametros.put("fechaHasta", fechaHasta);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteModificacionRecursos(String reporte, Long idServicio, Long idEjercicio, Date fechaDesde, Date fechaHasta, Long idOrganismo) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idServicio", idServicio);
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idOrganismo", idOrganismo);
            parametros.put("fechaDesde", fechaDesde);
            parametros.put("fechaHasta", fechaHasta);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteFecha(String reporte, Long idServicio, Long idEjercicio, Long idCuentaBancaria, Date fecha) throws Exception, JRException {
        try {

            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();            
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idServicio", idServicio);
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idCuenta", idCuentaBancaria);
            parametros.put("fecha", fecha);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());

            masterReport = (JasperReport) JRLoader.loadObject(url);

            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);

        } catch (Exception e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());

        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteLibroBanco(String reporte, Long idServicio, Long idEjercicio, Long idCuentaBancaria, Date fechaDesde, Date fechaHasta, Long nroPedidoFondo) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            System.out.println(dataSource.getConnection().getSchema());
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/" + reporte + ".jasper");
            parametros.put("idServicio", idServicio);
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("idCuenta", idCuentaBancaria);
            parametros.put("fechaDesde", fechaDesde);
            parametros.put("fechaHasta", fechaHasta);
            parametros.put("nroPedidoFondo", nroPedidoFondo);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint reporteExpedienteMovimientos(Long codigo, Long correlativo, Long anio, Long idEjercicio, Long numeroCuenta, Boolean verSituacionFinanciera) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/expedienteMovimientos.jasper");
            parametros.put("codigo", codigo);
            parametros.put("correlativo", correlativo);
            parametros.put("anio", anio);
            parametros.put("idEjercicio", idEjercicio);
            parametros.put("numeroCuenta", numeroCuenta);
            parametros.put("verSituacionFinanciera", verSituacionFinanciera);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    /**
     * Crea el reporte del trámite de modificación
     *
     * @author Matias Zakowicz
     * @param idHistorialProveedor
     * @return JasperPrint
     * @throws Exception
     * @throws JRException
     */
    public JasperPrint reporteModificacion(Long idHistorialProveedor, String texto) throws Exception, JRException {
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/constanciaModificacion.jasper");
            parametros.put("logo", getClass().getResource("logoCG.jpg").getPath());
            parametros.put("historial", idHistorialProveedor);
            parametros.put("texto", texto);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint exportMovimientos(String query) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/exportMovimientos.jasper");
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            parametros.put("query", query);
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint exportAuditoria(String query) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/exportAuditoria.jasper");
            parametros.put("query", query);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint exportAuditoriaProveedor(String query) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/exportAuditoriaProveedor.jasper");
            parametros.put("query", query);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }

    public JasperPrint exportProveedores(String query) throws Exception, JRException {

        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("jdbc/safi");
            conexion = dataSource.getConnection();
            url = this.getClass().getResource("/org/safi/reporte/exportProveedores.jasper");
            parametros.put("query", query);
            parametros.put("escudo", getClass().getResource("/org/safi/reporte/escudo.jpg").getPath());
            masterReport = (JasperReport) JRLoader.loadObject(url);
            masterPrint = JasperFillManager.fillReport(masterReport, parametros, conexion);
        } catch (NamingException | SQLException | JRException e) {
            System.err.println(e.getMessage() + " - " + e.getCause().toString());
            throw new Exception(e.getMessage());
        }
        conexion.close();
        return masterPrint;
    }
}
