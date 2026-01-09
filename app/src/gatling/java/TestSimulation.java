import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TestSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http.baseUrl("http://127.0.0.1:8080")
            .header("Content-Type", "application/json")
            .check(status().is(201));

    ScenarioBuilder scn = scenario("Posting garbage into /api/translations")
            .exec(http("").post("/").body(StringBody("{\"sourceText\": \"Garbage in\", \"targetText\": \"Garbage out\", \"xposition\": 0, \"yposition\": 0}")));

    {
        setUp(scn.injectOpen(constantUsersPerSec(10000).during(Duration.ofMinutes(2))))
                .protocols(httpProtocol)
                .assertions(global().responseTime().percentile3().lt(1000), global().successfulRequests().percent().gt(95.0))
        ;
    }
}
