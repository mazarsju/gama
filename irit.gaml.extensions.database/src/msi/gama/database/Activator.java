package msi.gama.database;

import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.vividsolutions.jts.geom.Envelope;

import msi.gama.common.interfaces.IEnvelopeComputer;
import msi.gama.common.util.GeometryUtils;
import msi.gama.database.sql.SqlConnection;
import msi.gama.database.sql.SqlUtils;
import msi.gama.metamodel.topology.projection.IProjection;
import msi.gama.runtime.IScope;
import msi.gama.util.IList;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) throws Exception {
		GeometryUtils.addEnvelopeComputer(new IEnvelopeComputer() {

			@Override
			public Envelope computeEnvelopeFrom(final IScope scope, final Object obj) {

				if (!(obj instanceof Map)) {
					return null;
				}
				final Map<String, Object> params = (Map<String, Object>) obj;
				final String crs = (String) params.get("crs");
				final String srid = (String) params.get("srid");
				final Boolean longitudeFirst = params.containsKey("longitudeFirst")
						? (Boolean) params.get("longitudeFirst") : true;
				SqlConnection sqlConn;
				Envelope env = null;
				// create connection
				sqlConn = SqlUtils.createConnectionObject(scope, params);
				// get data
				final IList gamaList = sqlConn.selectDB(scope, (String) params.get("select"));
				env = SqlConnection.getBounds(gamaList);

				// scope.getGui().debug("GeometryUtils.computeEnvelopeFromSQLData.Before
				// Projection:" + env);

				IProjection gis;
				gis = scope.getSimulationScope().getProjectionFactory().fromParams(params, env);
				env = gis.getProjectedEnvelope();

				// scope.getGui().debug("GeometryUtils.computeEnvelopeFromSQLData.After
				// Projection:" + env);
				return env;
				// ----------------------------------------------------------------------------------------------------

			}
		});

	}

	@Override
	public void stop(final BundleContext context) throws Exception {
	}

}
