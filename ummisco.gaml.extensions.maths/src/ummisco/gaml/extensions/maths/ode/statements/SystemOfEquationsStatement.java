/*********************************************************************************************
 *
 *
 * 'SystemOfEquationsStatement.java', in plugin 'ummisco.gaml.extensions.maths', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gaml.extensions.maths.ode.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import msi.gama.common.interfaces.IGamlIssue;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.GamlAnnotations.validator;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaListFactory;
import msi.gama.util.IList;
import msi.gama.util.TOrderedHashMap;
import msi.gaml.compilation.IDescriptionValidator;
import msi.gaml.compilation.ISymbol;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.AgentVariableExpression;
import msi.gaml.expressions.IExpression;
import msi.gaml.expressions.IVarExpression;
import msi.gaml.expressions.UnaryOperator;
import msi.gaml.operators.Cast;
import msi.gaml.species.GamlSpecies;
import msi.gaml.statements.AbstractStatementSequence;
import msi.gaml.types.IType;
import ummisco.gaml.extensions.maths.ode.statements.SystemOfEquationsStatement.SystemOfEquationsValidator;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.epidemiology.ClassicalSEIREquations;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.epidemiology.ClassicalSIEquations;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.epidemiology.ClassicalSIREquations;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.epidemiology.ClassicalSIRSEquations;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.epidemiology.ClassicalSISEquations;
import ummisco.gaml.extensions.maths.ode.utils.classicalEquations.populationDynamics.ClassicalLVEquations;

/**
 * The class SystemOfEquationsStatement. This class represents a system of
 * equations (SingleEquationStatement) that implements the interface
 * FirstOrderDifferentialEquations and can be integrated by any of the
 * integrators available in the Apache Commons Library.
 *
 * @author drogoul
 * @since 26 janv. 2013
 *
 */
@symbol(name = IKeyword.EQUATION, kind = ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true, concept = {
		IConcept.EQUATION })
@facets(value = {
		@facet(name = IKeyword.NAME, type = IType.ID /* CHANGE */, optional = false, doc = @doc("the equation identifier")),
		@facet(name = IKeyword.TYPE, type = IType.ID /* CHANGE */, optional = true, values = { "SI", "SIS", "SIR",
				"SIRS", "SEIR",
				"LV" }, doc = @doc(value = "the choice of one among classical models (SI, SIS, SIR, SIRS, SEIR, LV)")),
		@facet(name = IKeyword.VARS, type = IType.LIST, optional = true, doc = @doc("the list of variables used in predefined equation systems")),
		@facet(name = IKeyword.PARAMS, type = IType.LIST, optional = true, doc = @doc("the list of parameters used in predefined equation systems")),
		@facet(name = IKeyword.SIMULTANEOUSLY, type = IType.LIST, of = IType.SPECIES, optional = true, doc = @doc("a list of species containing a system of equations (all systems will be solved simultaneously)")) }, omissible = IKeyword.NAME)
@inside(kinds = { ISymbolKind.SPECIES, ISymbolKind.MODEL })
@doc(value = "The equation statement is used to create an equation system from several single equations.", usages = {
		@usage(value = "The basic syntax to define an equation system is:", examples = {
				@example(value = "float t;", isExecutable = false), @example(value = "float S;", isExecutable = false),
				@example(value = "float I;", isExecutable = false),
				@example(value = "equation SI { ", isExecutable = false),
				@example(value = "   diff(S,t) = (- 0.3 * S * I / 100);", isExecutable = false),
				@example(value = "   diff(I,t) = (0.3 * S * I / 100);", isExecutable = false),
				@example(value = "} ", isExecutable = false) }),
		@usage(value = "If the type: facet is used, a predefined equation system is defined using variables vars: and parameters params: in the right order. All possible predefined equation systems are the following ones (see [EquationPresentation161 EquationPresentation161] for precise definition of each classical equation system): ", examples = {
				@example(value = "equation eqSI type: SI vars: [S,I,t] params: [N,beta];", isExecutable = false),
				@example(value = "equation eqSIS type: SIS vars: [S,I,t] params: [N,beta,gamma];", isExecutable = false),
				@example(value = "equation eqSIR type:SIR vars:[S,I,R,t] params:[N,beta,gamma];", isExecutable = false),
				@example(value = "equation eqSIRS type: SIRS vars: [S,I,R,t] params: [N,beta,gamma,omega,mu];", isExecutable = false),
				@example(value = "equation eqSEIR type: SEIR vars: [S,E,I,R,t] params: [N,beta,gamma,sigma,mu];", isExecutable = false),
				@example(value = "equation eqLV type: LV vars: [x,y,t] params: [alpha,beta,delta,gamma] ;", isExecutable = false) }),
		@usage(value = "If the simultaneously: facet is used, system of all the agents will be solved simultaneously.") }, see = {
				"=", IKeyword.SOLVE })
