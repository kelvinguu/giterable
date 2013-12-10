package com.github.giterable;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class GitLoader {

    Repository repo;
    File repoDir;

    public GitLoader(File repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(repoPath);
        repoBuilder.readEnvironment().findGitDir();
        repo = repoBuilder.build();
        repoDir = repo.getDirectory().getParentFile();
    }

    public byte[] getBytes(File file, String revisionStr) throws IOException {
        ObjectId commitId = repo.resolve(revisionStr);
        RevTree tree = new RevWalk(repo).parseCommit(commitId).getTree();

        Path pathAbsolute = Paths.get(file.getCanonicalPath());
        Path pathBase = Paths.get(repoDir.getCanonicalPath());
        Path pathRelative = pathBase.relativize(pathAbsolute);
        String relativePathStr = pathRelative.toString();

        TreeWalk treeWalk = TreeWalk.forPath(repo, relativePathStr, tree);

        ObjectLoader loader = repo.open(treeWalk.getObjectId(0));
        return loader.getBytes();
    }

    public String getText(File file, String revisionStr) throws IOException {
        return new String(getBytes(file, revisionStr));
    }
}
