/*********************************************************************************************
 * 
 * 
 * 'Comparison.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.operators;

import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.*;
import com.vividsolutions.jts.index.quadtree.IntervalSize;

/**
 * Written by drogoul Modified on 10 dec. 2010
 * 
 * @todo Description
 * 
 */
public class Comparison {

	public final static String GT = ">";
	public final static String LT = "<";
	public final static String GTE = ">=";
	public final static String LTE = "<=";

	@operator(value = "between", can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "returns true the first integer operand is bigger than the second integer operand and smaller than the third integer operand",
		examples = @example(value = "between(5, 1, 10)", equals = "true"))
	public static
		Boolean between(final Integer a, final Integer inf, final Integer sup) {
		if ( inf > sup ) { return false; }
		return a >= sup ? false : a > inf;
	}

	@operator(value = "between", can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if the first float operand is bigger than the second float operand and smaller than the third float operand",
		examples = @example(value = "between(5.0, 1.0, 10.0)", equals = "true"))
	public static
		Boolean between(final Double a, final Double inf, final Double sup) {
		if ( inf > sup ) { return false; }
		return a >= sup ? false : a > inf;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		masterDoc = true,
		usages = { @usage("if one of the operands is nil, returns false") },
		examples = @example(value = "3 > 7", equals = "false"),
		see = {LT, GTE, LTE, "=", "!="})
	public static Boolean greater(final Integer a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a > b;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		examples = { @example(value = "3 > 2.5", equals = "true") })
	public static Boolean greater(final Integer a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a > b;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 > 7", equals = "false") })
	public static Boolean greater(final Double a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a > b;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 > 7.6", equals = "false") })
	public static Boolean greater(final Double a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a > b;
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "true if the left-hand operand is less than the right-hand operand, false otherwise.",
		masterDoc = true,
		special_cases = { "if one of the operands is nil, returns false" },
		examples = { @example(value = "3 < 7", equals = "true") },
		see = {GT, GTE, LTE, "=", "!="})
	public static Boolean less(final Integer a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a < b;
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less than the right-hand operand, false otherwise.",
		examples = { @example(value = "3 < 2.5", equals = "false") })
	public static Boolean less(final Integer a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a < b;
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 < 7", equals = "true") })
	public static Boolean less(final Double a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a < b;
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 < 7.6", equals = "true") })
	public static Boolean less(final Double a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a < b;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "true if the left-hand operand is greater or equal than the right-hand operand, false otherwise.",
		masterDoc = true,
		usages = { @usage("if one of the operands is nil, returns false") },
		examples = { @example(value = "3 >= 7", equals = "false") },
				see = {GT, LT, LTE, "=", "!="})
	public static Boolean greaterOrEqual(final Integer a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a >= b;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "3 >= 2.5", equals = "true") })
	public static Boolean greaterOrEqual(final Integer a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a >= b;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 >= 7", equals = "false") })
	public static Boolean greaterOrEqual(final Double a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a >= b;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is greater or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 >= 3.5", equals = "true") })
	public static Boolean greaterOrEqual(final Double a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return !(a < b);
	}

	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less or equal than the right-hand operand, false otherwise.",
		masterDoc = true,
		usages = { @usage("if one of the operands is nil, returns false") },
		examples = { @example(value = "3 <= 7", equals = "true") })
	public static Boolean opLessThanOrEqual(final Integer a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a <= b;
	}

	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "3 <= 2.5", equals = "false") },
		see = {GT, LT, GTE, "=", "!="})
	public static Boolean lessOrEqual(final Integer a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return a <= b;
	}

	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "7.0 <= 7", equals = "true") })
	public static Boolean lessOrEqual(final Double a, final Integer b) {
		if ( a == null || b == null ) { return false; }
		return a <= b;
	}

	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "true if the left-hand operand is less or equal than the right-hand operand, false otherwise.",
		examples = { @example(value = "3.5 <= 3.5", equals = "true") })
	public static Boolean lessOrEqual(final Double a, final Double b) {
		if ( a == null || b == null ) { return false; }
		return !(a > b);
	}

	@operator(value = { "=" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "returns true if both operands are equal, false otherwise", masterDoc = true, examples = {
		@example(value = "4.5 = 4.7", equals = "false") }, see = { GT, LT, GTE, LTE, "!=" })
	public static Boolean equal(final Double a, final Double b) {
		return a == null ? b == null : IntervalSize.isZeroWidth(a, b);
		// return !(a < b) && !(a > b);
	}

	@operator(value = { "=" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if both operands are equal, false otherwise", masterDoc = true, examples = {
		@example(value = "4 = 5", equals = "false") }, see = { "!=" })
	public static Boolean equal(final Integer a, final Integer b) {
		return a == null ? b == null : a.intValue() == b.intValue();
		// return !(a < b) && !(a > b);
	}

	@operator(value = { "=" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if both operands are equal, false otherwise", examples = {
		@example(value = "3 = 3.0", equals = "true"), @example(value = "4 = 4.7", equals = "false") }, see = { "!=" })
	public static Boolean equal(final Integer a, final Double b) {
		return a == null ? b == null : new Double(a).equals(b);
		// return !(a < b) && !(a > b);
	}

	@operator(value = { "=" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if both operands are equal, false otherwise", examples = {
		@example(value = "4.7 = 4", equals = "false") }, see = { "!=" })
	public static Boolean equal(final Double a, final Integer b) {
		return a == null ? b == null : new Double(b).equals(a);
		// return !(a < b) && !(a > b);
	}

	@operator(value = { "!=", "<>" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = { IConcept.COMPARISON })
	@doc(value = "true if both operands are different, false otherwise",
		masterDoc = true,
		examples = { @example(value = "3.0 != 3.0", equals = "false"), @example(value = "4.0 != 4.7", equals = "true") },
		see = { "=", GT, LT, GTE, LTE, "=" })
	public static Boolean different(final Double a, final Double b) {
		if ( a == null ) { return b != null; }
		if ( b == null ) { return false; }
		return !IntervalSize.isZeroWidth(a, b);
		// return a < b || a > b;
	}
	
	@operator(value = { "!=", "<>" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if both operands are different, false otherwise", examples = {
		@example(value = "3 != 3.0", equals = "false"), @example(value = "4 != 4.7", equals = "true") }, see = { "=" })
	public static Boolean different(final Integer a, final Double b) {
		return a == null ? b == null : ! (new Double(a).equals(b));
		// return !(a < b) && !(a > b);
	}

	@operator(value = { "!=", "<>" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = "returns true if both operands are different, false otherwise", examples = {
		@example(value = "3.0 != 3", equals = "false"), @example(value = "4.7 != 4", equals = "true") }, see = { "=" })
	public static Boolean different(final Double a, final Integer b) {
		return a == null ? b == null : ! (new Double(b).equals(a)); 
		// return !(a < b) && !(a > b);
	}


	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.STRING }, concept = { IConcept.STRING })
	@doc(value = "Returns true if the left-hand operand is smaller than or equal to the right-hand operand, false otherwise.",
		usages = @usage(value = "if both operands are String, uses a lexicographic comparison of two strings",
			examples = { @example(value = "'abc' <= 'aeb'", equals = "true") }))
	public static
		Boolean lessOrEqual(final String a, final String b) {
		if ( a == null ) { return false; }
		int i = a.compareTo(b);
		return i <= 0;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.STRING }, concept = { IConcept.STRING })
	@doc(value = "Returns true if the left-hand operand is greater than or equal to the right-hand operand, false otherwise.",
		usages = @usage(value = "if both operands are string, uses a lexicographic comparison of the two strings",
			examples = { @example(value = "'abc' >= 'aeb'", equals = "false"),
				@example(value = "'abc' >= 'abc'", equals = "true") }))
	public static
		Boolean greaterOrEqual(final String a, final String b) {
		if ( a == null ) { return false; }
		int i = a.compareTo(b);
		return i >= 0;
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.STRING }, concept = {})
	@doc(value = "A lexicographic comparison of two strings. Returns true if the left-hand operand is smaller than the right-hand operand, false otherwise.",
		usages = @usage(value = "if both operands are String, uses a lexicographic comparison of two strings",
			examples = { @example(value = "'abc' < 'aeb'", equals = "true") }))
	public static
		Boolean less(final String a, final String b) {
		if ( a == null ) { return false; }
		int i = a.compareTo(b);
		return i < 0;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.STRING }, concept = {})
	@doc(value = "Returns true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		usages = @usage(value = "if both operands are String, uses a lexicographic comparison of two strings",
			examples = { @example(value = "'abc' > 'aeb'", equals = "false") }))
	public static Boolean greater(final String a, final String b) {
		if ( a == null ) { return false; }
		int i = a.compareTo(b);
		return i > 0;
	}

	@operator(value = { "=" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(usages = @usage(value = "if both operands are any kind of objects, returns true if they are identical (i.e., the same object) or equal (comparisons between nil values are permitted)",
		examples = @example(value = "[2,3] = [2,3]", equals = "true") ))
	public static
		Boolean equal(final Object a, final Object b) {
		return a == null ? b == null : a.equals(b);
	}

	@operator(value = { "!=", "<>" }, can_be_const = true, category = { IOperatorCategory.COMPARISON }, concept = {})
	@doc(value = " Returns false if the two operands are identical (i.e., the same object) or equal. Comparisons between nil values are permitted.",
		examples = { @example(value = "[2,3] != [2,3]", equals = "false"),
			@example(value = "[2,4] != [2,3]", equals = "true") })
	public static
		Boolean different(final Object a, final Object b) {
		return a == null ? b != null : !a.equals(b);
	}

	@operator(value = LT, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.POINT }, concept = { IConcept.POINT })
	@doc(value = "true if the left-hand operand is lower than the right-hand operand, false otherwise.",
		usages = { @usage(value = "if both operands are points, returns true if and only if the left component (x) of the left operand if less than or equal to x of the right one and if the right component (y) of the left operand is greater than or equal to y of the right one.",
			examples = { @example(value = "{5,7} < {4,6}", equals = "false"),
				@example(value = "{5,7} < {4,8}", equals = "false") }) })
	public static
		Boolean less(final GamaPoint p1, final GamaPoint p) {
		return p1.x < p.x && p1.y < p.y;
	}

	@operator(value = GT, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.POINT }, concept = { IConcept.POINT })
	@doc(value = "true if the left-hand operand is greater than the right-hand operand, false otherwise.",
		usages = { @usage(value = "if both operands are points, returns true if and only if the left component (x) of the left operand if greater than x of the right one and if the right component (y) of the left operand is greater than y of the right one.",
			examples = { @example(value = "{5,7} > {4,6}", equals = "true"),
				@example(value = "{5,7} > {4,8}", equals = "false") }) })
	public static
		Boolean greater(final GamaPoint p1, final GamaPoint p) {
		return p1.x > p.x && p1.y > p.y;
	}

	@operator(value = LTE, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.POINT }, concept = { IConcept.POINT })
	@doc(value = "true if the left-hand operand is lower or equals than the right-hand operand, false otherwise.",
		usages = { @usage(value = "if both operands are points, returns true if and only if the left component (x) of the left operand if less than or equal to x of the right one and if the right component (y) of the left operand is greater than or equal to y of the right one.",
			examples = { @example(value = "{5,7} <= {4,6}", equals = "false"),
				@example(value = "{5,7} <= {4,8}", equals = "false") }) })
	public static
		Boolean lessOrEqual(final GamaPoint p1, final GamaPoint p) {
		return p1.x <= p.x && p1.y <= p.y;
	}

	@operator(value = GTE, can_be_const = true, category = { IOperatorCategory.COMPARISON, IOperatorCategory.POINT }, concept = { IConcept.POINT })
	@doc(value = "true if the left-hand operand is greater or equals than the right-hand operand, false otherwise.",
		usages = { @usage(value = "if both operands are points, returns true if and only if the left component (x) of the left operand if greater or equal than x of the right one and if the right component (y) of the left operand is greater than or equal to y of the right one.",
			examples = { @example(value = "{5,7} >= {4,6}", equals = "true"),
				@example(value = "{5,7} >= {4,8}", equals = "false") }) })
	public static
		Boolean greaterOrEqual(final GamaPoint p1, final GamaPoint p) {
		return p1.x >= p.x && p1.y >= p.y;
	}

}
