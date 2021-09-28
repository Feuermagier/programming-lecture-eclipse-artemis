package edu.kit.kastel.sdq.eclipse.grading.client.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.util.FileUtils;

public class EgitGitHandler extends AbstractGitHandler {

    private static final String REMOTE_NAME = "origin";

    public EgitGitHandler(String repoURL) {
        super(repoURL);
    }

    @Override
    public void cloneRepo(final File destination, final String branch) throws GitException {
        Repository repository = null;
        try {
            CloneCommand cloneRepository = Git.cloneRepository();
            cloneRepository.setBranch(branch);
            cloneRepository.setDirectory(destination);
            cloneRepository.setRemote(REMOTE_NAME);
            cloneRepository.setURI(new URIish(this.getRepoURL()).toString());
            cloneRepository.setCloneAllBranches(true);
            cloneRepository.setCloneSubmodules(false);
            Git git = cloneRepository.call();
            repository = git.getRepository();
        } catch (final Exception e) {
            try {
                FileUtils.delete(destination, FileUtils.RECURSIVE);
            } catch (IOException ioe) {
                throw new GitException("Git clone failed with exception" + ioe.getMessage(), ioe);
            }
            throw new GitException("Git clone failed with exception" + e.getMessage(), e);
        } finally {
            if (repository != null) {
                repository.close();
            }
        }

    }

}
