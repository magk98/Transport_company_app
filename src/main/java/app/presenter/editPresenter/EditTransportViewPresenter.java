package app.presenter.editPresenter;

import app.command.TransportSaveCommand;
import app.command.TransportUpdateCommand;
import app.dao.CurrentTransactionDAO;
import app.dao.DriverDAO;
import app.dao.TransactionDAO;
import app.dao.VehicleDAO;
import app.model.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EditTransportViewPresenter extends EditDialogPresenter{

    @FXML
    Spinner hourSpinner;
    @FXML
    Spinner minuteSpinner;
    private ObjectProperty<Driver> driverObjectProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Vehicle> vehicleObjectProperty = new SimpleObjectProperty<>();
    private BooleanProperty dateTimeProperty = new SimpleBooleanProperty();
    private Transport transport;
    @FXML
    private Label transactionIdLabel;
    @FXML
    private Label viewTransactionsLabel;
    @FXML
    private ChoiceBox<String> driverChoiceBox;
    private Map<String, Driver> driverMap = new LinkedHashMap<>();
    @FXML
    private Label viewDriversLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> vehicleChoiceBox;
    private Map<String, Vehicle> vehicleMap = new LinkedHashMap<>();
    @FXML
    private Label viewVehiclesLabel;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;

    @FXML
    private void initialize(){

        acceptButton.disableProperty().bind(dateTimeProperty);
        //driver box init
        driverChoiceBox.valueProperty().addListener((o, ov, nv) -> {
            driverObjectProperty.set(driverMap.get(nv));
        });
        setDriverChoiceBox(null);


        //vehicle box init
        vehicleChoiceBox.valueProperty().addListener((o, ov, nv) -> {
            vehicleObjectProperty.set(vehicleMap.get(nv));
        });
        setVehicleChoiceBox(null);

        datePicker.setValue(LocalDate.now());
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory
                (0, 23, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory
                (0, 59, 30));

        hourSpinner.valueProperty().addListener((o, ov, nv) -> {
            dateTimeProperty.setValue(datePicker.getValue()
                    .atTime((Integer) nv, (Integer) minuteSpinner.getValue()).isBefore(LocalDateTime.now()));
        });

        minuteSpinner.valueProperty().addListener((o, ov, nv) -> {
            dateTimeProperty.setValue(datePicker.getValue()
                    .atTime((Integer) hourSpinner.getValue(), (Integer) nv).isBefore(LocalDateTime.now()));
        });

        datePicker.valueProperty().addListener((o, ov, nv) -> {
            dateTimeProperty.setValue(
                    nv.atTime((Integer) hourSpinner.getValue(), (Integer) minuteSpinner.getValue()).isBefore(LocalDateTime.now()));
        });
    }

    @Override
    public void setOldObject(Object transport){
        this.transport = (Transport) transport;
        transactionIdLabel.setText("Transaction id: " + this.transport.getCurrentTransaction().getTransaction()._id);
        driverChoiceBox.setValue(this.transport.getDriver().getName() + " " + this.transport.getDriver().getPhone());
        datePicker.setValue(this.transport.getDepartureDate().toLocalDate());
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory
                (0, 23, this.transport.getDepartureDate().getHour()));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory
                (0, 59, this.transport.getDepartureDate().getMinute()));
        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);
        vehicleChoiceBox.setValue(this.transport.getVehicle().getRegistrationNo());
    }

    @FXML
    private void handleCancelButtonAction(){
        dialogStage.close();
    }

    private void updateTransport(Driver driver, LocalDateTime dateTime, Vehicle vehicle){
        transport.setDriver(driver);
        transport.setDepartureDate(dateTime);
        transport.setVehicle(vehicle);
    }

    @FXML
    private void handleAcceptButtonAction(){
        Driver driver = driverMap.get(driverChoiceBox.getValue());
        Vehicle vehicle = vehicleMap.get(vehicleChoiceBox.getValue());
        LocalDateTime dateTime = datePicker.getValue()
                .atTime((Integer)hourSpinner.getValue(),(Integer) minuteSpinner.getValue());
        updateTransport(driver, dateTime, vehicle);
        new TransportUpdateCommand(transport).execute();
        dialogStage.close();
    };

    @FXML
    private void handleViewTransLabelAction(){
        Transaction selectedTransaction = appPresenter.showSelectedTransaction(
                transport.getCurrentTransaction().getTransaction(), dialogStage);
    }

    @FXML
    private void handleDatePickerAction(){
//        TODO
    };

    @FXML
    private void handleViewDriversLabelAction(){
        Driver selectedDriver = appPresenter.showSelectedDriver(
                driverMap.get(driverChoiceBox.getValue()), dialogStage
        );
        setDriverChoiceBox(selectedDriver.getName() + ", " + selectedDriver.getPhone());
    }

    @FXML
    private void handleViewVehiclesLabelAction(){
        Vehicle selectedVehicle = appPresenter.showSelectedVehicle(
                vehicleMap.get(vehicleChoiceBox.getValue()), dialogStage
        );
        setVehicleChoiceBox(selectedVehicle.getRegistrationNo());
    }

    private void setDriverChoiceBox(String nameAndPhone){
        DriverDAO driverDAO = new DriverDAO();
        List<Driver> drivers = driverDAO.findAllDrivers();
        driverChoiceBox.getItems().clear();
        for(Driver d: drivers) {
            driverMap.put(d.getName() + ", " + d.getPhone(), d);
            driverChoiceBox.getItems().addAll(d.getName() + ", " + d.getPhone());
        }
        if (drivers.isEmpty()) return;
        driverChoiceBox.setValue(nameAndPhone == null?drivers.get(0).getName() + ", " + drivers.get(0).getPhone():nameAndPhone);
    }

    private void setVehicleChoiceBox(String registrationNo){
        VehicleDAO vehicleDAO = new VehicleDAO();
        List<Vehicle> vehicles = vehicleDAO.findAllVehicles();
        vehicleChoiceBox.getItems().clear();
        for(Vehicle v: vehicles) {
            vehicleMap.put(v.getRegistrationNo(), v);
            vehicleChoiceBox.getItems().add(v.getRegistrationNo());
        }
        if (vehicles.isEmpty()) return;
        vehicleChoiceBox.setValue(registrationNo == null?vehicles.get(0).getRegistrationNo():registrationNo);
    }
}
