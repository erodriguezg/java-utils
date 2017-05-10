package cl.zeke.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eduardo on 20-02-17.
 */
public class DateUtils {

    public Date sumarDias(Date date, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public Date sumarSegundos(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public Date sumarMinutos(Date date, int minutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutos);
        return calendar.getTime();
    }

    public Date sumarHoras(Date date, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, horas);
        return calendar.getTime();
    }

    public int diferenciaEnDias(Date date1, Date date2) {
        long milliseconds1 = date1.getTime();
        long milliseconds2 = date2.getTime();
        long diff = milliseconds1 - milliseconds2;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return (int) diffDays;
    }

    public int diferenciaEnSegundos(Date date1, Date date2) {
        long milliseconds1 = date1.getTime();
        long milliseconds2 = date2.getTime();
        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        return (int) diffSeconds;
    }

    public int diferenciaEnMinutos(Date date1, Date date2) {
        long milliseconds1 = date1.getTime();
        long milliseconds2 = date2.getTime();
        long diff = milliseconds2 - milliseconds1;
        long diffMinutes = diff / (60 * 1000);
        return (int) diffMinutes;
    }

    public int diferenciaEnHoras(Date date1, Date date2) {
        long milliseconds1 = date1.getTime();
        long milliseconds2 = date2.getTime();
        long diff = milliseconds2 - milliseconds1;
        long diffHours = diff / (60 * 60 * 1000);
        return (int) diffHours;
    }

    public Date crearDate(int diaDelMes, int mes, int anio, int horaDelDia, int minuto, int segundo) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, anio);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.DAY_OF_MONTH, diaDelMes);
        calendar.set(Calendar.HOUR_OF_DAY, horaDelDia);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, segundo);
        return calendar.getTime();
    }

    public static final String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
