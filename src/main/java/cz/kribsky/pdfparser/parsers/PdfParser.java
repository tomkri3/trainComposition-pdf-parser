package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.parsers.ParsingInterface;
import cz.kribsky.pdfparser.domain.Wagon;
import cz.kribsky.pdfparser.parsers.WagonParser;
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
import java.util.Collection;
import java.util.List;

public class PdfParser {

    public List<Wagon> parse(Path pathToFile) throws IOException, TikaException, SAXException {
        String plainText = parsePlainTextByTika(pathToFile);
        final String[] split = plainText.split("\n");
        return parseWagons(split);
    }

    private List<Wagon> parseWagons(String[] split) {
        List<Wagon> wagons = new ArrayList<>();
        final WagonParser parser = new WagonParser();
        int startWagonPart = findStart(split, parser);

        for (int i = startWagonPart +1 ; i < split.length; i++) {
            String s = split[i];
            if(parser.shouldConsumeLine(s)){
                wagons.add(parser.parse(s));
            }
        }

        return wagons;
    }

    private int findStart(String[] split, ParsingInterface parser) {
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if(parser.isHeader(s)){
                return i;
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
