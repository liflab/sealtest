SealTest: a test sequence generator with many tricks
====================================================

![Seal](https://liflab.github.io/sealtest/seal.jpg)

[![Travis](https://img.shields.io/travis/liflab/sealtest.svg?style=flat-square)]()
[![SonarQube Coverage](https://img.shields.io/sonar/http/sonarqube.com/liflab:sealtest/coverage.svg?style=flat-square)]()

SealTest is a library for generating test sequences based on a specification.

- To use SealTest, download the
  [latest release](https://github.com/sylvainhalle/sealtest/releases/latest)
- For more information about using the SealTest library,
  please visit the [online documentation](http://liflab.github.io/sealtest).

This file describes how to build SealTest.

Repository structure                                           {#structure}
--------------------

The repository is separated across the following folders.

- `Core`: main source files
- `CoreTest`: test source files. You need to compile these files only
  if you want to run SealTest's unit tests.

Compiling the project contained in the present repository generates the
file `sealtest.jar`.

BeepBeep tries to have as few dependencies as possible. However, the
following companion library needs to be installed for BeepBeep to
compile and run:

- The [Bullwinkle parser](https://github.com/sylvainhalle/Bullwinkle),
  an on-the-fly parser for BNF grammars *(tested with version 1.3.1)*

Compiling and Installing SealTest                                {#install}
---------------------------------

First make sure you have the following installed:

- The Java Development Kit (JDK) to compile. SealTest is developed to comply
  with Java version 6; it is probably safe to use any later version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

Download the sources for SealTest from
[GitHub](https://github.com/liflab/sealtest) or clone the
repository using Git:

    git@github.com:liflab/sealtest.git

If the project you want to compile has dependencies,
you can automatically download any libraries missing from your
system by typing:

    ant download-deps

This will put the missing JAR files in the `deps` folder in the project's
root. These libraries should then be put somewhere in the classpath, such as
in Java's extension folder (don't leave them there, it won't work). You can
do that by typing (**with administrator rights**):

    ant install-deps

or by putting them manually in the extension folder. Type `ant init` and it
will print out what that folder is for your system.

Do **not** create subfolders there (i.e. put the archive directly in that
folder).

### Compiling

Compile the sources by simply typing:

    ant

This will produce a file called `sealtest.jar` in the folder. This file
is runnable and stand-alone, or can be used as a library, so it can be moved
around to the location of your choice.

In addition, the script generates in the `doc` folder the Javadoc
documentation for using BeepBeep. To show documentation in Eclipse,
right-click on the jar, click "Properties", then fill the Javadoc location.

### Testing

SealTest can test itself by running:

    ant test

Unit tests are run with [jUnit](http://junit.org); a detailed report of
these tests in HTML format is availble in the folder `tests/junit`, which
is automatically created. Code coverage is also computed with
[JaCoCo](http://www.eclemma.org/jacoco/); a detailed report is available
in the folder `tests/coverage`.

Developing SealTest using Eclipse                                {#eclipse}
---------------------------------

If you wish to develop SealTest in Eclipse:

In short:

- Create a new empty workspace (preferably in a new, empty folder).
- Create new projects for each of the folders `Core`,
  `CoreTest`.
  
Then, setup the build path for each project:

- `Core` requires the Bullwinkle library (see above)
- `CoreTest` depends on `Core` and requires the JUnit 4 library

Warning                                                          {#warning}
-------

The SealTest project is under heavy development. The repository may be
restructured, the API may change, and so on. This is R&D!

About the author                                                   {#about}
----------------

SealTest was conceived and written by [Sylvain Hallé](http://leduotang.ca/sylvain),
associate professor at Université du Québec à Chicoutimi, Canada, and
[Raphaël Khoury](http://www.uqac.ca/portfolio/raphaelkhoury). Part of
this work has been funded by the Canada Research Chair in Software
Specification, Testing and Verification and the
[Natural Sciences and Engineering Research Council
of Canada](http://nserc-crsng.gc.ca).
