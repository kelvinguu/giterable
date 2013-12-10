package com.github.giterable;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * User: Kelvin
 * Date: 12/9/13
 * Time: 4:15 PM
 */
public class GitLoader {

    Repository repo;

    public GitLoader(File repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(repoPath);
        repoBuilder.readEnvironment().findGitDir();
        repo = repoBuilder.build();
    }

    public byte[] getFileBytes(File file, String revisionStr) throws IOException {
        ObjectId commitId = repo.resolve(revisionStr);
        RevTree tree = new RevWalk(repo).parseCommit(commitId).getTree();
        TreeWalk  treeWalk = TreeWalk.forPath(repo, file.getCanonicalPath(), tree);

        ObjectLoader loader = repo.open(treeWalk.getObjectId(0));
        return loader.getBytes();
    }

    public String getFileText(File file, String revisionStr) throws IOException {
        return new String(getFileBytes(file, revisionStr));
    }
}
