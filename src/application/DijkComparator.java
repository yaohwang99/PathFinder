package application;
import java.util.Comparator;

public class DijkComparator  implements Comparator<MapNode>{//no need to use h
	@Override
	public int compare(MapNode o1, MapNode o2) {
		//sort the pq from small to big
		//return ((int)o1.g - (int)o2.g);
		if ((int)o1.g < (int)o2.g) {
            return -1;
        }
        if ((int)o1.g > (int)o2.g) {
            return 1;
        }
        return 0;
	}

}
