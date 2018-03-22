package com.br.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 解决多线程SimpleDateFormat线程安全问题
 * @author zuohongliang1
 *
 */
public class DateUtils {
	
	public static final String yyyy_MM="yyyy-MM";
	public static final String yyyyMM="yyyyMM";
	public static final String yyyyMMddHHmmss="yyyyMMddHHmmss";
	public static final String yyyyMMdd="yyyyMMdd";
	private static final long ONE_DAY_MILL_SECOND = 1000*60*60*24;
	
	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 * 
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					//System.out.println("put new sdf of pattern " + pattern + " to map");

					// 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							//System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	/**
	 * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return getSdf(pattern).format(date);
	}

	public static String format(Date date) {
		return getSdf("yyyy-MM-dd").format(date);
	}

	public static Date parse(String dateStr, String pattern) throws ParseException {
		return getSdf(pattern).parse(dateStr);
	}
	public static long getDate(long time) {
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	public static String getNowyyyy_MM_dd() {
		Date date = new Date(System.currentTimeMillis());
		return format(date, "yyyy-MM-dd");
	}

	public static Date getDate2(String str) {
        SimpleDateFormat FORMAT_DATE_2_PATTERN = new SimpleDateFormat(yyyyMMdd);
        try {
            return FORMAT_DATE_2_PATTERN.parse(str);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
	public static String formatForDate2(Date date) {
        SimpleDateFormat FORMAT_DATE_2_PATTERN = new SimpleDateFormat(yyyyMMdd);
        return FORMAT_DATE_2_PATTERN.format(date);
    }
	
	 /**
     * 进行日期计算求和
     * @param currentDay
     * @param addDay
     * @return
     */
    public static int addDay(int currentDay,int addDay){
        Date currentDate = getDate2(currentDay+"");
        String addDateStr = formatForDate2(new Date(currentDate.getTime()+ONE_DAY_MILL_SECOND*addDay));
        return NumberUtils.toInt(addDateStr);
    }
    
    
    /**
     * 计算两个日期之间天数差别
     * @param startTime
     * @param endTime
     * @return
     */
    public static int dayDiffer(String startTime, String endTime)throws Exception{
        Date startDate = getDate2(startTime);
        Date endDate = getDate2(endTime);
        if(startDate == null || endDate == null){
            throw new Exception("日期格式错误.");
        }
        long timeDiff = timeDiffer(startDate, endDate);
        long modValue = timeDiff%(ONE_DAY_MILL_SECOND);
        return (int)((timeDiff /ONE_DAY_MILL_SECOND) + (modValue == 0? 0 : 1));
    }
    
    /**
     * 时间差
     *
     * @param startTime
     * @param endTime
     * @return 时间差 毫秒
     */
    public static long timeDiffer(Date startTime, Date endTime) {
        long time = (endTime.getTime() - startTime.getTime());
        return time >= 0 ? time : -time;
    }
    
    
	public static void main(String[] args) {
		System.out.println(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
}
