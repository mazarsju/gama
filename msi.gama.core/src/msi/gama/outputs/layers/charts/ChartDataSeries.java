package msi.gama.outputs.layers.charts;

import java.util.ArrayList;
import java.util.HashMap;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.runtime.IScope;
import msi.gama.util.GamaColor;
import msi.gama.util.GamaList;
import msi.gama.util.IList;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;

public class ChartDataSeries {

	ArrayList<String> cvalues = new ArrayList<String>(); // for categories
	ArrayList<Double> xvalues = new ArrayList<Double>(); // for xy charts
	ArrayList<Double> yvalues = new ArrayList<Double>();
	ArrayList<Double> svalues = new ArrayList<Double>(); // for marker sizes or
															// 3d charts
	ArrayList<Double> xerrvaluesmax = new ArrayList<Double>();
	ArrayList<Double> yerrvaluesmax = new ArrayList<Double>();
	ArrayList<Double> xerrvaluesmin = new ArrayList<Double>();
	ArrayList<Double> yerrvaluesmin = new ArrayList<Double>();
	ArrayList<Double> yvaluemax = new ArrayList<Double>(); // for box and
															// whisker
	ArrayList<Double> yvaluemin = new ArrayList<Double>(); // for box and
															// whisker

	GamaColor mycolor, mymincolor, mymedcolor;

	// HashMap<String,Object> serieParameters=new HashMap<String,Object>();
	ChartDataSource mysource;
	ChartDataSet mydataset;

	String name;

	boolean ongoing_update = false;

	ArrayList<String> oldcvalues = new ArrayList<String>(); // for categories
	ArrayList<Double> oldxvalues = new ArrayList<Double>(); // for xy charts
	ArrayList<Double> oldyvalues = new ArrayList<Double>();
	ArrayList<Double> oldsvalues = new ArrayList<Double>(); // for marker sizes
															// or 3d charts
	ArrayList<Double> oldxerrvaluesmax = new ArrayList<Double>();
	ArrayList<Double> oldyerrvaluesmax = new ArrayList<Double>();
	ArrayList<Double> oldxerrvaluesmin = new ArrayList<Double>();
	ArrayList<Double> oldyerrvaluesmin = new ArrayList<Double>();
	ArrayList<Double> oldyvaluemax = new ArrayList<Double>(); // for box and
																// whisker
	ArrayList<Double> oldyvaluemin = new ArrayList<Double>(); // for box and
																// whisker

	public boolean isOngoing_update() {
		return ongoing_update;
	}

	public ChartDataSet getDataset() {
		return mydataset;
	}

	public void setDataset(final ChartDataSet mydataset) {
		this.mydataset = mydataset;
	}

	public String getName() {
		return name;
	}

	public ChartDataSource getMysource() {
		return mysource;
	}

