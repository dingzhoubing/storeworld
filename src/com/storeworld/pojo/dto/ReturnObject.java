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
 * author : ��Ԩ<br>
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

	private boolean success;// �Ƿ�ɹ�

	private String errorString;// �쳣��Ϣ

	private CommonDTO returnDTO;// ���ع����������

	private String returnInfo;//������Ϣ


	/** @return : errorString �쳣��Ϣ */
	public String getErrorString() {
		return errorString;
	}

	/**
	 * set errorString
	 * 
	 * @param errorString
	 *            �쳣��Ϣ
	 */
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	/** @return : returnDTO ���ع���������� */
	public CommonDTO getReturnDTO() {
		return returnDTO;
	}

	/**
	 * set returnDTO
	 * 
	 * @param returnDTO
	 *            ���ع����������
	 */
	public void setReturnDTO(CommonDTO returnDTO) {
		this.returnDTO = returnDTO;
	}

	/** @return : success �Ƿ�ɹ� */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * set success
	 * 
	 * @param success
	 *            �Ƿ�ɹ�
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