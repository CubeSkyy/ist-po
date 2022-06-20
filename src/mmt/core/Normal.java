package mmt.core;

public class Normal extends Category implements java.io.Serializable {
	private String _name = "NORMAL";
	private double _discount = 0.0;

	double getDiscount() {
		return _discount;
	}

	@Override
	public String toString() {
		return _name;
	}

	Category next(double totalPaid){
		if(totalPaid > 250.0 && totalPaid < 2500.0){
			return new Frequente();
		}else if(totalPaid >= 2500.0) {
			return new Especial();
		}else{
			return this;
		}
	}

	}
