package com.example.jobresearch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Disabled locally: this test starts the full Spring context, including a real
// database connection via DB_URL/DB_USERNAME/DB_PASSWORD environment variables.
// Those are only available in the deployment environment (Render), not locally,
// so this test fails here with "Unable to determine Dialect without JDBC metadata".
// TODO: re-enable once a local/test database (e.g. H2) or test profile is set up.
@Disabled("Requires a real database connection not available in local test runs")
@SpringBootTest
class JobresearchApplicationTests {

	@Test
	void contextLoads() {
	}

}
