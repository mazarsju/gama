/*********************************************************************************************
 *
 *
 * 'Maths.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.operators;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.*;
import msi.gama.precompiler.GamlAnnotations.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.matrix.IMatrix;
import msi.gaml.operators.fastmaths.FastMath;

/**
 * The Class GamaMath.
 */
public class Maths {

	@operator(value = { "^" }, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the value (always a float) of the left operand raised to the power of the right operand.",
		masterDoc = true,
		usages = { @usage("if the right-hand operand is equal to 0, returns 1"),
			@usage("if it is equal to 1, returns the left-hand operand."),
			@usage(value = "Various examples of power",
				examples = { @example(value = "2 ^ 3", equals = "8.0"),
					@example(value = "4.0^2", equals = "16.0", isTestOnly = true),
					@example(value = "4.0^0.5", equals = "2.0", isTestOnly = true),
					@example(value = "8^0", equals = "1.0", isTestOnly = true),
					@example(value = "8.0^0", equals = "1.0", isTestOnly = true),
					@example(value = "8^1", equals = "8.0", isTestOnly = true),
					@example(value = "8.0^1", equals = "8.0", isTestOnly = true),
					@example(value = "8^1.0", equals = "8.0", isTestOnly = true),
					@example(value = "8.0^1.0", equals = "8.0", isTestOnly = true),
					@example(value = "2^0.5", equals = "sqrt(2)", isTestOnly = true),
					@example(value = "16.81^0.5", equals = "sqrt(16.81)", isTestOnly = true),
					@example(value = "assert (10^(-9) = 0) equals: false;", isTestOnly = true), }) },
		see = { "*", "sqrt" })
	public static Double pow(final Integer a, final Integer b) {
		return pow(a.doubleValue(), b.doubleValue());
	}

