package esprit.productgestion.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TwitterService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Value("${python.path:/usr/bin/python3}")
    private String pythonPath;

    public String postTweet(String text, String imageUrl) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet text cannot be empty");
        }

        String baseDir = System.getProperty("user.dir");
        Path scriptPath = Paths.get(baseDir, "src", "main", "resources", "scripts", "post_tweet.py");
        Path workingDir = Paths.get(baseDir, "src", "main", "resources");

        logger.info("Resolved script path: {}", scriptPath.toAbsolutePath());
        logger.info("Resolved working directory: {}", workingDir.toAbsolutePath());

        ProcessBuilder pb;
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            pb = new ProcessBuilder(pythonPath, scriptPath.toString(), text, imageUrl);
            logger.info("Executing Python script with image: {} {} '{}' '{}'", pythonPath, scriptPath, text, imageUrl);
        } else {
            pb = new ProcessBuilder(pythonPath, scriptPath.toString(), text);
            logger.info("Executing Python script without image: {} {} '{}'", pythonPath, scriptPath, text);
        }

        pb.directory(workingDir.toFile());
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                logger.debug("Script output: {}", line);
            }

            int exitCode = process.waitFor();
            String result = output.toString().trim();

            if (exitCode == 0 && result.contains("Success")) {
                String tweetId = result.split("ID: ")[1];
                logger.info("Tweet posted successfully with ID: {}", tweetId);
                return "Tweet posted successfully: " + tweetId;
            } else {
                logger.error("Python script failed with exit code {}: {}", exitCode, result);
                throw new Exception("Failed to post tweet: " + result);
            }
        } catch (Exception e) {
            logger.error("Error executing Python script: {}", e.getMessage());
            throw new Exception("Error posting tweet: " + e.getMessage(), e);
        }
    }
}