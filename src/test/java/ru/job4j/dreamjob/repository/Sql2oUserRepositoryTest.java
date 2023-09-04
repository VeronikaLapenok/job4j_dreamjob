//package ru.job4j.dreamjob.repository;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
//import ru.job4j.dreamjob.model.File;
//import ru.job4j.dreamjob.model.User;
//import ru.job4j.dreamjob.model.Vacancy;
//
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Properties;
//
//import static java.time.LocalDateTime.now;
//import static java.util.Collections.emptyList;
//import static java.util.Optional.empty;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//class Sql2oUserRepositoryTest {
//    private static Sql2oUserRepository sql2oUserRepository;
//
//    @BeforeAll
//    public static void initRepositories() throws Exception {
//        var properties = new Properties();
//        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
//                .getResourceAsStream("connection.properties")) {
//            properties.load(inputStream);
//        }
//        var url = properties.getProperty("datasource.url");
//        var username = properties.getProperty("datasource.username");
//        var password = properties.getProperty("datasource.password");
//
//        var configuration = new DatasourceConfiguration();
//        var datasource = configuration.connectionPool(url, username, password);
//        var sql2o = configuration.databaseClient(datasource);
//
//        sql2oUserRepository = new Sql2oUserRepository(sql2o);
//    }
//
//    @Test
//    public void whenSaveThenGetSame() {
//        var email = "email@email";
//        var password = "password";
//        var user = sql2oUserRepository.save(new User(
//                0, email, "name", password));
//        var savedUser = sql2oUserRepository.findByEmailAndPassword(email, password);
//        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
//    }
//}