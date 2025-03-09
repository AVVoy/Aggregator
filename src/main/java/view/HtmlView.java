package main.java.view;

import main.java.Controller;
import main.java.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {

    private final String filePath = "./src/" +
            this.getClass().getPackage().getName().replaceAll("[.]", "/") + "/vacancies.html";

    private Controller controller;

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String newText = getUpdatedFileContent(vacancies);
            updateFile(newText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document doc = getDocument();
            Elements templateHidden = doc.getElementsByClass("template");
            Element template = templateHidden.clone().removeAttr("style").removeClass("template").get(0);

            //remove all prev vacancies
            Elements prevVacancies = doc.getElementsByClass("vacancy");

            for (Element redundant : prevVacancies) {
                if (!redundant.hasClass("template")) {
                    redundant.remove();
                }
            }

            //add new vacancies
            for (Vacancy vacancy : vacancies) {
                Element vacancyElement = template.clone();

                Element city = vacancyElement.getElementsByClass("city").get(0);
                city.appendText(vacancy.getCity());
                Element companyName = vacancyElement.getElementsByClass("companyName").get(0);
                companyName.appendText(vacancy.getCompanyName());
                Element salary= vacancyElement.getElementsByClass("salary").get(0);
                salary.appendText(vacancy.getSalary());
                Element vacancyLink = vacancyElement.getElementsByAttribute("href").get(0);
                vacancyLink.appendText(vacancy.getTitle());
                vacancyLink.attr("href", vacancy.getUrl());

                templateHidden.before(vacancyElement.outerHtml());
            }
            return doc.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some exception occurred";
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    private void updateFile(String content) {
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
