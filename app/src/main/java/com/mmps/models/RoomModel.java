package com.mmps.models;

import java.io.Serializable;

public class RoomModel implements Serializable {
	private int srNo, roomCapacity, roomDeposit, roomRent;
	private String roomNo, roomType, taxDescription, description;
	private boolean status;


	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public int getRoomDeposit() {
		return roomDeposit;
	}

	public void setRoomDeposit(int roomDeposit) {
		this.roomDeposit = roomDeposit;
	}

	public int getRoomRent() {
		return roomRent;
	}

	public void setRoomRent(int roomRent) {
		this.roomRent = roomRent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getTaxDescription() {
		return taxDescription;
	}

	public void setTaxDescription(String taxDescription) {
		this.taxDescription = taxDescription;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
