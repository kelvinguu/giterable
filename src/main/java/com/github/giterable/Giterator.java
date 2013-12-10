package com.github.giterable;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Giterator implements Iterator<File> {

    private TreeWalk treeWalk;
    private boolean nextExist;
    private File repoDir;

    public Giterator(Repository repo, ObjectId commitId, TreeFilter filter) throws IOException {
        RevTree tree = new RevWalk(repo).parseCommit(commitId).getTree();
        treeWalk = new TreeWalk(repo);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(filter);
        nextExist = treeWalk.next();
        repoDir = repo.getDirectory().getParentFile();
    }

    @Override
    public void finalize() throws Throwable {
        treeWalk.release();
        super.finalize();
    }

    @Override
    public boolean hasNext() {
        return nextExist;
    }

    @Override
    public File next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        File returnFile = new File(repoDir, treeWalk.getPathString());

        try {
            nextExist = treeWalk.next();
        } catch (IOException e) {
            nextExist = false;
        }

        return returnFile;
    }

    @Override
    public void remove() {
        // remove does not do anything
    }
}
