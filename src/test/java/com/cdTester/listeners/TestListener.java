package com.cdTester.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.lang.reflect.Method;

public class TestListener implements ITestListener {

  @Override
  public void onStart(ITestContext context) {
    XmlSuite suite = context.getSuite().getXmlSuite();
    System.out.println("╔════════════════════════════════════════════════════════════════════════════════");
    System.out.println("║ Test Suite STARTED: " + context.getName());
    System.out.println("║ Parallel: " + suite.getParallel() + "; Threads: " + suite.getThreadCount());
    System.out.println("╠================================================================================");
  }

  @Override
  public void onFinish(ITestContext context) {
    System.out.println("╠================================================================================");
    System.out.println("║ Test Suite FINISHED: " + context.getName());
    System.out.println("║ Total tests run: " + context.getAllTestMethods().length);
    System.out.println("║ Passed: " + context.getPassedTests().size());
    System.out.println("║ Failed: " + context.getFailedTests().size());
    System.out.println("║ Skipped: " + context.getSkippedTests().size());
    System.out.println("╚════════════════════════════════════════════════════════════════════════════════");
  }

//  @Override
//  public void onTestStart(ITestResult result) {
//    System.out.println("\n▶️  STARTING: " + result.getName());
//    System.out.println("   Class: " + result.getTestClass().getName());
//    System.out.println("   Priority: " + result.getMethod().getPriority());
//  }

  @Override
  public void onTestSuccess(ITestResult result) {
    // result.getName gets the class name. I want the test description which needs Method which is not available on
    // ITestListener. So use Base test
    // long duration = result.getEndMillis() - result.getStartMillis();
    // System.out.println(result.getTestName() + "; " + result.getName() + "; " + result.getParameters().toString());
    // System.out.println("✅ Test PASSED: " + result.getTestName() + " (" + duration + "ms)");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    // result.getName gets the class name. I want the test description which needs Method which is not available on
    // ITestListener. So use Base test. This onTestFailure is good for taking screenshots on error.
    //
    // long duration = result.getEndMillis() - result.getStartMillis();
    // System.out.println("❌ FAILED: " + result.getName() + " (" + duration + "ms)");
    // System.out.println("   Error: " + result.getThrowable().getMessage());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
//    System.out.println("⏭️  SKIPPED: " + result.getName());
//    if (result.getThrowable() != null) {
//      System.out.println("   Reason: " + result.getThrowable().getMessage());
//    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    System.out.println("⚠️  FAILED (within success %): " + result.getName());
  }

  @Override
  public void onTestFailedWithTimeout(ITestResult result) {
    System.out.println("⏰ TIMEOUT: " + result.getName());
    onTestFailure(result);
  }
}