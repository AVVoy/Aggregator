package main.java.model;

import main.java.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy{
//https://hh.ru/search/vacancy?text=java+Moscow&salary=&ored_clusters=true&enable_snippets=true&area=1&page=1
//    private final static String URL_FORMAT = "https://hh.ru/search/" +
//        "vacancy?text=java+%s&salary=&ored_clusters=true&enable_snippets=true&area=1&page=%d";
private static final String URL_FORMAT = "https://hh.ru/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        int page = 0;
        try {
            do {
                Document doc = getDocument(searchString, page);

                Elements vacanciesHtmlList = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");

                if (vacanciesHtmlList.isEmpty()) break;

                for (Element element : vacanciesHtmlList) {
                    Elements links = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title");
                    Elements locations = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address");
                    Elements companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer");
                    Elements salary = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation");

                    Vacancy vacancy = new Vacancy();
                    vacancy.setSiteName("hh.ru");
                    vacancy.setTitle(links.get(0).text());
                    vacancy.setUrl(links.get(0).attr("href"));
                    vacancy.setCity(locations.get(0).text());
                    vacancy.setCompanyName(companyName.get(0).text());
                    vacancy.setSalary(salary.size() > 0 ? salary.get(0).text() : "");

                    vacancies.add(vacancy);
                }

                page++;
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .referrer("https://hh.ru/")
                .get();
    }
}
