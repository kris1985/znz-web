/**
 * 
 */
package com.znz.exception;

import lombok.Getter;


/**
 * @author huangtao
 * 
 */
public class ServiceException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private String errCode = "";

	@Getter
	private String errReason = "";

	public ServiceException(String errCode, String errReason) {
		super("[" + errCode + "]" + errReason);
		this.errCode = errCode;
		this.errReason =errReason;
	}

	/*public ServiceException(ErrorCodeEnum errorCodeEnum) {
		super("[" + errorCodeEnum.getErrorcode() + "]" + errorCodeEnum.getErrordesc());
		this.errCode = errorCodeEnum.getErrorcode();
		this.errReason =errorCodeEnum.getErrordesc();
	}*/

	public ServiceException(String msg) {
		super(msg);
	}}
