/*********************************************************************************************
 *
 *
 * 'MySqlConnection.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
package msi.gama.database.sql;

import java.sql.*;
import java.util.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.*;
import msi.gama.metamodel.topology.projection.IProjection;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;

/*
 * @Author
 * TRUONG Minh Thai
 * Fredric AMBLARD
 * Benoit GAUDOU
 * Christophe Sibertin-BLANC
 * Created date: 19-Apr-2013
 * Modified:
 *
 * 15-Jan-2014
 * Fix null error of getInsertString methods
 * Fix date/time error of getInsertString methods
 *
 * Last Modified: 15-Jan-2014
 */
public class MySqlConnection extends SqlConnection {

	private static final boolean DEBUG = false; // Change DEBUG = false for release version
	private static final String WKT2GEO = "GeomFromText";
	private static final String PREFIX_TIMESTAMP = "cast('";
	private static final String MID_TIMESTAMP = "' as ";
	private static final String SUPFIX_TIMESTAMP = ")";

	public MySqlConnection() {
		super();
	}

	public MySqlConnection(final String dbName) {
		super(dbName);
	}

	public MySqlConnection(final String venderName, final String database) {
		super(venderName, database);
	}

	public MySqlConnection(final String venderName, final String database, final Boolean transformed) {
		super(venderName, database, transformed);
	}

	public MySqlConnection(final String venderName, final String url, final String port, final String dbName,
		final String userName, final String password) {
		super(venderName, url, port, dbName, userName, password);
	}

	public MySqlConnection(final String venderName, final String url, final String port, final String dbName,
		final String userName, final String password, final Boolean transformed) {
		super(venderName, url, port, dbName, userName, password, transformed);
	}

