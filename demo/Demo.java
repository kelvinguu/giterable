import com.kelvingu.giterable.Giterable;
import com.kelvingu.giterable.GitLoader;

import java.io.File;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {

        File repoPath = new File("demo_project/.git");
        String revisionStr = "master";
        // String revisionStr = "HEAD";
        // String revisionStr = "aec1f2040b0eeef41f0f37535f8b4c6334b2a610";

        System.out.println("Loading repository at:");
        System.out.println(repoPath.getCanonicalPath() + '\n');

        Giterable gitFiles = new Giterable(repoPath, revisionStr);
        GitLoader loader = new GitLoader(repoPath);

        for (File file : gitFiles) {
            String text = loader.getText(file, revisionStr);
            byte[] bytes = loader.getBytes(file, revisionStr);

            System.out.println(file.getPath());
            System.out.println("- - - - - - - - - -");
            System.out.println(text + '\n');
        }
    }
}