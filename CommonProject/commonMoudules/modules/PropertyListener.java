package modules;


/**class for functional programming for value change action, for more details see "Property&lt;T&gt;"*/ 
public abstract class PropertyListener<T> {

	public PropertyListener() {
	}
	
	public abstract void onChange(T oldVal,T newVal);

}