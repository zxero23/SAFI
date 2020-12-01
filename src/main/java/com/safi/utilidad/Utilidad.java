package com.safi.utilidad;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 *
 * @author Alvarenga Angel,Gonzalez Facundo
 */
public class Utilidad {

    private static final String[] UNIDADES = {"", "uno ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private static final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
        "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
        "cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa "};
    private static final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
        "setecientos ", "ochocientos ", "novecientos "};

    public static String convertir(String numero, boolean mayusculas) {
        String literal;
        String parte_decimal;
        //si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        //si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = numero + ",00";
        }
        //se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            //se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");
            //de da formato al numero decimal
            parte_decimal = "con " + Num[1] + "/100 .-";
            //se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {//si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {//si es millon
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {//si es miles
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {//si es centena
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {//si es decena
                literal = getDecenas(Num[0]);
            } else {//sino unidades -> 9
                literal = getUnidades(Num[0]);
            }
            //devuelve el resultado en mayusculas o minusculas
            if (mayusculas) {
                return (literal + parte_decimal).toUpperCase();
            } else {
                return (literal + parte_decimal);
            }
        } else {//error, no se puede convertir
            return null;
        }
    }

    private static String getUnidades(String numero) {// 1 - 9
        //si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9       
        return UNIDADES[Integer.parseInt(numero.substring(numero.length() - 1))];
    }

    /**
     * @author Javier Escobar devuelve la mayor fecha en un nlistado de fechas
     */
    public static Date obtenerMaxDate(List<Date> fechas) {
        Date fechaRetorno = new Date();
        if (!fechas.isEmpty()) {
            fechaRetorno = fechas.get(0);
            for (Date fecha : fechas) {
                if (fecha.after(fechaRetorno)) {
                    fechaRetorno = fecha;
                }
            }
        }
        return fechaRetorno;
    }

    private static String getDecenas(String num) {// 99                        
        int n = Integer.parseInt(num);
        if (n < 10) {//para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {//para 20...99
            String u = getUnidades(num);
            if (u.equals("")) { //para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
        } else {//numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private static String getCentenas(String num) {// 999 o 099
        if (Integer.parseInt(num) > 99) {//es centena
            if (Integer.parseInt(num) == 100) {//caso especial
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {//por Ej. 099 
            //se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private static String getMiles(String numero) {// 999 999
        //obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        //obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n;
        //se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }
    }

    private static String getMillones(String numero) { //000 000 000        
        //se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        //se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n;
        if (millon.length() > 1) {
            n = getCentenas(millon) + "millones ";
        } else {
            n = getUnidades(millon) + "millon ";
        }
        return n + getMiles(miles);
    }

    public static String textoSinBlancos(String sTexto) {
        StringBuilder sCadenaSinBlancos = new StringBuilder("");
        StringTokenizer stTexto = new StringTokenizer(sTexto);
        while (stTexto.hasMoreElements()) {
            sCadenaSinBlancos.append(stTexto.nextElement());
        }
        return sCadenaSinBlancos.toString();
    }

    public static String dateString(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String resultado = sdf.format(fecha);
        return resultado;
    }

    /**
     * Convierte un Date a un formato String con año, mes, dia, hora, minutos,
     * segundos, milisegundos (Ej: 20200104125609478)
     *
     * @author Doroñuk Gustavo
     */
    public static String dateTimeString(Date fecha) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(fecha);
    }

    public static String obtenerAnio(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String resultado = sdf.format(date);
        return resultado;
    }

    public static Date sumarMeses(Date fecha, int meses) throws Exception {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, meses);
        return calendar.getTime();
    }

    /**
     * Suma X cantidad de días a una fecha.
     *
     * @author Doroñuk Gustavo
     * @param fecha Fecha a cual sumarle los días
     * @param dias Días a sumar
     * @return Fecha con los dias sumados.
     * @throws Exception retornando la misma fecha pasada por parámetro.
     */
    public static Date sumarDias(Date fecha, int dias) throws Exception {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(fecha);
            calendar.add(Calendar.DATE, dias);
            return calendar.getTime();
        } catch (Exception e) {
            System.out.println("Error al sumar dias Utilidades.sumarDias(fecha, dias): " + e.getMessage());
            return fecha;
        }
    }

    public static String desacentuar(String str) {
        String salida = str;
        if (str != null) {
            String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            salida = pattern.matcher(nfdNormalizedString).replaceAll("");
        }
        return salida;
    }

    public static Date quitarTiempo(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Encripta texto a MD5
     *
     * @author Doroñuk Gustavo
     * @param input Texto a encriptar
     * @return Texto encriptado a MD5
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica si dos Date tienen la misma fecha (sin tener en cuenta el
     * horario).
     *
     * @author Doroñuk Gustavo
     * @param fecha1
     * @param fecha2
     * @return true si tienen la misma fecha.
     */
    public static boolean isEqualFecha(Date fecha1, Date fecha2) {
        fecha1.setHours(0);
        fecha1.setMinutes(0);
        fecha1.setSeconds(0);
        fecha2.setHours(0);
        fecha2.setMinutes(0);
        fecha2.setSeconds(0);
        return fecha1.equals(fecha2);
    }

    public static String ceroIzquierda(Long codigo) {
        Formatter obj = new Formatter();
        return String.valueOf(obj.format("%02d", codigo));
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Verifica si una fecha se encuentra dentro de un rango de fechas.
     *
     * @author Gustavo Doroñuk
     * @param evaluar Fecha a evaluar si está dentro del rango.
     * @param inicio Fecha de inicio del rango de fechas.
     * @param fin Fecha de fin del rango de fechas.
     * @return
     */
    public static boolean isDentroRangoFecha(Date evaluar, Date inicio, Date fin) {
        return evaluar.after(inicio) && evaluar.before(fin);
    }

    //Distingue valores repetidos en un lista(lo uso en el .Filter del Stream)
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    public static String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom(); 
        Base64.Encoder base64Encoder = Base64.getUrlEncoder(); 
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
