# trainComposition-pdf-parser

simple application for parsing pdf then exporting to csv file
## Requirements
java 11 in system which we want to run

## Directory with runnable output
this is where you want to download jar file (excutable java) 
https://github.com/tomkri3/trainComposition-pdf-parser/tree/master/builded-jars

## Inputs
as input it take exactly one argumnet which is path to file OR directory

## Output
it wil create ouput file which is at the same directory as input + suffix `_converted.xlsx`

## Example usage
for converting file

`java -jar /path/to/jar/trainComposition-pdf-parser-1.1-jar-with-dependencies.jar "/path/to/pdfFile/SlozeniVlaku (35).pdf"`

for converting directory is the same like file but only different path

`java -jar /path/to/jar/trainComposition-pdf-parser-1.1-jar-with-dependencies.jar "/path/to/pdfFiles/"`

