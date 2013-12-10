package com.github.giterable;

import org.apache.commons.io.FileUtils;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GiterableTests {

    // Integration tests for Giterable

    private Repository repo;
    private File repoDir;
    private Set<File> correctFileSet;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            // construct repo files
            List<String> repoFilePaths = Arrays.asList(
                    "README",
                    "java/com/github/giterable/HelloWorld.java",
                    "java/com/github/giterable/FarewellWorld.java",
                    "lib/some.jar",
                    "out/HelloWorld.class",
                    "build/FarewellWorld.class",
                    "zed/something.txt"
                    );

            repoDir = Files.createTempDirectory("repoDir").toFile();
            correctFileSet = new HashSet<File>();
            for (String fp : repoFilePaths) {

                // convert to absolute file path
                File absFile = new File(repoDir, fp);
                absFile.getParentFile().mkdirs();
                absFile.createNewFile();

                correctFileSet.add(absFile);
            }

            repo = FileRepositoryBuilder.create(new File(repoDir, ".git"));
            repo.create();
            Git git = new Git(repo);
            git.add().addFilepattern(".").call();
            git.commit().setMessage("done").call();
        }

        @Override
        protected void after() {
            // destroy repo
            try {
                FileUtils.deleteDirectory(repoDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Test
    public void testCorrectFilesFound() throws IOException {

        Giterable gi = new Giterable(repo.getDirectory(),"HEAD");

        Set<File> testFileSet = new HashSet<File>();
        for (File file : gi) {
            testFileSet.add(file);
        }

        assertEquals(correctFileSet, testFileSet);
    }

    @Test
    public void testMultipleLoops() throws IOException {
        Giterable gi = new Giterable(repo.getDirectory(),"HEAD");

        Set<File> fileSet1 = new HashSet<File>();
        for (File file : gi) {
            fileSet1.add(file);
        }

        Set<File> fileSet2 = new HashSet<File>();
        for (File file : gi) {
            fileSet2.add(file);
        }

        assertEquals(fileSet1, fileSet2);
    }

    @Test
    public void testFilter() throws IOException {
        Giterable gi = new Giterable(repo.getDirectory(),"HEAD");
        TreeFilter javaSuffix = PathSuffixFilter.create(".java");
        gi.setFilter(javaSuffix);

        for (File file : gi) {
            String ext = FilenameUtils.getExtension(file.getName());
            assertEquals("extension should be java", "java", ext);
        }

    }

}
