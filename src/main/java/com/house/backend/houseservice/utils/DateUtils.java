package com.house.backend.houseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhengshuqin
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final Object lockObj = new Object();
    private static final ConcurrentMap<String, ThreadLocal<SimpleDateFormat>> FORMATTER_CACHE = new ConcurrentHashMap();
    private static final int PATTERN_CACHE_SIZE = 500;
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_CH = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_YYYY_MM_CH = "yyyy年MM月";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_TIMEONLY_STRING = "HHmmss";
    public static final String DATE_FORMAT_DATESTR = "yyyy/MM/dd";
    public static final String DATE_FORMAT_MM_DD = "MM-dd";
    public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String DATE_FORMAT_HH_MM = "HH:mm";
   public static  final String DATE_FORMAT_yyyyMMdd_HHmmssSSSZ = "yyyy-MM-dd HH:mm:ss.SSSZ";


    private DateUtils() {
    }

    public static Date parseDate(String str, String parsePatterns) {
        try {
            SimpleDateFormat formatter = createCacheFormatter(parsePatterns);
            return formatter.parse(str);
        } catch (ParseException var3) {
            logger.error("parseDate is error:::" + var3.getMessage());
            return null;
        }
    }

    public static Calendar parseCalendar(String str, String parsePatterns) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(str, parsePatterns));
        return calendar;
    }

    public static Calendar parseCalendar(String str) {
        return parseCalendar(str, "yyyyMMdd");
    }

    public static int getWeekIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(7);
    }


    public static String formatDate(Date date, String format) {
        try {
            SimpleDateFormat dateFormat = createCacheFormatter(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            logger.error("formatDate is error:::" + e.getMessage());
            return "";
        }
    }

    public static String formatDate(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return date;
        } else {
            SimpleDateFormat dateFormat = createCacheFormatter(format);

            try {
                if (format.length() != 10 && format.length() != 11) {
                    return format.length() == 8 ? dateFormat.format(parseDate(date, "HHmmss")) : dateFormat.format(parseDate(date, "yyyyMMddHHmmss"));
                } else {
                    return dateFormat.format(parseDate(date, "yyyyMMdd"));
                }
            } catch (Exception var4) {
                logger.error("formatDate is error:::" + var4.getMessage(), var4);
                return date;
            }
        }
    }

    public static String getCurrDate() {
        String currDate= DateUtils.getCurrentOsTime().substring(0, 8);
        return DateUtils.formatDateTime(currDate, DateUtils.DATE_FORMAT_YYYY_MM_DD);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat dateFormat = createCacheFormatter("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(date);
        }
    }

    public static String formatDateTime(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return date;
        } else {
            SimpleDateFormat dateFormat = createCacheFormatter(format);

            try {
                return dateFormat.format(parseDate(date, "yyyyMMddHHmmss"));
            } catch (Exception var4) {
                logger.error("formatDate is error:::" + var4.getMessage());
                return date;
            }
        }
    }

    public static Date getDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, days);
        return calendar.getTime();
    }

    public static Date getDateAddMinute(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(12, minutes);
        return calendar.getTime();
    }

    public static Date getDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, days);
        return calendar.getTime();
    }

    public static Date dateFormat(Date date) {
        SimpleDateFormat sdf = createCacheFormatter("yyyy-MM-dd");
        Date value = new Date();

        try {
            value = sdf.parse(sdf.format(date));
        } catch (ParseException var4) {
            logger.error("dateFormat error", var4);
        }

        return value;
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, day);
        return calendar.getTime();
    }

    public static String addDay(String date, int day) {
        String newDate = formatDate(addDay(parseDate(date, "yyyyMMdd"), day), "yyyyMMdd");
        return newDate;
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, month);
        return calendar.getTime();
    }

    public static String addMonth(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, "yyyyMMdd"));
        calendar.add(2, month);
        return formatDate(calendar.getTime(), "yyyyMMdd");
    }

    public static Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(1, year);
        return calendar.getTime();
    }

    public static long getMilliSecondsLeftToday() {
        return 0L;
    }

    public static String getAcceptTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static boolean compareTime(String startDate, String endDate) {
        try {
            Date $startDate = new Date();
            if (!StringUtils.isEmpty(startDate)) {
                $startDate = parseDate(startDate, "yyyyMMdd");
            }

            Date $endDate = parseDate(endDate, "yyyyMMdd");
            long startTimeLong=0L;
            long endTimeLong=0L;

            if($startDate!=null){
                 startTimeLong = $startDate.getTime();
            }
            if($endDate!=null){
                endTimeLong = $endDate.getTime();
            }
            if (startTimeLong > endTimeLong) {
                return true;
            }
        } catch (Exception var8) {
            logger.error("compareTime is error:::" + var8.getMessage());
        }

        return false;
    }

    public static boolean compareTimeMoreOrEqual(String startDate, String endDate) {
        try {
            Date $startDate = new Date();
            if (!StringUtils.isEmpty(startDate)) {
                $startDate = parseDate(startDate, "yyyyMMdd");
            }

            Date $endDate = parseDate(endDate, "yyyyMMdd");
            long startTimeLong =0L;
            long endTimeLong =0L;
            if($startDate!=null){
                startTimeLong= $startDate.getTime();
            }
            if($endDate!=null){
                endTimeLong= $endDate.getTime();
            }
            if (startTimeLong >= endTimeLong) {
                return true;
            }
        } catch (Exception var8) {
            logger.error("compareTimeMoreOrEqual is error:::" + var8.getMessage());
        }

        return false;
    }

    public static boolean compareTime1(String startDate, String endDate) {
        try {
            Date $startDate = new Date();
            if (!StringUtils.isEmpty(startDate)) {
                $startDate = parseDate(startDate, "yyyyMMddHHmmss");
            }
            Date $endDate = parseDate(endDate, "yyyyMMddHHmmss");
            long startTimeLong=0L;
            long endTimeLong=0L;

            if($startDate!=null){
                startTimeLong= $startDate.getTime();
            }
            if($endDate!=null){
                endTimeLong= $endDate.getTime();
            }
            if (startTimeLong > endTimeLong) {
                return true;
            }
        } catch (Exception var8) {
            logger.error("compareTime1 is error:::" + var8.getMessage());
        }

        return false;
    }

    public static String changeStyle(String pDate, String pOrgStyle, String pDateStyle) {
        String strDate = "";

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createCacheFormatter(pOrgStyle).parse(pDate));
            strDate = createCacheFormatter(pDateStyle).format(calendar.getTime());
        } catch (Exception var5) {
            strDate = pDate;
        }

        return strDate;
    }

    public static long getMinusDay(String dateMinuend, String dateMeiosis, String dateFormat) throws Exception {
        long day = 0L;
        if (dateMinuend.length() == dateMeiosis.length() && dateMinuend.length() == dateFormat.length()) {
            try {
                SimpleDateFormat formatter = createCacheFormatter(dateFormat);
                Date date1 = formatter.parse(dateMinuend);
                Date date2 = formatter.parse(dateMeiosis);
                day = (date1.getTime() - date2.getTime()) / 86400000L;
                return day;
            } catch (Exception var8) {
                logger.error("dateMinuend【" + dateMinuend + "】;dateMeiosis【" + dateMeiosis + "】;dateFormat【" + dateFormat + "】");
                throw new Exception("日期相减出错");
            }
        } else {
            logger.error("dateMinuend【" + dateMinuend + "】;dateMeiosis【" + dateMeiosis + "】;dateFormat【" + dateFormat + "】");
            throw new Exception("日期格式不正确");
        }
    }


    public static long getMinusSecond(String dateMinuend, String dateMeiosis, String dateFormat) throws Exception {
        long second = 0;
        if (dateMinuend.length() == dateMeiosis.length() && dateMinuend.length() == dateFormat.length()) {
            try {
                SimpleDateFormat formatter = createCacheFormatter(dateFormat);
                Date date1 = formatter.parse(dateMinuend);
                Date date2 = formatter.parse(dateMeiosis);
                second = (date1.getTime() - date2.getTime()) / 1000;
                return second;
            } catch (Exception var8) {
                logger.error("dateMinuend【" + dateMinuend + "】;dateMeiosis【" + dateMeiosis + "】;dateFormat【" + dateFormat + "】");
                throw new Exception("日期相减出错");
            }
        } else {
            logger.error("dateMinuend【" + dateMinuend + "】;dateMeiosis【" + dateMeiosis + "】;dateFormat【" + dateFormat + "】");
            throw new Exception("日期格式不正确");
        }
    }

    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int nSeason = getSeason(date);
        if (nSeason == 1) {
            c.set(2, 0);
            season[0] = c.getTime();
            c.set(2, 1);
            season[1] = c.getTime();
            c.set(2, 2);
            season[2] = c.getTime();
        } else if (nSeason == 2) {
            c.set(2, 3);
            season[0] = c.getTime();
            c.set(2, 4);
            season[1] = c.getTime();
            c.set(2, 5);
            season[2] = c.getTime();
        } else if (nSeason == 3) {
            c.set(2, 6);
            season[0] = c.getTime();
            c.set(2, 7);
            season[1] = c.getTime();
            c.set(2, 8);
            season[2] = c.getTime();
        } else if (nSeason == 4) {
            c.set(2, 9);
            season[0] = c.getTime();
            c.set(2, 10);
            season[1] = c.getTime();
            c.set(2, 11);
            season[2] = c.getTime();
        }

        return season;
    }

    public static int getSeason(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(2);
        switch (month) {
            case 0:
            case 1:
            case 2:
                season = 1;
                break;
            case 3:
            case 4:
            case 5:
                season = 2;
                break;
            case 6:
            case 7:
            case 8:
                season = 3;
                break;
            case 9:
            case 10:
            case 11:
                season = 4;
                break;
            default:
                season = 0;
                break;
        }

        return season;
    }

    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }

    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, c.getActualMinimum(5));
        return c.getTime();
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(5, c.getActualMaximum(5));
        return c.getTime();
    }

    public static String changeStyleDesc(String pDate, String pDateStyle) {
        String strDate = "";

        try {
            String pOrgStyle = "yyyyMMdd";
            if (pDate.length() == 14) {
                pOrgStyle = "yyyyMMddHHmmss";
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createCacheFormatter(pOrgStyle).parse(pDate));
            strDate = createCacheFormatter(pDateStyle).format(calendar.getTime());
        } catch (Exception var5) {
            strDate = pDate;
        }

        return strDate;
    }

    public static String nDaysAfterOneDateString(String dateStr, String dateFormatStr, int n) {
        SimpleDateFormat simpleDateFormat = createCacheFormatter(dateFormatStr);
        Date date = null;

        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (Exception var7) {
            logger.error("出错详情：", var7);
        }
        long nDay=0L;
        if(date!=null){
             nDay = (date.getTime() / 86400000L + 1L + (long) n) * 86400000L;
            date.setTime(nDay);
            return simpleDateFormat.format(date);
        }

     return  null;
    }

    public static String getNowDate(String pDateStyle) {
        return createCacheFormatter(pDateStyle).format(new Date());
    }

    public static String getCurrentOsTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getFormatedOsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getYear(String sourceDate) {
        Calendar calendar = parseCalendar(sourceDate, "yyyyMMdd");
        return Integer.toString(calendar.get(1));
    }

    public static String getMonth(String sourceDate) {
        Calendar calendar = parseCalendar(sourceDate, "yyyyMMdd");
        return Integer.toString(calendar.get(2) + 1);
    }

    public static String getInstanceDate(String startDate, int instance) {
        Calendar calendar = parseCalendar(startDate, "yyyyMMdd");
        return formatDate(getDate(calendar.getTime(), instance), "yyyyMMdd");
    }

    private static SimpleDateFormat createCacheFormatter(final String pattern) {
        if (pattern != null && pattern.length() != 0) {
            ThreadLocal<SimpleDateFormat> formatter = FORMATTER_CACHE.get(pattern);
            if (formatter == null) {
                synchronized (lockObj) {
                    formatter = FORMATTER_CACHE.get(pattern);
                    if (formatter == null) {
                        logger.info("put new sdf of pattern " + pattern + " to map");
                        formatter = ThreadLocal.withInitial(() -> {
                            DateUtils.logger.info("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        });
                        if (FORMATTER_CACHE.size() < PATTERN_CACHE_SIZE) {
                            FORMATTER_CACHE.put(pattern, formatter);
                        }
                    }
                }
            }

            return formatter.get();
        } else {
            throw new IllegalArgumentException("Invalid pattern specification");
        }
    }


    public static Timestamp getTimestamp(String str) throws Exception {
        String formatString = DateUtils.DATE_FORMAT_YYYY_MM_DD;
        if (str.length()>10){
            formatString = DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;
        }
        DateFormat format = new SimpleDateFormat(formatString);
        format.setLenient(false);
        Timestamp ts = null;
        try {
            ts = new Timestamp(format.parse(str).getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("(str:"+str+")getTimestamp error: " + e.toString());
            throw e;
        }
        return ts;
    }

    public static java.sql.Date stringToSqlDate(String dateStr) {
        if (dateStr==null || dateStr.length()<10)	{
            return null;
        }
        if (dateStr.length()>10){
            dateStr = dateStr.substring(0, 10);
        }
        return java.sql.Date.valueOf(dateStr);
    }

    /**
     * Get the Dates between Start Date and End Date.
     *
     * @param p_start
     *            Start Date
     * @param p_end
     *            End Date
     * @return Dates List
     */
    public static List<Date> getDates(Date p_start, Date p_end) {
        List<Date> result = new ArrayList<Date>();
        result.add(p_start);
        Calendar temp = Calendar.getInstance();
        temp.setTime(p_start);
        Calendar end = Calendar.getInstance();
        end.setTime(p_end);
        temp.add(Calendar.DAY_OF_YEAR, 1);
        while (temp.before(end)) {
            result.add(temp.getTime());
            temp.add(Calendar.DAY_OF_YEAR, 1);
        }
        result.add(p_end);
        return result;
    }

    public static String dateToString(Date date, String formatStr) {
        SimpleDateFormat formatDate = new SimpleDateFormat(formatStr);
        String str = formatDate.format(date);
        return str;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatDate.format(date);
        return str;
    }

    public static String unixToDate(Long unixTime, String parsePatterns){
        String res = new SimpleDateFormat(parsePatterns).format(new Date(unixTime));
        return res;
    }

    public static String getDayOfWeek(){
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(dayOfWeek <0){
            dayOfWeek=0;
        }
        return dayNames[dayOfWeek] ;
    }

}
