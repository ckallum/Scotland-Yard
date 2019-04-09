package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.base.StandardSystemProperty;
import com.google.common.io.ByteStreams;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;
import uk.ac.bris.cs.scotlandyard.ui.Utils;

/**
 * Small controller for bootstrapping the ScenicView debugger
 */
@BindFXML("layout/Debug.fxml")
public final class Debug implements Controller {

	private static final String SCENIC_VIEW_JAR = "scenicView.jar";
	private static final String SCENIC_VIEW_PATH = StandardSystemProperty.USER_DIR.value()
			+ File.separator + SCENIC_VIEW_JAR;

	@FXML private GridPane root;
	@FXML private TextField url;
	@FXML private ProgressIndicator progress;
	@FXML private Button install;
	@FXML private Label status;

	private final Stage stage;
	private final Stage target;

	private Debug(Stage stage, Stage target) {
		Controller.bind(this);
		this.stage = stage;
		this.target = target;
		install.setOnAction(e -> downloadAndInstall());
	}

	private void downloadAndInstall() {
		progress.setVisible(true);
		install.setDisable(true);
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ByteStreams.copy(new URL(url.getText()).openStream(), outputStream);
				ZipInputStream stream = new ZipInputStream(
						new ByteArrayInputStream(outputStream.toByteArray()));
				if (findAndCopy(stream, Paths.get(SCENIC_VIEW_PATH))) progress.setVisible(false);
				return null;
			}
		};

		task.setOnFailed(event -> {
			progress.setVisible(false);
			install.setDisable(false);
			Throwable exception = event.getSource().getException();
			Utils.handleNonFatalException(exception == null
					? new RuntimeException("Unable to download ScenicView") : exception,
					"Unable to install ScenicView");
		});
		task.setOnSucceeded(event -> {
			try {
				stage.close();
				showDebugger(target);
			} catch (Exception e) {
				Utils.handleFatalException(e);
			}
		});
		new Thread(task).start();
	}

	private static boolean findAndCopy(ZipInputStream stream, Path destination) throws IOException {
		ZipEntry entry = stream.getNextEntry();
		while (entry != null) {
			if (Objects.equals(entry.getName(), SCENIC_VIEW_JAR)) {
				Files.copy(stream, destination);
				return true;
			}
			entry = stream.getNextEntry();
		}
		return false;
	}

	public static void showDebugger(Stage stage) throws Exception {
		Path path = Paths.get(SCENIC_VIEW_PATH);
		if (Files.exists(path) && !Files.isDirectory(path)) {
			URLClassLoader loader = new URLClassLoader(new URL[] { path.toUri().toURL() },
					Debug.class.getClassLoader());
			Thread.currentThread().setContextClassLoader(loader);
			Class<?> name = Class.forName("org.scenicview.ScenicView", true, loader);
			Method method = name.getMethod("show", Parent.class);
			method.invoke(null, stage.getScene().getRoot());
		} else {
			Stage dialog = new Stage();
			dialog.setScene(new Scene(new Debug(dialog, stage).root()));
			dialog.showAndWait();
		}
	}

	@Override
	public Parent root() {
		return root;
	}
}
