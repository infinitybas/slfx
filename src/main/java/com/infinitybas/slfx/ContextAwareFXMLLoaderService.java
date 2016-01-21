package com.infinitybas.slfx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * This service uses the stock {@link FXMLLoader} to load FXML resources from
 * disk. As a resource is loaded, it's controller factory is set to use Spring's
 * ApplicationContext.getBean(). Credit to Sebastian Hesse for the original
 * idea.
 * 
 * http://sebastianhesse.com/2015/05/26/add-spring-to-javafx/
 */
@Service
public class ContextAwareFXMLLoaderService implements FXMLLoaderService {

	private static Logger log = LoggerFactory.getLogger(ContextAwareFXMLLoaderService.class);

	@Autowired
	private ApplicationContext context;

	private Map<String, IntentAware> controllerCache = new HashMap<>();

	/**
	 * Loads the root FXML file and uses Spring's context to get controllers.
	 *
	 * @param resource
	 *            location of FXML file
	 * @return parent object of FXML layout, see
	 *         {@link FXMLLoader#load(InputStream)}
	 * @throws IOException
	 *             in case of problems with FXML file
	 */
	public Object load(final String resource) throws IOException {
		return load(resource, getClass().getClassLoader());
	}

	/**
	 * Loads the root FXML file and uses Spring's context to get controllers.
	 *
	 * @param resource
	 *            location of FXML file
	 * @param classLoader
	 *            ClassLoader to use, if different from the system class loader.
	 * @return parent object of FXML layout, see
	 *         {@link FXMLLoader#load(InputStream)}
	 * @throws IOException
	 *             in case of problems with FXML file
	 */
	public Object load(final String resource, final ClassLoader classLoader) throws IOException {

		log.debug("Loading {} from {}", resource, classLoader.toString());
		URL location = classLoader.getResource(resource);
		log.debug("{}", location);

		if (location == null)
			throw new IOException(
					String.format("Could not load classpath resource %s. Expected URL, got null.", resource));

		try (InputStream fxmlStream = location.openStream()) {
			FXMLLoader loader = getLoader();
			loader.setLocation(location);
			loader.setClassLoader(classLoader);

			// Load FXML into Parent object
			Object root = loader.load(fxmlStream);
			Object controller = loader.getController();

			if (controller instanceof IntentAware) {
				controllerCache.put(resource, loader.getController());
				if (controller instanceof SLFXController) {
					// Inject root element. This approach means that root
					// doesn't need to be specified with fx:id="root" to be
					// available in controller.
					((SLFXController) controller).setRoot((Parent) root);
				}
			} else {
				log.warn("Controller {} was not an instance of {}", controller.getClass(), IntentAware.class);
			}

			return root;
		} catch (BeansException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Attempts to register the given intent against the controller for the
	 * specified resource. Assumes the controller implements
	 * {@link com.infinitybas.slfx.IntentAware}
	 * 
	 * @param resource
	 *            location of FXML file
	 * @param intent
	 *            the intent to register with the linked controller
	 */
	public boolean registerIntent(final String resource, final Intent intent) {
		if (controllerCache.containsKey(resource)) {
			controllerCache.get(resource).setIntent(intent);
			return true;
		} else {
			log.warn("Loaded {}, but no controller found. Could not inject intent", resource);
			return false;
		}
	}

	private FXMLLoader getLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(context::getBean);
		return loader;
	}
}
