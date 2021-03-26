CC=javac
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
