package perspectives.base;

import perspectives.base.PropertyType;

public interface PropertyChangeListener {
	abstract void propertyValueChanges(Property p, PropertyType newValue);
	abstract void propertyReadonlyChanges(Property p, boolean newReadOnly);
	abstract void propertyVisibleChanges(Property p, boolean newVisible);
	abstract void propertyPublicChanges(Property p, boolean newPublic);
	abstract void propertyDisabledChanges(Property p,boolean enabled);
}
