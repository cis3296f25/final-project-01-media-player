# JMP

JMP is a media player with an audio visualizer 

# How to run
## Mac 
Download `mac.zip` from the latest release.
Extract and run `mac\bin\app`

## Windows
Download `windows.zip` from the latest release.
Extract and run `bin\app.bat`

### How to build
- Use this github repository: ... 
- Specify what branch to use for a more stable release or for cutting edge development.  
- Use InteliJ 11
- Specify additional library to download if needed 
- What file and target to compile and run. 
- What is expected to happen when the app start. 


- Clone this repository with `git clone`
- Preferably use IntelliJ for the most simple experience setting up the project 
	- Open the local repository from the IDE 
	- Click the build icon on the top righthand corner of the screen.
	- Navigate to the `target\`
- If you want to use the CLI instead.
	- Navigate to the local repository from the command line. 
	- in the root folder of the repository run: `./mvn javafx:jlink`
	- this will compile the app file and create a zip in the `target\` folder

PREREQUISITES:
- openjdk21++
- Maven
- JavaFX
OPERATING SYSTEMS:
- Windows 11 Version 10.0.26100
- MacOS Sequoia 15.6.1
PREFERED IDE
- Intellij V21
