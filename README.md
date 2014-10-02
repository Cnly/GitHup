GitHup
======

An auto-updater for Java apps.
It uses GitHub as the site for checking for updates and downloading updates(planned feature).

Usage
------

In your project, create an instance of GitHup(documented), call start() and it's done!

In your repo, create a branch called githup-updates. In that branch, you can create folders for your projects.
Folder structure:

    ---
    |-proj1
    | |
    | |- info.txt (The text file with update information. The format is described below.)
    |
    |-proj2
    | |- info.txt
    ---

The format of info.txt is:

    version: YOUR_VERSION_HERE_AS_STRING
    releaseDate: YOUR_DATE_HERE_AS_STRING
    downloadLink: https://here.to.download.me/new.jar
    description: YOUR_DESCRIPTION_HERE
    MULTILINE_SUPPORTED_FOR_DESCRIPTION
    
Note: `downloadLink` is optional.

Note2: `downloadLink` has 2 formats. One is a normal URL and the other is gh:`username`:`repoName`:`projectName`:`filename` which will be converted to the file `filename` in folder `projectName/files/` in branch `githup-updates` in your repo `repoName`.

TODO
------
(PRs and issues are welcome!)
