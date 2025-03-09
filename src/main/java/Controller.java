package main.java;


import main.java.model.Model;

public class Controller {

    private Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException("Illegal model's data");
        this.model = model;
    }

    public void onCitySelect(String cityName) {
        model.selectCity(cityName);
    }
}
