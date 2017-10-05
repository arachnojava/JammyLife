@echo off
call makejar.bat
cd ..

md Release
md Release\images
rem md Release\audio

copy bin\*.jar Release
copy images\*.* Release\images
