/*********************************************************************************************
 * 
 *
 * 'RuleStatement.java', in plugin 'msi.gaml.architecture.simplebdi', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/

package msi.gaml.architecture.simplebdi;

import java.util.Map;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaMapFactory;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.statements.AbstractStatement;
import msi.gaml.types.IType;


@symbol(name = RuleStatement.RULE, kind = ISymbolKind.SINGLE_STATEMENT, with_sequence = false)
@inside(kinds = { ISymbolKind.SPECIES })
@facets(value = {
	@facet(name = RuleStatement.BELIEF, type = IType.NONE, optional = false, doc = @doc("The belief required")),
	@facet(name = RuleStatement.DESIRE, type = IType.NONE, optional = false, doc = @doc("The desire that will be added")),
	@facet(name = IKeyword.WHEN, type = IType.BOOL, optional = true, doc = @doc(" ")),
	@facet(name = RuleStatement.PRIORITY, type = {IType.FLOAT,IType.INT}, optional = true, doc = @doc("The priority of the predicate added as a desire")),
	@facet(name = IKeyword.NAME, type = IType.ID, optional = true, doc = @doc("The name of the rule"))}
,omissible = IKeyword.NAME)
@doc( value = "enables to add a desire if the agent gets the belief mentioned.",
		examples={@example("rule belif: new_predicate(\"test\") desire: new_predicate(\"test\")")})
public class RuleStatement extends AbstractStatement{

	public static final String RULE = "rule";
	public static final String BELIEF = "belief";
	public static final String DESIRE = "desire";
	public static final String PRIORITY = "priority";
	
	final IExpression when;
	final IExpression belief;
	final IExpression desire;
	final IExpression priority;
	
	public RuleStatement(IDescription desc) {
		super(desc);
		when = getFacet(IKeyword.WHEN);
		belief = getFacet(RuleStatement.BELIEF);
		desire = getFacet(RuleStatement.DESIRE);
		priority = getFacet(RuleStatement.PRIORITY);
	}

	@Override
	protected Object privateExecuteIn(IScope scope) throws GamaRuntimeException {
		if ( when == null || Cast.asBool(scope, when.value(scope)) ){
			if((belief != null)){
				if (SimpleBdiArchitecture.hasBelief(scope, (Predicate)(belief.value(scope)))){
					if((desire != null)){
						//Bricolage avec le copy en attendant que le map soit fix�
						if(((Predicate)(desire.value(scope))).getValues()==null){
							SimpleBdiArchitecture.addDesire(scope, null, ((Predicate)(desire.value(scope))));
						}else{
							Predicate temp;
							temp =  (Predicate)(desire.value(scope));
							//Il faut copier la liste des valeurs.
							temp.setValues((Map<String, Object>) GamaMapFactory.createWithoutCasting(((Predicate)(desire.value(scope))).getType().getKeyType(), ((Predicate)(desire.value(scope))).getType().getContentType(), ((Predicate)(desire.value(scope))).getValues()));
							if(priority!=null){
								temp.setPriority(Cast.asFloat(scope, priority.value(scope)));
							}
							SimpleBdiArchitecture.addDesire(scope, null, temp);
						}
					}
				}
			}
		}
		return null;
	}

}
