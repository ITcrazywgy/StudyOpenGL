package com.study.studyopengl.parser;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Felix on 2018/1/6 15:05
 */
class Line {
    private static final String WHITE_SPACE_PATTERN = "[\\s]+";
    private static final String COMMENT_SEPARATOR = "#";
    private final StringBuilder logicalLineBuilder = new StringBuilder();
    private static final int COMMENT_SEPARATOR_LENGTH = COMMENT_SEPARATOR.length();
    private static final String LINE_EXTENSION = "\\";
    private String logicalLine;
    private String[] segments;

    public String get() {
        return logicalLine;
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        if (!line.endsWith(LINE_EXTENSION)) {
            return line;
        }
        logicalLineBuilder.setLength(0);
        while ((line != null) && (line.endsWith(LINE_EXTENSION))) {
            final String lineContent = line.substring(0, line.length() - 1);
            logicalLineBuilder.append(lineContent);
            line = reader.readLine();
        }
        if (line != null) {
            logicalLineBuilder.append(line);
        }
        return logicalLineBuilder.toString();
    }

    boolean parse(BufferedReader reader) throws IOException {
        final String line = readLine(reader);
        if (line == null) {
            return false;
        }
        logicalLine = line.trim();
        segments = logicalLine.split(WHITE_SPACE_PATTERN);
        return true;
    }

    boolean isEmpty() {
        return logicalLine == null || logicalLine.isEmpty();
    }

    boolean isComment() {
        return segments[0].startsWith(COMMENT_SEPARATOR);
    }

    boolean isCommand(String commandName) {
        return commandName.equals(segments[0]);
    }

    String getString(int index) {
        if (index + 1 < segments.length) {
            return segments[index + 1];
        }
        return "";
    }

    float getFloat(int index) {
        return Float.parseFloat(getString(index));
    }

    int getInt(int index) {
        return Integer.parseInt(getString(index));
    }

    int getParamCount() {
        return Math.max(0, segments.length - 1);
    }


}
