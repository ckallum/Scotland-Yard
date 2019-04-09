package uk.ac.bris.cs.fxkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

// TODO we seriously need to move on to annotations!
public interface Controller extends Initializable {

	// static Default DEFAULT = new Default();

	class Default {
		private static ResourceBundle RESOURCE_BUNDLE = null;
		private static String cssPath = null;
	}

	static void setResourceBundle(ResourceBundle bundle) {
		Default.RESOURCE_BUNDLE = bundle;
	}


	static void setGlobalCSS(String cssPath) {
		Default.cssPath = cssPath;
	}

	static void bind(Controller controller) {
		BindFXML bind = controller.getClass().getAnnotation(BindFXML.class);
		if (bind == null) throw new IllegalArgumentException("@BindFXML annotation not found");
		Controller.bind(bind.value(), "NULL".equals(bind.css()) ? null : bind.css(), controller);
	}

	static void bind(String fxmlPath, Controller controller) {
		bind(fxmlPath, null, controller);
	}


	/**
	 * Loads the main view and inject all fields annotated with {@link javafx.fxml.FXML}
	 *
	 * @param fxmlPath the FXML file path
	 * @param cssPath optional CSS file, will be loaded after the view is injected
	 * @param controller the controller object
	 */
	static void bind(String fxmlPath, String cssPath, Controller controller) {
		FXMLLoader loader = new FXMLLoader();
		if (Default.RESOURCE_BUNDLE != null)
			loader.setResources(Default.RESOURCE_BUNDLE);
		loader.setRoot(controller.root());
		loader.setController(controller);

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			InputStream stream = classLoader
					                     .getResourceAsStream(fxmlPath);
			if (stream == null)
				throw new IllegalArgumentException("Unable to find " + fxmlPath);
			loader.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cssPath = cssPath == null ? Default.cssPath : cssPath;
		if (cssPath != null && controller.root() != null) {
			controller.root().getStylesheets().add(toExternalString(classLoader, cssPath));
		}
	}

	static String toExternalString(ClassLoader loader, String path) {
		return loader.getResource(path).toExternalForm();
	}

	/**
	 * get the root view in the controller, this will be the enclosing view for
	 * all children
	 *
	 * @return a subclass of {@link Parent}
	 */
	Parent root();

	@Override
	default void initialize(URL location, ResourceBundle resources) {

	}
}