@validator(SystemOfEquationsValidator.class)

public class SystemOfEquationsStatement extends AbstractStatementSequence implements FirstOrderDifferentialEquations {

	public static class SystemOfEquationsValidator implements IDescriptionValidator<IDescription> {
		public final static Set<String> CLASSICAL_NAMES = new HashSet<String>(
				Arrays.asList("SIR", "SI", "SIS", "SIRS", "SEIR", "LV"));

		@Override
		public void validate(final IDescription description) {
			final String type = description.getFacets().getLabel(TYPE);
			if (type == null)
				return;
			if (!CLASSICAL_NAMES.contains(type)) {
				description.error(type + " is not a recognized classical equation name", IGamlIssue.WRONG_TYPE, TYPE);
			}
		}
	}

	public final Map<String, SingleEquationStatement> equations = new TOrderedHashMap<>();
	public final Map<String, IExpression> variables_diff = new TOrderedHashMap<>();
	public IExpression variable_time = null;
	private IScope currentScope;
	IExpression simultan = null;

	public SystemOfEquationsStatement(final IDescription desc) {
		super(desc);
		setName(getLiteral(IKeyword.NAME));
		simultan = getFacet(IKeyword.SIMULTANEOUSLY);

	}

	/**
	 * This method separates regular statements and equations.
	 *
	 * @see msi.gaml.statements.AbstractStatementSequence#setChildren(java.util.List)
	 */
	@Override
	public void setChildren(final List<? extends ISymbol> commands) {
		List<? extends ISymbol> cmd = commands;
		if (getFacet(IKeyword.TYPE) != null) {
			final String type = getFacet(IKeyword.TYPE).literalValue();
			switch (type) {
			case "SIR":
				cmd.clear();
				cmd = new ClassicalSIREquations(getDescription()).SIR(getFacet(IKeyword.VARS),
						getFacet(IKeyword.PARAMS));
				break;
			case "SI":
				cmd.clear();
				cmd = new ClassicalSIEquations(getDescription()).SI(getFacet(IKeyword.VARS), getFacet(IKeyword.PARAMS));
				break;
			case "SIS":
				cmd.clear();
				cmd = new ClassicalSISEquations(getDescription()).SIS(getFacet(IKeyword.VARS),
						getFacet(IKeyword.PARAMS));
				break;
			case "SIRS":
				cmd.clear();
				cmd = new ClassicalSIRSEquations(getDescription()).SIRS(getFacet(IKeyword.VARS),
						getFacet(IKeyword.PARAMS));
				break;
			case "SEIR":
				cmd.clear();
				cmd = new ClassicalSEIREquations(getDescription()).SEIR(getFacet(IKeyword.VARS),
						getFacet(IKeyword.PARAMS));
				break;
			case "LV":
				cmd.clear();
				cmd = new ClassicalLVEquations(getDescription()).LV(getFacet(IKeyword.VARS), getFacet(IKeyword.PARAMS));
			}
		}

		final List<ISymbol> others = new ArrayList<>();
		for (final ISymbol s : cmd) {
			if (s instanceof SingleEquationStatement) {
				((SingleEquationStatement) s).establishVar();
				equations.put(((SingleEquationStatement) s).toString(), (SingleEquationStatement) s);
				for (int i = 0; i < ((SingleEquationStatement) s).getVars().size(); i++) {
					final IExpression v = ((SingleEquationStatement) s).getVar(i);
					if (((SingleEquationStatement) s).getOrder() > 0) {
						variables_diff.put(((SingleEquationStatement) s).toString(), v);
					}
				}
				variable_time = ((SingleEquationStatement) s).getVarTime();
			} else {
				others.add(s);
			}
		}
		super.setChildren(others);
	}

