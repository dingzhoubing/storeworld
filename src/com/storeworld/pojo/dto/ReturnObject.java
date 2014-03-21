package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.storeworld.framework.CommonDTO;
import com.storeworld.framework.IStoreworld;
import com.storeworld.framework.ObjectExternalizable;


/**
 * CCB-COS copyright ccb 2009<br>
 * author : 吴渊<br>
 * company : CCB <br>
 * Create at: 2009-10-13
 * 
 * 
 
 1.write cost:188ms
1.read cost:312ms
2.write cost:78ms
2.read cost:156ms

 * @version : 1.0<br>
 *          Description :
 */
public class ReturnObject extends ObjectExternalizable implements IStoreworld {
	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 5302193557671104130L;

	private boolean success;// 是否成功

	private String errorString;// 异常信息

	private CommonDTO returnDTO;// 返回工作任务对象

	private String returnInfo;//返回信息


	/** @return : errorString 异常信息 */
	public String getErrorString() {
		return errorString;
	}

	/**
	 * set errorString
	 * 
	 * @param errorString
	 *            异常信息
	 */
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	/** @return : returnDTO 返回工作任务对象 */
	public CommonDTO getReturnDTO() {
		return returnDTO;
	}

	/**
	 * set returnDTO
	 * 
	 * @param returnDTO
	 *            返回工作任务对象
	 */
	public void setReturnDTO(CommonDTO returnDTO) {
		this.returnDTO = returnDTO;
	}

	/** @return : success 是否成功 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * set success
	 * 
	 * @param success
	 *            是否成功
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
		errorString = readUTF(in);
		returnDTO = (CommonDTO) in.readObject();// out.writeObject(returnDTO);
		success = in.readBoolean();// out.writeBoolean(success);
		returnInfo = readUTF(in);
	}

	/**
	 * 
	 * @param out
	 * @throws IOException
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		writeUTF(out,errorString);
		out.writeObject(returnDTO);
		out.writeBoolean(success);
		writeUTF(out,returnInfo);
	}


	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}



}