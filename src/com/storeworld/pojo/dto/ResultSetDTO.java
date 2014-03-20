package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

import com.storeworld.framework.ObjectExternalizable;

/**
 * CCB-COS copyright ccb 2009<br>
 * author : sunbingbing<br>
 * company : CCB <br>
 * 
 * @version : 1.0<br>
 *          Description :
 */
public class ResultSetDTO extends ObjectExternalizable {
	/** 
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = -6676512100706716922L;
	private Map<String,Object> map=new HashMap<String,Object>();

	/** @return : map */
	public Map<String,Object> getMap() {
		return map;
	}

	/**
	 * set map
	 * 
	 * @param map
	 */
	public void setMap(Map<String,Object> map) {
		this.map = map;
	}

	/**
	 * @param key
	 */
	public Object get(String key){
		return this.map.get(key);
	}
	public void readExternal(ObjectInput in) throws IOException,ClassNotFoundException {
		map = (Map)in.readObject();
	}


	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(map);
	}

}
