package cz.kribsky.pdfparser.parsers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParseMonitor {

    private final Map<Exception, String> exceptionStringMap = new ConcurrentHashMap<>();
    private final List<Path> successFullyProcessed = new ArrayList<>();

    public void addException(Exception e, String comment) {
        exceptionStringMap.put(e, comment);
    }

    public Map<Exception, String> getExceptionStringMap() {
        return exceptionStringMap;
    }

    public List<Path> getSuccessFullyProcessed() {
        return successFullyProcessed;
    }

    public boolean hasRisenException(){
        return !exceptionStringMap.isEmpty();
    }

    public void addSuccesFull(Path pathToFile) {
        successFullyProcessed.add(pathToFile);
    }
}
