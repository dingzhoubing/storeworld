package com.storeworld.pojo.dto;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import com.storeworld.framework.ObjectExternalizable;

public class AnalysticDTO extends ObjectExternalizable{

	String table_field1 = "";
	String table_field2 = "";
	String table_field3 = "";	
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		
		this.table_field1 = readUTF(in);
		this.table_field2 = readUTF(in);
		this.table_field3 = readUTF(in);
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		
		writeUTF(out, this.table_field1);
		writeUTF(out, this.table_field2);
		writeUTF(out, this.table_field3);
	}
	
	public String getField1(){
		return this.table_field1;
	}
	public void setField1(String field){
		this.table_field1 = field;
	}
	public String getField2(){
		return this.table_field2;
	}
	public void setField2(String field){
		this.table_field2 = field;
	}
	public String getField3(){
		return this.table_field3;
	}
	public void setField3(String field){
		this.table_field3 = field;
	}

}
