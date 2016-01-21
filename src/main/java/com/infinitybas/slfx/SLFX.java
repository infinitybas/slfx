package com.infinitybas.slfx;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.stage.Stage;

public interface SLFX {

	void show(Intent intent);

	void setPrimaryStage(Stage stage);

	Stage getPrimaryStage();

	void forward();

	void back();

	ReadOnlyBooleanProperty canGoForward();

	ReadOnlyBooleanProperty canGoBackward();

}
