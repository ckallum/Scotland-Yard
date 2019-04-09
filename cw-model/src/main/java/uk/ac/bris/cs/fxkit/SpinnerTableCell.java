package uk.ac.bris.cs.fxkit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;

/**
 * Table cell with persistent Spinner
 *
 * @param <S> originating model type
 */
public class SpinnerTableCell<S> extends TableCell<S, Number> {

    private final Spinner<Integer> spinner;
    private IntegerProperty sourceObservable;

    public SpinnerTableCell(int min, int max) {
        spinner = new Spinner<>(min, max, min);
        spinner.disableProperty().bind(disabledProperty());
        setGraphic(spinner);
    }


    @Override
    public void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            return;
        }
        ObjectProperty<Integer> spinnerObservable = spinner.getValueFactory().valueProperty();
        if (sourceObservable != null) sourceObservable.unbind();
        spinnerObservable.setValue(item.intValue());
        sourceObservable = getObservable();
        sourceObservable.bind(spinnerObservable);
        setGraphic(spinner);
        setText(null);
    }

    private IntegerProperty getObservable() {
        return (IntegerProperty) getTableColumn().getCellObservableValue(getIndex());
    }

}
