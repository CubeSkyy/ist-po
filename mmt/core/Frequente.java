package mmt.core;

public class Frequente extends Category implements java.io.Serializable {
	private String _name = "FREQUENTE";
	private double _discount = 0.15;

	double getDiscount() {
		return _discount;
	}

	@Override
	public String toString() {
		return _name;
	}


	Category next(double totalPaid){
		if(totalPaid < 250.0){
			return new Normal();
		}else if(totalPaid >= 250.0 && totalPaid < 2500){
			return this;
		}else{
			return new Especial();
		}
	}

}