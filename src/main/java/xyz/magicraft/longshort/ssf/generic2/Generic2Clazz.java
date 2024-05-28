package xyz.magicraft.longshort.ssf.generic2;

public class Generic2Clazz<T> {

	Class<T> clazz;
	
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
		

		System.out.println("set clazz: " + clazz.getSimpleName());
	}
	

}
