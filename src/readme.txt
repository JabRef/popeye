--------------------------------------------------------------------------------
                               Popeye @version@
--------------------------------------------------------------------------------

Introduction :

  Popeye is a graphical Java (swing) application for managing java language
  bundles. Such bundles consists of a set of properties files. Each file
  contains a special language translation in a key=value style. For every key
  there is a language dependent translation (value). Popeye reads all files
  and collect these values for the keys. Now the user can edit, resort, remove
  or insert new entries.
  Another feature of Popeye is the unicode support. All values can include
  language dependent characters. These characters are coded in an unreadable
  format, like \u00EF. Popeye can encode/decode such characters.
  We can't found an application, which take care of this problem. But a lot
  of (powerfull) properties editors for single files.

Features :

  - editing of the complete java properties file set
  - UTF encoding/decoding
  - resolving of the 2 letter iso language code into full language names
  - all incomplete entries (a key but some translation are not available)
    can be marked


License :

  Popeye is free open source software, distributed under a BSD styled License.
  See the enclosed text file 'LICENSE.txt', for details about it.


Requirements :

  Popeye runs on any system equipped with the Java Virtual Machine
  (1.4.2 or newer), which can be downloaded at no cost from
  http://java.sun.com. If you do not plan to compile Popeye, the Java
  Runtime Environment may be a better choice than the Java Development
  Kit.


Installing and running, general :

  Popeye can be downloaded as an executable .jar file. Run the program as
  follows:
  If you are using the Java Development Kit:
     java -jar <path to jar>
  or, if you are using the Java Runtime Environment:
     jre -new -jar <path to jar> or
     jrew -new -jar <path to jar>


--------------------------------------------------------------------------------
--------------------------------------------------------------------------------
Change log:
Here is a list of the major changes for each version, see the ChangeLog file
for a complete list.

 [bf] = bugfix
 [nf] = new feature

--------------------------------------------------------------------------------
  0.55   - bugfix: false "\=" handling
         - transition to github (https://github.com/koppor/popeye)

  0.54   - new translationtable-menu features (right click popup)
         - bugfixes

  0.53   - validate all entries (duplicate keys, same content...)
         - file encoding stuff
         - insert iso country codes
         - bugfixes

  0.52   - save files as java utf string or with host encoding
         - language font selection
         - reload function
         - many bugfixes

  0.51   - [bf] #1378434 save the project xml file correctly (avoid empty strings)
         - [bf] #1379907 - Duplicating newlines on save
         - extend the "new project" wizard

  0.5    - country flag binding
         - [bf] active menu items and tool buttons without any open file (application start)
         - [bf] broken icons in language textfields
         - a complete refactoring of filehandling, insert project managment
         - trash functionality for entries (erase) or delete entries
           permanently (shred)
            erase  = mark the entries as erased => undelete possible
            shred  = delete the entry immediately
         - [bf] false marking of an entry after it was completed
         - new project properties settings
         - "add new language version" feature
         - [bf] sometimes the icons of "mark entries" and "show source" are not
           synchronized (after application start)
         - fix some java 1.5 deprecation compile messages

  0.41   - disable the "replace space by xxx" option in the
           out of the box settings
         - [bf] accurate handling of escape sequences (e.g. \n, \t)
         - [nf] remember the last project files

  0.40   - [bf]: new keys are not converted into Unicode and/or whitespaces are
                 not replaced (if active)
         - [bf]: "Replace Blanks" option doesn't works well
         - improve row selection (ensure visibility)
         - new key bindings, e.g. quick entry selection in text-input
           section (up, down key)
         - [nf] options dialog and save options feature

  0.32   - [bf]: table-header menu doesn't pops up (windows)

  0.31   - disable html redering into the table view
         - position of the search field is rearranged
         - mark incomplete entries for visible languages only
         - a right click popup-menu for the table-header allows a
           quick access to some features

  0.30   - [bf] fixed : "Replace Blanks in Menu is not working"
         - simple undo for translation fields
         - [nf] search function

  0.23   - view the first line of comments into the statusline (if available)
         - insert some keystrokes
         - [nf] check new version
         - [bf] fix build.xml

  0.22   - [nf] save language versions separately (single file)
         - all toolbar buttons was furnished with a short tooltip text
         - open the application window centered on screen

  0.21   - [nf] project properties dialog (handling of language versions)
         - [nf] visibility of language versions
         - all blanks between two words can be replaced by a "_"

  0.2    - [bf] loading and saving of comments can be performed in a
           correct kind
         - [bf] fixed startup problems (ms windows)
         - add java logging
         - the language columns/textfields are arranged in an alphabetical order

  0.1    - First release