	public void assignValue(final IScope scope, final double time, final double[] y) {
		final List<SingleEquationStatement> equationValues = new ArrayList<SingleEquationStatement>(equations.values());
		final List<IExpression> variableValues = new ArrayList<IExpression>(variables_diff.values());
		for (int i = 0, n = equationValues.size(); i < n; i++) {

			final SingleEquationStatement s = equationValues.get(i);
			if (s.getOrder() == 0) {
				continue;
			}

			final IAgent remoteAgent = getEquationAgents(scope).get(i);
			boolean pushed = false;
			if (!remoteAgent.dead()) {
				pushed = scope.push(remoteAgent);
				try {
					if (s.getVarTime() instanceof IVarExpression) {
						((IVarExpression) s.getVarTime()).setVal(scope, time, false);
					}
					if (variableValues.get(i) instanceof IVarExpression) {
						((IVarExpression) variableValues.get(i)).setVal(scope, y[i], false);
					}
				} catch (final Exception ex1) {
					GAMA.reportAndThrowIfNeeded(scope, GamaRuntimeException.create(ex1, scope), true);
				} finally {
					if (pushed) {
						scope.pop(remoteAgent);
					}
				}
			}

		}

		for (int i = 0, n = equationValues.size(); i < n; i++) {
			final SingleEquationStatement s = equationValues.get(i);
			if (s.getOrder() == 0) {
				final IExpression tmp = ((UnaryOperator) s.getFunction()).arg(0);
				final Object v = s.getExpression().value(currentScope);
				if (tmp instanceof AgentVariableExpression) {
					((AgentVariableExpression) tmp).setVal(currentScope, v, false);
				}
			}

		}

	}

	/**
	 * This method is bound to be called by the integrator of the equations
	 * system (instantiated in SolveStatement).
	 *
	 * @see org.apache.commons.math3.ode.FirstOrderDifferentialEquations#computeDerivatives(double,
	 *      double[], double[])
	 */

	@Override
	public void computeDerivatives(final double time, final double[] y, final double[] ydot)
			throws MaxCountExceededException, DimensionMismatchException {
		/*
		 * the y value is calculated automatically inside integrator's algorithm
		 * just get y, and assign value to Variables in GAMA, which is use by
		 * GAMA modeler
		 */

		assignValue(currentScope, time, y);

		/*
		 * with variables assigned, calculate new value of expression in
		 * function loop through equations (internal and external) to get
		 * SingleEquation values
		 */

		final List<SingleEquationStatement> equationValues = new ArrayList<SingleEquationStatement>(equations.values());
		final IList<IAgent> equaAgents = getEquationAgents(currentScope);
		for (int i = 0, n = getDimension(); i < n; i++) {
			try {
				final Object[] result = new Object[1];
				currentScope.execute(equationValues.get(i), equaAgents.get(i), null, result);
				ydot[i] = Cast.asFloat(currentScope, result[0]);
			} catch (final Exception e2) {
				GAMA.reportAndThrowIfNeeded(currentScope, GamaRuntimeException.create(e2, currentScope), true);
			}
		}

	}

	/**
	 * The dimension of the equations system is simply, here, the number of
	 * equations.
	 *
	 * @see org.apache.commons.math3.ode.FirstOrderDifferentialEquations#getDimension()
	 */
	@Override
	public int getDimension() {
		int count = 0;
		for (final SingleEquationStatement equation : equations.values()) {
			if (equation.getOrder() > 0) {
				count++;
			}
		}

		return count;
	}

	private void setCurrentScope(final IScope currentScope) {
		this.currentScope = currentScope;
	}

