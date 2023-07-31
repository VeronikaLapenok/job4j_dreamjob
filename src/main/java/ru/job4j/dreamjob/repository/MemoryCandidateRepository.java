package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
    private int nextId = 1;
    private final HashMap<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Mikhailov A.V.", "junior",
                LocalDateTime.of(2023, 5, 4, 12, 18)));
        save(new Candidate(0, "Alekseeva T.P.", "middle",
                LocalDateTime.of(2023, 2, 17, 10, 29)));
        save(new Candidate(0, "Petrov G.S.", "junior",
                LocalDateTime.of(2023, 3, 15, 14, 34)));
        save(new Candidate(0, "Obodova V.R.", "senior",
                LocalDateTime.of(2023, 4, 29, 13, 50)));
        save(new Candidate(0, "Morozov F.S.", "middle",
                LocalDateTime.of(2023, 6, 12, 10, 45)));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) ->
                new Candidate(oldCandidate.getId(), candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
