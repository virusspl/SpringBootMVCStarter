package sbs.helpers;

import org.springframework.stereotype.Component;

@Component
public class MathHelper {

	public MathHelper() {
	}
	
	public int randomInRange(int min, int max){
		return (int)((Math.random() * ((max - min) + 1)) + min);
	}
	
}