	private Set<IAgent> getExternalAgents(final IScope scope) {
		Set<IAgent> result = (Set<IAgent>) scope.getAgentScope().getAttribute("__externalAgents");
		if (result == null) {
			result = new HashSet();
			scope.getAgentScope().setAttribute("__externalAgents", result);
		}
		return result;
	}

	public IList<IAgent> getEquationAgents(final IScope scope) {
		IList<IAgent> result = (IList<IAgent>) scope.getAgentScope().getAttribute("__equationAgents");
		if (result == null) {
			result = GamaListFactory.create();
			scope.getAgentScope().setAttribute("__equationAgents", result);
		}
		return result;

	}

	private void beginWithScope(final IScope scope) {
		setCurrentScope(scope);
		addInternalAgents(scope);
		addExternalAgents(scope);
		addExternalEquations(scope);
	}

	private void finishWithScope(final IScope scope) {
		removeExternalEquations(scope);
		getEquationAgents(scope).clear();
		getExternalAgents(scope).clear();
		setCurrentScope(null);
	}

	public synchronized void executeInScope(final IScope scope, final Runnable criticalSection) {
		beginWithScope(scope);
		criticalSection.run();
		finishWithScope(scope);
	}

	private void addInternalAgents(final IScope scope) {
		getEquationAgents(scope).addAll(Collections.nCopies(equations.size(), scope.getAgentScope()));
	}

	private void addExternalAgents(final IScope scope) {
		addExternalAgents(scope, simultan, getExternalAgents(scope));
	}

	private void addExternalEquations(final IScope scope) {
		for (final IAgent remoteAgent : getExternalAgents(scope)) {
			if (!remoteAgent.dead()) {
				addEquationsOf(remoteAgent);
			}
		}
	}

	private void removeExternalEquations(final IScope scope) {
		for (final IAgent remoteAgent : getExternalAgents(scope)) {
			if (!remoteAgent.dead()) {
				removeEquationsOf(remoteAgent);
			}
		}
	}

	private void addEquationsOf(final IAgent remoteAgent) {
		final SystemOfEquationsStatement ses = remoteAgent.getSpecies().getStatement(SystemOfEquationsStatement.class,
				getName());
		if (ses != null) {
			for (int i = 0; i < ses.equations.size(); i++) {
				getEquationAgents(currentScope).add(remoteAgent);
			}
			if (equations.keySet().containsAll(ses.equations.keySet())) {
				return;
			}
			for (final Map.Entry<String, SingleEquationStatement> s : ses.equations.entrySet()) {
				final String name = remoteAgent.getName() + s.getKey();
				final SingleEquationStatement eq = s.getValue();
				equations.put(name, eq);
			}
			for (final Map.Entry<String, IExpression> s : ses.variables_diff.entrySet()) {
				final String name = remoteAgent.getName() + s.getKey();
				final IExpression v = s.getValue();
				variables_diff.put(name, v);
			}
		}

	}

	private void removeEquationsOf(final IAgent remoteAgent) {
		final SystemOfEquationsStatement ses = remoteAgent.getSpecies().getStatement(SystemOfEquationsStatement.class,
				getName());
		if (ses != null) {
			for (final String s : ses.equations.keySet()) {
				equations.remove(remoteAgent.getName() + s);
				variables_diff.remove(remoteAgent.getName() + s);
			}
		}
	}

	private void addExternalAgents(final IScope scope, final Object toAdd, final Set<IAgent> externalAgents) {
		if (toAdd instanceof IExpression) {
			addExternalAgents(scope, ((IExpression) toAdd).value(scope), externalAgents);
		} else if (toAdd instanceof IAgent && !toAdd.equals(scope.getAgentScope()) && !((IAgent) toAdd).dead())
			externalAgents.add((IAgent) toAdd);
		else if (toAdd instanceof GamlSpecies)
			addExternalAgents(scope, ((GamlSpecies) toAdd).getPopulation(scope), externalAgents);
		else if (toAdd instanceof IList)
			for (final Object o : ((IList) toAdd).iterable(scope)) {
				addExternalAgents(scope, o, externalAgents);
			}
	}

}
