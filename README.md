# RestAssured API Automation on TESTNG framework

## TestNG
### Annotations
| Annotation   | Description                                                                                                          | Paramaters                                                                                                                                                                                                                                                                                                                                                                                                                                |
|--------------|----------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| @BeforeSuite | The annotated method will be run before all tests in this suite have run.                                            | (groups={"group1","group2"}): This attribute specifies the list of groups this class/method belongs to.<br/> (dependsOnGroups={"group1","group2"}): This attribute specifies the list of groups that this method depends on.                                                                                                                                                                                                              |
| @BeforeClass | The annotated method will be run before the first test method in the current class is invoked.                       | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @BeforeTest  | The annotated method will be run before any test method belonging to the classes inside the <test> tag is run.       | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @BeforeGroups | The annotated method will be run before the first test method that belongs to any of these groups is invoked.        | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @AfterGroups  | The annotated method will be run after the last test method that belongs to any of these groups is invoked.          | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @AfterClass   | The annotated method will be run after all the test methods in the current class have been run.                      | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @AfterTest | The annotated method will be run after all the test methods belonging to the classes inside the <test> tag have run. | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @AfterSuite | The annotated method will be run after all tests in this suite have run.                                             | (groups={"group1","group2"}) <br/>(dependsOnGroups={"group1","group2"})                                                                                                                                                                                                                                                                                                                                                                   |
| @DataProvider | Marks a method as supplying data for a test method. The annotated method must return an Object[][] where each Object[] can be assigned the parameter list of the test method. The @Test method that wants to receive data from this DataProvider needs to use a dataProvider name equals to the name of this annotation. | (name="dataProviderName") <br/>(parallel=true/false)                                                                                                                                                                                                                                                                                                                                                                                      |
| @Parameters | Describes how to pass parameters to a test method. | (value={"param1","param2"})                                                                                                                                                                                                                                                                                                                                                                                                               |
| @Factory | Marks a method as a factory that returns objects that will be used as TestNG test classes. |                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| @Listeners | Defines listeners on a test class or a suite. | (value={ListenerClass1.class, ListenerClass2.class})                                                                                                                                                                                                                                                                                                                                                                                      |
| @Test | The annotated method is a test method. | (alwaysRun=true/false) <br/>(enabled=true/false) <br/>(description="Test Description") <br/>(priority=1) <br/>(dependsOnMethods={"method1","method2"}) <br/>(dependsOnGroups={"group1","group2"}) <br/>(groups={"group1","group2"}) <br/>(dataProvider="dataProviderName") <br/>(dataProviderClass="dataProviderName") <br/>(successPercentage=80) <br/>(timeOut=milliseconds) <br/>(invocationCount=number) <br/>(threadPoolSize=number) |

### TestNG XML Suite Configuration
The testng.xml file is used to configure and organise TestNG test suites. Multiple testng.xml files can be created to run different sets of tests with different configurations. Below is an example of a basic testng.xml file:

testng_1.xml
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="User API test suite" parallel="tests" thread-count="2">
    <test name="Smoke Tests" preserve-order="false">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="com.cdTester.restAssured.tests.user.UserApiTest"/>
            <class name="com.cdTester.restAssured.tests.user.AnotherClass"/>
        </classes>
    </test>
    <test name="Regression Tests">
        <groups>
            <run>
                <include name="regression"/>
            </run>
        </groups>
        <packages>
            <package name="com.cdTester.restAssured.tests.user"/>
        </packages>
    </test>    
</suite>
```

### Running TestNG Suite
To run the TestNG suite, you can use the following command in the terminal:

Using MVN
The following tells Maven to run the test using a specific testng.xml file and overrides the pom.xml groups.
```bash
mvn clean test -DsuiteXmlFile=testng_1.xml -Dgroups=
```

To run testNG methods in isolation from the intelliJ IDE, you need to add `VM options: -Denv=dev -Dgroups=` to run TestNG run configurations template.
On the main menu, select Run > Edit Configurations... > Edit template configurations... > TestNG
Now you can use the `Run Test` icon next to your @Test method.


## Extent Reporter
tbc


## API helper
tbc

## 