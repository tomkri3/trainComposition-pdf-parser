package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.domain.Train;
import cz.kribsky.pdfparser.domain.TrainCompost;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ParserComposition {

    public TrainCompost parseCompost(Path pathToFile) throws TikaException, SAXException, IOException {
        final String[] lines = parsePlainTextByTika(pathToFile).split("\n");


        final Train train = TrainParser.combine(parseToCollection(lines, new TrainParser()));
        return new TrainCompost(
                train,
                parseSingle(lines, new TrainMetaInfoParser()),
                parseToCollection(lines, new WagonParser()),
                parseToCollection(lines, new EngineParser())
        );
    }

    private <T extends PrintableInterface> T parseSingle(String[] lines, ParsingInterface<T> parsingInterface) {
        return Iterables.getOnlyElement(parseToCollection(lines, parsingInterface));
    }

    private <T extends PrintableInterface> List<T> parseToCollection(String[] lines, ParsingInterface<T> parsingInterface) {
        List<T> wagons = new ArrayList<>();
        int startWagonPart = findStart(lines, parsingInterface);

        for (int i = startWagonPart; i < lines.length; i++) {
            String s = lines[i];
            if (parsingInterface.shouldConsumeLine(s)) {
                wagons.add(parsingInterface.parse(s));
            }
        }

        return wagons;
    }

    private int findStart(String[] split, ParsingInterface parser) {
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (parser.isHeader(s)) {
                // +1 to return first non header line
                return i + 1;
            }
        }

        throw new IllegalStateException("Should not get here!!!");
    }

    private String parsePlainTextByTika(Path pathToFile) throws IOException, SAXException, TikaException {
        Preconditions.checkArgument(Files.exists(pathToFile), "File %s does not exist!", pathToFile.toString());
        Preconditions.checkArgument(Files.isReadable(pathToFile), "File %s is not readable!", pathToFile.toString());

        //parse method parameters
        Parser parser = new AutoDetectParser();
        ContentHandlerDecorator handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        //parsing the file
        parser.parse(Files.newInputStream(pathToFile), handler, metadata, context);
        return handler.toString();
    }
}
