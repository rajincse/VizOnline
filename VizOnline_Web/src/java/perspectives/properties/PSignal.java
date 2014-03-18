package perspectives.properties;

import perspectives.base.PropertyType;

public class PSignal extends PropertyType {

	@Override
	public PSignal copy() {
		return new PSignal();
	}

	@Override
	public String typeName() {
		return "PSignal";
	}

	@Override
	public String serialize() {
		return "";
	}

	@Override
	public PropertyType deserialize(String s) {
		return new PSignal();
	}

}
