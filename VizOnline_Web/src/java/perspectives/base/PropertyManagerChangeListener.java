package perspectives.base;

import perspectives.base.PropertyType;

public interface PropertyManagerChangeListener {
	
	public void propertyValueChanges(PropertyManager pm, Property p, PropertyType newValue);
	public void propertyReadonlyChanges(PropertyManager pm, Property p, boolean newReadOnly);
	public void propertyVisibleChanges(PropertyManager pm, Property p, boolean newVisible);
	public void propertyPublicChanges(PropertyManager pm, Property p, boolean newPublic);
	public void propertyDisabledChanges(PropertyManager pm, Property p,boolean enabled);
	public void propertyAdded(PropertyManager pm, Property p);
	public void propertyRemoved(PropertyManager pm, Property p);

}
