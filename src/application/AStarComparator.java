package application;

import java.util.Comparator;

public class AStarComparator implements Comparator<MapNode>{

	@Override
	public int compare(MapNode o1, MapNode o2) {
		// TODO Auto-generated method stub
		if((int)o1.f - (int)o2.f == 0) {
			return (int)o1.h - (int)o2.h;
		}
		return (int)o1.f - (int)o2.f;
	}

}
