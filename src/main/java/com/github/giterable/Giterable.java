package com.github.giterable;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Iterator;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 5:14 PM
 */
public class Giterable implements Iterable {

    private Giterator giterator;

    public Giterable(Repository repo, ObjectId commitId, GitProcessor processor) throws IOException {
        giterator = new Giterator(repo, commitId, processor);
    }

    public Iterator iterator() {
        return giterator;
    }

}
