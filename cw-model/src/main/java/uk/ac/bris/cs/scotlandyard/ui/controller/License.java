package uk.ac.bris.cs.scotlandyard.ui.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Resources;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.bris.cs.fxkit.BindFXML;
import uk.ac.bris.cs.fxkit.Controller;

/**
 * Controller for the license dialog
 */
@BindFXML("layout/License.fxml")
public final class License implements Controller {

	@FXML private VBox root;
	@FXML private TextArea content;
	@FXML private Button dismiss;

	License(Stage stage) {
		Controller.bind(this);
		try {
			String license = Resources.toString(getClass().getResource("/LICENSE.txt"),
					StandardCharsets.UTF_8);
			content.setText(license);

			dismiss.setOnAction(e -> stage.close());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Parent root() {
		return root;
	}
}
