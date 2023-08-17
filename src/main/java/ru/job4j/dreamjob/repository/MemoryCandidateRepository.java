package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    @GuardedBy("this")
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Mikhailov A.V.", "junior",
                LocalDateTime.of(2023, 5, 4, 12, 18), 3, 0));
        save(new Candidate(0, "Alekseeva T.P.", "middle",
                LocalDateTime.of(2023, 2, 17, 10, 29), 2, 0));
        save(new Candidate(0, "Petrov G.S.", "junior",
                LocalDateTime.of(2023, 3, 15, 14, 34), 1, 0));
        save(new Candidate(0, "Obodova V.R.", "senior",
                LocalDateTime.of(2023, 4, 29, 13, 50), 3, 0));
        save(new Candidate(0, "Morozov F.S.", "middle",
                LocalDateTime.of(2023, 6, 12, 10, 45), 2, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.getAndIncrement());
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
                        candidate.getDescription(), candidate.getCreationDate(),
                        candidate.getCityId(), candidate.getFileId())) != null;
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
