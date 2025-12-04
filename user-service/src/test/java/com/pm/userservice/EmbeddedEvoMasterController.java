package com.pm.userservice;

import org.evomaster.client.java.controller.EmbeddedSutController;
import org.evomaster.client.java.controller.InstrumentedSutStarter;
import org.evomaster.client.java.controller.api.dto.SutInfoDto;
import org.evomaster.client.java.controller.api.dto.auth.AuthenticationDto;
import org.evomaster.client.java.controller.api.dto.database.schema.DatabaseType;
import org.evomaster.client.java.controller.problem.GraphQlProblem;
import org.evomaster.client.java.controller.problem.ProblemInfo;
import org.evomaster.client.java.controller.problem.RestProblem;
import org.evomaster.client.java.sql.DbSpecification;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmbeddedEvoMasterController extends EmbeddedSutController {

//    private static final String POSTGRES_VERSION = "18";
//
    private static final String POSTGRES_PASSWORD = "graphql_password";

    private static final String POSTGRES_USER = "graphql_user";
//
//    private static final int POSTGRES_PORT = 5011;
//
//    private static final GenericContainer postgresContainer = new GenericContainer("postgres:" + POSTGRES_VERSION)
//            .withEnv("POSTGRES_USER",  POSTGRES_USER)
//            .withEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD)
//            .withEnv("POSTGRES_DB", "db")
//            .withTmpFs(Collections.singletonMap("/var/lib/postgresql/data", "rw"))
//            .withExposedPorts(POSTGRES_PORT);

    private Connection sqlConnection;
    private ConfigurableApplicationContext ctx;
    private List<DbSpecification> dbSpecification;

    public EmbeddedEvoMasterController() {
        this(40100);
    }

    public EmbeddedEvoMasterController(int port) {
        setControllerPort(port);
    }

    public static void main(String[] args) {
        int port = 40100;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        EmbeddedEvoMasterController controller = new EmbeddedEvoMasterController(port);
        InstrumentedSutStarter starter = new InstrumentedSutStarter(controller);

        starter.start();
    }
    @Override
    public boolean isSutRunning() {
        return ctx!=null && ctx.isRunning();
    }

    @Override
    public String getPackagePrefixesToCover() {
        return "com.pm.userservice.";
    }

    @Override
    public List<AuthenticationDto> getInfoForAuthentication() {
        return null;
    }

    //For GraphQL
    @Override
    public ProblemInfo getProblemInfo() {
        return new GraphQlProblem(
                "http://localhost:" + getSutPort() + "/graphql"
        );
    }

    //For RestAPI
//    @Override
//    public ProblemInfo getProblemInfo() {
//        return new RestProblem(
//                "http://localhost:" + getSutPort() + "/v3/api-docs",
//                null
//        );
//    }

    @Override
    public SutInfoDto.OutputFormat getPreferredOutputFormat() {
        return SutInfoDto.OutputFormat.JAVA_JUNIT_5;
    }

    @Override
    public String startSut() {
//        String postgresURL = "jdbc:postgresql://" + postgresContainer.getHost() + ":"
//                + postgresContainer.getMappedPort(POSTGRES_PORT) + "/db";
        String postgresURL = "jdbc:postgresql://localhost:5011/db";

        System.setProperty("spring.application.name", "user-service");
//        System.setProperty("server.port", "0"); // random port for testing
        System.setProperty("server.port", "7777");
        System.setProperty("spring.datasource.url", postgresURL);
        System.setProperty("spring.datasource.username", POSTGRES_USER);
        System.setProperty("spring.datasource.password", POSTGRES_PASSWORD);
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");
        System.setProperty("spring.jpa.show-sql", "true");
        System.setProperty("spring.graphql.graphiql.enabled", "true");
        System.setProperty("spring.graphql.schema.introspection.enabled", "true");
        System.setProperty("spring.graphql.path", "/graphql");
        System.setProperty("spring.graphql.graphiql.path", "/graphiql");

        ctx = SpringApplication.run(com.pm.userservice.UserServiceApplication.class, new String[]{});

        if (sqlConnection != null) {
            try {
                sqlConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            sqlConnection = DriverManager.getConnection(postgresURL, POSTGRES_USER, POSTGRES_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dbSpecification = Arrays.asList(new DbSpecification(DatabaseType.POSTGRES, sqlConnection));

        return "http://localhost:" + getSutPort();
    }

    @Override
    public void stopSut() {
        if(ctx!=null)ctx.stop();
    }

    @Override
    public void resetStateOfSUT() {

    }

    @Override
    public List<DbSpecification> getDbSpecifications() {
        return dbSpecification;
    }

    protected int getSutPort() {
        //    return ctx.getEnvironment().getProperty("server.port", Integer.class);
//        return (Integer) ((Map) ctx.getEnvironment()
//                .getPropertySources().get("server.ports").getSource())
//                .get("local.server.port");
        return 7777;
    }

}
