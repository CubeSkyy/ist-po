package mmt.core;

/**
 * Class representing the Passenger category.
 */

public abstract class Category {
	/** Represents Category name */
	private String _name;
	/** Represents the discount for each category. */
	private double _discount;

	/** 
	 * Returns category discount.
	 */
	abstract double getDiscount();

	abstract Category next(double totalPaid);

	/**
	 * Returns category name.
	 */
	public abstract String toString();
}