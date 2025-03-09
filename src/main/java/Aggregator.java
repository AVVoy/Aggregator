package main.java;


import main.java.model.HHStrategy;
import main.java.model.HabrCareerStrategy;
import main.java.model.Model;
import main.java.model.Provider;
import main.java.view.HtmlView;

public class Aggregator {

    public static void main(String[] args) {
        HtmlView view = new HtmlView();
        Model model = new Model(view, new Provider(new HHStrategy()), new Provider(new HabrCareerStrategy()));
        Controller controller = new Controller(model);

        view.setController(controller);

        view.userCitySelectEmulationMethod();
    }
}
