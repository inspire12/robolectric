package org.robolectric.internal.dependency;

import java.net.URL;

public interface DependencyResolver {
  /**
   * Get an array of local artifact URLs for the given dependencies. The order of the URLs is guaranteed to be the
   * same as the input order of dependencies, i.e., urls[i] is the local artifact URL for dependencies[i].
   */
  URL[] getLocalArtifactUrls(DependencyJar... dependencies);

  URL getLocalArtifactUrl(DependencyJar dependency);
}
