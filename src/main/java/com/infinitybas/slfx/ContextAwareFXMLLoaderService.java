package com.infinitybas.slfx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
	public Object load(final String resource, final Intent intent) throws IOException {
		return load(resource, intent, getClass().getClassLoader());
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
	public Object load(final String resource, final Intent intent, final ClassLoader classLoader) throws IOException {
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
			
			// If this is one of our managed controllers, inject root.
			// Required to enable 'getScene()' and 'getParent()' methods
			// on controllers. Inject intent for extras.
			if(controller instanceof SLFXController) {
				SLFXController slfx = (SLFXController)controller;
				slfx.setRoot((Parent)root);
				slfx.setIntent(intent);
			}

			return root;
		} catch (BeansException e) {
			throw new RuntimeException(e);
		}
	}

	private FXMLLoader getLoader() {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(context::getBean);
		return loader;
	}
}
