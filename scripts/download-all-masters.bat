@echo off
kotlinc -cp master-lib\target\master-lib-1.0.0-SNAPSHOT-jar-with-dependencies.jar -script "%~dp0download-all-masters.kts"
