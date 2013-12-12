# Giterable
A Java package for iterating through files in your Git repo and loading them.

```java
File repoPath = new File("demo_project/.git");
String revisionStr = "master";
// = "HEAD";
// = "aec1f2040b0eeef41f0f37535f8b4c6334b2a610";

Giterable gitFiles = new Giterable(repoPath, revisionStr);
GitLoader loader = new GitLoader(repoPath);

for (File file : gitFiles) {
    String text = loader.getText(file, revisionStr);
    byte[] bytes = loader.getBytes(file, revisionStr);
}
```

# Dependencies
Giterable uses [JGit](http://www.eclipse.org/jgit/) to access Git repositories.

# Usage
1. Download the JGit jar file `org.eclipse.jgit.jar` at: http://www.eclipse.org/jgit/download/
2. [Download](https://github.com/ke1vin/giterable/blob/master/giterable.jar) `giterable.jar`.
3. Add both jars to your classpath when compiling and running your code.

```bash
$ javac -cp .:giterable.jar:jgit.jar Demo.java
$ java -cp .:giterable.jar:jgit.jar Demo
```

Here's how to use Giterable in your code:

```java
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
``` 

This produces the following output:

```
Loading repository at:
demo/demo_project/.git

demo_project/.gitignore
- - - - - - - - - -
.DS_Store
*.swp

demo_project/README
- - - - - - - - - -
README:
1st commit

demo_project/src/file1.txt
- - - - - - - - - -
Contents of file1.txt

demo_project/src/file2.txt
- - - - - - - - - -
Contents of file2.txt
```

To run the above demo, you can clone this repo and run `demo/demo.sh`

```bash
$ git clone git@github.com:ke1vin/giterable.git
$ cd giterable/demo
$ ./demo.sh
```