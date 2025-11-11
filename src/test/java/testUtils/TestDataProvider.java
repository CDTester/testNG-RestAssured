package testUtils;

import org.testng.annotations.DataProvider;
import java.io.IOException;

public class TestDataProvider extends BaseTestDataProvider{

  @DataProvider(name = "userTestData")
  public static Object[][] getUserTestData() throws IOException {
    return loadTestData("src/test/resources/testdata/users.json");
  }


}