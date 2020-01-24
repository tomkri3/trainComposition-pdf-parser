package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.*;
import cz.kribsky.pdfparser.parsers.domain.*;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParserComposition {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserComposition.class);

    private final boolean throwExceptionWhenRaised;
    private final ParseMonitor parseMonitor = new ParseMonitor();

    public ParserComposition(boolean throwExceptionWhenRaised) {
        this.throwExceptionWhenRaised = throwExceptionWhenRaised;
    }

    public ParserComposition() {
        this(true);
    }

    public TrainCompost parseCompost(Path pathToFile) {
        LOGGER.info("Processing file: {}", pathToFile.toAbsolutePath());
        String[] lines;
        try {
            lines = parsePlainTextByTika(pathToFile).split("\n");

            final List<GroupBuilder.Group> groups = new GroupBuilder(lines)
                    .groupLinesByHeader(
                            List.of(
                                    new EngineParser(),
                                    new TrainMetaInfoParser(),
                                    new MainTrainMetaInfoParser(),
                                    new TrainPathParser(),
                                    new WagonParser()
                            )
                    );

            final List<SinglePath> singlePaths = parseSinglePaths(groups);

            final MainTrainMetaInfo mainTrainMetaInfo = findMainMetaInfo(groups);
            TrainCompost trainCompost = new TrainCompost(
                    mainTrainMetaInfo,
                    singlePaths
            );
            parseMonitor.addSuccesFull(pathToFile);
            return trainCompost;
        } catch (Exception e) {
            String x = "Exception when processing file: '" + pathToFile.toAbsolutePath().toString() + "'";
            parseMonitor.addException(e, x);
            if (throwExceptionWhenRaised) {
                throw new RuntimeException(e);
            } else {
                return new TrainCompost(new MainTrainMetaInfo(), Collections.emptyList());
            }
        }
    }

    private MainTrainMetaInfo findMainMetaInfo(List<GroupBuilder.Group> groups) {
        final MainTrainMetaInfoParser trainMetaInfoParser = new MainTrainMetaInfoParser();
        final List<MainTrainMetaInfo> collect = groups.stream()
                .filter(group -> group.isSameParserClass(MainTrainMetaInfoParser.class))
                .map(GroupBuilder.Group::getInputLines)
                .map(trainMetaInfoParser::parse)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Preconditions.checkArgument(collect.size() == 1,
                "Is expected that there is exactly one MainTrainMetaInfo but actually was %s",
                collect.size()
        );
        return collect.get(0);
    }

    private List<SinglePath> parseSinglePaths(List<GroupBuilder.Group> groups) {
        final List<SinglePath> singlePaths = new ArrayList<>();
        SinglePath.SinglePathBuilder builder = SinglePath.builder();
        Class<?> expectedNextParser = null;
        for (GroupBuilder.Group group : groups) {
            if (expectedNextParser != null) {
                Preconditions.checkArgument(group.isSameParserClass(expectedNextParser));
            }
            if (group.isSameParserClass(TrainMetaInfoParser.class)) {
                // first parser
                Preconditions.checkNotNull(builder);
                builder = SinglePath.builder();

                builder.trainMetaInfo(
                        group.getTypedParse(TrainMetaInfoParser.class)
                                .parseToOnly(group.getInputLines())
                );
                expectedNextParser = EngineParser.class;
            }
            if (group.isSameParserClass(EngineParser.class)) {
                builder.engines(
                        group.getTypedParse(EngineParser.class)
                                .parse(group.getInputLines())
                );
                expectedNextParser = WagonParser.class;
            }
            if (group.isSameParserClass(WagonParser.class)) {
                // last one
                final List<Wagon> wagons = group.getTypedParse(WagonParser.class).parse(group.getInputLines());
                builder.wagons(wagons);

                singlePaths.add(builder.build());
                builder = SinglePath.builder();
                expectedNextParser = TrainMetaInfoParser.class;
            }
        }

        return singlePaths;
    }

    public ParseMonitor getParseMonitor() {
        return parseMonitor;
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
