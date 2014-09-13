GitHup
======

An auto-updating module for Java apps.
It uses GitHub as the site for checking for updates and downloading updates(planned feature).

Usage
------

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

The format of the info.txt is:

    version: YOUR_VERSION_HERE_AS_STRING
    releaseDate: YOUR_DATE_HERE_AS_STRING
    description: YOUR_DESCRIPTION_HERE
    MULTILINE_SUPPORTED_FOR_DESCRIPTION

ToDo
------
Add the downloading function.