	@operator(value = { "^" }, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the value (always a float) of the left operand raised to the power of the right operand.")
	public static Double pow(final Double a, final Integer b) {
		return pow(a, b.doubleValue());
	}

	@operator(value = { "^" }, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the value (always a float) of the left operand raised to the power of the right operand.")
	public static Double pow(final Integer a, final Double b) {
		return pow(a.doubleValue(), b);
	}

	@operator(value = { "^" }, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the value (always a float) of the left operand raised to the power of the right operand.",
		usages = { @usage(value = "", examples = { @example(value = "4.84 ^ 0.5", equals = "2.2") }) })
	public static Double pow(final Double a, final Double b) {
		return FastMath.pow(a, b);
	}

	// ==== Operators

	@operator(value = "abs", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the absolute value of the operand (so a positive int or float depending on the type of the operand).",
		masterDoc = true,
		usages = { @usage(value = "", examples = { @example(value = "abs (200 * -1 + 0.5)", equals = "199.5") }) })
	public static Double abs(final Double rv) {
		return rv < 0 ? -rv : rv;
	}

	@operator(value = "abs", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(
		value = "Returns the absolute value of the operand (so a positive int or float depending on the type of the operand).",
		usages = { @usage(value = "",
			examples = { @example(value = "abs (-10)", equals = "10"), @example(value = "abs (10)", equals = "10"),
				@example(value = "abs (-0)", equals = "0", isTestOnly = true) }) })
	public static Integer abs(final Integer rv) {
		return (rv ^ rv >> 31) - (rv >> 31);
	}

	@operator(value = "acos", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in the interval [0,180], in decimal degrees) of the arccos of the operand (which should be in [-1,1]).",
		masterDoc = true,
		usages = { @usage(value = "if the right-hand operand is outside of the [-1,1] interval, returns NaN") },
		examples = @example(value = "acos (0)", equals = "90.0"),
		see = { "asin", "atan", "cos" })
	public static Double acos(final Double rv) {
		return FastMath.acos(rv) * toDeg;
	}

	@operator(value = "acos", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the arccos of the operand ")
	public static Double acos(final Integer rv) {
		return FastMath.acos(rv) * toDeg;
	}

	@operator(value = "asin", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in the interval [-90,90], in decimal degrees) of the arcsin of the operand (which should be in [-1,1]).",
		usages = { @usage(value = "if the right-hand operand is outside of the [-1,1] interval, returns NaN") },
		examples = @example(value = "asin (0)", equals = "0.0"),
		see = { "acos", "atan", "sin" })
	public static Double asin(final Double rv) {
		return FastMath.asin(rv) * toDeg;
	}

	@operator(value = "asin", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the arcsin of the operand",
		masterDoc = true,
		examples = @example(value = "asin (90)", equals = "#nan"),
		see = { "acos", "atan" })
	public static Double asin(final Integer rv) {
		return FastMath.asin(rv) * toDeg;
	}

	@operator(value = "atan", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in the interval [-90,90], in decimal degrees) of the arctan of the operand (which can be any real number).",
		masterDoc = true,
		examples = @example(value = "atan (1)", equals = "45.0"),
		see = { "acos", "asin", "tan" })
	public static Double atan(final Double rv) {
		return FastMath.atan(rv) * toDeg;
	}

	@operator(value = "atan", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the arctan of the operand")
	public static Double atan(final Integer rv) {
		return FastMath.atan(rv) * toDeg;
	}

	@operator(value = "tanh", can_be_const = true,
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in the interval [-1,1]) of the hyperbolic tangent of the operand (which can be any real number, expressed in decimal degrees).",
		masterDoc = true,
		examples = { @example(value = "tanh(0)", equals = "0.0"), @example(value = "tanh(100)", equals = "1.0") })
	public static Double tanh(final Double rv) {
		return FastMath.tanh(rv);
	}

	@operator(value = "tanh", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the hyperbolic tangent of the operand (which has to be expressed in decimal degrees).")
	public static Double tanh(final Integer rv) {
		return FastMath.tanh(rv);
	}

	// hqnghi & Tri 14/04/2013
	@operator(value = "cos_rad", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the cosinus of the operand (in decimal degrees).  The argument is casted to an int before being evaluated.",
		masterDoc = true,
		special_cases = "Operand values out of the range [0-359] are normalized.",
		see = { "sin", "tan" })
	public static Double cos_rad(final Double rv) {
		return FastMath.cos(rv);
	}

	@operator(value = "sin_rad", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the sinus of the operand (in decimal degrees). The argument is casted to an int before being evaluated.",
		masterDoc = true,
		usages = @usage("Operand values out of the range [0-359] are normalized."),
		examples = { @example(value = "sin(360)", equals = "0.0") },
		see = { "cos", "tan" })
	public static Double sin_rad(final Double rv) {
		return FastMath.sin(rv);
	}

	@operator(value = "tan_rad", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the trigonometric tangent of the operand (in decimal degrees). The argument is casted to an int before being evaluated.",
		masterDoc = true,
		usages = {
			@usage(
				value = "Operand values out of the range [0-359] are normalized. Notice that tan(360) does not return 0.0 but -2.4492935982947064E-16"),
			@usage(
				value = "The tangent is only defined for any real number except 90 + k `*` 180 (k an positive or negative integer). Nevertheless notice that tan(90) returns 1.633123935319537E16 (whereas we could except infinity).") },
		see = { "cos", "sin" })
	public static Double tan_rad(final Double v) {
		return FastMath.tan(v);
	}

	// end hqnghi & Tri 14/04/2013

	@operator(value = "cos", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the cosinus of the operand (in decimal degrees).  The argument is casted to an int before being evaluated.",
		masterDoc = true,
		special_cases = "Operand values out of the range [0-359] are normalized.",
		see = { "sin", "tan" })
	public static Double cos(final Double rv) {
		int i = rv.intValue();
		if ( i == rv ) { return cos(i); }
		double rad = toRad * rv;
		return FastMath.cos(rad);
	}

	@operator(value = "cos", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the cosinus of the operand.",
		examples = { @example(value = "cos (0)", equals = "1.0"), @example(value = "cos(360)", equals = "1.0"),
			@example(value = "cos(-720)", equals = "1.0") })
	public static Double cos(final Integer rv) {
		return FastMath.cos(rv * toRad);
		// double rad = toRad * rv;
		// return FastMath.cos(rad);
	}

	@operator(value = "sin", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the sinus of the operand (in decimal degrees). The argument is casted to an int before being evaluated.",
		masterDoc = true,
		usages = @usage("Operand values out of the range [0-359] are normalized."),
		examples = { @example(value = "sin(360)", equals = "0.0") },
		see = { "cos", "tan" })
	public static Double sin(final Double rv) {
		int i = rv.intValue();
		if ( i == rv ) { return sin(i); }
		double rad = toRad * rv;
		return FastMath.sin(rad);
	}

	@operator(value = "sin", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the sinus of the operand (in decimal degrees).",
		examples = { @example(value = "sin (0)", equals = "0.0") })
	public static Double sin(final Integer rv) {
		// double rad = toRad * rv;
		return FastMath.sin(rv * toRad);
		// double rad = rv / 180 * FastMath.PI;
		// return FastMath.sin(rad);
	}

	@operator(value = "tan", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns the value (in [-1,1]) of the trigonometric tangent of the operand (in decimal degrees). The argument is casted to an int before being evaluated.",
		masterDoc = true,
		usages = {
			@usage(
				value = "Operand values out of the range [0-359] are normalized. Notice that tan(360) does not return 0.0 but -2.4492935982947064E-16"),
			@usage(
				value = "The tangent is only defined for any real number except 90 + k `*` 180 (k an positive or negative integer). Nevertheless notice that tan(90) returns 1.633123935319537E16 (whereas we could except infinity).") },
		see = { "cos", "sin" })
	public static Double tan(final Double v) {
		double rad = toRad * v;
		return FastMath.tan(rad);
	}

	@operator(value = "tan", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(
		value = "Returns the value (in [-1,1]) of the trigonometric tangent of the operand (in decimal degrees). The argument is casted to an int before being evaluated.",
		examples = { @example(value = "tan (0)", equals = "0.0"),
			@example(value = "tan(90)", equals = "1.633123935319537E16") })
	public static Double tan(final Integer v) {
		double rad = toRad * v;
		return FastMath.tan(rad);
	}

	@operator(value = "even", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns true if the operand is even and false if it is odd.",
		usages = { @usage(value = "if the operand is equal to 0, it returns true."),
			@usage(value = "if the operand is a float, it is truncated before") },
		examples = { @example(value = "even (3)", equals = "false"), @example(value = "even(-12)", equals = "true") })
	public static Boolean even(final Integer rv) {
		return rv.intValue() % 2 == 0;
	}

	@operator(value = "exp", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns Euler's number e raised to the power of the operand.",
		masterDoc = true,
		usages = @usage(value = "the operand is casted to a float before being evaluated."),
		examples = @example(value = "exp (0)", equals = "1.0"),
		see = "ln")
	public static Double exp(final Double rv) {
		return FastMath.exp(rv);
	}

	@operator(value = "exp", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "returns Euler's number e raised to the power of the operand.",
		special_cases = "the operand is casted to a float before being evaluated.")
	public static Double exp(final Integer rv) {
		return FastMath.exp(rv.doubleValue());
	}

	@operator(value = "fact", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the factorial of the operand.",
		usages = @usage("if the operand is less than 0, fact returns 0."),
		examples = @example(value = "fact(4)", equals = "24"))
	public static Double fact(final Integer n) {
		if ( n < 0 ) { return 0.0; }
		double product = 1;
		for ( int i = 2; i <= n; i++ ) {
			product *= i;
		}
		return product;
	}

	@operator(value = "ln", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the natural logarithm (base e) of the operand.",
		masterDoc = true,
		usages = @usage(value = "an exception is raised if the operand is less than zero."),
		examples = @example(value = "ln(exp(1))", equals = "1.0"),
		see = "exp")
	public static Double ln(final IScope scope, final Double x) {
		if ( x <= 0 ) { throw GamaRuntimeException.warning("The ln operator cannot accept negative or null inputs");
		// return Double.MAX_VALUE; // A compromise...
		}
		return FastMath.log(x);
	}

	@operator(value = "ln", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "returns the natural logarithm (base e) of the operand.",
		examples = @example(value = "ln(1)", equals = "0.0"))
	public static Double ln(final IScope scope, final Integer x) {
		if ( x <= 0 ) { throw GamaRuntimeException.warning("The ln operator cannot accept negative or null inputs",
			scope);
		// return Double.MAX_VALUE; // A compromise...
		}
		return FastMath.log(x);
	}

	@operator(value = "log", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the logarithm (base 10) of the operand.",
		masterDoc = true,
		usages = @usage("an exception is raised if the operand is equals or less than zero."),
		examples = @example(value = "log(10)", equals = "1.0"),
		see = "ln")
	public static Double log(final IScope scope, final Double x) {
		if ( x <= 0 ) { throw GamaRuntimeException.warning("The ln operator cannot accept negative or null inputs",
			scope);
		// return Double.MAX_VALUE; // A compromise...
		}
		return FastMath.log10(x);
	}

	@operator(value = "log", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "returns the logarithm (base 10) of the operand.",
		examples = @example(value = "log(1)", equals = "0.0"))
	public static Double log(final IScope scope, final Integer x) {
		if ( x <= 0 ) { throw GamaRuntimeException.warning("The ln operator cannot accept negative or null inputs",
			scope);
		// return Double.MAX_VALUE; // A compromise...
		}
		return FastMath.log10(x);
	}

	@operator(value = "-", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "If it is used as an unary operator, it returns the opposite of the operand.", masterDoc = true)
	public static Double negate(final Double x) {
		return -x;
	}

	@operator(value = "-", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the opposite of the operand.", examples = @example(value = "- (-56)", equals = "56"))
	public static Integer negate(final Integer x) {
		return -x;
	}

	@operator(value = "round", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the rounded value of the operand.",
		masterDoc = true,
		examples = { @example(value = "round (0.51)", equals = "1"), @example(value = "round (100.2)", equals = "100"),
			@example(value = "round(-0.51)", equals = "-1") },
		see = { "int", "with_precision" })
	public static Integer round(final Double v) {
		int i;
		if ( v >= 0 ) {
			i = (int) (v + .5);
		} else {
			i = (int) (v - .5);
		}
		return i;
	}

	@operator(value = "round", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(special_cases = "if the operand is an int, round returns it")
	public static Integer round(final Integer v) {
		return v;
	}

	@operator(value = "sqrt", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the square root of the operand.",
		masterDoc = true,
		usages = @usage(value = "if the operand is negative, an exception is raised"),
		examples = @example(value = "sqrt(4)", equals = "2.0"))
	public static Double sqrt(final IScope scope, final Integer v) throws GamaRuntimeException {
		if ( v < 0 ) { throw GamaRuntimeException.warning("The sqrt operator cannot accept negative inputs", scope); }
		return FastMath.sqrt(v);
	}

	@operator(value = "sqrt", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the square root of the operand.", examples = @example(value = "sqrt(4)", equals = "2.0"))
	public static Double sqrt(final Double v) throws GamaRuntimeException {
		if ( v < 0 ) { throw GamaRuntimeException.warning("The sqrt operator cannot accept negative inputs"); }
		return FastMath.sqrt(v);
	}

	@operator(value = IKeyword.DIVIDE, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the division of the two operands.",
		masterDoc = true,
		usages = { @usage(
			value = "if both operands are numbers (float or int), performs a normal arithmetic division and returns a float.",
			examples = { @example(value = "3 / 5.0", equals = "0.6") }) },
		special_cases = "if the right-hand operand is equal to zero, raises a \"Division by zero\" exception",
		see = { IKeyword.PLUS, IKeyword.MINUS, IKeyword.MULTIPLY })
	public static Double opDivide(final IScope scope, final Integer a, final Integer b) throws GamaRuntimeException {
		if ( b == 0 ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return Double.valueOf(a.doubleValue() / b.doubleValue());
	}

	@operator(value = IKeyword.DIVIDE, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns a float, equal to the division of the left-hand operand by the right-hand operand.",
		see = "*")
	public static Double opDivide(final IScope scope, final Double a, final Integer b) throws GamaRuntimeException {
		if ( b == 0 ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return a / b.doubleValue();
	}

	@operator(value = IKeyword.DIVIDE, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns a float, equal to the division of the left-hand operand by the right-hand operand.",
		see = "*")
	public static Double opDivide(final IScope scope, final Double a, final Double b) throws GamaRuntimeException {
		if ( b.equals(0.0) ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return a / b;
	}

	@operator(value = IKeyword.DIVIDE, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns a float, equal to the division of the left-hand operand by the right-hand operand.",
		see = "*")
	public static Double opDivide(final IScope scope, final Integer a, final Double b) throws GamaRuntimeException {
		if ( b.equals(0.0) ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return a.doubleValue() / b.doubleValue();
	}

	@operator(value = IKeyword.MULTIPLY, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the product of the two operands.",
		masterDoc = true,
		usages = @usage(
			value = "if both operands are numbers (float or int), performs a normal arithmetic product and returns a float if one of them is a float.",
			examples = @example(value = "1 * 1", equals = "1")),
		see = { IKeyword.PLUS, IKeyword.MINUS, IKeyword.DIVIDE })
	public static Integer opTimes(final Integer a, final Integer b) {
		return a * b;
	}

	@operator(value = IKeyword.MULTIPLY, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the product of the two operands",
		examples = { @example(value = "2.5 * 2", equals = "5.0") },
		see = "/")
	public static Double opTimes(final Double a, final Integer b) {
		return Double.valueOf(a * b);
	}

	@operator(value = IKeyword.MULTIPLY, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "Returns the product of the two operands", examples = {}, see = "/")
	public static Double opTimes(final Double a, final Double b) {
		return a * b;
	}

	@operator(value = IKeyword.MULTIPLY, can_be_const = true,
			concept = {})
	@doc(value = "Returns the product of the two operands", examples = {}, see = "/")
	public static Double opTimes(final Integer a, final Double b) {
		return Double.valueOf(a * b);
	}

	@operator(value = IKeyword.MULTIPLY,
		can_be_const = true,
		content_type = ITypeProvider.SECOND_CONTENT_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	@doc(usages = { @usage(
		value = "if one operand is a matrix and the other a number (float or int), performs a normal arithmetic product of the number with each element of the matrix (results are float if the number is a float.",
		examples = {
			@example("matrix<float> m <- (3.5 * matrix([[2,5],[3,4]]));	//m equals matrix([[7.0,17.5],[10.5,14]])") }) })
	public static IMatrix opTimes(final Integer a, final IMatrix b) {
		return b.times(a);
	}

	@operator(value = IKeyword.MULTIPLY,
		can_be_const = true,
		content_type = ITypeProvider.FIRST_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	public static IMatrix opTimes(final Double a, final IMatrix b) {
		return b.times(a);
	}

	@operator(value = IKeyword.PLUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the sum, union or concatenation of the two operands.",
		masterDoc = true,
		usages = { @usage(
			value = "if both operands are numbers (float or int), performs a normal arithmetic sum and returns a float if one of them is a float.",
			examples = { @example(value = "1 + 1", equals = "2"), @example(value = "1.0 + 1", equals = "2.0"),
				@example(value = "1.0 + 2.5", equals = "3.5") }) },
		see = { IKeyword.MINUS, IKeyword.MULTIPLY, IKeyword.DIVIDE })
	public static Integer opPlus(final Integer a, final Integer b) {
		return a + b;
	}

	@operator(value = IKeyword.PLUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the sum, union or concatenation of the two operands.")
	public static Double opPlus(final Double a, final Integer b) {
		return a + b;
	}

	@operator(value = IKeyword.PLUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the sum, union or concatenation of the two operands.")
	public static Double opPlus(final Double a, final Double b) {
		return a + b;
	}

	@operator(value = IKeyword.PLUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the sum, union or concatenation of the two operands.")
	public static Double opPlus(final Integer a, final Double b) {
		return a + b;
	}

	@operator(value = IKeyword.PLUS,
		can_be_const = true,
		content_type = ITypeProvider.SECOND_CONTENT_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	@doc(usages = { @usage(
		value = "if one operand is a matrix and the other a number (float or int), performs a normal arithmetic sum of the number with each element of the matrix (results are float if the number is a float.",
		examples = { @example(value = "3.5 + matrix([[2,5],[3,4]])", equals = "matrix([[5.5,8.5],[6.5,7.5]])") }) })
	// TODO check update
	public static IMatrix opPlus(final Integer a, final IMatrix b) {
		return b.plus(a);
	}

	@operator(value = IKeyword.PLUS,
		can_be_const = true,
		content_type = ITypeProvider.FIRST_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	@doc(value = "Returns the sum of the two operands", examples = {}, see = "/")
	public static IMatrix opPlus(final Double a, final IMatrix b) {
		return b.plus(a);
	}

	@operator(value = IKeyword.MINUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the difference of the two operands.",
		masterDoc = true,
		usages = { @usage(
			value = "if both operands are numbers, performs a normal arithmetic difference and returns a float if one of them is a float.",
			examples = { @example(value = "1 - 1", equals = "0"), @example(value = "1.0 - 1", equals = "0.0"),
				@example(value = "3.7 - 1.2", equals = "2.5"), @example(value = "3 - 1.2", equals = "1.8") }) },
		see = { IKeyword.PLUS, IKeyword.MULTIPLY, IKeyword.DIVIDE })
	public static Integer opMinus(final Integer a, final Integer b) {
		return a - b;
	}

	@operator(value = IKeyword.MINUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the difference of the two operands")
	public static Double opMinus(final Double a, final Integer b) {
		return a - b;
	}

	@operator(value = IKeyword.MINUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the difference of the two operands")
	public static Double opMinus(final Double a, final Double b) {
		return a - b;
	}

	@operator(value = IKeyword.MINUS, can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "the difference of the two operands")
	public static Double opMinus(final Integer a, final Double b) {
		return a - b;
	}

	@operator(value = IKeyword.MINUS,
		can_be_const = true,
		content_type = ITypeProvider.SECOND_CONTENT_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	@doc(value = "the difference of the two operands",
		usages = { @usage(
			value = "if one operand is a matrix and the other a number (float or int), performs a normal arithmetic difference of the number with each element of the matrix (results are float if the number is a float.",
			examples = {
				@example(value = "3.5 - matrix([[2,5],[3,4]])", equals = "matrix([[1.5,-1.5],[0.5,-0.5]])") }) })
	// TODO check update
	public static IMatrix opMinus(final Integer a, final IMatrix b) {
		return b.times(-1).plus(a);
	}

	@operator(value = IKeyword.MINUS,
		can_be_const = true,
		content_type = ITypeProvider.FIRST_TYPE,
		category = { IOperatorCategory.ARITHMETIC },
		concept = {})
	@doc(value = "the difference of the two operands")
	public static IMatrix opMinus(final Double a, final IMatrix b) {
		return b.times(-1).plus(a);
	}

	// @operator(value = "with_precision", can_be_const = true)
	// @doc(value =
	// "round off the value of left-hand operand to the precision given by the value of right-hand operand",
	// examples = {
	// "12345.78943 with_precision 2 --: 12345.79", "123 with_precision 2 --: 123.00" }, see =
	// "round")
	public static Double opTruncate(final Double x, final Integer precision) {
		double x1 = x.doubleValue();
		int precision1 = precision.intValue();
		double fract;
		double whole;
		double mult;
		if ( x1 > 0 ) {
			whole = floor(x1);
			mult = pow(10.0, precision1);
			fract = floor((x1 - whole) * mult) / mult;
		} else {
			whole = ceil(x1);
			mult = pow(10, precision1);
			fract = ceil((x1 - whole) * mult) / mult;
		}
		return whole + fract;
	}

	@operator(value = "with_precision", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Rounds off the value of left-hand operand to the precision given by the value of right-hand operand",
		examples = { @example(value = "12345.78943 with_precision 2", equals = "12345.79"),
			@example(value = "123 with_precision 2", equals = "123.00") },
		see = "round")
	public static double round(final Double v, final Integer precision) {
		long t = TENS[precision]; // contains powers of ten.
		return (double) (long) (v > 0 ? v * t + 0.5 : v * t - 0.5) / t;
	}

	@operator(value = "floor", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Maps the operand to the largest previous following integer, i.e. the largest integer not greater than x.",
		examples = { @example(value = "floor(3)", equals = "3.0"), @example(value = "floor(3.5)", equals = "3.0"),
			@example(value = "floor(-4.7)", equals = "-5.0") },
		see = { "ceil", "round" })
	public static final double floor(final double d) {
		return FastMath.floor(d);
	}

	@operator(value = "ceil", can_be_const = true,
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Maps the operand to the smallest following integer, i.e. the smallest integer not less than x.",
		examples = { @example(value = "ceil(3)", equals = "3.0"), @example(value = "ceil(3.5)", equals = "4.0"),
			@example(value = "ceil(-4.7)", equals = "-4.0") },
		see = { "floor", "round" })
	public static final double ceil(final double d) {
		return FastMath.ceil(d);
	}

	@operator(value = "mod", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the remainder of the integer division of the left-hand operand by the right-hand operand.",
		usages = { @usage(value = "if operands are float, they are truncated"),
			@usage(value = "if the right-hand operand is equal to zero, raises an exception.") },
		examples = { @example(value = "40 mod 3", equals = "1") },
		see = "div")
	public static Integer opMod(final IScope scope, final Integer a, final Integer b) {
		return a % b;
	}

	@operator(value = "div", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns the truncation of the division of the left-hand operand by the right-hand operand.",
		masterDoc = true,
		usages = @usage(value = "if the right-hand operand is equal to zero, raises an exception."),
		examples = @example(value = "40 div 3", equals = "13"),
		see = "mod")
	public static Integer div(final IScope scope, final Integer a, final Integer b) throws GamaRuntimeException {
		if ( b == 0 ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return a / b;
	}

	@operator(value = "div", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "an int, equal to the truncation of the division of the left-hand operand by the right-hand operand.",
		usages = @usage(value = "if the right-hand operand is equal to zero, raises an exception."),
		examples = @example(value = "40.5 div 3", equals = "13"),
		see = "mod")
	public static Integer div(final IScope scope, final Double a, final Integer b) throws GamaRuntimeException {
		if ( b == 0 ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return (int) (a / b);
	}

	@operator(value = "div", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "an int, equal to the truncation of the division of the left-hand operand by the right-hand operand.",
		usages = @usage(value = "if the right-hand operand is equal to zero, raises an exception."),
		examples = @example(value = "40 div 4.1", equals = "9"))
	public static Integer div(final IScope scope, final Integer a, final Double b) throws GamaRuntimeException {
		if ( b.equals(0.0) ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return (int) (a / b);
	}

	@operator(value = "div", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = {})
	@doc(value = "an int, equal to the truncation of the division of the left-hand operand by the right-hand operand.",
		examples = @example(value = "40.1 div 4.5", equals = "8"))
	public static Integer div(final IScope scope, final Double a, final Double b) throws GamaRuntimeException {
		if ( b.equals(0.0) ) { throw GamaRuntimeException.error("Division by zero", scope); }
		return (int) (a / b);
	}

	/** Constant field PI. */
	public static final double PI = java.lang.Math.PI;
	/** Constant field PI_4. */
	public final static double PI_4 = PI / 4d;
	/** Constant field PRECISION. */
	public final static int PRECISION = 360;
	/** Constant field PI_2. */
	public static final double PI_2 = PI * 2;
	/** Constant field PI_2_OVER1. */
	public final static double PI_2_OVER1 = 1f / PI_2;
	/** Constant field PI_2_OVER1_P. */
	public final static double PI_2_OVER1_P = PI_2_OVER1 * PRECISION;
	/** Constant field PI_34. */
	public final static double PI_34 = PI_4 * 3d;
	/** Constant field PREC_MIN_1. */
	public final static int PREC_MIN_1 = PRECISION - 1;

	public static final double SQRT2 = FastMath.sqrt(2);
	/** Constant field toDeg. */
	public static final double toDeg = 180d / FastMath.PI;
	/** Constant field toRad. */
	public static final double toRad = FastMath.PI / 180d;
	public static final long[] TENS = new long[100];

	static {
		for ( int i = 0; i < TENS.length; i++ ) {
			TENS[i] = (long) FastMath.pow(10, i);
		}
	}

	@operator(value = "atan2", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "the atan2 value of the two operands.",
		comment = "The function atan2 is the arctangent function with two arguments. The purpose of using two arguments instead of one is to gather information on the signs of the inputs in order to return the appropriate quadrant of the computed angle, which is not possible for the single-argument arctangent function.",
		masterDoc = true,
		examples = @example(value = "atan2 (0,0)", equals = "0.0"),
		see = { "atan", "acos", "asin" })
	public static double atan2(final double y, final double x) {
		return FastMath.atan2(y, x) * toDeg;
	}

	/**
	 * Check heading : keep it in the 0 - 360 degrees interval.
	 *
	 * @param newHeading the new heading
	 *
	 * @return the integer
	 */
	public static int checkHeading(final int newHeading) {
		int result = newHeading;
		while (result < 0) {
			result += PRECISION;
		}
		return result % PRECISION;
	}

	/**
	 * Check heading : keep it in the 0 - 360 degrees interval.
	 *
	 * @param newHeading the new heading
	 *
	 * @return the double
	 */
	public static double checkHeading(final double newHeading) {
		double result = newHeading;
		while (result < 0) {
			result += PRECISION;
		}
		while (result > 360) {
			result -= PRECISION;
		}
		return result;
	}

	@operator(value = "hypot", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns sqrt(x2 +y2) without intermediate overflow or underflow.",
		special_cases = "If either argument is infinite, then the result is positive infinity. If either argument is NaN and neither argument is infinite, then the result is NaN.",
		examples = @example(value = "hypot(0,1,0,1)", equals = "sqrt(2)"))
	public static double hypot(final double x1, final double x2, final double y1, final double y2) {
		// return FastMath.hypot(x2 - x1, y2 - y1); VERY SLOW !
		final double dx = x2 - x1;
		final double dy = y2 - y1;
		return sqrt(dx * dx + dy * dy);
	}

	@operator(value = "is_number", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC, IConcept.TYPE })
	@doc(value = "Returns whether the argument is a real number or not",
		examples = { @example(value = "is_number(4.66)", equals = "true"),
			@example(value = "is_number(#infinity)", equals = "true"),
			@example(value = "is_number(#nan)", equals = "false") })
	public static Boolean is_number(final Double d) {
		return !Double.isNaN(d);
	}

	@operator(value = "is_finite", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(value = "Returns whether the argument is a finite number or not",
		examples = { @example(value = "is_finite(4.66)", equals = "true"),
			@example(value = "is_finite(#infinity)", equals = "false") })
	public static Boolean is_finite(final Double d) {
		return !Double.isInfinite(d);
	}

	@operator(value = "signum", can_be_const = true, category = { IOperatorCategory.ARITHMETIC },
			concept = { IConcept.MATH, IConcept.ARITHMETIC })
	@doc(
		value = "Returns -1 if the argument is negative, +1 if it is positive, 0 if it is equal to zero or not a number",
		examples = { @example(value = "signum(-12)", equals = "-1"), @example(value = "signum(14)", equals = "1"),
			@example(value = "signum(0)", equals = "0") })
	public static Integer signum(final Double d) {
		if ( d == null || d.isNaN() || Comparison.equal(d, 0d) ) { return 0; }
		if ( d < 0 ) { return -1; }
		return 1;
	}

	public static double hypot(final double x1, final double x2, final double y1, final double y2, final double z1,
		final double z2) {
		final double dx = x2 - x1;
		final double dy = y2 - y1;
		final double dz = z2 - z1;
		return sqrt(dx * dx + dy * dy + dz * dz);
	}

}
