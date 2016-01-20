package com.infinitybas.slfx;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Service
public class SLFXImpl implements SLFX {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SLFX.class);

	public static Stage primaryStage;

	@Autowired
	private FXMLLoaderService loader;

	public void show(Intent intent) {
		
		try {
			Parent root = null;
			
			if(intent.getRoot().isPresent())
				root = intent.getRoot().get();
			else if(intent.getFxml().isPresent()) {
				root = loadFXML(intent.getFxml().get(), intent);
			} else if (intent.getController().isPresent()) {
				Class<?> controllerClass = intent.getController().get();
				root = FXMLFor(controllerClass, intent);
			} else {
				log.warn("Intent did not have root, fxml or controller. Doing nothing.");
				return;
			}
			
			primaryStage.setScene(new Scene(root));
			
		} catch (IOException e) {
			log.error("Exception occurred trying to execute intent", e);
		}
	}

	public void setPrimaryStage(Stage stage) {
		SLFXImpl.primaryStage = stage;
	}

	/**
	 * Attempts to load the view from the specified resource string, and cast it
	 * to a scene graph parent.
	 * 
	 * @param fxml
	 *            Resource identifier string
	 * @return A javafx scene graph object
	 * @throws IOException
	 *             if the resource cannot be loaded
	 */
	private Parent loadFXML(String fxml, Intent intent) throws IOException {
		return (Parent) loader.load(fxml, intent);
	}

	/**
	 * Attempts to retrieve the linked FXML resource for the specified
	 * controller class.
	 * 
	 * @param controller
	 *            The controller class.
	 * @return A javafx scene graph object
	 * @throws IOException
	 *             if the resource cannot be loaded
	 * @throws IllegalArgumentException
	 *             if the specified controller has no annotation.
	 */
	private Parent FXMLFor(Class<?> controller, Intent intent) throws IOException {
		FXControllerFor[] annotations = controller.getDeclaredAnnotationsByType(FXControllerFor.class);

		if (annotations.length == 0) {
			throw new IllegalArgumentException("Attempted to retrieve FXML for an un-annotated controller. "
					+ "Controllers must be annotated, i.e. @FXControllerFor(\"view.fxml\")");
		}

		String fxml = annotations[0].value();

		return loadFXML(fxml, intent);
	}
}
