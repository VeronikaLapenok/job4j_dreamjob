package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private int nextId = 1;
    private final HashMap<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "intern",
                LocalDateTime.of(2023, 1, 13, 12, 30)));
        save(new Vacancy(0, "Junior Java Developer", "junior",
                LocalDateTime.of(2023, 3, 18, 13, 9)));
        save(new Vacancy(0, "Junior+ Java Developer", "junior+",
                LocalDateTime.of(2023, 5, 1, 18, 20)));
        save(new Vacancy(0, "Middle Java Developer", "middle",
                LocalDateTime.of(2023, 2, 24, 10, 15)));
        save(new Vacancy(0, "Middle+ Java Developer", "middle+",
                LocalDateTime.of(2023, 1, 29, 14, 50)));
        save(new Vacancy(0, "Senior Java Developer", "senior",
                LocalDateTime.of(2023, 6, 16, 13, 45)));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) ->
                new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(),
                        vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
