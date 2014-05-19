package com.storeworld.analyze;

public class AnalyzerUtils {

	public enum TYPE {
		MONTH("month"),SEASON("season"),YEAR("year"),ALL("all");
		String type="";
		private TYPE(String type){
			this.type = type;
		}
		@Override
        public String toString() {
            return this.type;
        }
	};
	
	public enum KIND{
		SHIPMENT("shipment"), PROFIT("profit"), NONE("none");
		String kind="";
		private KIND(String kind){
			this.kind = kind;
		}
		@Override
        public String toString() {
            return this.kind;
        }
		
	}
	

}
