/*********************************************************************************************
 *
 *
 * 'LoopStatement.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gaml.statements;

import msi.gama.common.interfaces.IGamlIssue;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.serializer;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.IContainer;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.IExpressionDescription;
import msi.gaml.descriptions.SymbolDescription;
import msi.gaml.descriptions.SymbolSerializer;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.statements.IStatement.Breakable;
import msi.gaml.statements.LoopStatement.LoopSerializer;
import msi.gaml.statements.LoopStatement.LoopValidator;
import msi.gaml.types.IType;

// A group of commands that can be executed repeatedly.

@symbol(name = IKeyword.LOOP, kind = ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true, concept = { IConcept.LOOP })
@facets(value = { @facet(name = IKeyword.FROM, type = IType.INT, optional = true, doc = @doc("an int expression")),
		@facet(name = IKeyword.TO, type = IType.INT, optional = true, doc = @doc("an int expression")),
		@facet(name = IKeyword.STEP, type = IType.INT, optional = true, doc = @doc("an int expression")),
		@facet(name = IKeyword.NAME, type = IType.NEW_TEMP_ID, optional = true, doc = @doc("a temporary variable name")),
		@facet(name = IKeyword.OVER, type = { IType.CONTAINER,
				IType.POINT }, optional = true, doc = @doc("a list, point, matrix or map expression")),
		@facet(name = IKeyword.WHILE, type = IType.BOOL, optional = true, doc = @doc("a boolean expression")),
		@facet(name = IKeyword.TIMES, type = IType.INT, optional = true, doc = @doc("an int expression")) }, omissible = IKeyword.NAME)
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT, ISymbolKind.LAYER })
@doc(value = "Allows the agent to perform the same set of statements either a fixed number of times, or while a condition is true, or by progressing in a collection of elements or along an interval of integers. Be aware that there are no prevention of infinite loops. As a consequence, open loops should be used with caution, as one agent may block the execution of the whole model.", usages = {
		@usage(value = "The basic syntax for repeating a fixed number of times a set of statements is:", examples = {
				@example(value = "loop times: an_int_expression {", isExecutable = false),
				@example(value = "     // [statements]", isExecutable = false),
				@example(value = "}", isExecutable = false), @example(value = "int sumTimes <- 1;", isTestOnly = true),
				@example(value = "loop times: 3 {sumTimes <- sumTimes + sumTimes;}", isTestOnly = true),
				@example(var = "sumTimes", equals = "8", isTestOnly = true) }),
		@usage(value = "The basic syntax for repeating a set of statements while a condition holds is:", examples = {
				@example(value = "loop while: a_bool_expression {", isExecutable = false),
				@example(value = "     // [statements]", isExecutable = false),
				@example(value = "}", isExecutable = false), @example(value = "int sumWhile <- 1;", isTestOnly = true),
				@example(value = "loop while: (sumWhile < 5) {sumWhile <- sumWhile + sumWhile;}", isTestOnly = true),
				@example(var = "sumWhile", equals = "8", isTestOnly = true) }),
		@usage(value = "The basic syntax for repeating a set of statements by progressing over a container of a point is:", examples = {
				@example(value = "loop a_temp_var over: a_collection_expression {", isExecutable = false),
				@example(value = "     // [statements]", isExecutable = false),
				@example(value = "}", isExecutable = false) }),
		@usage(value = "The basic syntax for repeating a set of statements while an index iterates over a range of values with a fixed step of 1 is:", examples = {
				@example(value = "loop a_temp_var from: int_expression_1 to: int_expression_2 {", isExecutable = false),
				@example(value = "     // [statements]", isExecutable = false),
				@example(value = "}", isExecutable = false) }),
		@usage(value = "The incrementation step of the index can also be chosen:", examples = {
				@example(value = "loop a_temp_var from: int_expression_1 to: int_expression_2 step: int_expression3 {", isExecutable = false),
				@example(value = "     // [statements]", isExecutable = false),
				@example(value = "}", isExecutable = false), @example(value = "int sumFor <- 0;", isTestOnly = true),
				@example(value = "loop i from: 10 to: 30 step: 10 {sumFor <- sumFor + i;}", isTestOnly = true),
				@example(var = "sumFor", equals = "60", isTestOnly = true) }),
		@usage(value = "In these latter three cases, the name facet designates the name of a temporary variable, whose scope is the loop, and that takes, in turn, the value of each of the element of the list (or each value in the interval). For example, in the first instance of the \"loop over\" syntax :", examples = {
				@example(value = "int a <- 0;"), @example(value = "loop i over: [10, 20, 30] {"),
				@example(value = "     a <- a + i;"), @example(value = "} // a now equals 60"),
				@example(var = "a", equals = "60", isTestOnly = true) }),
		@usage(value = "The second (quite common) case of the loop syntax allows one to use an interval of integers. The from and to facets take an integer expression as arguments, with the first (resp. the last) specifying the beginning (resp. end) of the inclusive interval (i.e. [to, from]). If the step is not defined, it is assumed to be equal to 1 or -1, depending on the direction of the range. If it is defined, its sign will be respected, so that a positive step will never allow the loop to enter a loop from i to j where i is greater than j", examples = {
				@example(value = "list the_list <-list (species_of (self));"),
				@example(value = "loop i from: 0 to: length (the_list) - 1 {"),
				@example(value = "     ask the_list at i {"), @example(value = "        // ..."),
				@example(value = "     }"),
				@example(value = "} // every  agent of the list is asked to do something") }) })
@serializer(LoopSerializer.class)
@validator(LoopValidator.class)
public class LoopStatement extends AbstractStatementSequence implements Breakable {

	public static class LoopValidator implements IDescriptionValidator {

		/**
		 * Method validate()
		 * 
		 * @see msi.gaml.compilation.IDescriptionValidator#validate(msi.gaml.descriptions.IDescription)
		 */
		@Override
		public void validate(final IDescription description) {
			final IExpressionDescription times = description.getFacets().get(TIMES);
			final IExpressionDescription over = description.getFacets().get(OVER);
			final IExpressionDescription from = description.getFacets().get(FROM);
			final IExpressionDescription to = description.getFacets().get(TO);
			final IExpressionDescription step = description.getFacets().get(STEP);
			final IExpressionDescription cond = description.getFacets().get(WHILE);
			IExpressionDescription name = description.getFacets().get(NAME);
			if (name != null && name.isConstant() && name.toString().startsWith(INTERNAL)) {
				name = null;
			}

			if (times != null) {
				if (over != null) {
					description.error("'times' and 'over' are not compatible", IGamlIssue.CONFLICTING_FACETS, TIMES,
							OVER);
					return;
				}
				if (cond != null) {
					description.error("'times' and 'while' are not compatible", IGamlIssue.CONFLICTING_FACETS, TIMES,
							WHILE);
					return;
				}
				if (from != null) {
					description.error("'times' and 'from' are not compatible", IGamlIssue.CONFLICTING_FACETS, TIMES,
							FROM);
					return;
				}
				if (to != null) {
					description.error("'times' and 'to' are not compatible", IGamlIssue.CONFLICTING_FACETS, TIMES, TO);
					return;
				}
				if (name != null) {
					description.error("No variable should be declared", IGamlIssue.UNUSED, TIMES, NAME);
					return;
				}
			} else if (over != null) {
				if (cond != null) {
					description.error("'over' and 'while' are not compatible", IGamlIssue.CONFLICTING_FACETS, OVER,
							WHILE);
					return;
				}
				if (from != null) {
					description.error("'over' and 'from' are not compatible", IGamlIssue.CONFLICTING_FACETS, OVER,
							FROM);
					return;
				}
				if (to != null) {
					description.error("'over' and 'to' are not compatible", IGamlIssue.CONFLICTING_FACETS, OVER, TO);
					return;
				}
				if (name == null) {
					description.error("No variable has been declared", IGamlIssue.MISSING_NAME, OVER);
					return;
				}
			} else if (cond != null) {
				if (from != null) {
					description.error("'while' and 'from' are not compatible", IGamlIssue.CONFLICTING_FACETS, WHILE,
							FROM);
					return;
				}
				if (to != null) {
					description.error("'while' and 'to' are not compatible", IGamlIssue.CONFLICTING_FACETS, WHILE, TO);
					return;
				}
				if (name != null) {
					description.error("No variable should be declared", IGamlIssue.UNUSED, WHILE, NAME);
					return;
				}
			} else if (from != null) {
				if (name == null) {
					description.error("No variable has been declared", IGamlIssue.MISSING_NAME, NAME);
					return;
				}
				if (to == null) {
					description.error("'loop' is missing the 'to:' facet", IGamlIssue.MISSING_FACET, FROM);
					return;
				}
			} else if (to != null) {
				description.error("'loop' is missing the 'from:' facet", IGamlIssue.MISSING_FACET, TO);
				return;
			} else {
				description.error("Missing the definitions of the loop to perform", IGamlIssue.MISSING_FACET);
				return;
			}
		}

	}

	public static class LoopSerializer extends SymbolSerializer {

		@Override
		protected String serializeFacetValue(final SymbolDescription s, final String key,
				final boolean includingBuiltIn) {
			if (key.equals(NAME)) {
				final Facets f = s.getFacets();
				if (f.containsKey(TIMES) || f.containsKey(WHILE)) {
					return null;
				}
			}
			return super.serializeFacetValue(s, key, includingBuiltIn);
		}

	}

	private final LoopExecuter executer;
	private final String varName;
	// private final Object[] result = new Object[1];

	public LoopStatement(final IDescription desc) throws GamaRuntimeException {
		super(desc);
		final boolean isWhile = getFacet(IKeyword.WHILE) != null;
		final boolean isList = getFacet(IKeyword.OVER) != null;
		final boolean isBounded = getFacet(IKeyword.FROM) != null && getFacet(IKeyword.TO) != null;
		varName = getLiteral(IKeyword.NAME);
		executer = isWhile ? new While() : isList ? new Over() : isBounded ? new Bounded() : new Times();
	}

	@Override
	public void enterScope(final IScope scope) {
		// 25/02/14: Suppressed because already done in loopBody() :
		// super.enterScope(scope);

		if (varName != null) {
			scope.addVarWithValue(varName, null);
		}
	}

	@Override
	public void leaveScope(final IScope scope) {
		// Should clear any _loop_halted status present
		scope.popLoop();
		// 25/02/14: Suppressed because already done in loopBody() :
		// super.leaveScope(scope);
	}

	// final Object[] result = new Object[1];

	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		return executer.runIn(scope);
	}

	protected boolean loopBody(final IScope scope, final Object var) {
		scope.push(this);
		if (varName != null) {
			scope.setVarValue(varName, var);
		}

		// result[0] =
		super.privateExecuteIn(scope);
		scope.pop(this);
		return !scope.interrupted();
	}

	interface LoopExecuter {

		abstract Object runIn(final IScope scope);
	}

	class Bounded implements LoopExecuter {

		private final IExpression from = getFacet(IKeyword.FROM);
		private final IExpression to = getFacet(IKeyword.TO);
		private final IExpression step = getFacet(IKeyword.STEP);
		private Integer constantFrom, constantTo, constantStep;
		private final boolean stepDefined;

		Bounded() throws GamaRuntimeException {
			final IScope scope = null;
			// final IScope scope = GAMA.obtainNewScope();
			if (from.isConst()) {
				constantFrom = Cast.asInt(scope, from.value(scope));
			}
			if (to.isConst()) {
				constantTo = Cast.asInt(scope, to.value(scope));
			}
			if (step == null) {
				stepDefined = false;
				constantStep = 1;
			} else if (step.isConst()) {
				stepDefined = true;
				constantStep = Cast.asInt(scope, step.value(scope));
			} else {
				stepDefined = true;
			}
			// GAMA.releaseScope(scope);
		}

		@Override
		public Object runIn(final IScope scope) throws GamaRuntimeException {
			final int f = constantFrom == null ? Cast.asInt(scope, from.value(scope)) : constantFrom;
			final int t = constantTo == null ? Cast.asInt(scope, to.value(scope)) : constantTo;
			int s = constantStep == null ? Cast.asInt(scope, step.value(scope)) : constantStep;
			final boolean negative = f - t > 0;
			// if ( f == t ) { return null; }
			if (negative) {
				if (s > 0) {
					if (!stepDefined) {
						s = -s;
					} else {
						return null;
					}
				}
				for (int i = f, n = t - 1; i > n && loopBody(scope, i); i += s) {
				}
			} else {
				for (int i = f, n = t + 1; i < n && loopBody(scope, i); i += s) {
				}
			}
			return true;
			// return result[0];
		}
	}

	class Over implements LoopExecuter {

		private final IExpression over = getFacet(IKeyword.OVER);

		@Override
		public Object runIn(final IScope scope) throws GamaRuntimeException {
			final Object obj = over.value(scope);
			final Iterable list_ = !(obj instanceof IContainer) ? Cast.asList(scope, obj)
					: ((IContainer) obj).iterable(scope);
			for (final Object each : list_) {
				if (!loopBody(scope, each)) {
					break;
				}
			}
			return true;
			// return result[0];
		}
	}

	class Times implements LoopExecuter {

		private final IExpression times = getFacet(IKeyword.TIMES);
		private Integer constantTimes;

		Times() throws GamaRuntimeException {
			if (times.isConst()) {
				constantTimes = Cast.as(times, Integer.class, false);
			}
		}

		@Override
		public Object runIn(final IScope scope) throws GamaRuntimeException {
			final int max = constantTimes == null ? Cast.asInt(scope, times.value(scope)) : constantTimes;
			for (int i = 0; i < max && loopBody(scope, null); i++) {
			}
			return null;//
			// return result[0];
		}

	}

	class While implements LoopExecuter {

		private final IExpression cond = getFacet(IKeyword.WHILE);

		@Override
		public Object runIn(final IScope scope) throws GamaRuntimeException {
			while (Cast.asBool(scope, cond.value(scope)) && loopBody(scope, null)) {
			}
			return null;
			// return result[0];
		}
	}

}