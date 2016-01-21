package com.infinitybas.slfx;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Service
class SLFXImpl implements SLFX {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SLFX.class);

	public static Stage primaryStage;

	@Autowired
	private FXMLLoaderService loader;

	private HashMap<String, Scene> sceneCache = new HashMap<>();

	public void show(Intent intent) {

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

			loader.registerIntent(resource, intent);

			primaryStage.setScene(sceneCache.get(resource));

		} catch (IOException e) {
			log.error("Exception occurred trying to execute intent", e);
		}
	}

	public void setPrimaryStage(Stage stage) {
		SLFXImpl.primaryStage = stage;
	}

}
