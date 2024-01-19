package com.food.delivery.users.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	

	
	

	
	
	/* Bash Script
	 * public void run(String... args) throws Exception { try { File file =
	 * ResourceUtils.getFile("classpath:" + "script.sh");
	 * Runtime.getRuntime().exec(file.toString()); } catch(IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	 
	
	/* Python Script
	 * public void run(String... args) throws Exception { try {
	 * 
	 * String pythonScriptPath = "/resources/script.sh";
	 * 
	 * // Execute the Python script Process process =
	 * Runtime.getRuntime().exec("python" + pythonScriptPath);
	 * 
	 * // Optional: Wait for the script to complete process.waitFor(); } catch
	 * (IOException e) { e.printStackTrace(); } }
	 */

}
