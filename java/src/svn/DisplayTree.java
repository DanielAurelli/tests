/*
 * ====================================================================
 * Copyright (c) 2004-2011 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package svn;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/*
 * This example shows how to get the repository tree at the latest (HEAD)
 * revision starting with the directory that is the path/to/repository part of
 * the repository location URL. The main point is SVNRepository.getDir() method
 * that is called recursively for each directory (till the end of the tree).
 * getDir collects all entries located inside a directory and returns them as a
 * java.util.Collection. As an example here's one of the program layouts (for
 * the default url used in the program ):
 * 
 * Repository Root: http://svn.svnkit.com/repos/svnkit
 * Repository UUID: 0a862816-5deb-0310-9199-c792c6ae6c6e
 * 
 * /examples (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/svnkit-examples.iml (author: 'alex'; revision: 2775; date: Fri Nov 10 02:08:45 NOVT 2006)
 * /examples/src (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/src/org (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/src/org/tmatesoft (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/src/org/tmatesoft/svn (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/src/org/tmatesoft/svn/examples (author: 'sa'; revision: 2794; date: Tue Nov 14 03:21:11 NOVT 2006)
 * /examples/src/org/tmatesoft/svn/examples/wc (author: 'alex'; revision: 2776; date: Fri Nov 10 02:25:08 NOVT 2006)
 * ......................................................
 * ---------------------------------------------
 * Repository latest revision: 2802
 */
public class DisplayTree 
{
  /*
   * args parameter is used to obtain a repository location URL, user's
   * account name & password to authenticate him to the server.
   */
  public static void main(String[] args) {
    /*
     * default values:
     */
    //String url = "https://192.168.0.71:18080/svn/teste/java/tests/src";
    String url = "https://192.168.2.71:18080/svn/teste/application/SAMPLES";
    String name = "ctdaa";
    String password = "ctdaa";

    setupLibrary();
    if (args != null) {
      url = (args.length >= 1) ? args[0] : url;
      name = (args.length >= 2) ? args[1] : name;
      password = (args.length >= 3) ? args[2] : password;
    }
    ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
    SVNRepository repository = null;
    try {
      repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
    } catch (SVNException svne) {
      System.err
      .println("error while creating an SVNRepository for location '"
          + url + "': " + svne.getMessage());
      System.exit(1);
    }

    repository.setAuthenticationManager(authManager);

    try {
      SVNNodeKind nodeKind = repository.checkPath("", -1);
      if (nodeKind == SVNNodeKind.NONE) {
        System.err.println("There is no entry at '" + url + "'.");
        System.exit(1);
      } else if (nodeKind == SVNNodeKind.FILE) {
        System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
        System.exit(1);
      }
      System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
      System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
      System.out.println("");

      listEntries(repository, "");
    } catch (SVNException svne) {
      System.err.println("error while listing entries: "
          + svne.getMessage());
      System.exit(1);
    }
    long latestRevision = -1;
    try {
      latestRevision = repository.getLatestRevision();
    } catch (SVNException svne) {
      System.err
      .println("error while fetching the latest repository revision: "
          + svne.getMessage());
      System.exit(1);
    }
    System.out.println("");
    System.out.println("---------------------------------------------");
    System.out.println("Repository latest revision: " + latestRevision);
    System.exit(0);
  }

  private static void setupLibrary() {
    DAVRepositoryFactory.setup();
    SVNRepositoryFactoryImpl.setup();
    FSRepositoryFactory.setup();
  }

  public static void listEntries(SVNRepository repository, String path)
      throws SVNException {
    Collection entries = repository.getDir(path, -1, null,
        (Collection) null);
    Iterator iterator = entries.iterator();
    while (iterator.hasNext()) {
      SVNDirEntry entry = (SVNDirEntry) iterator.next();
      System.out.println("/" + (path.equals("") ? "" : path + "/")
          + entry.getName() + " (author: '" + entry.getAuthor()
          + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
      /*
       * Checking up if the entry is a directory.
       */
      if (entry.getKind() == SVNNodeKind.DIR) {
        listEntries(repository, (path.equals("")) ? entry.getName()
            : path + "/" + entry.getName());
      }
    }
  }
}