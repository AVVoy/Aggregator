package main.java.model;


import main.java.vo.Vacancy;

import java.util.List;

public interface Strategy {

    List<Vacancy> getVacancies(String searchString);
}
