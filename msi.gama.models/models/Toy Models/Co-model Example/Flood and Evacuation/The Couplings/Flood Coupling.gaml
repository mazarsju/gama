/**
* Name: flood_coupling
* Author: HUYNH Quang Nghi
* Description: It is NOT supposed to launch. This is the coupling of Flood model. It is used in the "Flood and Evacuation" as an interface. 
* Tags: comodel
*/
model flood_coupling

import "../../../../Toy Models/Flood Simulation/models/Hydrological Model.gaml"
experiment "Coupling Experiment" type: gui
{
	point newSize <- { 0.07, 0.07 };
	cell get_cell_at (geometry p)
	{
		ask simulation
		{
			return cell closest_to p;
		}

	}

	list<cell> get_cell
	{
		return list(cell) where (each.grid_value > 8.0);
	}

	list<buildings> get_buildings
	{
		return list(buildings);
	}

	list<dyke> get_dyke
	{
		return list(dyke);
	}

	output
	{
	}

}