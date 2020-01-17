package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GradController {
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<Drzava> choiceDrzava;
    public ChoiceBox choiceTipGrada;
    public ObservableList<Drzava> listDrzave;
    public ArrayList<String> lista = new ArrayList<>();
    public ObservableList<String> listaZaChoiceBox;
    private Grad grad;

    public GradController(Grad grad, ArrayList<Drzava> drzave) {
        this.grad = grad;
        listDrzave = FXCollections.observableArrayList(drzave);
        lista.add("Razvijen");
        lista.add("Srednje razvijen");
        lista.add("Nerazvijen");
        listaZaChoiceBox = FXCollections.observableArrayList(lista);

    }

    @FXML
    public void initialize() {
        choiceDrzava.setItems(listDrzave);
        choiceTipGrada.setItems(listaZaChoiceBox);
        choiceTipGrada.setValue("Razvijen");
        if (grad != null) {
            fieldNaziv.setText(grad.getNaziv());
            fieldBrojStanovnika.setText(Integer.toString(grad.getBrojStanovnika()));
            // choiceDrzava.getSelectionModel().select(grad.getDrzava());
            // ovo ne radi jer grad.getDrzava() nije identički jednak objekat kao član listDrzave
            for (Drzava drzava : listDrzave)
                if (drzava.getId() == grad.getDrzava().getId())
                    choiceDrzava.getSelectionModel().select(drzava);
            if (grad instanceof SrednjeRazvijeniGrad)
                choiceTipGrada.setValue("Srednje razvijen");
            else if (grad instanceof NerazvijeniGrad)
                choiceTipGrada.setValue("Nerazvijen");
        } else {
            choiceDrzava.getSelectionModel().selectFirst();
        }
    }

    public Grad getGrad() {
        return grad;
    }

    public void clickCancel(ActionEvent actionEvent) {
        grad = null;
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }

    public void clickOk(ActionEvent actionEvent) {
        boolean sveOk = true;

        if (fieldNaziv.getText().trim().isEmpty()) {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        }


        int brojStanovnika = 0;
        try {
            brojStanovnika = Integer.parseInt(fieldBrojStanovnika.getText());
        } catch (NumberFormatException e) {
            // ...
        }
        if (brojStanovnika <= 0) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
        }

        if (!sveOk) return;

        // Čuvamo id da bismo mogli kreirati novi Grad
        int idGrada = 0;
        if (grad != null) idGrada = grad.getId();

        if (choiceTipGrada.getValue().equals("Razvijen"))
            grad = new RazvijeniGrad(idGrada, fieldNaziv.getText(), Integer.parseInt(fieldBrojStanovnika.getText()),choiceDrzava.getValue());
        else if (choiceTipGrada.getValue().equals("Srednje razvijen"))
            grad = new SrednjeRazvijeniGrad(idGrada, fieldNaziv.getText(), Integer.parseInt(fieldBrojStanovnika.getText()),choiceDrzava.getValue());
        else
            grad = new NerazvijeniGrad(idGrada, fieldNaziv.getText(), Integer.parseInt(fieldBrojStanovnika.getText()),choiceDrzava.getValue());
//        grad.setId(idGrada);
//        grad.setNaziv(fieldNaziv.getText());
//        grad.setBrojStanovnika(Integer.parseInt(fieldBrojStanovnika.getText()));
//        grad.setDrzava(choiceDrzava.getValue());
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }
}
