package com.infinitybas.slfx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.scene.Parent;

public class Intent {
	
	private Optional<Class<?>> controller = Optional.empty();
	private Optional<String> fxml = Optional.empty();
	private Optional<Parent> root = Optional.empty();
	
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
	
	public Intent(Parent root) {
		this();
		this.setRoot(root);
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

	public void setFxml(String fxml) {
		this.fxml = Optional.of(fxml);
	}

	public void setRoot(Parent root) {
		this.root = Optional.of(root);
	}

	public void setController(Class<?> controller) {
		this.controller = Optional.of(controller);
	}

	public Optional<String> getFxml() {
		return fxml;
	}

	public Optional<Parent> getRoot() {
		return root;
	}

}
