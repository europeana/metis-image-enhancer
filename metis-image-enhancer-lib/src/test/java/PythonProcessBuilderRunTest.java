import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PythonProcessBuilderRunTest {

    @Test
    public void givenPythonScript_whenPythonProcessInvoked_thenSuccess() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", resolvePythonScriptPath("src/test/resources/image-super-resolution-test.py"));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        readProcessOutput(process.getInputStream());

        int exitCode = process.waitFor();
        Assertions.assertEquals(0, exitCode);
    }

    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        List<String> results = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        return results;
    }

    private String resolvePythonScriptPath(String path){
        File file = new File(path);
        return file.getAbsolutePath();
    }
}