	public void setMysource(final ChartDataSource mysource) {
		this.mysource = mysource;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Comparable getSerieLegend(final IScope scope) {
		// TODO Auto-generated method stub
		return name;
	}

	public String getSerieId(final IScope scope) {
		// TODO Auto-generated method stub
		return name;
	}

	public String getStyle(final IScope scope) {
		return this.getMysource().getStyle(scope);
	}

	public GamaColor getMycolor() {
		return mycolor;
	}

	public GamaColor getMyMedcolor() {
		return mymedcolor;
	}

	public GamaColor getMyMincolor() {
		return mymincolor;
	}

	public void setMycolor(final GamaColor mycolor) {
		this.mycolor = mycolor;
	}

	public void setMyMedcolor(final GamaColor mycolor) {
		this.mymedcolor = mycolor;
	}

	public void setMyMincolor(final GamaColor mycolor) {
		this.mymincolor = mycolor;
	}

	public boolean isUseYErrValues() {
		return this.getMysource().useYErrValues;
	}

	public void setUseYErrValues(final boolean useYErrValues) {
		this.getMysource().useYErrValues = useYErrValues;
	}

	public boolean isUseXErrValues() {
		return this.getMysource().useXErrValues;
	}

	public void setUseXErrValues(final boolean useXErrValues) {
		this.getMysource().useXErrValues = useXErrValues;
	}

	public boolean isUseYMinMaxValues() {
		return this.getMysource().useYMinMaxValues;
	}

	public void setUseYMinMaxValues(final boolean useYMinMaxValues) {
		this.getMysource().useYMinMaxValues = useYMinMaxValues;
	}

	public ArrayList<String> getCValues(final IScope scope) {
		// TODO Auto-generated method stub
		if (isOngoing_update())
			return oldcvalues;
		return cvalues;
	}

	public ArrayList<Double> getXValues(final IScope scope) {
		// TODO Auto-generated method stub
		if (isOngoing_update())
			return oldxvalues;
		return xvalues;
	}

	public ArrayList<Double> getYValues(final IScope scope) {
		// TODO Auto-generated method stub
		if (isOngoing_update())
			return oldyvalues;
		return yvalues;
	}

	public ArrayList<Double> getSValues(final IScope scope) {
		// TODO Auto-generated method stub
		if (isOngoing_update())
			return oldsvalues;
		return svalues;
	}

	/*
	 * public void addxysvalue(double dx, double dy, double dz, int date) { //
	 * TODO Auto-generated method stub xvalues.add(dx); yvalues.add(dy);
	 * svalues.add(dz);
	 * this.getDataset().serieToUpdateBefore.put(this.getName(), date);
	 * 
	 * }
	 */
	public void clearValues(final IScope scope) {
		// TODO Auto-generated method stub

		oldcvalues = cvalues;
		oldxvalues = xvalues;
		oldyvalues = yvalues;
		oldsvalues = svalues;
		oldxerrvaluesmax = xerrvaluesmax;
		oldyerrvaluesmax = yerrvaluesmax;
		oldxerrvaluesmin = xerrvaluesmin;
		oldyerrvaluesmin = yerrvaluesmin;
		oldyvaluemax = yvaluemax;
		oldyvaluemin = yvaluemin;

		cvalues = new ArrayList<String>(); // for xy charts
		xvalues = new ArrayList<Double>(); // for xy charts
		yvalues = new ArrayList<Double>();
		svalues = new ArrayList<Double>(); // for marker sizes or 3d charts
		xerrvaluesmax = new ArrayList<Double>();
		yerrvaluesmax = new ArrayList<Double>();
		xerrvaluesmin = new ArrayList<Double>();
		yerrvaluesmin = new ArrayList<Double>();
		yvaluemax = new ArrayList<Double>(); // for box and whisker
		yvaluemin = new ArrayList<Double>(); // for box and whisker

	}

	private Object getlistvalue(final IScope scope, final HashMap barvalues, final String valuetype,
			final int listvalue) {
		// TODO Auto-generated method stub
		if (!barvalues.containsKey(valuetype))
			return null;
		boolean uselist = true;
		if (listvalue < 0)
			uselist = false;
		final Object oexp = barvalues.get(valuetype);
		Object o = oexp;
		if (oexp instanceof IExpression)
			o = ((IExpression) oexp).value(scope);

		if (!uselist)
			return o;

		if (o instanceof GamaList) {
			final IList ol = Cast.asList(scope, o);
			if (ol.size() < listvalue)
				return null;
			return ol.get(listvalue);

		} else {
			return o;
		}

	}

	public void addxysvalue(final IScope scope, final double dx, final double dy, final double ds, final int date,
			final HashMap barvalues, final int listvalue) {

		svalues.add(ds);
		addxyvalue(scope, dx, dy, date, barvalues, listvalue);

	}

	public void addxyvalue(final IScope scope, final double dx, final double dy, final int date,
			final HashMap barvalues, final int listvalue) {
		// TODO Auto-generated method stub
		xvalues.add(dx);
		yvalues.add(dy);
		if (barvalues.containsKey(IKeyword.COLOR)) {
			final Object o = getlistvalue(scope, barvalues, IKeyword.COLOR, listvalue);
			if (o != null) {
				if (o instanceof GamaList) {
					final IList ol = Cast.asList(scope, o);
					if (ol.size() == 1)
						this.setMycolor(Cast.asColor(scope, ol.get(0)));
					if (ol.size() == 2) {
						this.setMycolor(Cast.asColor(scope, ol.get(1)));
						this.setMyMincolor(Cast.asColor(scope, ol.get(0)));
					}
					if (ol.size() > 2) {
						this.setMyMincolor(Cast.asColor(scope, ol.get(0)));
						this.setMyMedcolor(Cast.asColor(scope, ol.get(1)));
						this.setMycolor(Cast.asColor(scope, ol.get(2)));
					}
				} else {
					final GamaColor col = Cast.asColor(scope, o);
					this.setMycolor(col);

				}

			}

		}
		if (barvalues.containsKey(ChartDataStatement.MARKERSIZE)) {
			final Object o = getlistvalue(scope, barvalues, ChartDataStatement.MARKERSIZE, listvalue);
			if (o != null) {
				if (svalues.size() > xvalues.size())
					svalues.remove(svalues.get(svalues.size() - 1));
				svalues.add(Cast.asFloat(scope, o));
			}

		}
		if (this.isUseYErrValues()) {
			final Object o = getlistvalue(scope, barvalues, ChartDataStatement.YERR_VALUES, listvalue);
			if (o != null) {
				if (o instanceof GamaList) {
					final IList ol = Cast.asList(scope, o);
					if (ol.size() > 1) {
						this.yerrvaluesmin.add(Cast.asFloat(scope, ol.get(0)));
						this.yerrvaluesmax.add(Cast.asFloat(scope, ol.get(1)));

					} else {
						this.yerrvaluesmin.add(dy - Cast.asFloat(scope, ol.get(0)));
						this.yerrvaluesmax.add(dy + Cast.asFloat(scope, ol.get(0)));
					}
				} else {
					this.yerrvaluesmin.add(dy - Cast.asFloat(scope, o));
					this.yerrvaluesmax.add(dy + Cast.asFloat(scope, o));

				}
			}

		}
		if (this.isUseXErrValues()) {
			final Object o = getlistvalue(scope, barvalues, ChartDataStatement.XERR_VALUES, listvalue);
			if (o != null) {
				if (o instanceof GamaList) {
					final IList ol = Cast.asList(scope, o);
					if (ol.size() > 1) {
						this.xerrvaluesmin.add(Cast.asFloat(scope, ol.get(0)));
						this.xerrvaluesmax.add(Cast.asFloat(scope, ol.get(1)));

					} else {
						this.xerrvaluesmin.add(dx - Cast.asFloat(scope, ol.get(0)));
						this.xerrvaluesmax.add(dx + Cast.asFloat(scope, ol.get(0)));
					}
				} else {
					this.xerrvaluesmin.add(dx - Cast.asFloat(scope, o));
					this.xerrvaluesmax.add(dx + Cast.asFloat(scope, o));

				}
			}

		}

		this.getDataset().serieToUpdateBefore.put(this.getName(), date);

	}

	public void addcysvalue(final IScope scope, final String dx, final double dy, final double ds, final int date,
			final HashMap barvalues, final int listvalue) {

		svalues.add(ds);
		addcyvalue(scope, dx, dy, date, barvalues, listvalue);

	}

	public void addcyvalue(final IScope scope, final String dx, final double dy, final int date,
			final HashMap barvalues, final int listvalue) {
		cvalues.add(dx);
		yvalues.add(dy);
		if (barvalues.containsKey(IKeyword.COLOR)) {
			final Object o = getlistvalue(scope, barvalues, IKeyword.COLOR, listvalue);
			if (o != null) {
				if (o instanceof GamaList) {
					final IList ol = Cast.asList(scope, o);
					if (ol.size() == 1)
						this.setMycolor(Cast.asColor(scope, ol.get(0)));
					if (ol.size() == 2) {
						this.setMycolor(Cast.asColor(scope, ol.get(1)));
						this.setMyMincolor(Cast.asColor(scope, ol.get(0)));
					}
					if (ol.size() > 2) {
						this.setMyMincolor(Cast.asColor(scope, ol.get(0)));
						this.setMyMedcolor(Cast.asColor(scope, ol.get(1)));
						this.setMycolor(Cast.asColor(scope, ol.get(2)));
					}
				} else {
					final GamaColor col = Cast.asColor(scope, o);
					this.setMycolor(col);

				}
			}

		}
		if (barvalues.containsKey(ChartDataStatement.MARKERSIZE)) {
			final Object o = getlistvalue(scope, barvalues, ChartDataStatement.MARKERSIZE, listvalue);
			if (o != null) {
				if (svalues.size() > xvalues.size())
					svalues.remove(svalues.get(svalues.size() - 1));
				svalues.add(Cast.asFloat(scope, o));
			}

		}
		if (this.isUseYErrValues()) {
			final Object o = getlistvalue(scope, barvalues, ChartDataStatement.YERR_VALUES, listvalue);
			if (o != null) {
				if (o instanceof GamaList) {
					final IList ol = Cast.asList(scope, o);
					if (ol.size() > 1) {
						this.yerrvaluesmin.add(Cast.asFloat(scope, ol.get(0)));
						this.yerrvaluesmax.add(Cast.asFloat(scope, ol.get(1)));

					} else {
						this.yerrvaluesmin.add(dy - Cast.asFloat(scope, ol.get(0)));
						this.yerrvaluesmax.add(dy + Cast.asFloat(scope, ol.get(0)));
					}
				} else {
					this.yerrvaluesmin.add(dy - Cast.asFloat(scope, o));
					this.yerrvaluesmax.add(dy + Cast.asFloat(scope, o));

				}
			}

		}

		this.getDataset().serieToUpdateBefore.put(this.getName(), date);

	}

	public void endupdate(final IScope scope) {
		// TODO Auto-generated method stub
		this.ongoing_update = false;
	}

	public void startupdate(final IScope scope) {
		// TODO Auto-generated method stub
		this.ongoing_update = true;
	}

	public void savelistd(final IScope scope, final StringBuilder history, final ArrayList<Double> mylist) {
		if (mylist.size() == 0) {
			history.append(",");
			return;
		}
		for (int i = 0; i < mylist.size(); i++) {
			history.append(Cast.asFloat(scope, mylist.get(i)).floatValue() + ",");
		}

	}

	public void savelists(final IScope scope, final StringBuilder history, final ArrayList mylist) {
		// history.append("[");
		if (mylist.size() == 0) {
			// history.append("],");
			return;
		}
		for (int i = 0; i < mylist.size(); i++) {
			history.append(Cast.asString(scope, mylist.get(i)) + ",");
		}
		// history.append("],");

	}

	public void savehistory(final IScope scope, final StringBuilder history) {
		// TODO Auto-generated method stub
		// StringBuilder history=this.getDataset().getHistory();
		history.append(this.getName() + ",");
		if (mysource.isByCategory()) {
			if (this.cvalues.size() > 0) {
				if (this.getMysource().isCumulative)
					history.append(this.cvalues.get(this.cvalues.size() - 1) + ",");
				else
					savelists(scope, history, this.cvalues);
			}
		} else {
			if (this.xvalues.size() > 0) {
				if (this.getMysource().isCumulative)
					history.append(this.xvalues.get(this.xvalues.size() - 1) + ",");
				else
					savelistd(scope, history, this.xvalues);

			}
		}
		if (this.yvalues.size() > 0) {
			if (this.getMysource().isCumulative)
				history.append(this.yvalues.get(this.yvalues.size() - 1) + ",");
			else
				savelistd(scope, history, this.yvalues);

		}
		if (this.svalues.size() > 0)
			if (this.svalues.size() >= this.yvalues.size()) {
				if (this.getMysource().isCumulative)
					history.append(this.svalues.get(this.svalues.size() - 1) + ",");
				else
					savelistd(scope, history, this.svalues);

			}
	}

	public void inithistory(final StringBuilder history) {
		// TODO Auto-generated method stub
		// StringBuilder history=this.getDataset().getHistory();
		history.append(this.getName() + "_X" + ",");
		history.append(this.getName() + "_Y" + ",");
	}

}
