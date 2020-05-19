package com.ibm.training;


import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = "\\,")
public class Shipper {
	@DataField(pos=1)
	private String shipperId;
	@DataField(pos=2)
	private String location;
	
	public String getShipperId() {
		return shipperId;
	}

	public void setShipperId(String shipperId) {
		this.shipperId = shipperId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return shipperId + " ... " +location;
	}

	
}
