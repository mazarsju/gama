package ummisco.gama.serializer.gamaType.converters;

import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import msi.gama.util.graph.GamaGraph;
import msi.gama.util.graph.IGraph;
import msi.gama.util.matrix.GamaMatrix;
import ummisco.gama.serializer.gamaType.reduced.GamaGraphReducer;
import ummisco.gama.serializer.gamaType.reduced.GamaMatrixReducer;

public class GamaGraphConverter implements Converter {

	ConverterScope convertScope;
	
	public GamaGraphConverter(ConverterScope s){
		convertScope = s;
	}
	
	@Override
	public boolean canConvert(Class arg0) {		
		List allInterfaceApa = ClassUtils.getAllInterfaces(arg0);
		
		for(Object i : allInterfaceApa) {
			if(i.equals(IGraph.class))
				return true;
		}
		return false;
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter writer, MarshallingContext arg2) {
		GamaGraph graph = (GamaGraph) arg0;

		System.out.println("ConvertAnother : GamaList " + graph.getClass());			        
		arg2.convertAnother(new GamaGraphReducer(convertScope.getScope(), graph));        		
		System.out.println("END --- ConvertAnother : GamaList ");			        

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext arg1) {
		GamaGraphReducer rmt = (GamaGraphReducer) arg1.convertAnother(null, GamaGraphReducer.class);
		return rmt.constructObject(convertScope.getScope());
	}

}
