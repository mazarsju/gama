package ummisco.gama.network.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import msi.gama.extensions.messaging.GamaMessage;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaMap;

public interface IConnector {
	public void connect(IAgent agent) throws Exception;
	public void close(final IScope scope) throws GamaNetworkException;
	public void send(IAgent agent,String dest, GamaMessage  data) ;
	public List<ConnectorMessage> fetchMessageBox(final IAgent agt);
	public void configure(String parameterName,String value);
	public void joinAGroup(final IAgent agt, final String groupName);
	public void leaveTheGroup(final IAgent agt, final String groupName);
	public Map<IAgent,LinkedList<ConnectorMessage>>  fetchAllMessages();
	
	public final static String SERVER_URL = "SERVER_URL";
	public final static String SERVER_PORT = "SERVER_PORT";
	public final static String LOCAL_NAME = "LOCAL_NAME";
	public final static String LOGIN = "LOGIN";
	public final static String PASSWORD = "PASSWORD";

	
}
