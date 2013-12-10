package com.github.giterable;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Giterable implements Iterable<File> {

    private Repository repo;
    private ObjectId commitId;
    private TreeFilter filter;

    public Giterable(File repoPath, String revisionStr, TreeFilter filter) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(repoPath);
        repoBuilder.readEnvironment().findGitDir();
        repo = repoBuilder.build();

        commitId = repo.resolve(revisionStr);
        this.filter = filter;
    }

    @Override
    public void finalize() throws Throwable {
        repo.close();
        super.finalize();
    }

    public Giterable(File repoPath, String revisionStr) throws IOException {
        this(repoPath, revisionStr, null);
    }

    public void setFilter(TreeFilter filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<File> iterator() {
        try {
            return new Giterator(repo, commitId, filter);
        } catch (IOException e) {
            // TODO: how to handle this better?
            e.printStackTrace();
        }
        return null;
    }

}
