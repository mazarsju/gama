/*********************************************************************************************
 * 
 *
 * 'Parameter.java', in plugin 'msi.gama.headless', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.headless.job;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.headless.common.DataType;
import msi.gama.kernel.model.IModel;
import msi.gaml.descriptions.ExperimentDescription;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.IExpressionDescription;
import msi.gaml.descriptions.VariableDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.types.IType;
import msi.gaml.variables.IVariable;

public class Parameter {

	private String name;
	private Object value;
    private DataType type;

    public Parameter(Parameter p)
    {
    	this.name = p.name;
    	this.value = p.value;
    	this.type = p.type;
    }
    
    public Parameter clone(Parameter p)
    {
    	return new Parameter(p); 
    }
    public static Parameter loadAndBuildParameter(IDescription paramDesc,final IModel model)
    {
       String name = paramDesc.getFacets().getLabel(IKeyword.NAME);
       @SuppressWarnings("rawtypes")
       String varName = paramDesc.getFacets().getLabel(IKeyword.VAR);
       IExpression exp = paramDesc.getFacets().get(IKeyword.INIT).getExpression();
       Object val = exp.isConst() ? exp.value(null) : exp.serialize(true); 
       Parameter res = new Parameter(name, val, translate(paramDesc.getType().id()));
       return res;
    }
    
	public static DataType translate(Integer t)
	{
		DataType res ;
		if(t.equals(IType.BOOL)){return DataType.BOOLEAN;} 
		else if(t.equals(IType.INT)){ return DataType.INT;}
		else if(t.equals(IType.FLOAT)){ return DataType.FLOAT;}
		else if(t.equals(IType.STRING)){ return DataType.STRING;}
		return DataType.UNDEFINED;
	}
    
	public Parameter(final String name, final Object value, final DataType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}
	public DataType getType()
	{
		return type;
	}

}
