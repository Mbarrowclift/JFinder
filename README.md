JFinder
=======

<img src="https://raw.githubusercontent.com/Barrowclift/JFinder/master/screenshot.png">

General Information
-------------------

This is a relic of mine from Drexel University's CS 338 Graphical User Interfaces course. The project required we make a GUI of any kind—pending approval from the professor—and implement it in Java Swing. Quite a few Drexel courses up to that point had us working with Java Swing (much to my dismay), so at that point I was intimately familiar with the disaster which is Swing's included file dialog, JFileChooser.

<img src="https://raw.githubusercontent.com/Barrowclift/JFinder/master/jfilechooser.jpg">

Yikes.

I thought it would be a fun little project to attempt to make my own JFileChooser that would better emulate the file dialogs of whatever respective system the application was running on (so on OS X it would be similar to OS X's file chooser, on Windows it would resemble the classic Windows Explorer chooser, etc.). To help limit my scope I decided for now to just start with the OS X version.

Now, it's important to note that I'm aware that AWT's FileDialog already fulfills this goal by actually using the OS's native file chooser. I do not believe this was an appropriate solution, however, since Sun has [all but abandoned support for it](http://stackoverflow.com/questions/1241984/need-filedialog-with-a-file-type-filter-in-java/1241996#1241996), [it's known to have poor performance]((http://stackoverflow.com/questions/1241984/need-filedialog-with-a-file-type-filter-in-java/1241996#1241996)), and [lacks important features like the ability to select directories](http://www.coderanch.com/t/345134/GUI/java/FileDialog-JFileChooser) that the Swing JFileChooser has.

Official development ceased soon after the original prototype was finished. At the time the project was created, I could not find an existing library or tool that would return the actual file icons used for any particular file on OS X larger than 16x16, [and it seemed according to the community that this was simply not possible](http://nadeausoftware.com/articles/2008/12/mac_java_tip_how_access_aqua_file_and_folder_icons) (at least without a massive number of hacks). Unfortunately, the additional engineering effort required to get the intended design complete was far out of scope for the class.

Install
-------

If you're curious to try it out for yourself, feel free to download or clone this project and add as an existing project in Eclipse. You can run the example dialog launcher using the "Example.java" class. This was never tested on Windows or Linux so it may not work on these platforms.
