package cz.kribsky.pdfparser.parsers;

import com.google.common.base.Preconditions;
import cz.kribsky.pdfparser.domain.MainTrainMetaInfo;
import cz.kribsky.pdfparser.domain.PrintableInterface;
import cz.kribsky.pdfparser.domain.SinglePath;
import cz.kribsky.pdfparser.domain.TrainCompost;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
            lines = parseToPlainTextByTika(pathToFile).split("\n");
            LOGGER.debug("Parsed PDF is");
            LOGGER.debug(String.join("\n", lines));

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
                .map(trainMetaInfoParser::parseToOne)
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
        LOGGER.debug("Groups {}",groups.stream().map(GroupBuilder.Group::debugOutput).collect(Collectors.joining("\n")));

        for (GroupBuilder.Group group : groups) {
            if (expectedNextParser != null) {
                Preconditions.checkArgument(
                        group.isSameParserClass(expectedNextParser),
                        "Expecting group %s but found %s", expectedNextParser.getName(), group.getTypedParser(InputLineParsingInterface.class)
                );
            }

            if (group.isSameParserClass(TrainPathParser.class)) {
                // first parser
                Preconditions.checkNotNull(builder);
                builder = SinglePath.builder();
                parseAndSet(group, builder::trainPath, TrainPathParser.class);
                expectedNextParser = TrainMetaInfoParser.class;
            }

            if (group.isSameParserClass(TrainMetaInfoParser.class)) {
                parseAndSet(group, builder::trainMetaInfo, TrainMetaInfoParser.class);
                expectedNextParser = EngineParser.class;
            }
            if (group.isSameParserClass(EngineParser.class)) {
                parseAndSet(group, builder::engines, EngineParser.class);
                expectedNextParser = WagonParser.class;
            }
            if (group.isSameParserClass(WagonParser.class)) {
                // last one
                parseAndSet(group, builder::wagons, WagonParser.class);

                singlePaths.add(builder.build());
                builder = SinglePath.builder();
                expectedNextParser = TrainPathParser.class;
            }
        }

        return singlePaths;
    }

    private <P extends InputLineParsingInterface<?>, O> void parseAndSet(
            GroupBuilder.Group group,
            Function<O, SinglePath.SinglePathBuilder> setFc,
            Class<P> parserClass) {

        final P typedParser = group.getTypedParser(parserClass);
        final List<GroupBuilder.InputLine> filtered = group.getInputLines()
                .stream()
                .filter(typedParser::shouldConsumeLine)
                .peek(GroupBuilder.InputLine::consume)
                .collect(Collectors.toUnmodifiableList());

        if (typedParser instanceof InputLineParsingInterface.SingleObject) {
            final PrintableInterface printableInterface = ((InputLineParsingInterface.SingleObject<?>) typedParser).parseToOne(filtered);
            setFc.apply((O) printableInterface);
        } else {
            setFc.apply((O) typedParser.parse(filtered));
        }
    }

    public ParseMonitor getParseMonitor() {
        return parseMonitor;
    }

    private String parseToPlainTextByTika(Path pathToFile) throws IOException, SAXException, TikaException {
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
