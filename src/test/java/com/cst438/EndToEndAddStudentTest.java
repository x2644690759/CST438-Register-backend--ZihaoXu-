package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class EndToEndAddStudentTest {
    public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/zihaoxu/Documents/GitHub/CST438-Register-backend--ZihaoXu-/chromedriver";

	public static final String URL = "http://localhost:3001";

	public static final String TEST_USER_EMAIL = "test@csumb.edu";

	public static final int TEST_COURSE_ID = 40443; 

    public static final int TEST_STUDENT_ID = 1; 

	public static final String TEST_SEMESTER = "2021 Fall";

    public static final String TEST_EMAIL = "test123@csumb.edu";

    public static final String TEST_STUDENT = "test123";

	public static final int SLEEP_DURATION = 1000; // 1 second.

	/*
	 * When running in @SpringBootTest environment, database repositories can be used
	 * with the actual database.
	 */
	
	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;

	/*
	 * Student add course TEST_COURSE_ID to schedule for 2021 Fall semester.
	 */
	
	@Test
	public void addStudentTest() throws Exception {

		/*
		 * if student is already enrolled, then delete the enrollment.
		 */
		
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

            driver.findElement(By.xpath("(//a)[last()]")).click();
			Thread.sleep(SLEEP_DURATION);

            driver.findElement(By.xpath("//input")).sendKeys(TEST_STUDENT);
            driver.findElement(By.xpath("(//input)[last()]")).sendKeys(TEST_EMAIL);
            driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);

			Student e = studentRepository.findByEmail(TEST_EMAIL);
            assertNotNull(e, "Student not found in database.");


		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
            Student e = studentRepository.findByEmail(TEST_EMAIL);
            if (e != null)
				studentRepository.delete(e);

			driver.quit();
		}

	}
}
