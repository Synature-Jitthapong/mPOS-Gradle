package com.synature.pos;

import java.util.List;

public class TableInfo {
	public List<TableZone> TableZone;
	public List<TableName> TableName;
	
	public TableInfo(){
		
	}
	
	public static class TableZone{
		private int ZoneID;
		private String ZoneName;
		
		public int getZoneID() {
			return ZoneID;
		}
		public void setZoneID(int zoneID) {
			ZoneID = zoneID;
		}
		public String getZoneName() {
			return ZoneName;
		}
		public void setZoneName(String zoneName) {
			ZoneName = zoneName;
		}
		@Override
		public String toString() {
			return ZoneName;
		}
	}
	
	public static class TableName{
		private int TableID;
		private String TableName;
		private int ZoneID;
		private int Capacity;
		private int STATUS;
		
		public int getTableID() {
			return TableID;
		}
		public void setTableID(int tableID) {
			TableID = tableID;
		}
		public int getCapacity() {
			return Capacity;
		}
		public void setCapacity(int capacity) {
			Capacity = capacity;
		}
		public int getTableStatus() {
			return STATUS;
		}
		public void setTableStatus(int tableStatus) {
			STATUS = tableStatus;
		}
		public int getZoneID() {
			return ZoneID;
		}
		public void setZoneID(int zoneID) {
			ZoneID = zoneID;
		}
		public String getTableName() {
			return TableName;
		}
		public void setTableName(String tableName) {
			TableName = tableName;
		}
		public int getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(int sTATUS) {
			STATUS = sTATUS;
		}
		
		@Override
		public String toString() {
			return TableName;
		}
	}
}
