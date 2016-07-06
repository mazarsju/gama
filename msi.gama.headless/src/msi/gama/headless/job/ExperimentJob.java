/*********************************************************************************************
 * 
 * 
 * 'Simulation.java', in plugin 'msi.gama.headless', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.headless.job;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.headless.common.Display2D;
import msi.gama.headless.common.Globals;
import msi.gama.headless.core.HeadlessSimulationLoader;
import msi.gama.headless.core.IRichExperiment;
import msi.gama.headless.core.RichExperiment;
import msi.gama.headless.core.RichOutput;
import msi.gama.headless.runtime.LocalSimulationRuntime;
import msi.gama.headless.runtime.RuntimeContext;
import msi.gama.headless.runtime.SimulationRuntime;
import msi.gama.headless.xml.Writer;
import msi.gama.headless.xml.XmlTAG;
import msi.gama.kernel.experiment.ExperimentPlan;
import msi.gama.kernel.experiment.IExperimentPlan;
import msi.gama.kernel.model.IModel;
import msi.gaml.descriptions.ExperimentDescription;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.IExpressionDescription;

public class ExperimentJob implements IExperimentJob{

	private static long GLOBAL_ID_GENERATOR = 0;
	public static enum OutputType {
		OUTPUT, EXPERIMENT_ATTRIBUTE, SIMULATION_ATTRIBUTE
	}

	public static class ListenedVariable {

		String name;
		int frameRate;
		OutputType type;
		Object value;
		long step;
		String path;

		public ListenedVariable(final String name, final int frameRate, final OutputType type, final String outputPath) {
			this.name = name;
			this.frameRate = frameRate;
			this.type = type;
			this.path = outputPath;
		}

		public String getName() {
			return name;
		}

		public void setValue(final Object obj,long st) {
			value = obj;
			this.step=st;
		}

		public Object getValue() {
			return value;
		}

		public OutputType getType() {
			return type;
		}
		
		public String getPath() {
			return path;
		}
	}

	/**
	 * Variable listeners
	 */
	private ListenedVariable[] listenedVariables;
	private List<Parameter> parameters;
	private List<Output> outputs;
	private Writer outputFile;
	private String sourcePath;
	private String experimentName;
	private String modelName;
	private long seed;

	/**
	 * simulator to be loaded
	 */
	public IRichExperiment simulator;

	public IRichExperiment getSimulation() {
		return simulator;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * current step
	 */
	private long step;

	/**
	 * id of current experiment
	 */
	private String experimentID;
	public long finalStep;

	
	private static long generateID()
	{
		return ExperimentJob.GLOBAL_ID_GENERATOR++;
	}
	
	public void setBufferedWriter(final Writer w) {
		this.outputFile = w;
	}

	public void addParameter(final Parameter p) {
		this.parameters.add(p);
	}

	public void addOutput(final Output p) {
		p.setId(""+outputs.size());
		this.outputs.add(p);
	}
	private ExperimentJob()
	{
		initialize();
		
	}
	public ExperimentJob(final ExperimentJob clone) {
		this();
		this.experimentID=(clone.experimentID!=null)?clone.experimentID:""+ ExperimentJob.generateID();
		this.sourcePath = clone.sourcePath;
		this.finalStep = clone.finalStep;
		this.experimentName = clone.experimentName;
		this.modelName = clone.modelName;
		this.parameters = new ArrayList<>();
		this.outputs = new ArrayList<>();
		this.listenedVariables = clone.listenedVariables;
		this.step = clone.step;
		this.seed = clone.seed;
		for(Parameter p:clone.parameters) {
			this.addParameter(new Parameter(p));
		}
		for(Output o:clone.outputs)
		{
			this.addOutput(new Output(o));
		}
		
	}
	
	public ExperimentJob(final String sourcePath, final String exp, final long max, final long s){
				this(sourcePath,new Long(ExperimentJob.generateID()).toString(),exp,max,s);
		}
	
	public ExperimentJob( final String sourcePath, final String expId,final String exp, final long max, final long s) {
		this();
		this.experimentID = expId;
		this.sourcePath = sourcePath;
		this.finalStep = max;
		this.experimentName = exp;
		this.seed = s;
		this.modelName=null;
		
	}

	public void loadAndBuild(RuntimeContext rtx) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		this.load(rtx);
		this.listenedVariables = new ListenedVariable[outputs.size()];

		for ( int i = 0; i < parameters.size(); i++ ) {
			Parameter temp = parameters.get(i);
			this.simulator.setParameter(temp.getName(), temp.getValue());
		}
		this.setup();
		simulator.setup(experimentName,this.seed);
		for ( int i = 0; i < outputs.size(); i++ ) {
			Output temp = outputs.get(i);
			this.listenedVariables[i] =
				new ListenedVariable(temp.getName(), temp.getFrameRate(), simulator.getTypeOf(temp.getName()), temp.getOutputPath());
		}

	}

	public void load(RuntimeContext ctx) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		System.setProperty("user.dir", this.sourcePath);
		IModel mdl  = ctx.loadModel(new File(this.sourcePath));
		this.modelName = mdl.getName();
		this.simulator = new RichExperiment(mdl);
	}

	public void setup() {
		this.step = 0;
	
	}

	public void play() {
		if ( this.outputFile != null ) {
			this.outputFile.writeSimulationHeader(this);
		}
		System.out.println("Simulation is running...");
		long startdate = Calendar.getInstance().getTimeInMillis();
		long affDelay = finalStep < 100 ? 1 : finalStep / 100;
		for ( ; step < finalStep; step++ ) {
			if ( step % affDelay == 0 ) {
				System.out.print(".");
			}
		if(simulator.isInterrupted())
				break;
			doStep();
			
		}
		long endDate = Calendar.getInstance().getTimeInMillis();
		this.simulator.dispose();
		if ( this.outputFile != null ) {
			this.outputFile.close();
		}
		System.out.println("\nSimulation duration: " + (endDate - startdate) + "ms");
	}

	public void doStep() {
		this.step=simulator.step();
		this.exportVariables();
	}

	public String getExperimentID() {
		return experimentID;
	}

	public void setExperimentID(final String experimentID) {
		this.experimentID = experimentID;
	}

	private void exportVariables() {
		int size = this.listenedVariables.length;
		if ( size == 0 ) { return; }
		for ( int i = 0; i < size; i++ ) {
			ListenedVariable v = this.listenedVariables[i];
			if ( this.step % v.frameRate == 0 ) {
				RichOutput out = simulator.getRichOutput(v.getName());
				if(out.getValue() == null)
				{
					//LOGGER UNE ERREUR
					//GAMA.reportError(this.  .getCurrentSimulation().getScope(), g, shouldStopSimulation)
				}
				else if(out.getValue() instanceof BufferedImage)
				{
					v.setValue(writeImageInFile((BufferedImage)out.getValue(), v.getName(), v.getPath()),step);
				}
				else
				{
					v.setValue(out.getValue(), out.getStep());
				}
				
			}
		}
		if ( this.outputFile != null ) {
			this.outputFile.writeResultStep(this.step, this.listenedVariables);
		}

	}

	public void initialize() {
		parameters = new Vector<Parameter>();
		outputs = new Vector<Output>();
		if ( simulator != null ) {
			simulator.dispose();
			simulator = null;
		}
	}

	
	public long getStep() {
		return step;
	}
	
	private Display2D writeImageInFile(final BufferedImage img, final String name, final String outputPath) {
		String fileName = name + this.getExperimentID() + "-" + step + ".png";
		String fileFullName = Globals.IMAGES_PATH + "/" + fileName;
		if (outputPath != "" && outputPath != null) {
			// a specific output path has been specified with the "output_path" keyword in the xml
			fileFullName = outputPath + "-" + step + ".png";
			// check if the folder exists, create a new one if it does not
			File tmp = new File(fileFullName);
			tmp.getParentFile().mkdirs();
		}
		try {
			ImageIO.write(img, "png", new File(fileFullName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Display2D(name + this.getExperimentID() + "-" + step + ".png");
	}

	@Override
	public void setSeed(long s) {
		this.seed = s;
	}

	@Override
	public long getSeed() {
		return this.seed;
	}

	public Element asXMLDocument(Document doc)
	{
		Element simulation = doc.createElement(XmlTAG.SIMULATION_TAG);

		Attr attr = doc.createAttribute(XmlTAG.ID_TAG);
		attr.setValue(this.experimentID);
		simulation.setAttributeNode(attr);

		Attr attr3 = doc.createAttribute(XmlTAG.SOURCE_PATH_TAG);
		attr3.setValue(this.sourcePath);
		simulation.setAttributeNode(attr3);

		Attr attr2 = doc.createAttribute(XmlTAG.FINAL_STEP_TAG);
		attr2.setValue(new Long(this.finalStep).toString());
		simulation.setAttributeNode(attr2);

		Attr attr5 = doc.createAttribute(XmlTAG.SEED_TAG);
		attr5.setValue(new Long(this.seed).toString());
		simulation.setAttributeNode(attr5);

		Attr attr4 = doc.createAttribute(XmlTAG.EXPERIMENT_NAME_TAG);
		attr4.setValue(this.experimentName);
		simulation.setAttributeNode(attr4);

		Element parameters = doc.createElement(XmlTAG.PARAMETERS_TAG);
		simulation.appendChild(parameters);

		for(Parameter p:this.parameters)
		{
			Element aparameter = doc.createElement(XmlTAG.PARAMETER_TAG);
			parameters.appendChild(aparameter);
			
			Attr ap1 = doc.createAttribute(XmlTAG.NAME_TAG);
			ap1.setValue(p.getName());
			aparameter.setAttributeNode(ap1);

			Attr ap2 = doc.createAttribute(XmlTAG.TYPE_TAG);
			ap2.setValue(p.getType().toString());
			aparameter.setAttributeNode(ap2);

			Attr ap3 = doc.createAttribute(XmlTAG.VALUE_TAG);
			ap3.setValue(p.getValue().toString());
			aparameter.setAttributeNode(ap3);
		}
		
		Element outputs = doc.createElement(XmlTAG.OUTPUTS_TAG);
		simulation.appendChild(outputs);

		for(Output o:this.outputs)
		{
			Element aOutput = doc.createElement(XmlTAG.OUTPUT_TAG);
			outputs.appendChild(aOutput);
			
			Attr o3 = doc.createAttribute(XmlTAG.ID_TAG);
			o3.setValue(o.getId());
			aOutput.setAttributeNode(o3);
			
			Attr o1 = doc.createAttribute(XmlTAG.NAME_TAG);
			o1.setValue(o.getName());
			aOutput.setAttributeNode(o1);

			Attr o2 = doc.createAttribute(XmlTAG.FRAMERATE_TAG);
			o2.setValue(new Integer(o.getFrameRate()).toString());
			aOutput.setAttributeNode(o2);
		}
		return simulation;
	}

	public static ExperimentJob loadAndBuildJob( final ExperimentDescription expD, final String path, IModel model)
	{
		String expName = expD.getName();
		IExpressionDescription  seedDescription =  expD.getFacets().get(IKeyword.SEED);
		long mseed = 0l;
		if(seedDescription !=null)
		{
			mseed = Long.valueOf(seedDescription.getExpression().literalValue()).longValue();
		}
		IDescription d = expD.getChildWithKeyword(IKeyword.OUTPUT);
		ExperimentJob expJob = new ExperimentJob(path,new Long(ExperimentJob.generateID()).toString(),expName,0,mseed );
		
		if(d != null)
		{
			Iterable<IDescription> monitors = d.getChildrenWithKeyword(IKeyword.MONITOR);
			for(IDescription moni:monitors) {
				expJob.addOutput(Output.loadAndBuildOutput(moni));
			}
			
			Iterable<IDescription> displays = d.getChildrenWithKeyword(IKeyword.DISPLAY);
			for(IDescription disp:displays) {
				expJob.addOutput(Output.loadAndBuildOutput(disp));
			}
		}
		
		Iterable<IDescription> parameters = expD.getChildrenWithKeyword(IKeyword.PARAMETER);
		for(IDescription para:parameters) {
			expJob.addParameter(Parameter.loadAndBuildParameter(para, model));
		}
		
		return expJob;
	}
	
	public String getExperimentName() {
		
		return this.experimentName;
	}
	
	private Parameter getParameter(String name) {
		for(Parameter p:parameters)
		{
			if(p.getName().equals(name))
					return p;
		}
		return null;
	}
	
	public List<Parameter> getParameters()
	{
		return this.parameters;
	}
	
	private Output getOutput(String name) {
		for(Output p:outputs)
		{
			if(p.getName().equals(name))
					return p;
		}
		return null;
	}
	
	public List<Output> getOutputs()
	{
		return this.outputs;
	}
	
	
	public void setParameterValueOf(final String name, final Object val){
		this.getParameter(name).setValue(val);	
	}
	
	public void removeOutputWithName(final String name){
		this.outputs.remove(this.getOutput(name));
	}
	
	public void setOutputFrameRate(final String name,final int frameRate){
		this.getOutput(name).setFrameRate(frameRate);
	}
	
	public List<String> getOutputNames()
	{
		List<String> res = new ArrayList<>();
		for(Output o: outputs)
			res.add(o.getName());
		return res;
	}
	public long getFinalStep() {
		return finalStep;
	}

	public void setFinalStep(long finalStep) {
		this.finalStep = finalStep;
	}

	@Override
	public String getModelName() {
		return this.modelName;
	}

	
}
