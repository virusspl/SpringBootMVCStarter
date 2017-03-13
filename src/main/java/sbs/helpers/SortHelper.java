package sbs.helpers;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

@Component
public class SortHelper {
	
	public<T extends Comparable<? super T>> TreeSet<T> sortSet(Set<T> set){
		TreeSet<T> tset = new TreeSet<>();
		for(T item : set){
			tset.add(item);
		}
		return tset;
	}
	
}
