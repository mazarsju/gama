model example_spatial_operators

global {
	// Parameters
	file shape_file_name_init  <- file('../gis/init.shp') ;
	file shape_file_name_background  <- file('../gis/background.shp');		
	float dying_size min: 100.0  <-10000.0 ; 
	float crossover_size min: 100.0  <- 1000.0;
	float minimum_size min: 100.0 <- 500.0; 
	int time_wthout_co min: 1 <- 7;
	float speed min: 1.0  <- 10.0; 
	float crossover_rate min: 0.1  <- 0.95; 
	float scaling_factor min: 1.001 <- 1.05;
	float angle_rotation_max min: 0.0 <- 45.0;
	int nb_partners_max min: 1  <- 1;
	int max_side_size min: 1 <- 5;  
	int background_size_side min: 20 max: 100 <- 80 ;
	
	// Environment
	geometry shape <- envelope(shape_file_name_background);

	geometry the_background;

	reflex stop when: empty ( people ) {
		do halt;
  	} 
	init {
		create background from: shape_file_name_background ;
		the_background <- ((first (background)).shape).contour;
	}
}

species people topology: topology(shape_file_name_init) {
	rgb color <- rgb ( [ rnd ( 255 ) , rnd ( 255 ) , rnd ( 255 ) ]);
	point location_new_Ag <- nil;
	rgb color_new_Ag <- nil;
	int nb_last_rep <- 0;
		
	reflex evolve {
		nb_last_rep <- nb_last_rep + 1;
		shape <- shape scaled_by scaling_factor;
		shape <- shape rotated_by ((rnd ( 100 * angle_rotation_max))/ 100.0);		
	}
	
	reflex move {
		location <- location + { speed * ( 1 - rnd ( 2 ) ) , speed * ( 1 - rnd ( 2 ) ) };
		if ( (shape.area > dying_size) or (shape intersects the_background)) {
			do die; 
		}
			
	}
	
	reflex crossover when: ( shape.area > crossover_size ) and ( nb_last_rep > time_wthout_co ) { 
		int nb_partners  <- 0;
		list<people> list_people <- shuffle ( people );
		loop p over: list_people {
			if ( p != self ) and ( nb_partners <= nb_partners_max ) and (rnd ( 100 ) < ( crossover_rate * 100 ) ) and ( (p.shape).area > crossover_size ) and ( p . nb_last_rep > time_wthout_co ) and (shape intersects p.shape) {
				nb_partners <- nb_partners + 1;
				geometry new_ag <- (shape inter p.shape);
				if ( new_ag != nil ) and ( new_ag.area > minimum_size ) {
					nb_last_rep <- 0;
					ask p {
						nb_last_rep <- 0;
					}
					create people  {
						color <- (myself.color + p.color) / 2;
						shape <-  convex_hull(new_ag);
					}
				}
			}
		}	
	}
		
	aspect geometry {
		draw shape color: color;
	}
}

species background {
	rgb color <- rgb ([ 255 , 240 , 240 ]);
	aspect geometry {
		draw shape color: color;
	}	
}

experiment example_spatial_operators type: gui {
	parameter 'Path of shapefile to load for the initial agent:' var: shape_file_name_init  category: 'GIS specific' ;
	parameter 'Path of shapefile to load for the background:' var: shape_file_name_background category: 'GIS specific';
	parameter 'Size (area) from which an agent dies:' var: dying_size min: 100.0  category: 'Population'; 
	parameter 'Min size (area) for crossover:' var:crossover_size category: 'Population';
	parameter 'Minimum size (area) of a agent produced by a crossover:' var:minimum_size category: 'Population';
	parameter 'Number of steps without crossing-over for an agent:' var:time_wthout_co category: 'Population';
	parameter 'Agent deplacement speed:' var:speed category: 'Population' ; 
	parameter 'Rate of crossover' var:crossover_rate category: 'Population'; 
	parameter 'Scaling factor for agent geometry (at each step):' var:scaling_factor category: 'Population';
	parameter 'Max rotation angle for agent geometry (at each step):' var:angle_rotation_max category:'Population';
	parameter 'Max number of possible partners for crossing-overs (per step)' var:nb_partners_max category: 'Population' ;
	parameter 'Size max of the initiale side of an agent:' var:max_side_size category: 'Population'; 
	parameter 'Size background side:' var: background_size_side category: 'Population' ;

	output {
		display space_display refresh_every: 1{
			species background aspect: geometry;
			species people aspect: geometry;
		}
	}
}
