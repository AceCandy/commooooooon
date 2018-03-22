package com.br.common.exception;

public class ServiceException extends Exception {

	/**
	 * 定义bankserver2.0中的服务错误。
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String exception) {
		super(exception);
	}
}
