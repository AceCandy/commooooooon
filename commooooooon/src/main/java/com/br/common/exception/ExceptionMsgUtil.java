package com.br.common.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.bsf.ext.app.util.BrBSFConsumerBean;
import com.br.common.bean.ResponseCode;
import com.br.common.util.DateUtils;
import com.br.common.util.JsonProcessUtil;
import com.br.common.util.JsonUtils;
import com.br.common.util.StringUtils;
import com.br.ice.service.alarm.BrSendAlarmNewServicePrx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExceptionMsgUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionMsgUtil.class);

	public static String getExceptionMsg(Exception ex) {
		String str = ex.getMessage();
		try {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw, true));
			str = sw.toString();
			sw.close();
			// str = str + "\n 发生时间 :" + DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将异常堆栈转换为字符串
	 * 
	 * @param aThrowable
	 *            异常
	 * @return String
	 */
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
	/**
	 * 
	 * @param exMap
	 * @param ex
	 * @param mailTitle 邮件标题，默认加了api4 标识
	 * @param alarmType 控制输出格式// 1表示发送邮件和短信,2表示邮件，3表示短信
	 * @param servicePort 此服务端口
	 * @return
	 */
	public static ObjectNode getMap(ObjectNode exMap, Exception ex, String mailTitle, int alarmType, int servicePort) {
		return getMap(exMap, ex, mailTitle, alarmType, null, servicePort, "");
	}
	private static ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
	/**
	 * 
	 * @param exMap
	 * @param ex
	 * @param mailTitle 邮件标题，默认加了api4 标识
	 * @param alarmType 控制输出格式// 1表示发送邮件和短信,2表示邮件，3表示短信
	 * @param exceptionCode 默认为 applicationErrorCode
	 * @param servicePort 此服务端口
	 * @param serviceMsg 此消息追加在异常内容前
	 * @return
	 */
	public static ObjectNode getMap(ObjectNode exMap, Exception ex, String mailTitle, int alarmType, String exceptionCode, int servicePort, String serviceMsg) {
		if (StringUtils.isBlank(exceptionCode)) {
			exceptionCode = ResponseCode.applicationErrorCode;
		}
		String exMessage = "null";
		if (null != ex) {
			exMessage = getExceptionMsg(ex);
		}
		if (ex instanceof ServiceException) {
			if (StringUtils.contains(exMessage, "Exception: neo")) {
				exceptionCode = ResponseCode.neoErrorCode;
				mailTitle = "neo error:";
			} else if (StringUtils.contains(exMessage, "Exception: es")) {
				mailTitle = "es error:";
				exceptionCode = ResponseCode.esErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: RuleError")) {
				mailTitle = "Rule Error";
				exceptionCode = ResponseCode.RuleErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: hbase")) {
				mailTitle = "hbase Error";
				exceptionCode = ResponseCode.hbaseErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: datacenter")) {
				mailTitle = "datacenter Error";
				exceptionCode = ResponseCode.datacenterErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: UserCenterError")) {
				mailTitle = "userCenter error";
				exceptionCode = ResponseCode.userCenterErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: DacError")) {
				mailTitle = "DacError";
				exceptionCode = ResponseCode.dacErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: redis")) {
				mailTitle = "redis Error";
				exceptionCode = ResponseCode.redisErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: mcServiceError")) {
				mailTitle = "mcServiceError";
				exceptionCode = ResponseCode.mcServiceErrorCode;
			} else if (StringUtils.contains(exMessage, "Exception: iceServiceError")) {
				mailTitle = "iceServiceError";
				exceptionCode = ResponseCode.iceServiceErrorCode;
			}

			exMap.put("alarmLevel", 2);// 1表示的是即时，2表示的是延迟发送
		} else {
			// 应用的报警
			exMap.put("alarmLevel", 2);
		}

		mailTitle = "hx4:" + mailTitle;
		exMap.put("alarmType", alarmType);// 1表示发送邮件和短信,2表示邮件，3表示短信
		exMap.put("exceptionCode", exceptionCode);// 错误码
		exMap.put("appType", "HUAXIANG4");
		exMap.put("mailTitle", mailTitle);// 邮件标题
		// String mailContent = "IP:"+" " + getIP() + " "+mailTitle;

		String time = "时间: " + DateUtils.format(new Date(), "HH:mm:ss") + " ";
		String mailContent = time + serviceMsg + " [异常堆栈]: \n 流水号：" + JsonUtils.getString(exMap, "swift_number") + " " + exMessage;

		// 写入异常库
		exMap.put("exceptionMsg", mailContent);

		exMap.put("mailContent", mailContent);// 邮件内容

		exMap.put("invokedIP", getIP() + ":" + servicePort);

		String msgContent = StringUtils.substring(mailTitle + mailContent, 0, 70);
		boolean sendAlarm = true;
		if (StringUtils.isNotBlank(serviceMsg)) {
			// 2016年12月14日优化短信报警针对serviceMsg
			try {
				ArrayNode dts = (ArrayNode) mapper.readTree(serviceMsg);
				boolean dacError = false;
				if (null != dts && dts.size() > 0) {
					msgContent = mailTitle + time;
					for (JsonNode jsonNode : dts) {
						if (StringUtils.equalsIgnoreCase(JsonUtils.getString(jsonNode, "service"), "DAC_SERVICE")) {
							dacError = true;
						}
						msgContent += JsonUtils.getString(jsonNode, "exceptionMsg") + ",";
					}
					if (dacError && dts.size() == 1) {
						// sendAlarm = false;//只有dac服务异常，不报警,因为ribbon无法区分，暂停此功能
					}
				}
			} catch (IOException e) {
				logger.error("发送警报格式化异常", e);
			}
		}
		exMap.put("msgContent", msgContent);// 短信内容
		exMap.put("sendAlarm", sendAlarm);
		return exMap;
	}

	public static String getIP() {
		InetAddress ia = null;
		String localip = "null";
		try {
			ia = InetAddress.getLocalHost();
			// String localname = ia.getHostName();
			localip = ia.getHostAddress();
			// System.out.println("本机名称是：" + localname);
			// System.out.println("本机的ip是 ：" + localip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localip;
	}

	private static ExecutorService alarmThread = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200), new MyCallerRunsPolicy());
	public static class MyCallerRunsPolicy implements RejectedExecutionHandler {
		public MyCallerRunsPolicy() {
		}

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			logger.error("报警发送线程繁忙，此次发送被丢弃：" + alarmThread.toString());
		}
	}
	public static class AlarmSendThread implements Runnable {
		private String content;
		int timeout;
		public AlarmSendThread(String content, int timeout) {
			this.content = content;
			this.timeout = timeout;
		}
		@Override
		public void run() {
			try {
				ObjectNode exMap = (ObjectNode) mapper.readTree(content);
				if (StringUtils.equals(JsonUtils.getString(exMap, "sendAlarm"), "false")) {
					logger.warn("报警调用完毕,主动忽略，不发送报警:" + content);
				} else {
					BrSendAlarmNewServicePrx service = (BrSendAlarmNewServicePrx) BrBSFConsumerBean.getServiceProxy(BrSendAlarmNewServicePrx.class);
					BrSendAlarmNewServicePrx client = (BrSendAlarmNewServicePrx) service.ice_invocationTimeout(timeout);
					String rs = client.sendAlarm(content);
					if (!StringUtils.contains(rs, "\"000\"")) {
						logger.error("报警发送异常, rs : {} , content : {} ", rs, content);
					}
					logger.warn("报警调用完毕:" + content);
				}
			} catch (Throwable e) {
				logger.error("报警发送异常，content：" + content, e);
			}
		}

	}
	/**
	 * 异步发送警报，单线程，队列为200，超出忽略
	 * @param content
	 * @param timeout 调用报警ice超时时间，单位毫秒
	 */
	public static void sendAlarm(String content, int timeout) {
		alarmThread.submit(new AlarmSendThread(content, timeout));
	}

}
