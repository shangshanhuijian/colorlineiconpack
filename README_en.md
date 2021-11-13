# colorline(Color drawing icon package)

[app download](https://www.coolapk.com/apk/colorline.huijian.icon)

Color drawing label template from the And icon pack template, the original project address：

Color drawing mark 1.2 project display => [Bilibili](https://www.bilibili.com/video/BV1CL4y1B7yY)

### Write in front
Many experiments have ended up with acryl and linear styles chosen in many styles to draw icon packs, acryl style 2020 has been made in Camus (although not so good-looking, but let me know a lot, illustrator is at that time to learn, and XD), 2021 linear style is finally coming! Mainly made in Baicheng and Beidaihe, feel very good!
### 💡 Here's the official explanation (directly applied here)：

You need to install it on your device [Android Studio](https://developer.android.com/studio)

To download this item, you can click on the green "Code" button on this page, Download Zip, and unzip to a path without Chinese directory

#### 💻 Open the project

First open the installed Android Studio, and if you're opening it for the first time, it might let you set something up, skip or continue all the way. After that, the real welcome interface, this time you can choose the 'Open an existing Android Studio Project'on the interface, and then pop up a file selection window, select the unzipped project and click OK, at which point the project is loaded into Android Studio.

But this time is not over, you should be able to see (Android Studio AS) under the AS interface there is a moving progress bar, the first time you open the project, please keep the network open, it may need to download 100-200MB files. And it won't be fast. There is a good chance of failure. If it fails, reopen the project and try again. However, it is possible to keep failing, at this time the most reliable way is to catalog the project`build`、 `.gradle`(Note that it is.gradle)、and`app\build`Delete the directory and do it again.

Do not make any changes to the project until the progress bar below has disappeared.
If you're lucky, you may have successfully opened the project, at which point you can plug in your phone, turn on the set developer option, enable adb, connect your phone to your computer with a data cable, and then click the green triangle button at the top right of the AS to run the project on your phone

#### 🚀 Modify the basic information on the interface

Open app/main/res/values/strings.xmlThis file, in which you can see a lot of messy text. These are the things you need to modify.


It's probably longer
```xml
    <!-- App name -->
    <string name="app_name">colorline</string>
    <!-- Home title information -->
    <string name="home_title">colorline</string>
```

That's what you need to modify\<string name="xxxxx"> `between the words` \<string/>

When you're done having made the changes, you can click the green triangle button at the top right of the AS again to run the project to your phone to see the effect.

If you need to modify the picture information on the interface，app/main/res/drawable,Next, find the picture you need to change, with a name that can only be lowercase, underlined, or any combination of numbers (not beginning with numbers). If you make sure that the picture name is unique, delete the picture with the same name that you don't need, even though the suffix name is different and you also need to overwrite it.

#### 🥑 Add icons that need to be adapted

The key file for the icon pack adaptation app is app/main/res/xml appfilter.xml and drawable.xml。

What they need to fill out is roughly this:

`appfilter.xml`
```xml
<item component="ComponentInfo{com.android.chrome/com.google.android.apps.chrome.Main}" drawable="ic_chrome"/>
```

`drawable.xml`
```xml
    <category title="system"/>
    <item drawable="ic_settings" name="setting" />
```

##### appfilter.xml

In fact, it is very simple, they are basically the content of both of these, first look atappfilter.xml

\<item componet="" drawable=""/>This is the same format

The first of them""The format of the fill-in is `The package name of the software needs to be adapted` + `/` + `The boot Activity name that needs to be adapted to the software`，The second one""Fill in yours`Icon file name`。

First of all， `The package name of the software needs to be adapted` + `/` + `The boot Activity name that needs to be adapted to the software`This does not really need you to figure out what this information is like, because each software is different, but also can not directly see, so then open the template app, to the third page, that is, the adaptation page casually select an unsaturated app, click on the send button below, sent to your computer, unzip the inside has a txt file, inside already has the information you just selected the app needs. In fact, this txt file is a complete line. The one in theredrawable="The name of the software"You need to modify it yourself.

Let's see`Icon file name`，This requires your icon file，Usually xxx.png，Naming rules are the beginning of lowercase letters, allowing only any combination of lowercase letters and underscores and numbers.like ic_chrome.png，Now copy the icon file you made toapp/main/res/drawable-nodpi directory，then drawable="The name of the software"Replace withdrawable="Icon file name"，Note that suffixes are not required.

##### drawable.xml

This file is a must, although it may not be available without it, but the main purpose of this file is that some desktops are free to replace the icon of an app, and this time read the configuration in the drawable .xml. Its fixed format is:

```xml
    <category title="category"/>
    <item drawable="Icon file name" name="The app name" />
    <item drawable="Icon file name" name="The app name" />
    <item drawable="Icon file name" name="The app name" />
    ...
```

The first is one`<categor title="category"/>`，This is to categorize icons, and the title is taken at will, for example`system`、`game`...Once the label is finished, you can start writing below it`<item drawable="Icon file name" name="The app name" />`，`Icon file name`Do the same with appfilter，`The app name`Is the corresponding APP Chinese name, in fact, can not write, but if you want to write, it can be inside the template app click on the icon to show the value of name, otherwise the default display icon file name. If you don't want to write, you can delete it directly`name="The app name"`，It's like this:

```xml
    <category title="category"/>
    <item drawable="Icon file name" />
    <item drawable="Icon file name" />
    <item drawable="Icon file name" />
```

Every new app that fits in later needs to do this, and when you've done a good version, don't forget to copy the appfilter.xml and drawable .xml files to the app/src/main/assets directory, because some desktops are using this directory, not xml this directory.

#### 😍 Package apk and updates

When everything is done, it's finally ready to pack. Make sure that the package name is modified in the app/build.gradle file, and click Sync Now, and you need to click Sync Now every time you modify the build.gradle content.

You can also see it in build.gradle

```groovy
versionCode 1
versionName "1.0"
```

This is the version number and version name, each time you update the version to ensure that the number after the versionCode is larger than before, the version name is filled in at will, this is your first version, so write it like this.

Next, using Android Studio to compile the project into an apk, first click Build in the menu bar above the toolbar above, which will pop up a lot of options if you need to release release, select`Generate Signed Bundle / APK`，Then it will let you choose whether to compile into Bundle or apk, as you go. I chose apk, the next need to sign the file, as to how to create a signature file, you can look at the online tutorial, here is not introduced. Also in AS this interface can be created, the best time a little longer, otherwise after the signature time apk can not be installed. Suppose you have got the signature file, this time select your signature file and enter the password, name, password. Click on next, then select myIconRelease, above is the output apk directory, you can write down or choose a directory, click finish, window closed. Then you see a progress bar under AS, waiting for the progress bar to disappear, prompted in the lower right corner, and there's a blue text that you can click on to the output directory, and now you've got an apk. Note that this time the signature is already your own, if you have run on your phone before, can not overwrite the installation, you need to uninstall the icon pack you tested before.

#### 😜 Screenshots of the software

<img src="https://github.com/shangshanhuijian/colorlineiconpack/blob/master/Screenshot/软件首页.jpg"/>

<img src="https://github.com/shangshanhuijian/colorlineiconpack/blob/master/Screenshot/适配图例.jpg"/>

🤫 If you are a professional developer, you can see that the code is very bad, don't say it!
