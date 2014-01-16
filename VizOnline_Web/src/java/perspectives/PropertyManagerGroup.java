package perspectives;

import properties.Property;
import properties.PropertyType;

public abstract class PropertyManagerGroup {
	public abstract <T extends PropertyType> void broadcast(PropertyManager p, Property prop, T newvalue);
}