	@Override
	public Connection connectDB()
		throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			if ( vender.equalsIgnoreCase(MYSQL) ) {
				Class.forName(MYSQLDriver).newInstance();
				conn =
					DriverManager.getConnection("jdbc:mysql://" + url + ":" + port + "/" + dbName, userName, password);
			} else {
				throw new ClassNotFoundException("MySqlConnection.connectDB: The " + vender + " is not supported!");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.toString());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InstantiationException(e.toString());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalAccessException(e.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException(e.toString());
		}
		return conn;

	}

	@Override
	protected IList<IList<Object>> resultSet2GamaList(final ResultSetMetaData rsmd, final ResultSet rs) {
		// TODO Auto-generated method stub
		// convert Geometry in SQL to Geometry type in GeoTool

		IList<IList<Object>> repRequest = GamaListFactory.create(msi.gaml.types.Types.LIST);
		try {
			List<Integer> geoColumn = getGeometryColumns(rsmd);
			int nbCol = rsmd.getColumnCount();
			int i = 1;
			// if ( DEBUG ) {
			// scope.getGui().debug("Number of col:" + nbCol);
			// }
			// if ( DEBUG ) {
			// scope.getGui().debug("Number of row:" + rs.getFetchSize());
			// }
			while (rs.next()) {
				// InputStream inputStream = rs.getBinaryStream(i);
				// if ( DEBUG ) {
				// scope.getGui().debug("processing at row:" + i);
				// }

				IList<Object> rowList = GamaListFactory.create();
				for ( int j = 1; j <= nbCol; j++ ) {
					// check column is geometry column?
					// if ( DEBUG ) {
					// scope.getGui().debug("col " + j + ": " + rs.getObject(j));
					// }
					if ( geoColumn.contains(j) ) {
						// if ( DEBUG ) {
						// scope.getGui().debug("convert at [" + i + "," + j + "]: ");
						// }
						rowList.add(SqlUtils.InputStream2Geometry(rs.getBinaryStream(j)));
					} else {
						rowList.add(rs.getObject(j));
					}
				}
				repRequest.add(rowList);
				i++;
			}
			// if ( DEBUG ) {
			// scope.getGui().debug("Number of row:" + i);
			// }
		} catch (Exception e) {

		}
		return repRequest;

	}

	@Override
	protected List<Integer> getGeometryColumns(final ResultSetMetaData rsmd) throws SQLException {
		// TODO Auto-generated method stub
		int numberOfColumns = rsmd.getColumnCount();
		List<Integer> geoColumn = new ArrayList<Integer>();
		for ( int i = 1; i <= numberOfColumns; i++ ) {
			//
			// if ( DEBUG ) {
			// scope.getGui().debug("col " + i + ": " + rsmd.getColumnName(i));
			// scope.getGui().debug(" - Type: " + rsmd.getColumnType(i));
			// scope.getGui().debug(" - TypeName: " + rsmd.getColumnTypeName(i));
			// scope.getGui().debug(" - size: " + rsmd.getColumnDisplaySize(i));
			//
			// }

			/*
			 * for Geometry
			 * - in MySQL Type: -2/-4 - TypeName: UNKNOWN - size: 2147483647
			 * - In MSSQL Type: -3 - TypeName: geometry - size: 2147483647
			 * - In SQLITE Type: 2004 - TypeName: BLOB - size: 2147483647
			 * - In PostGIS/PostGresSQL Type: 1111 - TypeName: geometry - size: 2147483647
			 * st_asbinary(geom): - Type: -2 - TypeName: bytea - size: 2147483647
			 */
			// Search column with Geometry type
			if ( vender.equalsIgnoreCase(MYSQL) & rsmd.getColumnType(i) == -2 ||
				vender.equalsIgnoreCase(MYSQL) & rsmd.getColumnType(i) == -4 ) {
				geoColumn.add(i);
			}
		}
		return geoColumn;

	}

	@Override
	protected IList<Object> getColumnTypeName(final ResultSetMetaData rsmd) throws SQLException {
		// TODO Auto-generated method stub
		int numberOfColumns = rsmd.getColumnCount();
		IList<Object> columnType = GamaListFactory.create();
		for ( int i = 1; i <= numberOfColumns; i++ ) {
			/*
			 * for Geometry
			 * - in MySQL Type: -2/-4 - TypeName: UNKNOWN - size: 2147483647
			 * - In MSSQL Type: -3 - TypeName: geometry - size: 2147483647
			 * - In SQLITE Type: 2004 - TypeName: BLOB - size: 2147483647
			 * - In PostGIS/PostGresSQL Type: 1111 - TypeName: geometry - size: 2147483647
			 */
			// Search column with Geometry type
			if ( vender.equalsIgnoreCase(MYSQL) & rsmd.getColumnType(i) == -2 ||
				vender.equalsIgnoreCase(MYSQL) & rsmd.getColumnType(i) == -4 ) {
				columnType.add(GEOMETRYTYPE);
			} else {
				columnType.add(rsmd.getColumnTypeName(i).toUpperCase());
			}
		}
		return columnType;

	}

	@Override
	protected String getInsertString(final IScope scope, final Connection conn, final String table_name,
		final IList<Object> cols, final IList<Object> values) throws GamaRuntimeException {
		// TODO Auto-generated method stub
		int col_no = cols.size();
		String insertStr = "INSERT INTO ";
		String selectStr = "SELECT ";
		String colStr = "";
		String valueStr = "";
		// Check size of parameters
		if ( values.size() != col_no ) { throw new IndexOutOfBoundsException(
			"Size of columns list and values list are not equal"); }
		// Get column name
		for ( int i = 0; i < col_no; i++ ) {
			if ( i == col_no - 1 ) {
				colStr = colStr + (String) cols.get(i);
			} else {
				colStr = colStr + (String) cols.get(i) + ",";
			}
		}
		// create SELECT statement string
		selectStr = selectStr + colStr + " FROM " + table_name + " LIMIT 1 ;";

		if ( DEBUG ) {
			scope.getGui().debug("MySqlConnection.getInsertString.select command:" + selectStr);
		}

		try {
			// get column type;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(selectStr);
			ResultSetMetaData rsmd = rs.getMetaData();
			IList<Object> col_Names = getColumnName(rsmd);
			IList<Object> col_Types = getColumnTypeName(rsmd);

			if ( DEBUG ) {
				scope.getGui().debug("list of column Name:" + col_Names);
				scope.getGui().debug("list of column type:" + col_Types);
			}
			// Insert command
			// set parameter value
			valueStr = "";
			IProjection saveGis = getSavingGisProjection(scope);
			for ( int i = 0; i < col_no; i++ ) {
				// Value list begin-------------------------------------------
				if ( values.get(i) == null ) {
					valueStr = valueStr + NULLVALUE;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(GEOMETRYTYPE) ) { // for GEOMETRY type
					// // Transform GAMA GIS TO NORMAL
					// if ( transformed ) {
					// WKTReader wkt = new WKTReader();
					// Geometry geo2 =
					// scope.getTopology().getGisUtils()
					// .inverseTransform(wkt.read(values.get(i).toString()));
					// valueStr = valueStr + WKT2GEO + "('" + geo2.toString() + "')";
					// } else {
					// valueStr = valueStr + WKT2GEO + "('" + values.get(i).toString() + "')";
					// }

					// 23/Jul/2013 - Transform GAMA GIS TO NORMAL
					WKTReader wkt = new WKTReader();
					Geometry geo = wkt.read(values.get(i).toString());
					// System.out.println(geo.toString());
					if ( transformed ) {
						geo = saveGis.inverseTransform(geo);
					}
					// System.out.println(geo.toString());
					valueStr = valueStr + WKT2GEO + "('" + geo.toString() + "')";

				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(CHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(VARCHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(NVARCHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(TEXT) ) { // for String type
					// Correct error string
					String temp = values.get(i).toString();
					temp = temp.replaceAll("'", "''");
					// Add to value:
					valueStr = valueStr + "'" + temp + "'";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(TIMESTAMP) ) {
					valueStr = valueStr + "TIMESTAMP('" + values.get(i).toString() + "')";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(YEAR) ) {
					valueStr = valueStr + "YEAR('" + values.get(i).toString() + "')";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(DATETIME) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + DATETIME +
						SUPFIX_TIMESTAMP;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(DATE) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + DATE +
						SUPFIX_TIMESTAMP;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(TIME) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + TIME +
						SUPFIX_TIMESTAMP;
				} else { // For other type
					valueStr = valueStr + values.get(i).toString();
				}
				if ( i != col_no - 1 ) { // Add delimiter of each value
					valueStr = valueStr + ",";
				}
				// Value list end--------------------------------------------------------

			}
			insertStr = insertStr + table_name + "(" + colStr + ") " + "VALUES(" + valueStr + ")";

			if ( DEBUG ) {
				scope.getGui().debug("MySqlConection.getInsertString:" + insertStr);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw GamaRuntimeException.error("MySqlConection.getInsertString:" + e.toString(), scope);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw GamaRuntimeException.error("MySqlConection.getInsertString:" + e.toString(), scope);
		}

		return insertStr;
	}

	@Override
	protected String getInsertString(final IScope scope, final Connection conn, final String table_name,
		final IList<Object> values) throws GamaRuntimeException {
		// TODO Auto-generated method stub
		String insertStr = "INSERT INTO ";
		String selectStr = "SELECT ";
		String colStr = "";
		String valueStr = "";

		// Get column name
		// create SELECT statement string
		selectStr = selectStr + " * " + " FROM " + table_name + " LIMIT 1 ;";
		if ( DEBUG ) {
			scope.getGui().debug("MySqlConnection.getInsertString.select command:" + selectStr);
		}

		try {
			// get column type;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(selectStr);
			ResultSetMetaData rsmd = rs.getMetaData();
			IList<Object> col_Names = getColumnName(rsmd);
			IList<Object> col_Types = getColumnTypeName(rsmd);
			int col_no = col_Names.size();
			// Check size of parameters
			if ( values.size() != col_Names
				.size() ) { throw new IndexOutOfBoundsException("Size of columns list and values list are not equal"); }

			if ( DEBUG ) {
				scope.getGui().debug("list of column Name:" + col_Names);
				scope.getGui().debug("list of column type:" + col_Types);
			}
			// Insert command
			// set parameter value
			colStr = "";
			valueStr = "";
			for ( int i = 0; i < col_no; i++ ) {
				// Value list begin-------------------------------------------
				if ( values.get(i) == null ) {
					valueStr = valueStr + NULLVALUE;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(GEOMETRYTYPE) ) { // for GEOMETRY type
					// // Transform GAMA GIS TO NORMAL
					// if ( transformed ) {
					// WKTReader wkt = new WKTReader();
					// Geometry geo2 =
					// scope.getTopology().getGisUtils()
					// .inverseTransform(wkt.read(values.get(i).toString()));
					// valueStr = valueStr + WKT2GEO + "('" + geo2.toString() + "')";
					// } else {
					// valueStr = valueStr + WKT2GEO + "('" + values.get(i).toString() + "')";
					// }

					// 23/Jul/2013 - Transform GAMA GIS TO NORMAL
					WKTReader wkt = new WKTReader();
					Geometry geo = wkt.read(values.get(i).toString());
					// System.out.println(geo.toString());
					if ( transformed ) {
						geo = getSavingGisProjection(scope).inverseTransform(geo);
					}
					// System.out.println(geo.toString());
					valueStr = valueStr + WKT2GEO + "('" + geo.toString() + "')";

				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(CHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(VARCHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(NVARCHAR) ||
					((String) col_Types.get(i)).equalsIgnoreCase(TEXT) ) { // for String type
																			// Correct error string
					String temp = values.get(i).toString();
					temp = temp.replaceAll("'", "''");
					// Add to value:
					valueStr = valueStr + "'" + temp + "'";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(TIMESTAMP) ) {
					valueStr = valueStr + "TIMESTAMP('" + values.get(i).toString() + "')";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(YEAR) ) {
					valueStr = valueStr + "YEAR('" + values.get(i).toString() + "')";
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(DATETIME) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + DATETIME +
						SUPFIX_TIMESTAMP;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(DATE) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + DATE +
						SUPFIX_TIMESTAMP;
				} else if ( ((String) col_Types.get(i)).equalsIgnoreCase(TIME) ) {
					valueStr = valueStr + PREFIX_TIMESTAMP + values.get(i).toString() + MID_TIMESTAMP + TIME +
						SUPFIX_TIMESTAMP;
				} else { // For other type
					valueStr = valueStr + values.get(i).toString();
				}
				// Value list end--------------------------------------------------------
				// column list
				colStr = colStr + col_Names.get(i).toString();

				if ( i != col_no - 1 ) { // Add delimiter of each value
					colStr = colStr + ",";
					valueStr = valueStr + ",";
				}
			}

			insertStr = insertStr + table_name + "(" + colStr + ") " + "VALUES(" + valueStr + ")";

			if ( DEBUG ) {
				scope.getGui().debug("MySqlConection.getInsertString:" + insertStr);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw GamaRuntimeException.error("MySqlConection.getInsertString:" + e.toString(), scope);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw GamaRuntimeException.error("MySqlConection.getInsertString:" + e.toString(), scope);
		}

		return insertStr;
	}
}
