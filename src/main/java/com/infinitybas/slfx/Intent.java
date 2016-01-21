package com.infinitybas.slfx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Intent {
	
	private Optional<Class<?>> controller = Optional.empty();
	private Optional<String> fxml = Optional.empty();
	
	private Map<Class<?>, HashMap<String, Object>> extrasByClass;
	
	public Intent() {
		extrasByClass = new HashMap<Class<?>, HashMap<String,Object>>();
	}
	
	public Intent(Class<?> controller) {
		this();
		this.setController(controller);
	}
	
	public Intent(String fxml) {
		this();
		this.setFxml(fxml);
	}

	public Optional<Class<?>> getController() {
		return controller;
	}
	
	public <T> Intent withExtra(String name, T extra) {
		Class<?> c = extra.getClass();
		
		if(!extrasByClass.containsKey(c)) {
			extrasByClass.put(c, new HashMap<String, Object>());
		}
		
		Map<String, Object> extras = extrasByClass.get(c);
		
		extras.put(name, extra);
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getExtra(Class<?> clazz, String name) {

		if(extrasByClass.containsKey(clazz) && extrasByClass.get(clazz).containsKey(name)) {
			return Optional.of((T)extrasByClass.get(clazz).get(name));
		} else {
			return Optional.empty();
		}
	}
	
	public boolean isComplete() {
		return getController().isPresent() && getFxml().isPresent();
	}

	public void setFxml(String fxml) {
		this.fxml = Optional.of(fxml);
	}

	public void setController(Class<?> controller) {
		this.controller = Optional.of(controller);
		this.setFxml(getFxmlResource(controller));
	}

	public Optional<String> getFxml() {
		return fxml;
	}
	
	private String getFxmlResource(Class<?> controller) {
		SLFXControllerFor[] annotations = controller.getDeclaredAnnotationsByType(SLFXControllerFor.class);

		if (annotations.length == 0) {
			throw new IllegalArgumentException(String.format(
					"\n\n\tAttempted to retrieve FXML for an un-annotated controller [%s]. "
					+ "Controllers must be annotated, i.e.\n\t\t@SLFXControllerFor(\"view.fxml\")\n\t\tpublic class %s {\n",
					controller.getName(),
					controller.getSimpleName()));
		}

		String fxml = annotations[0].value();

		return fxml;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<Class<?>, HashMap<String,Object>> entry : extrasByClass.entrySet()) {
			builder.append(String.format("\t%s:\n", entry.getKey().toString()));
			for(Map.Entry<String, Object> inner : entry.getValue().entrySet()) {
				builder.append(String.format("\t\t%s: %s\n", inner.getKey(), inner.getValue().toString()));
			}
		}
		
		String target = getFxml().isPresent() ? getFxml().get() : getController().get().getName();
		
		return String.format("%s for %s\nExtras:\n%s", 
				super.toString(),
				target,
				builder.toString()
				);
	}

}
