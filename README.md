JFinder
=======

<img src="https://raw.githubusercontent.com/Barrowclift/JFinder/master/screenshot.png">

General Information
-------------------

This was an old final project of mine for Drexel University's CS 338 Graphical User Interfaces course. The requirements were to make any little GUI tool our heart desired (with approval from the professor) and implement it in Java Swing. Now, Drexel University used Java Swing in plenty of other classes as well much to my dismay so I had become intimately familiar with the disaster which is Swing's included file dialog, JFileChooser.

<img src="https://raw.githubusercontent.com/Barrowclift/JFinder/master/jfilechooser.jpg">

Yikes. I thought it would be a fun little project to attempt to make my own JFileChooser that would better emulate the file dialogs of whatever respective system the application was running on (so on OS X it would be similar to OS X's file chooser, on Windows it would resemble the classic Windows Explorer chooser, etc.). To help limit my scope I decided for now to just start with the OS X version.

Now, it's important to note that I am aware AWT's FileDialog already fulfills this goal by actually using the OS's native file chooser. I do not believe this was an appropriate solution, however, since Sun has [all but abandoned support for it](http://stackoverflow.com/questions/1241984/need-filedialog-with-a-file-type-filter-in-java/1241996#1241996), [is known to have mixed performance]((http://stackoverflow.com/questions/1241984/need-filedialog-with-a-file-type-filter-in-java/1241996#1241996)), and [lacks important features like the ability to select directories](http://www.coderanch.com/t/345134/GUI/java/FileDialog-JFileChooser) that the newer Swing JFileChooser has.

Official development was ceased soon after the original prototype was finished. At the time the project was created, I could not find an existing library or tool that would return the actual file icons used for any particular file on OS X larger than 16x16, [and it seemed according to the community that this was simply not possible]((http://nadeausoftware.com/articles/2008/12/mac_java_tip_how_access_aqua_file_and_folder_icons)) without an extensive amount of additional engineering effort that was far out of scope for the class and original project goals.

This was the death nail of the project primarily since I wanted the ability to support icon view and larger icons in the favorites bar like OS X's native dialog (which I had planned to be the big "presentation tentpole” of the project) but it was simply not doable in the timeframe I had, especially for what was supposed to be a quick, goofy project to pass a class (honestly, who the hell writes *anything* in Swing?)

Install
-------

If you're curious to try it out for yourself, feel free to download or clone this project and add as an existing project in Eclipse. You can run the example dialog launcher using the "Example.java" class. This was never tested on Windows or Linux so it may not work on these platforms.