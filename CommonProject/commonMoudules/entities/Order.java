package entities;

import java.sql.Timestamp;

public class Order {
	public enum IdType {
		PRIVATE, PRIVATEGROUP, GUIDE, FAMILY
	};
	public enum OrderStatus {
		CANCEL,SEMICANCELED,IDLE,CONFIRMED,WAITINGLIST,WAITINGLISTMASSAGESENT
	};
	// Identification Number // not for now
	public String parkSite;
	public int numberOfVisitors;
	public int orderID = 0; // change letter serial number of the order
	public float priceOfOrder; // should be calculated
	public String email, phone;
	public IdType type;
	public OrderStatus orderStatus;
	public Timestamp visitTime;
	public Timestamp timeOfOrder;
	public boolean isUsed; // by default false
	public String ownerID;
	public int numberOfSubscribers;
	
	public Order(String parkSite, int numberOfVisitors, int orderID, float priceOfOrder, String email, String phone,
			IdType type, OrderStatus orderStatus, Timestamp visitTime, Timestamp timeOfOrder, boolean isUsed, String ownerID,int numberOfSubscribers) {
		super();
		this.parkSite = parkSite;
		this.numberOfVisitors = numberOfVisitors;
		this.orderID = orderID;
		this.priceOfOrder = priceOfOrder;
		this.email = email;
		this.phone = phone;
		this.type = type;
		this.orderStatus = orderStatus;
		this.visitTime = visitTime;
		this.timeOfOrder = timeOfOrder;
		this.isUsed = isUsed;
		this.ownerID = ownerID;
		this.numberOfSubscribers = numberOfSubscribers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderID != other.orderID)
			return false;
		return true;
	}

	

}