CC=/usr/lib/jvm/java-8-openjdk-amd64/bin/javac
DIR=SimpleComputerEmulator

all: Emulator run SimpleComputerEmulator.jar

Emulator:
	$(CC) -Xlint:unchecked $(DIR)/*.java


SimpleComputerEmulator.jar:
	jar cfmv SimpleComputerEmulator.jar Manifest SimpleComputerEmulator/*.class


run:
	java $(DIR).SimpleComputerEmulator

clean: 
	$(RM) $(DIR)/*~  $(DIR)/*.class SimpleComputerEmulator.jar
