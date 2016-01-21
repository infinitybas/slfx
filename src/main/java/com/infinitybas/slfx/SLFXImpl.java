package com.infinitybas.slfx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Service
class SLFXImpl implements SLFX {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SLFX.class);

	private Stack<Intent> forward = new Stack<>();
	private Intent current;
	private Stack<Intent> backward = new Stack<>();

	public static Stage primaryStage;

	@Autowired
	private FXMLLoaderService loader;

	private HashMap<String, Scene> sceneCache = new HashMap<>();
	
	private BooleanProperty canGoForward = new SimpleBooleanProperty(false);
	private BooleanProperty canGoBack = new SimpleBooleanProperty(false);

	public void show(Intent intent) {
		
		if(current != null) {
			forward.clear();
			backward.push(current);
		}
		updateBooleans();
		current = intent;

		doShow(intent);
		loader.registerIntent(intent.getFxml().get(), intent);
	}
	
	public void forward() {
		if(!canGoForward.get())
			return;
		
		backward.push(current);
		current = forward.pop();
		updateBooleans();
		doShow(current);
	}
	
	public void back() {
		if(!canGoBack.get())
			return;
		
		forward.push(current);
		current = backward.pop();
		updateBooleans();
		doShow(current);
	}
	
	private void updateBooleans() {
		canGoForward.set(!forward.isEmpty());
		canGoBack.set(!backward.isEmpty());
	}
	
	private void doShow(Intent intent) {
		try {
			String resource = "";
			if (intent.getFxml().isPresent()) {
				resource = intent.getFxml().get();
			} else {
				throw new IllegalStateException(
						"Intent MUST provide an fxml resource string, either verbatim or through an annotated controller.");
			}

			if (!sceneCache.containsKey(resource)) {
				Parent root = (Parent) loader.load(resource);
				sceneCache.put(resource, new Scene(root));
			} else {
				if (log.isDebugEnabled())
					log.debug("{} found in Scene cache, retrieving...", resource);
			}

			primaryStage.setScene(sceneCache.get(resource));

		} catch (IOException e) {
			log.error("Exception occurred trying to execute intent", e);
		}
	}
	
	public ReadOnlyBooleanProperty canGoForward() {
		return canGoForward;
	}
	
	public ReadOnlyBooleanProperty canGoBackward() {
		return canGoBack;
	}

	public void setPrimaryStage(Stage stage) {
		SLFXImpl.primaryStage = stage;
	}

	public Stage getPrimaryStage() {
		return SLFXImpl.primaryStage;
	}

}
