package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    @GuardedBy("this")
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "intern",
                LocalDateTime.of(2023, 1, 13, 12, 30), true, 2, 0));
        save(new Vacancy(0, "Junior Java Developer", "junior",
                LocalDateTime.of(2023, 3, 18, 13, 9), true, 3, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "junior+",
                LocalDateTime.of(2023, 5, 1, 18, 20), true, 2, 0));
        save(new Vacancy(0, "Middle Java Developer", "middle",
                LocalDateTime.of(2023, 2, 24, 10, 15), true, 1, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "middle+",
                LocalDateTime.of(2023, 1, 29, 14, 50), true, 1, 0));
        save(new Vacancy(0, "Senior Java Developer", "senior",
                LocalDateTime.of(2023, 6, 16, 13, 45), true, 1, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
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
                        vacancy.getCreationDate(), vacancy.getVisible(),
                        vacancy.getCityId(), vacancy.getFileId())) != null;
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
