/*********************************************************************************************
 * 
 *
 * 'Output.java', in plugin 'msi.gama.headless', is part of the source code of the 
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
import msi.gaml.descriptions.IDescription;

public class Output {
	public static final int DEFAULT_FRAME_RATE = 1;
	
	private static int OUTPUT_ID=0;
	public String name;
	public int frameRate;
	public String	id;
	public String path;
	
	public Output(Output o)
	{
		this.id=new Integer(Output.generateID()).toString();
		this.frameRate = o.frameRate;
		this.name = o.name;
	}
	
	public Output clone(Output o)
	{
		return new Output(o);
	}
	
	public static int generateID()
	{
		return OUTPUT_ID++;
	}

   public static Output loadAndBuildOutput(IDescription exp)
    {
        String name = exp.getFacets().get(IKeyword.NAME).getExpression().literalValue();
        @SuppressWarnings("rawtypes")
        Output res = new Output(name,DEFAULT_FRAME_RATE, new Integer(OUTPUT_ID).toString(),null);
        return res;
    }
	
	public Output(String name, int frameRate, String id, String path) {
		super();
		this.name = name;
		this.frameRate = frameRate;
		this.id = id;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOutputPath() {
		return this.path;
	}
	
}
